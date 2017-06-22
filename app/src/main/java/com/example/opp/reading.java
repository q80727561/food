package com.example.opp;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 8 on 2017/6/15.
 */

public class reading extends DialogFragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {   if(getTag()=="reading")
        getDialog().setTitle("取得定位");
       else if(getTag()=="createuser")
            getDialog().setTitle("註冊中");
        else if(getTag()=="readings")
        getDialog().setTitle("更新資料庫");
    else if(getTag()=="loginuser")
        getDialog().setTitle("登入中");
        View view = inflater.inflate(R.layout.reading, container);
        return  view;
    }
}
