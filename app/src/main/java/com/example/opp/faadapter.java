package com.example.opp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by 8 on 2017/6/6.
 */

public class faadapter extends FragmentPagerAdapter {
    private long baseId = 0;
    final int PAGE_COUNT = 4;
    double latitude,longitude;
    String edit,city,location;
    private String tabTitlesa[] = new String[]{"全部","想去","去過","收藏"};
    public faadapter(FragmentManager fm,double latitude,double longitude) {
        super(fm);
        this.latitude=latitude;
        this.longitude=longitude;
    }
    @Override
    public Fragment getItem(int position) {
            return chidtabfa1.newInstance(position + 1,edit,city,location,latitude,longitude);
    }
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
    @Override
    public CharSequence getPageTitle(int position) {
            return tabTitlesa[position];
    }
    @Override
    public long getItemId(int position) {
        return baseId + position;
    }
    public void changeId(int n,String edit,String city,String location,double latitude,double longitude) {
        baseId += getCount() + n;
        this.edit=edit;
        this.city=city;
        this.location=location;
        this.latitude=latitude;
        this.longitude=longitude;
    }
}
