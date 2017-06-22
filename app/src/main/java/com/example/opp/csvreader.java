package com.example.opp;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by 8 on 2017/6/5.
 */

public class csvreader {
    private AssetManager am;
    public csvreader(Context context){
        am=context.getAssets();
    }
    public ArrayList<String> readFile(String file){
        ArrayList<String> list=new ArrayList<String>();
        try {
            InputStream in = am.open(file);
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(in));
            while(br.ready()){
                String str=br.readLine();
                if(str==null ||str.equals("")) {
                    continue;
                }
                list.add(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
