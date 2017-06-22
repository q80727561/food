package com.example.opp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by 8 on 2017/6/12.
 */

public class custad extends BaseAdapter  {
    private final LayoutInflater inflater;
    database helper;
    ArrayList<structure> list;
   String fa[]={"想去","去過","收藏","移出"};
    Context context;
    Button Button;
    public custad(Context context, ArrayList<structure> list) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list=list;
        this.context=context;
        helper=new database(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view,ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.listviewitems, null);
        TextView  textView = (TextView) view.findViewById(R.id.textView);
        TextView  textView1 = (TextView) view.findViewById(R.id.textView1);
        TextView  textView2 = (TextView) view.findViewById(R.id.textView2);
         Button = (Button) view.findViewById(R.id.button4);

        if(list.get(i).fv==null){
            Button.setText("加入最愛");
        }else{
            Button.setText(list.get(i).fv);
        }
        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new AlertDialog.Builder(context).setTitle("選擇")
                        .setItems(fa, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                 if(which!=3){
                                if(list.get(i).fv==null){
                                list.get(i).fv=fa[which];
                                    helper.update(list.get(i).id,list.get(i).fv);
                                refresh(list);}
                                else if(!list.get(i).fv.equals(fa[which])){
                                    list.get(i).fv=fa[which];
                                    helper.update(list.get(i).id,list.get(i).fv);
                                    refresh(list);}
                            }else if(list.get(i).fv!=null){
                                     list.get(i).fv=null;
                                     helper.update(list.get(i).id,list.get(i).fv);
                                     refresh(list);

                                 }}
                        })
                        .show();
            }
        });
        ImageView Imageviews = (ImageView)view.findViewById(R.id.imageView5);
        new DownLoadImageTask(Imageviews).execute(list.get(i).image);
        if(list.get(i).distance!=0)
        textView2.setText(getdis(list.get(i).distance));
        else
            textView2.setText("");
        textView1.setText(list.get(i).address);
        textView.setText(list.get(i).name);
        return view;
    }
    String getdis(double s){
        if(s < 1000 )
            return   String.valueOf((int) s) + "公尺";
        else
            return    String.format("%.2f", s/1000) + "公里";
    }
    public void refresh(ArrayList<structure> lists) {
        list = lists;
        notifyDataSetChanged();
    }
    static class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {
        ImageView imageView;
        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }
        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){
                e.printStackTrace();
            }
            return logo;
        }
        protected void onPostExecute(Bitmap result){
            if(result!=null)
            imageView.setImageBitmap(result);
        }
    }
}
