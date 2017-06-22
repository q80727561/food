package com.example.opp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by 8 on 2017/6/6.
 */

public class fa1 extends Fragment implements View.OnClickListener {
    ImageButton Im,Im1,Im2;
    private ReceiveBroadCast receiveBroadCast;
    double latitude;
    double longitude;
    public static fa1 newInstance() {
        fa1 pageFragment = new fa1();
        return pageFragment;
    }
    public void newInstances(double latitude,double longitude){
        this.longitude=longitude;
        this.latitude=latitude;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fa1, container, false);
        Im= (ImageButton)view.findViewById(R.id.imageButton);
        Im1= (ImageButton)view.findViewById(R.id.imageButton3);
        Im2= (ImageButton)view.findViewById(R.id.imageButton2);
        Im.setOnClickListener(this);
        Im1.setOnClickListener(this);
        Im2.setOnClickListener(this);
        setHasOptionsMenu(true);
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        if (id == R.id.search) {
            search searchs = new search();
            searchs.newInstance(longitude,latitude);
            searchs.show(getActivity().getFragmentManager(),"search");
            return true;
        } else if (id == R.id.recommend) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("mailto:101405029@stud.sju.edu.tw"));
            intent.putExtra(Intent.EXTRA_SUBJECT,"推薦美食");
            intent.putExtra(Intent.EXTRA_TEXT,"店名:\n地址:\n店家電話:\n");
            startActivity(Intent.createChooser(intent, "選擇寄件方式"));
            return true;
        }else if (id == R.id.repoet) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("mailto:101405029@stud.sju.edu.tw"));
            intent.putExtra(Intent.EXTRA_SUBJECT,"建議回報");
            startActivity(Intent.createChooser(intent, "選擇寄件方式"));
            return true;}
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
            switch (view.getId()){
                case R.id.imageButton:
                    location locations = new location();
                    locations.newInstance(latitude,longitude);
                    locations.show(getActivity().getFragmentManager(),"location");
                    break;
                case R.id.imageButton3:
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("mailto:101405029@stud.sju.edu.tw"));
                    intent.putExtra(Intent.EXTRA_SUBJECT,"推薦美食");
                    intent.putExtra(Intent.EXTRA_TEXT,"店名:\n地址:\n店家電話:\n");
                    startActivity(Intent.createChooser(intent, "選擇寄件方式"));
                    break;
                case R.id.imageButton2:
                    nightmarket nightmarkets = new nightmarket();
                    nightmarkets.newInstance(latitude,longitude);
                    nightmarkets.show(getActivity().getFragmentManager(),"nightmarket");
                    break;
            }
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiveBroadCast = new ReceiveBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("getgps");
        getActivity().registerReceiver(receiveBroadCast, filter);
    }
    class ReceiveBroadCast extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            latitude=intent.getExtras().getDouble("latitude");
            longitude=intent.getExtras().getDouble("longitude");
        }
    }
    @Override
    public void onDestroyView() {
        getActivity().unregisterReceiver(receiveBroadCast);
        super.onDestroyView();
    }
}
