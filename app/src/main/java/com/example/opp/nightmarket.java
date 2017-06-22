package com.example.opp;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by 8 on 2017/6/17.
 */

public class nightmarket extends DialogFragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
    Toolbar mToolbar;
    double latitude,longitude;
    ArrayList<structure> list,lists;
    TextView TextView,TextView1;
    ArrayAdapter<String> adapter;
    BaseAdapter BaseAdapters;
    Button Button;
    database helper;
    Spinner Spinner1,Spinner2;
    String[][] type2 = new String[][]{{"全部"},
            {"全部","內湖區","士林區","中正區","松山區"}, {"全部","淡水區","八里區","永和區"},
            {"全部","桃園區","中壢區","八德區"},{"全部","仁愛區","中正區","信義區"},
            {"全部","東區","北區","香山區"}};
    public  void newInstance( double latitude,double longitude) {
        this.longitude=longitude;
        this.latitude=latitude;

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialogTheme);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nightmarket, container);
        ListView ListView=(ListView)view.findViewById(R.id.market);
        helper=new database(getActivity());
        mToolbar = (Toolbar)view.findViewById(R.id.toolbar5);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(this);
        Spinner1 = (Spinner)view.findViewById(R.id.spinner);
        Spinner2 = (Spinner)view.findViewById(R.id.spinner6);
        final LayoutInflater inflaters=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        list=new ArrayList();
        BaseAdapters=new BaseAdapter(){
            @Override
            public int getCount() {
                return list.size();
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
            public View getView(final int i, View view, ViewGroup viewGroup) {
                view = inflaters.inflate(R.layout.marketitem, null);
                TextView=(TextView)view.findViewById(R.id.textView9);
                TextView1=(TextView)view.findViewById(R.id.textView10);
                TextView.setText(list.get(i).name);
                TextView1.setText(list.get(i).address);
                Button = (Button) view.findViewById(R.id.button8);
                Button.setText(getdis(list.get(i).distance));
                Button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        Uri gmmIntentUri = Uri.parse("geo:0,0?q="+list.get(i).latitude+","+list.get(i).longitude+"("+list.get(i).name+")");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        getActivity().startActivity(mapIntent);
                    }
                });
                ImageView Imageviews = (ImageView)view.findViewById(R.id.imageView3);
                new DownLoadImageTask(Imageviews).execute(list.get(i).image);
                return view;
            }

        };
        ListView.setAdapter(BaseAdapters);
        ListView.setOnItemClickListener(this);
        Spinner1.setOnItemSelectedListener(this);
        Spinner2.setOnItemSelectedListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }
    String getdis(double s){
        if(s < 1000 )
            return   String.valueOf((int) s) + "公尺";
        else
            return    String.format("%.2f", s/1000) + "公里";
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        location locations = new location();
        locations.newInstance(list.get(i).latitude,list.get(i).longitude);
        locations.show(getActivity().getFragmentManager(),list.get(i).name+"附近");
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.spinner) {
            adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, type2[i]);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner2.setAdapter(adapter);
        }else {
            lists=helper.select(Spinner1.getSelectedItem().toString(),Spinner2.getSelectedItem().toString(),latitude,longitude);
            list.clear();
            for (structure structure : lists)
                list.add(structure);
            BaseAdapters.notifyDataSetChanged();
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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


