package com.example.opp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


/**
 * Created by 8 on 2017/6/6.
 */

public class childtab extends Fragment {
    private faadapter pagerAdapter;
    double latitude,longitude;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ReceiveBroadCast receiveBroadCast;
    String edit,city,location;
    public static childtab newInstance() {
        childtab pageFragment = new childtab();
        return pageFragment;
    }
    public  void newInstance( double latitude,double longitude) {
        this.longitude=longitude;
        this.latitude=latitude;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chidtab, container, false);
        FragmentManager childFragmentManager=getChildFragmentManager();
        pagerAdapter = new faadapter(childFragmentManager,latitude,longitude);
        viewPager = (ViewPager) view.findViewById(R.id.vi);
        viewPager.setAdapter(pagerAdapter);
        tabLayout = (TabLayout) view.findViewById(R.id.sl);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        requestUserLocation();
        return view;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiveBroadCast = new ReceiveBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("search");
        getActivity().registerReceiver(receiveBroadCast, filter);
    }
    class ReceiveBroadCast extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {

               edit = intent.getExtras().getString("edit");
               city = intent.getExtras().getString("city");
               location = intent.getExtras().getString("location");
               pagerAdapter.changeId(1, edit, city, location,latitude,longitude);
               pagerAdapter.notifyDataSetChanged();
               viewPager.setAdapter(pagerAdapter);

        }
    }
    @Override
    public void onDestroyView() {
        getActivity().unregisterReceiver(receiveBroadCast);
        super.onDestroyView();
    }
    public void requestUserLocation() {
        final LocationManager mLocation = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        if(mLocation.isProviderEnabled(LocationManager.GPS_PROVIDER)|| mLocation.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getActivity(),"權限沒開",Toast.LENGTH_SHORT).show();
                return;
            }
            mLocation.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, new LocationListener() {
                @Override
                public void onLocationChanged(final Location location) {
                    latitude=location.getLatitude();
                    longitude=location.getLongitude();
                    try{
                    Toast.makeText(getActivity(),"已取得目前位置",Toast.LENGTH_SHORT).show();
                        button();
                    }
                    catch (Exception e){

                    }

                }
                @Override
                public void onStatusChanged(final String s, final int i, final Bundle bundle) {
                }
                @Override
                public void onProviderEnabled(final String s) {
                }
                public void onProviderDisabled(final String s) {
                }
            },  getActivity().getMainLooper());
        } else {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            latitude=25;
            longitude=121;
        }
    }
    void button(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pagerAdapter.changeId(1, edit, city, location,latitude,longitude);
                pagerAdapter.notifyDataSetChanged();
                viewPager.setAdapter(pagerAdapter);
            }
        });
    }
}
