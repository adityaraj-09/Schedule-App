package com.example.timble;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class ChatGptFragment extends Fragment {

    TextView result;
    Button get,geti;
    EditText srch,si;
    ProgressDialog progressDialog;
    ArrayList<ChatgptImage> list;
    ChatGptAdapter chatGptAdapter;
    RecyclerView  gi;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_chat_gpt, container, false);
        result=v.findViewById(R.id.result);
        get=v.findViewById(R.id.get);
        srch=v.findViewById(R.id.srch);
        geti=v.findViewById(R.id.geti);
        si=v.findViewById(R.id.si);
        gi=v.findViewById(R.id.gi);
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String txt=srch.getText().toString();
                if(txt.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Enter search query", Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        run1(txt);
                    } catch (IOException e) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        geti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String txt=si.getText().toString();
                if(txt.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Enter search query", Toast.LENGTH_SHORT).show();
                }else {
                    try {
                      generateImage(txt);
                    } catch (IOException e) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    void run1(String txt) throws IOException {
        OkHttpClient client = new OkHttpClient();
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        MediaType mediaType = MediaType.parse("application/json");
        String value = "{\"model\": \"gpt-3.5-turbo\",\"messages\": [{\"role\": \"user\",\"content\":\""+txt+"\"}]}";
        RequestBody body = RequestBody.create(value,mediaType);
        Request request = new Request.Builder()
                .url("https://openai80.p.rapidapi.com/chat/completions")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("X-RapidAPI-Key", "0f7c6865e1msh354ced5678e7ddep1f9338jsn57f866913ff1")
                .addHeader("X-RapidAPI-Host", "openai80.p.rapidapi.com")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
                assert response.body() != null;
                final String myResponse = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            JSONObject jsonObject = new JSONObject(myResponse);
                            JSONArray getSth = jsonObject.getJSONArray("choices");
                            JSONObject chat1= (JSONObject) getSth.getJSONObject(0).get("message");
                            Object level = chat1.get("content");
                            progressDialog.dismiss();
                            result.setText(level.toString());
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), ""+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                call.cancel();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), ""+e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

            }


        });
    }
    void generateImage(String txt) throws IOException{
        OkHttpClient client = new OkHttpClient();
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        MediaType mediaType = MediaType.parse("application/json");
        String value = "{\r\n    \"prompt\": \""+txt+"\",\r\n    \"n\": 10,\r\n    \"size\": \"256x256\"\r\n}";
        RequestBody body = RequestBody.create(value,mediaType);
        Request request = new Request.Builder()
                .url("https://openai80.p.rapidapi.com/images/generations")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("X-RapidAPI-Key", "13624fc66fmshaa5fb2e9fb93382p181eaajsn80f1f5c01158")
                .addHeader("X-RapidAPI-Host", "openai80.p.rapidapi.com")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
                assert response.body() != null;
                final String myResponse = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            JSONObject jsonObject = new JSONObject(myResponse);
                            JSONArray getSth = jsonObject.getJSONArray("data");
                            GsonBuilder gsonBuilder=new GsonBuilder();
                            Gson gson=gsonBuilder.create();
                            list=new ArrayList<>();
                            list=gson.fromJson(getSth.toString(), new TypeToken<List<ChatgptImage>>() {}.getType());
                            gi.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
                            chatGptAdapter =new ChatGptAdapter(getActivity(),list);
                            gi.setAdapter(chatGptAdapter);
                            progressDialog.dismiss();

                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), ""+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                call.cancel();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), ""+e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

            }


        });


    }

}