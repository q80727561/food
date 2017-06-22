package com.example.opp;

import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by 8 on 2017/6/19.
 */

public class createuser extends DialogFragment implements View.OnClickListener {
    Toolbar mToolbar;
    EditText EditText,EditText1;
    Button  Button;
    int mPage;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.createuser, container);
        mToolbar = (Toolbar)view.findViewById(R.id.toolbar6);
        mToolbar.setTitle("註冊帳號");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(this);
        Button=(Button)view.findViewById(R.id.button9);
        Button.setOnClickListener(this);
        EditText=(EditText)view.findViewById(R.id.editText3);
        EditText1=(EditText)view.findViewById(R.id.editText4);
     return  view;
    }
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.button9){
            if(!EditText.getText().toString().equals("")&&!EditText1.getText().toString().equals("")&&EditText1.getText().toString().length()>5&&EditText.getText().toString().length()>5){
                     new Task().execute();
            }else{
                Toast.makeText(getActivity(),"帳號和密碼不可為空並且長度都要至少6",Toast.LENGTH_LONG).show();
            }
        }else
      dismiss();

    }
    public  void post(){
        FormBody body = new FormBody.Builder()
                .add("name", EditText.getText().toString())
                .add("pass",EditText1.getText().toString())
                .build();
        Request request = new Request.Builder()
                .url("http://jasonlol.esy.es/createusers.php")
                .post(body)
                .build();
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
                            }else if(s.equals("Oops! name is exist")){
                                mPage=3;
                            }else if(s.equals("Required field(s) is missing")||s.equals("Oops! An error occurred")){
                                mPage=4;
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
        @Override
        protected void onPreExecute() {
            reading readings = new reading();
            readings.show(getFragmentManager(),"createuser");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            post();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            while (mPage==0){

            }
            reading readings= (reading) getFragmentManager()
                    .findFragmentByTag("createuser");
            readings.dismiss();
            if(mPage==2)
                Toast.makeText(getContext(),"註冊失敗(可能網路不佳或沒開網路)",Toast.LENGTH_LONG).show();
            else if(mPage==1){

                Toast.makeText(getActivity(),"成功註冊",Toast.LENGTH_SHORT).show();
                dismiss();
            }else if(mPage==3){
                Toast.makeText(getActivity(),"帳號重複",Toast.LENGTH_SHORT).show();
            }
            else if(mPage==4){
                Toast.makeText(getActivity(),"註冊失敗",Toast.LENGTH_SHORT).show();
            }
            mPage=0;
        }
    }
}
