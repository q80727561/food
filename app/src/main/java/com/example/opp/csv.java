package com.example.opp;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by 8 on 2017/6/5.
 */

public class csv {
    String id;
    public ArrayList<String> csvList;
    public csvreader reader;
    database helper;
    public csv(Context context, String fileName){
        reader=new csvreader(context);
        csvList=reader.readFile(fileName);
        helper=new database(context);
        helper.addUser(csvList);
        helper.addUser("1.0");
    }
}
