package com.example.timble;

import static android.content.Context.DOWNLOAD_SERVICE;
import static com.example.timble.PostFragment.p_h;
import static com.example.timble.otpActivity.MY_Prefernce;

import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.Viewholder> {
    Context context;
    ArrayList<Posts> postlist;
    FirebaseDatabase database;
    String clg,title;
    BottomSheetDialog bottomSheetDialog;

    public PostAdapter(Context context,ArrayList<Posts> postlist) {
        this.context=context;
        this.postlist=postlist;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_posts,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Posts posts=postlist.get(position);
        holder.p_t.setText(posts.post_time);
        holder.p_n.setText(posts.p_name);
        Picasso.get().load(posts.p_image).resize(400,400).centerCrop().into( holder.p_i);
        holder.p_des.setText(posts.description);
        holder.p_des1.setText(posts.p_name);
        database=FirebaseDatabase.getInstance();
        SharedPreferences preferences= context.getSharedPreferences(MY_Prefernce,Context.MODE_PRIVATE);
        clg=preferences.getString("college","");
        holder.threed.setOnClickListener(v -> {
            bottomSheetDialog=new BottomSheetDialog(context,R.style.BottomSheetStyle);
            View view1= LayoutInflater.from(context).inflate(R.layout.bottomsheet_post,(LinearLayout) v.findViewById(R.id.sheet));
            bottomSheetDialog.setContentView(view1);
            LinearLayout lay_del=(LinearLayout) view1.findViewById(R.id.lay_del);
            LinearLayout lay_qr=(LinearLayout) view1.findViewById(R.id.lay_qr);
            LinearLayout lay_save=(LinearLayout) view1.findViewById(R.id.lay_save);
            LinearLayout lay_share=(LinearLayout) view1.findViewById(R.id.lay_share);
            LinearLayout lay_edit=(LinearLayout) view1.findViewById(R.id.lay_edit);
            ImageView qri=(ImageView)view1.findViewById(R.id.qri);
            EditText newdes=(EditText) view1.findViewById(R.id.newdes);
            TextView up_des=(TextView) view1.findViewById(R.id.up_des);
            LinearLayout edit_des=(LinearLayout) view1.findViewById(R.id.edit_des);
            lay_del.setOnClickListener(v1 -> {
                DatabaseReference postrefernce=database.getReference().child(clg).child("Posts");
                Query query=postrefernce.child(p_h).orderByChild("post_image").equalTo(posts.post_image);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            dataSnapshot.getRef().removeValue();
                            bottomSheetDialog.dismiss();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            });
            lay_qr.setOnClickListener(v14 -> {
                qri.setVisibility(qri.getVisibility()==View.GONE?View.VISIBLE:View.GONE);
                WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();

                Point point = new Point();
                display.getSize(point);
                int width = point.x;
                int height = point.y;
                int dimen = width < height ? width : height;
                dimen = dimen * 3 / 4;
                    QRGEncoder qrgEncoder = new QRGEncoder(posts.post_image, null, QRGContents.Type.TEXT,dimen);
                qrgEncoder.setColorBlack(Color.WHITE);
                qrgEncoder.setColorWhite(Color.BLACK);
                    try {
                        Bitmap bitmap;
                        bitmap = qrgEncoder.getBitmap();
                       qri.setImageBitmap(bitmap);

                    }catch (RuntimeException e){

                    }
                });
            lay_save.setOnClickListener(v12 -> {
                Bitmap bitmap;
                bitmap =((BitmapDrawable)holder.Post_img.getDrawable()).getBitmap();
                Date date=new Date();
                String time= String.valueOf(date.getTime());
                Uri image;
                ContentResolver contentResolver=context.getContentResolver();
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
                    image= MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);

                }else{
                    image=MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                }
                ContentValues contentValues=new ContentValues();
                contentValues.put(MediaStore.Images.Media.DISPLAY_NAME,System.currentTimeMillis()+".jpg");
                contentValues.put(MediaStore.Images.Media.MIME_TYPE,"images/*");
                Uri uri=contentResolver.insert(image,contentValues);
                ContextWrapper cw = new ContextWrapper(context);

                File dir= cw.getDir("DCIM", Context.MODE_PRIVATE);
                if(!dir.exists()){
                    dir.mkdirs();
                }
                String imagename="IMG-"+time+".jpg";
                File file=new File(dir,imagename);

                try {
                    OutputStream out=contentResolver.openOutputStream(uri);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);

                    bottomSheetDialog.dismiss();
                    Toast.makeText(context, "File Saved ", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(context, ""+e.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            lay_share.setOnClickListener(v13 -> {
                Bitmap bitmap;
                bitmap =((BitmapDrawable) holder.Post_img.getDrawable()).getBitmap();
                Uri uri=getImageTOShare(bitmap);
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM,uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setType("image/*");
                context.startActivity(Intent.createChooser(intent,"Share Image"));
            });
            lay_edit.setOnClickListener(v15 -> {
                       edit_des.setVisibility(edit_des.getVisibility()==View.GONE?View.VISIBLE:View.GONE);
                       up_des.setOnClickListener(v16 -> {
                           String n_d=newdes.getText().toString();
                           if(TextUtils.isEmpty(n_d)){
                               newdes.setError("Enter new desciption");
                           }else {
                               DatabaseReference postrefernce=database.getReference().child(clg).child("Posts");
                               Query query=postrefernce.child(p_h).orderByChild("post_image").equalTo(posts.getPost_image());
                               query.addListenerForSingleValueEvent(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                                       for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                                           Posts posts1=new Posts(posts.post_image, posts.post_time, posts.p_image, posts.p_name, posts.p_uid, posts.type,n_d);
                                           dataSnapshot.getRef().setValue(posts1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(@NonNull Task<Void> task) {
                                                   if(task.isSuccessful()){
                                                       edit_des.setVisibility(View.GONE);
                                                       Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
                                                   }
                                               }
                                           });

                                       }
                                   }

                                   @Override
                                   public void onCancelled(@NonNull DatabaseError error) {

                                   }
                               });

                           }
                       });
            });
            bottomSheetDialog.show();
        });
        if(posts.type.equals("image")){
            Picasso.get().load(posts.post_image).into( holder.Post_img);
        }else if(posts.type.equals("video")){
            holder.Post_vdo.setVisibility(View.VISIBLE);
            holder.Post_img.setVisibility(View.GONE);
             holder.Post_vdo.setVideoURI(Uri.parse(posts.post_image));
            MediaController mediaController=new MediaController(context);
            holder.Post_vdo.setMediaController(new MediaController(context));
            mediaController.setAnchorView(holder.Post_vdo);
            holder.Post_vdo.start();
        } else if (posts.type.equals("pdf")) {
            holder.pdf_view.setVisibility(View.VISIBLE);
           holder.Post_img.setVisibility(View.GONE);

           holder.pdf_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    title= URLUtil.guessFileName(posts.post_image,null,"application/pdf");
                    DownloadManager manager=(DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                    Uri uri=Uri.parse(posts.post_image);
                    File file=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    File myfile=new File(file.getAbsolutePath()+"/"+title);
                    if (myfile.exists()){
                        int file_size=Integer.parseInt(String.valueOf(myfile.length()/1024));
                        holder.pdf_property.setText(file_size+" kB ");
                        Intent intent=new Intent(Intent.ACTION_VIEW);
                        Uri pdf=FileProvider.getUriForFile(context,context.getApplicationContext().getPackageName(),myfile);
                        intent.setDataAndType(pdf,"application/pdf");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        context.startActivity(Intent.createChooser(intent,"Choose app to open"));
                    }else {
                        DownloadManager.Request request=new DownloadManager.Request(uri);
                        request.setTitle(title);
                        request.setDescription("Downloading pdf");
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        String cookie= CookieManager.getInstance().getCookie(posts.post_image);
                        request.addRequestHeader("cookie",cookie);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,title);
                        manager.enqueue(request);
                    }

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return postlist.size();
    }
    class Viewholder extends RecyclerView.ViewHolder{

        ImageView Post_img,threed,download;
        CircleImageView p_i;
        TextView p_n,p_t,p_des,p_des1,pdf_property,pdf_name;
        VideoView Post_vdo;
        LinearLayout pdf_view;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            Post_img=itemView.findViewById(R.id.Post_img);
            p_i=itemView.findViewById(R.id.p_i);
            p_t=itemView.findViewById(R.id.p_t);
            p_n=itemView.findViewById(R.id.p_n);
            Post_vdo=itemView.findViewById(R.id.Post_vdo);
            threed=itemView.findViewById(R.id.threed);
            p_des=itemView.findViewById(R.id.p_des);
            p_des1=itemView.findViewById(R.id.p_des1);
            pdf_view=itemView.findViewById(R.id.pdf_view);
            pdf_name=itemView.findViewById(R.id.pdf_name);
            pdf_property=itemView.findViewById(R.id.pdf_property);




        }
    }
    private Uri getImageTOShare(Bitmap bitmap) {
        File folder=new File(context.getCacheDir(),"images");
        Uri uri=null;
        try {
            folder.mkdirs();
            File file=new File(folder,"shared_image_timble.jpg");
            FileOutputStream fileOutputStream= null;
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            uri= FileProvider.getUriForFile(context,"com.example.timble",file);
        } catch (Exception e) {
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return uri;
    }
}
