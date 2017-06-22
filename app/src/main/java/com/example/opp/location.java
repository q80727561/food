package com.example.opp;

import android.Manifest;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by 8 on 2017/6/8.
 */

public class location extends DialogFragment implements View.OnClickListener, Toolbar.OnMenuItemClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
    double latitude,longitude;
    Spinner Spinner1,Spinner2;
    database helper;
    String edits;
    ListView ListView;
    ArrayList<structure> list,lists;
    ArrayAdapter<String> adapter;
    int flag,a2,a3;
    String[][] type2 = new String[][]{{"1公里內"},{"全部"},
            {"全部","內湖區","士林區","中正區","松山區"}, {"全部","淡水區","八里區","永和區"},
            {"全部","桃園區","中壢區","八德區"},{"全部","仁愛區","中正區","信義區"},
            {"全部","東區","北區","香山區"}};
    custad custad1;
    private Toolbar mToolbar;
    public  void newInstance(int a3,int a2,String edit) {
           this.a2=a2;
        this.a3=a3;
        this.edits=edit;

    }
    public  void newInstance( double latitude,double longitude) {
        this.longitude=longitude;
        this.latitude=latitude;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialogTheme);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

      View view = inflater.inflate(R.layout.location, container);
        ListView=(ListView) view.findViewById(R.id.f);
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab5);
        fab.setOnClickListener(this);
        mToolbar = (Toolbar)view.findViewById(R.id.toolbar2);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(this);
        mToolbar.inflateMenu(R.menu.main4);
        mToolbar.setOnMenuItemClickListener(this);
        Spinner1 = (Spinner)view.findViewById(R.id.spinner3);
        Spinner2 = (Spinner)view.findViewById(R.id.spinner2);
        Spinner1.setOnItemSelectedListener(this);
        Spinner2.setOnItemSelectedListener(this);
        helper=new database(getActivity());
        if(getTag()=="location"){
        mToolbar.setTitle("附近美食");
            list=helper.select(latitude,longitude,edits);
            custad1= new custad(getActivity(),list);
            flag=1;
        }
        else if(getTag()=="searchs"){
            mToolbar.setTitle("搜尋:" + edits);
            list=new ArrayList();
            custad1= new custad(getActivity(),list);
            Spinner1.setSelection(a3);
        }else{
            mToolbar.setTitle(getTag());
            list=helper.select(latitude,longitude,edits);
            custad1= new custad(getActivity(),list);
            flag=1;
        }
        ListView.setAdapter(custad1);
        ListView.setOnItemClickListener(this);

           return  view;
    }
        @Override
    public void onClick(View view) {
       if(view.getId()==R.id.fab5){
           if(latitude==0){
               Toast.makeText(getActivity(),"未定位",Toast.LENGTH_SHORT).show();
           }else{
           search searchs = new search();
               searchs.newInstance(longitude,latitude,getTag());
           searchs.show(getFragmentManager(),"search1");}
       }else
           dismiss();
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        latitude=0;
        new Task().execute();
        return false;
    }
    public void requestUserLocation() {
        final LocationManager mLocation = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        if(mLocation.isProviderEnabled(LocationManager.GPS_PROVIDER)|| mLocation.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getActivity(),"權限沒開",Toast.LENGTH_SHORT).show();
                return;
            }
            Location location =
                    mLocation.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location != null){
                latitude=location.getLatitude();
                longitude=location.getLongitude();
            }else{
            mLocation.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, new LocationListener() {
                @Override
                public void onLocationChanged(final Location location) {
                    latitude=location.getLatitude();
                    longitude=location.getLongitude();

                }
                @Override
                public void onStatusChanged(final String s, final int i, final Bundle bundle) {
                }
                @Override
                public void onProviderEnabled(final String s) {
                }
                public void onProviderDisabled(final String s) {
                }
            },  getActivity().getMainLooper());}
        } else {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            latitude=25;
            longitude=121;
        }
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        new locationdetail(getActivity(),list.get(i)).show();
    }
   public void search(int a1,int a2,String edits){
       this.a2=a2;
       this.edits=edits;
      if(a1==Spinner1.getSelectedItemPosition()) {
          if(!Spinner1.getSelectedItem().toString().equals("附近")){
          lists = helper.select(Spinner1.getSelectedItem().toString(), Spinner2.getSelectedItem().toString(), longitude, latitude, edits);}
          else
              lists=helper.select(latitude,longitude,edits);
          list.clear();
          for (structure structure : lists)
              list.add(structure);
          custad1.notifyDataSetChanged();
      }  else
       Spinner1.setSelection(a1);
   }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if (adapterView.getId() == R.id.spinner3) {
            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, type2[i]);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner2.setAdapter(adapter);
                Spinner2.setSelection(a2);
                a2=0;
        } else if (adapterView.getId() == R.id.spinner2) {
             if(!Spinner1.getSelectedItem().toString().equals("附近")){
                list.clear();
                lists = helper.select(Spinner1.getSelectedItem().toString(), Spinner2.getSelectedItem().toString(), longitude, latitude,edits);
                for (structure structure : lists)
                    list.add(structure);
                 custad1.notifyDataSetChanged();
                 flag=0;
             }else if(flag==0){
                 list.clear();
                 lists=helper.select(latitude,longitude,edits);
                 for (structure structure : lists)
                     list.add(structure);
                 custad1.notifyDataSetChanged();
             }

        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    class Task extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            requestUserLocation();
            while (latitude==0){

            }
            button();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if( latitude==25){
                Toast.makeText(getActivity(),"定位失敗",Toast.LENGTH_SHORT).show();
            }else{
                Intent intent = new Intent();
                intent.setAction("getgps");
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                getActivity().sendBroadcast(intent);
            Toast.makeText(getActivity(),"已取得目前位置",Toast.LENGTH_SHORT).show();}
        }
    }
    void button(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!Spinner1.getSelectedItem().toString().equals("附近"))
                lists = helper.select(Spinner1.getSelectedItem().toString(), Spinner2.getSelectedItem().toString(), longitude, latitude,edits);
                else
                    lists=helper.select(latitude,longitude,edits);
                list.clear();
                for (structure structure : lists)
                    list.add(structure);
                custad1.notifyDataSetChanged();
            }
        });
    }

}
