package com.example.opp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by 8 on 2017/6/6.
 */

public class oppactivity extends AppCompatActivity{
    private TabLayout mTabLayout;
    private Fragment []mFragmensts;
    SharedPreferences k;
    double latitude;
    double longitude;
    String l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintab);
        k=this.getSharedPreferences("storepwd", Context.MODE_PRIVATE);
        l=k.getString("pwd", null);
        if(l==null){
            new csv(this,"person.csv");
            SharedPreferences.Editor editor=k.edit();
            editor.putString("pwd","lol");
            editor.commit();
        }
        new Task().execute();
    }
    private void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tabs) {
                onTabItemSelected(tabs.getPosition());
                for (int i=0;i<mTabLayout.getTabCount();i++){
                    View view = mTabLayout.getTabAt(i).getCustomView();
                    TextView text = (TextView) view.findViewById(R.id.news_title);
                    ImageView tabIcon = (ImageView) view.findViewById(R.id.imageView);
                    if(i == tabs.getPosition()){
                        tabIcon.setImageResource(tab.mTabRes[i]);
                        text.setTextColor(getResources().getColor(android.R.color.black));
                    }else{
                        text.setTextColor(getResources().getColor(android.R.color.white));
                        tabIcon.setImageResource(tab.mTabRess[i]);
                    }
                }
        }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        for (int i = 0; i < 4; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setCustomView(tab.getTabView(this,i)));
            if(i!=0){
                View view = mTabLayout.getTabAt(i).getCustomView();
                TextView text = (TextView) view.findViewById(R.id.news_title);
                text.setTextColor(getResources().getColor(android.R.color.white));
            }
        }
    }

    private void onTabItemSelected(int position){
        Fragment fragment = null;
        fragment = mFragmensts[position];
        if(fragment!=null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.home_container,fragment).commit();
        }
    }
    public void requestUserLocation() {

        final LocationManager mLocation = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if(mLocation.isProviderEnabled(LocationManager.GPS_PROVIDER)|| mLocation.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            if (ActivityCompat.checkSelfPermission(oppactivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(oppactivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"權限沒開",Toast.LENGTH_SHORT).show();
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
            },  this.getMainLooper());}
        } else {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            latitude=25;
            longitude=121;
        }
    }
    class Task extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            reading readings = new reading();
            readings.show(getFragmentManager(),"reading");
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
            reading readings= (reading) getFragmentManager()
                    .findFragmentByTag("reading");
            readings.dismiss();
          if( latitude==25){
              Toast.makeText(oppactivity.this,"定位失敗",Toast.LENGTH_SHORT).show();
          }else
            Toast.makeText(oppactivity.this,"已取得目前位置",Toast.LENGTH_SHORT).show();
        }
    }
    void button(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mFragmensts=tab.getFragments(latitude,longitude);
                initView();
            }
        });
    }
}

