package com.example.opp;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
public class about extends DialogFragment implements View.OnClickListener {
    Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialogTheme);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about, container);
        mToolbar = (Toolbar)view.findViewById(R.id.toolbar4);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(this);
        TextView TextView= (TextView)view.findViewById(R.id.textView8);
        if (getTag() == "關於作者"){
            mToolbar.setTitle("關於作者");
            TextView.setText("我叫陳居賢\n非常願意學習新的知識，希望能有機會到貴公司上班，希望可以有面試的機會。");

        }
        else{
            mToolbar.setTitle("關於作品");
            TextView.setText("作品的遠端資料庫是我免費伺服器架的。\n資料庫是讀取事先從mysql匯出的csv，資料很少以後會增加。\n如果發現任何問題請回報給我。");



        }
        return view;
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }
}