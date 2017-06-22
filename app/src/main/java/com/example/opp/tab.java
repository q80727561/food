package com.example.opp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 8 on 2017/6/8.
 */

public class tab{
        public static final int []mTabRes = new int[]{R.drawable.ichomeblack,
                R.drawable.icfavoriteblack,
                R.drawable.icpeopleblack,R.drawable.icmorevertblack};
    public static final int []mTabRess = new int[]{R.drawable.ichomewhite,
            R.drawable.icfavoritewhite,
            R.drawable.icpeoplewhite,R.drawable.icmorevertwhite};
        public static final String []mTabTitle = new String[]{"首頁","最愛","會員","更多"};
        public static Fragment[] getFragments(Double latitude,Double longitude){
            Fragment fragments[] = new Fragment[4];
            fa1 fa= new fa1();
            fa.newInstances(latitude,longitude);
            fragments[0] = fa;
            childtab childtab1=new childtab();
            childtab1.newInstance(latitude,longitude);
            fragments[1] = childtab1;
            fragments[2] = fa3.newInstance();
            fragments[3] = fa4.newInstance();
            return fragments;
        }
        public static View getTabView(Context context, int position){
            View view = LayoutInflater.from(context).inflate(R.layout.customtab,null);
            ImageView tabIcon = (ImageView) view.findViewById(R.id.imageView);
            TextView tabText = (TextView) view.findViewById(R.id.news_title);
            tabIcon.setImageResource(tab.mTabRess[position]);
            tabText.setText(mTabTitle[position]);
            return view;
        }
}
