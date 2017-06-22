package com.example.opp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/*
 * Created by 8 on 2017/6/6.
 */

public class chidtabfa1 extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    ListView ListView;
    String edit,city,location;
    database helper;
    double latitude,longitude;
    ArrayList<structure> list;
    public static chidtabfa1 newInstance(int page,String edit,String city,String location,double latitude,double longitude) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putString("edit", edit);
        args.putString("city", city);
        args.putString("location", location);
        args.putDouble("latitude", latitude);
        args.putDouble("longitude", longitude);
        chidtabfa1 pageFragment = new chidtabfa1();
        pageFragment.setArguments(args);
        return pageFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.childtabfa1, container, false);
        mPage = getArguments().getInt(ARG_PAGE);
        edit = getArguments().getString("edit");
        city = getArguments().getString("city");
        location = getArguments().getString("location");
        latitude = getArguments().getDouble("latitude");
        longitude = getArguments().getDouble("longitude");
         ListView=(ListView) view.findViewById(R.id.g);
        helper=new database(getActivity());
        list=helper.select(mPage,edit,city,location,latitude,longitude);
        custad custad1= new custad(getActivity(),list);
        ListView.setAdapter(custad1);
        ListView.setOnItemClickListener(this);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(this);
        return view;
    }
    @Override
    public void onClick(View view) {
        search searchs = new search();
        searchs.show(getActivity().getFragmentManager(),"search2");
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        new locationdetail(getActivity(),list.get(i)).show();
    }
}
