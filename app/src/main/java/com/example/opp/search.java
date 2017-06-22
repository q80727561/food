package com.example.opp;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by 8 on 2017/6/7.
 */

public class search extends DialogFragment implements View.OnClickListener, Toolbar.OnMenuItemClickListener, AdapterView.OnItemSelectedListener {
    private Toolbar mToolbar;
    Button Button;
    Spinner Spinner1,Spinner2;
    database helper;
    String tag;
    double longitude,latitude;
    EditText EditText;
    ArrayAdapter<String> adapter;
    String[][] type2 = new String[][]{{"1公里內"},{"全部"},
            {"全部","內湖區","士林區","中正區","松山區"}, {"全部","淡水區","八里區","永和區"},
            {"全部","桃園區","中壢區","八德區"},{"全部","仁愛區","中正區","信義區"},
            {"全部","東區","北區","香山區"}};
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialogTheme);
    }
    public  void newInstance(double longitude,double latitude,String tag) {
        this.longitude=longitude;
        this.latitude=latitude;
        this.tag=tag;
    }
    public  void newInstance(double longitude,double latitude) {
        this.longitude=longitude;
        this.latitude=latitude;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.search, container);
        mToolbar = (Toolbar)view.findViewById(R.id.toolbar);
        Spinner1 = (Spinner)view.findViewById(R.id.spinner4);
        Spinner2 = (Spinner)view.findViewById(R.id.spinner5);
        EditText=(EditText)view.findViewById(R.id.editText);
        Spinner1.setOnItemSelectedListener(this);
        mToolbar.setTitle("查詢美食");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(this);
        mToolbar.inflateMenu(R.menu.main3);
        mToolbar.setOnMenuItemClickListener(this);
        helper=new database(getActivity());
        Button=(Button)view.findViewById(R.id.button3);
        Button.setOnClickListener(this);
        return  view;
    }
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.button3){
            if(getTag()=="search1"){
                location locations;
                if(tag=="location")
               locations = (location) getFragmentManager()
                        .findFragmentByTag("location");
                else
                    locations = (location) getFragmentManager()
                            .findFragmentByTag("searchs");
                Toolbar Toolbar= (Toolbar) locations.getView()
                        .findViewById(R.id.toolbar2);
                Toolbar.setTitle("搜尋:"+EditText.getText().toString());
                    locations.search(Spinner1.getSelectedItemPosition(),Spinner2.getSelectedItemPosition(),EditText.getText().toString());
            }else if(getTag()=="search2"){
                Intent intent = new Intent();
                intent.setAction("search");
                intent.putExtra("edit",EditText.getText().toString());
                intent.putExtra("city",Spinner1.getSelectedItem().toString());
                intent.putExtra("location",Spinner2.getSelectedItem().toString());
                getActivity().sendBroadcast(intent);
            }else if(getTag()=="search"){
                location locations = new location();
                locations.newInstance(latitude,longitude);
                locations.newInstance(Spinner1.getSelectedItemPosition(), Spinner2.getSelectedItemPosition(),EditText.getText().toString());
                locations.show(getActivity().getFragmentManager(),"searchs");
            }
        }
        dismiss();
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, type2[i]);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner2.setAdapter(adapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
