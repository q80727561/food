package com.example.opp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by 8 on 2017/6/12.
 */

public class locationdetail extends Dialog implements View.OnClickListener, AdapterView.OnItemClickListener {
    Toolbar mToolbar;
    structure st;
    int is;
    public locationdetail(@NonNull final Context context, final structure st) {
        super(context, R.style.MyDialogTheme);
        setContentView(R.layout.locationdetail);
        this.st=st;
        final LayoutInflater inflater=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageView Imageviews = (ImageView)findViewById(R.id.imageView2);
        new custad.DownLoadImageTask(Imageviews).execute(st.image);
        mToolbar = (Toolbar) findViewById(R.id.toolbar3);
        mToolbar.setTitle("店家資訊");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(this);
        TextView textView = (TextView)findViewById(R.id.textView);
        ListView ListView=(ListView) findViewById(R.id.ls);
        BaseAdapter BaseAdapters=new BaseAdapter(){
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                view = inflater.inflate(R.layout.base, null);
                TextView textView5 = (TextView)view.findViewById(R.id.textView5);
                if(i==0)
                textView5.setText("看地圖:"+st.address);
                else
                textView5.setText("打電話:"+st.phone+"(可按)");
                return view;
            }
        };
        ListView.setAdapter(BaseAdapters);
        ListView.setOnItemClickListener(this);
        textView.setText(st.name);
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(i==0){
            Uri gmmIntentUri = Uri.parse("geo:0,0?q="+st.latitude+","+st.longitude+"("+st.name+")");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            getContext().startActivity(mapIntent);
        }else{
            if(is==0){
                Toast.makeText(getContext(),"再按一次就撥號",Toast.LENGTH_SHORT).show();
                is++;
            }else{
            Intent intentDial = new Intent("android.intent.action.CALL", Uri.parse("tel:"+st.phone));
            getContext().startActivity(intentDial);
            is=0;
            }
        }
    }
}