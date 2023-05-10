package com.example.timble;

import static android.content.Context.MODE_PRIVATE;

import static com.example.timble.ScheduleFragment.Visibility;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;

public class DietFragment extends Fragment {

   FirebaseAuth auth;
   FirebaseDatabase database;
   RecyclerView brec,hrec,frec,fanrec,rrec100,hrec100,frec100,fanrec100,trec100,srec;
   ImageView si,ci;
   TextInputEditText sb;
    ArrayList<Books> list;
    ArrayList<Books> list1;

    ArrayList<Books> list2;
    ArrayList<Books> list3;
    ArrayList<Books> list4;
    ArrayList<Books> list5;
    ArrayList<Books> list6;
    ArrayList<Books> list7;
    ArrayList<Books> list8;
    BookAdapter bookAdapter;
    HorrorAdapter horrorAdapter;
    FictionAdapter fictionAdapter;
    FantasyAdapter fantasyAdapter;
    SearchAdapter searchAdapter;
    ArrayList<SearchBook> searchArrayList;
    LinearLayout SL;
    ParentAdapter parentAdapter;
    RecyclerView mainrec;
    ArrayList<ParentModel>listofbooklist;
    ImageView svc;
    LinearLayout srch_ti;
    GridView gv;
    RelativeLayout rr;
    TextView nb;



    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_diet, container, false);

        si=view.findViewById(R.id.si);
        sb=view.findViewById(R.id.sb);
        srec=view.findViewById(R.id.srec);
        SL=view.findViewById(R.id.sL);
        svc=view.findViewById(R.id.svc);
        srch_ti=view.findViewById(R.id.srch_ti);
        mainrec=view.findViewById(R.id.mainrec);
        gv=view.findViewById(R.id.gv);
        rr=view.findViewById(R.id.rr);
        nb=view.findViewById(R.id.nb);


        database=FirebaseDatabase.getInstance();


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        si.setOnClickListener(v -> {
            String SB=sb.getText().toString();
            if(!SB.isEmpty()){
                try {
                    run(SB);

                } catch (IOException e) {
                    throw new RuntimeException(e);

                }
            }
        });
        svc.setOnClickListener(v -> {
            srch_ti.setVisibility(srch_ti.getVisibility()==View.GONE?View.VISIBLE:View.GONE);
        });

        SharedPreferences preferences= getActivity().getSharedPreferences(Visibility,MODE_PRIVATE);
        GsonBuilder gsonBuilder=new GsonBuilder();
        Gson gson=gsonBuilder.create();
        listofbooklist=new ArrayList<>();

        String myResponse=preferences.getString("rom","");
        list=gson.fromJson(myResponse, new TypeToken<List<Books>>() {}.getType());
        listofbooklist.add(new ParentModel("Romance",list));



        String myResponse1=preferences.getString("horror","");
        list1=gson.fromJson(myResponse1, new TypeToken<List<Books>>() {}.getType());
        listofbooklist.add(new ParentModel("Horror",list1));




        String myResponse2=preferences.getString("fiction","");
        list2=gson.fromJson(myResponse2, new TypeToken<List<Books>>() {}.getType());
        listofbooklist.add(new ParentModel("Fiction",list2));



        String myResponse3=preferences.getString("fantasy","");
        list3=gson.fromJson(myResponse3, new TypeToken<List<Books>>() {}.getType());
        listofbooklist.add(new ParentModel("Fantasy",list3));

        String myResponse4=preferences.getString("Romance100","");
        list4=gson.fromJson(myResponse4, new TypeToken<List<Books>>() {}.getType());
        listofbooklist.add(new ParentModel("Romance",list4));


        String myResponse5=preferences.getString("Horror100","");
        list5=gson.fromJson(myResponse5, new TypeToken<List<Books>>() {}.getType());
        listofbooklist.add(new ParentModel("Horror",list5));


        String myResponse6=preferences.getString("Thriller100","");
        list6=gson.fromJson(myResponse6, new TypeToken<List<Books>>() {}.getType());
        listofbooklist.add(new ParentModel("Thriller",list6));


        String myResponse7=preferences.getString("Fiction100","");
        list7=gson.fromJson(myResponse7, new TypeToken<List<Books>>() {}.getType());
        listofbooklist.add(new ParentModel("Fiction",list7));



        String myResponse8=preferences.getString("Fantasy100","");
        list8=gson.fromJson(myResponse8, new TypeToken<List<Books>>() {}.getType());
        listofbooklist.add(new ParentModel("Fantasy",list8));

        mainrec.setLayoutManager(new LinearLayoutManager(getActivity()));
        parentAdapter=new ParentAdapter(getActivity(),listofbooklist);
        mainrec.setAdapter(parentAdapter);
        parentAdapter.notifyDataSetChanged();


        nb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rr.setVisibility(View.GONE);
                GridAdapter gridAdapter=new GridAdapter(getActivity(),list8);
                gv.setAdapter(gridAdapter);
            }
        });





    }

    void run(String book) throws IOException {

        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("https://hapi-books.p.rapidapi.com/search/"+book)
                .get()
                .addHeader("X-RapidAPI-Key", "13624fc66fmshaa5fb2e9fb93382p181eaajsn80f1f5c01158")
                .addHeader("X-RapidAPI-Host", "hapi-books.p.rapidapi.com")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
                final String myResponse = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GsonBuilder gsonBuilder=new GsonBuilder();
                        Gson gson=gsonBuilder.create();
                        searchArrayList=gson.fromJson(myResponse, new TypeToken<List<SearchBook>>() {}.getType());
                        srec.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
                        SL.setVisibility(View.VISIBLE);
                        searchAdapter=new SearchAdapter(getActivity(),searchArrayList);
                        srec.setAdapter(searchAdapter);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }


        });
    }
}