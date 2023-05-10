package com.example.timble;

import static com.example.timble.otpActivity.MY_Prefernce;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.FilenameUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class PostFragment extends Fragment {

    FirebaseAuth auth;
    public static String p_h;
    RecyclerView postrec;
    ArrayList<Posts> postlist;
    ArrayList<Posts> postlist1;
    LinearLayout add;
    EditText des;
    ImageView addpos;
    PostAdapter postAdapter;
    ActivityResultLauncher<String > mTakePhoto;
    Uri imageUri;
    Uri pdfUri;
    TextView addpost;
    String descrip;
    ImageView pi;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String p_n,p_i,imageURI,clg,pdfURI;
    ActivityResultLauncher<Intent> mTakePdf;
    ProgressDialog progressDialog;
    BottomSheetDialog bottomSheetDialog;
    String extension;

    public PostFragment() {
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        auth= FirebaseAuth.getInstance();
        View view=inflater.inflate(R.layout.fragment_post, container, false);
        postrec=view.findViewById(R.id.postrec);
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        addpost=view.findViewById(R.id.addpost);
        pi=view.findViewById(R.id.pi);
        addpos=view.findViewById(R.id.addpos);
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);
        add=view.findViewById(R.id.add);
        des=view.findViewById(R.id.des);

        SharedPreferences preferences= getActivity().getSharedPreferences(MY_Prefernce, Context.MODE_PRIVATE);
        clg=preferences.getString("college","IIT DELHI");
        mTakePdf =registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @SuppressLint("Range")
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Intent data=result.getData();
                        if(data!=null){
                            pdfUri=data.getData();
                            String fileName = null;
                            if (pdfUri.getScheme().equals("content")) {
                                Cursor cursor = getContext().getContentResolver().query(pdfUri, null, null, null, null);
                                try {
                                    if (cursor != null && cursor.moveToFirst()) {
                                        fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                                    }
                                } finally {
                                    cursor.close();
                                }
                            }
                            if (fileName == null){
                                fileName = pdfUri.getPath();
                                int mark = fileName.lastIndexOf("/");
                                if (mark != -1){
                                    fileName = fileName.substring(mark + 1);
                                }
                            }
                            extension = fileName.substring(fileName.lastIndexOf(".") );

                        }
                    }
                }

        );

        mTakePhoto =registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                      imageUri=result;
                    }
                }
        );
        pi.setOnClickListener(v -> {
            Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");

            mTakePdf.launch(intent);
        });
        addpos.setOnClickListener(v -> {
            add.setVisibility(add.getVisibility()==View.GONE?View.VISIBLE:View.GONE);
        });
        DatabaseReference reference=database.getReference().child(clg).child("Users").child(auth.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                p_i=snapshot.child("imageUri").getValue().toString();
                p_n=snapshot.child("name").getValue().toString();
                p_h=snapshot.child("hostel").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        DatabaseReference postrefernce=database.getReference().child(clg).child("Posts");
        postrefernce.keepSynced(true);
        postlist=new ArrayList<>();
        postrefernce.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postlist.clear();
                if(snapshot.exists())
                    for (DataSnapshot dataSnapshot : snapshot.child(p_h).getChildren()){
                        Posts posts=dataSnapshot.getValue(Posts.class);
                        postlist.add(posts);
                    }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        postrec.setLayoutManager(new LinearLayoutManager(getActivity()));
        postAdapter=new PostAdapter(getActivity(),postlist);
        postrec.setAdapter(postAdapter);
        return view;


    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        addpost.setOnClickListener(v -> {
            descrip=des.getText().toString();
            progressDialog.show();
            if (TextUtils.isEmpty(descrip)){
                progressDialog.dismiss();
                des.setError("Enter description");
            }
            else if(!TextUtils.isEmpty(descrip)){
                Date date = new Date();
                String f= String.valueOf(date.getTime());
                String  saveCurrentDate,saveCurrentTime,postT;
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("EEE d MMM,yyyy");
                saveCurrentDate = currentDate.format(calendar.getTime());


                SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
                saveCurrentTime = currentTime.format(calendar.getTime());
                postT=saveCurrentTime+","+saveCurrentDate;

                DatabaseReference reference =database.getReference().child(clg).child("Posts");
                StorageReference storageReference= storage.getReference().child(clg).child("Posts").child(f);
                storageReference.putFile(pdfUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imageURI=uri.toString();
                                    if (extension.equals(".pdf")){
                                        Posts posts=new Posts(imageURI,postT,p_i,p_n,auth.getUid(),"pdf",descrip);
                                        reference.child(p_h).push().setValue(posts).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    progressDialog.dismiss();
                                                    add.setVisibility(View.GONE);
                                                    des.setText("");
                                                    imageUri=null;
                                                    Toast.makeText(getActivity(), "Posted", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getActivity(), "error ", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else if (extension.equals(".jpg") || extension.equals(".png")) {
                                        Posts posts=new Posts(imageURI,postT,p_i,p_n,auth.getUid(),"image",descrip);
                                        reference.child(p_h).push().setValue(posts).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    progressDialog.dismiss();
                                                    add.setVisibility(View.GONE);
                                                    des.setText("");
                                                    imageUri=null;
                                                    Toast.makeText(getActivity(), "Posted", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getActivity(), "error ", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }else {
                                        Toast.makeText(getActivity(), "unsupported file", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            });
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else {
                Toast.makeText(getActivity(), "No file chosen", Toast.LENGTH_SHORT).show();
            }
        });
    }
}