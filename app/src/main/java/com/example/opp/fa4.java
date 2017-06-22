package com.example.opp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 8 on 2017/6/6.
 */

public class fa4 extends Fragment implements View.OnClickListener {
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    String kk;
    about about;
    Button Button,Button1,Button2;
    database helper;
    public static fa4 newInstance() {
        fa4 pageFragment = new fa4();
        return pageFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fa4, container, false);
        mPage=0;
        Button=(Button)view.findViewById(R.id.button);
        Button1=(Button)view.findViewById(R.id.button6);
        Button2=(Button)view.findViewById(R.id.button7);
        Button.setOnClickListener(this);
        Button1.setOnClickListener(this);
        Button2.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button:
                helper=new database(getActivity());
                new Task().execute();
            break;
            case R.id.button6:
                 about = new about();
                about.show(getActivity().getFragmentManager(),"關於作者");
                break;
            case R.id.button7:
                about = new about();
                about.show(getActivity().getFragmentManager(),"關於作品");
                break;
        }
    }
    class Task extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            reading readings = new reading();
            readings.show(getActivity().getFragmentManager(),"readings");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://jasonlol.esy.es/updatadb.php")
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                             @Override
                             public void onResponse(Call call, Response response) throws IOException {
                                 String json = response.body().string();

                                 try {
                                     String s="";
                                     JSONObject Jobject = new JSONObject(json);
                                     JSONArray Jarray = Jobject.getJSONArray("products");
                                     JSONArray Jarrays = Jobject.getJSONArray("version");
                                     for (int i = 0; i < Jarrays.length(); i++) {
                                         JSONObject c = Jarrays.getJSONObject(i);
                                       s= c.getString("name");
                                     }
                                     if(s.equals(helper.select())){
                                         mPage=3;
                                     }else{
                                         helper.addUser(s);
                              kk=helper.addUsers(Jarray);
                                     mPage=1;}
                                 }catch (JSONException e) {
                                     e.printStackTrace();
                                 }
                             }
                             @Override
                             public void onFailure(Call call, IOException e) {
                                 mPage=2;
                             }
                         }
            );

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            while (mPage==0){

            }
            if(mPage==2)
                Toast.makeText(getActivity(),"更新失敗(可能網路不佳或沒開網路)",Toast.LENGTH_LONG).show();
            else  if(mPage==1)
                Toast.makeText(getActivity(),"更新成功"+kk,Toast.LENGTH_LONG).show();
            else  if(mPage==3)
                Toast.makeText(getActivity(),"已是最新版本",Toast.LENGTH_SHORT).show();
            reading readings= (reading) getActivity().getFragmentManager()
                    .findFragmentByTag("readings");
            readings.dismiss();
            mPage=0;
        }
    }
}
