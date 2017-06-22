package com.example.opp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 8 on 2017/6/7.
 */

public class fa3 extends Fragment implements View.OnClickListener {
    public static final String ARG_PAGE = "ARG_PAGE";
    String sa,y1="new",y2="new",y3="new";
    EditText EditText,EditText1;
    private int mPage;
    ArrayList<String> list,lists;
    database helper;
    TextView TextView,TextView1,TextView2;
    Button Button,Button1,Button2;

    public static fa3 newInstance() {
        fa3 pageFragment = new fa3();
        return pageFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fa3, container, false);
        mPage=0;
        helper=new database(getActivity());
         TextView=(TextView)view.findViewById(R.id.textView7);
        TextView1=(TextView)view.findViewById(R.id.textView11);
        TextView2=(TextView)view.findViewById(R.id.textView12);
        Button=(Button)view.findViewById(R.id.button5);
        Button1=(Button)view.findViewById(R.id.button11);
        Button2=(Button)view.findViewById(R.id.button10);
        EditText=(EditText)view.findViewById(R.id.editText2);
        EditText1=(EditText)view.findViewById(R.id.editText5);
        list=helper.selectuser();
        Button.setOnClickListener(this);
        TextView.setOnClickListener(this);
        Button2.setOnClickListener(this);
        Button1.setOnClickListener(this);
        if(list.get(0).equals("no")){
            TextView1.setVisibility(View.GONE);
            TextView2.setVisibility(View.GONE);
            Button1.setVisibility(View.GONE);
            Button2.setVisibility(View.GONE);
        }else{
            TextView1.setText(list.get(0));
            TextView.setVisibility(View.GONE);
            Button.setVisibility(View.GONE);
            EditText.setVisibility(View.GONE);
            EditText1.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.textView7){
            createuser  createuser = new createuser();
            createuser.show(getActivity().getFragmentManager(),"createuser");
        }else if(view.getId()==R.id.button5){

            new Task("post").execute();

        }else if(view.getId()==R.id.button11){
            TextView1.setVisibility(View.GONE);
            TextView2.setVisibility(View.GONE);
            Button1.setVisibility(View.GONE);
            Button2.setVisibility(View.GONE);
            TextView.setVisibility(View.VISIBLE);
            Button.setVisibility(View.VISIBLE);
            EditText.setVisibility(View.VISIBLE);
            EditText1.setVisibility(View.VISIBLE);
            helper.onUpgrades();
            helper.update();
        }else if(view.getId()==R.id.button10){
            lists=helper.selecfv();
             if(!lists.get(0).equals("")){
                    y1=lists.get(0);
             }
            if(!lists.get(1).equals("")){
                y2=lists.get(1);
            }
            if(!lists.get(2).equals("")){
                y3=lists.get(2);
            }
            new Task("post1").execute();
        }
    }
   public  void post(final String a){
       Request request;
       if(a.equals("post")){
        FormBody body = new FormBody.Builder()
                .add("name", EditText.getText().toString())
                .add("pass",EditText1.getText().toString())
                .build();
       request = new Request.Builder()
                .url("http://jasonlol.esy.es/loginuser.php")
                .post(body)
                .build();}
       else{
           ArrayList<String> lista=helper.selectuser();

           FormBody body = new FormBody.Builder()
                   .add("name",lista.get(0))
                   .add("want",y1)
                   .add("been",y2)
                   .add("col",y3)
                   .build();
          request = new Request.Builder()
                   .url("http://jasonlol.esy.es/updatauser.php")
                   .post(body)
                   .build();
       }
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mPage=2;
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {


                    String resp = response.body().string();
                    if(response.isSuccessful()) {
                        try {

                            JSONObject Jobject = new JSONObject(resp);
                            String s=Jobject.getString("message");
                            if(s.equals("successfully")){
                                mPage=1;
                                JSONArray Jarray = Jobject.getJSONArray("users");
                                helper.update();
                                sa=helper.adduser(Jarray);

                            }else if(s.equals("not")){
                                mPage=3;
                            }else if(s.equals("Required field(s) is missing")){
                                mPage=4;
                            }else if(s.equals("successfully up")){
                                mPage=5;
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }


                    } else {



                    }
                } catch(IOException e) {
                }
            }
        });
    }

    class Task extends AsyncTask<Void,Void,Void> {
        String s;
        @Override
        protected void onPreExecute() {

        }
        public Task(String s){
            this.s = s;
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            post(s);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            while (mPage==0){

            }
            if(mPage==2)
                if(s.equals("post"))
                Toast.makeText(getContext(),"登入失敗(可能網路不佳或沒開網路)",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getContext(),"上傳失敗(可能網路不佳或沒開網路)",Toast.LENGTH_LONG).show();
            else if(mPage==1){

                while (sa==null) {

                }
                if(s.equals("post"))
                Toast.makeText(getActivity(),"成功登入",Toast.LENGTH_SHORT).show();
                button();
                sa=null;
            }else if(mPage==3){
                if(s.equals("post"))
                Toast.makeText(getActivity(),"帳號不存在或密碼錯誤",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getActivity(),"上傳失敗",Toast.LENGTH_LONG).show();
            }
            else if(mPage==4){
                if(s.equals("post"))
                Toast.makeText(getActivity(),"登入失敗",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getActivity(),"上傳失敗",Toast.LENGTH_SHORT).show();
            }
            else if(mPage==5){
                Toast.makeText(getActivity(),"上傳成功",Toast.LENGTH_SHORT).show();
            }
            mPage=0;
        }
    }
    void button(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView1.setVisibility(View.VISIBLE);
                TextView2.setVisibility(View.VISIBLE);
                Button1.setVisibility(View.VISIBLE);
                Button2.setVisibility(View.VISIBLE);
                TextView.setVisibility(View.GONE);
                Button.setVisibility(View.GONE);
                EditText.setVisibility(View.GONE);
                EditText1.setVisibility(View.GONE);
                TextView1.setText(sa);
            }
        });
    }
}
