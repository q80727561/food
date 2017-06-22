package com.example.opp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by 8 on 2017/6/5.
 */

public class database extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "android_api";
    private static final String TABLE_LOGIN = "food";
    private static final String FAVORITE = "FAVORITE";
    private static final String KEY_ID = "id";
    private static final String city = "city";
    private static final String location = "location";
    private static final String phone = "phone";
    private static final String address = "address";
    private static final String KEY_NAME = "name";
    private static final String type = "type";
    private static final String longitude = "longitude";
    private static final String latitude = "latitude";
    private static final String image = "image";
    private static final String fv = "fv";
    private static final String version = "version";
    String CREATEFAVORIT;
    public database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + city + " TEXT,"
                + location + " TEXT,"
                + phone + " TEXT,"
                + address + " TEXT,"
                + type + " TEXT,"
                + longitude + " DOUBLE,"
                + latitude + " DOUBLE,"
                + image + " VARCHAR,"
                + fv + " VARCHAR"
                + ")";
        String CREATEFAVORITE = "CREATE TABLE " + version + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT"
                + ")";
         CREATEFAVORIT = "CREATE TABLE " + FAVORITE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT"
                + ")";
        sqLiteDatabase.execSQL(CREATE_LOGIN_TABLE);
        sqLiteDatabase.execSQL(CREATEFAVORITE);
        sqLiteDatabase.execSQL(CREATEFAVORIT);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        onCreate(sqLiteDatabase);
    }
    public void onUpgrades() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FAVORITE,"name is not null",null);
        db.close();
    }
    public void addUser (String name) {
        SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues(8);
            cv.put(KEY_NAME,name);
        db.insert(version, null, cv);
        db.close(); // Closing database connection
    }
    public String select( ) {
        SQLiteDatabase db = this.getReadableDatabase();
        String s="";
        String selectQuery = "select * from version";
        Cursor cursor = db.rawQuery(selectQuery, null);
        while(cursor.moveToNext()) {
           s=cursor.getString(1);
        }
        db.close();
        return s;
    }
    public void addUser (ArrayList<String> list) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransactionNonExclusive();
        for(int i = 0;i < list.size();i++) {
            String[] data = list.get(i).split(",");
            if (data.length != 10) {
                Log.d("CSVParser", "Skipping Bad CSV Row");
                continue;
            }
            ContentValues cv = new ContentValues(8);
            cv.put(city, data[2]);
            cv.put(location, data[3]);
            cv.put(KEY_NAME, data[1]);
            cv.put(phone, data[4]);
            cv.put(address, data[5]);
            cv.put(type, data[6]);
            cv.put(longitude, Double.parseDouble(data[7]));
            try{
                cv.put(latitude, Double.parseDouble(data[8]));
            }catch(NumberFormatException e){
                cv.put(latitude,0);
            }catch (Exception e){
                cv.put(latitude,0);
            }
            cv.put(image, data[9]);
            db.insert(TABLE_LOGIN, null, cv);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close(); // Closing database connection
    }
    public String addUsers ( JSONArray Jarray) {
        SQLiteDatabase db = this.getWritableDatabase();
        String s="";
        int add=0,updat=0;
        ContentValues cv = new ContentValues(10);
        Cursor cursor = null;
        db.beginTransactionNonExclusive();
        for (int i = 0; i < Jarray.length(); i++) {
            try{
            JSONObject c = Jarray.getJSONObject(i);
                cv.put(KEY_NAME,c.getString("name"));
                cv.put(city, c.getString("city"));
                cv.put(location, c.getString("location"));
                cv.put(phone, c.getString("phone"));
                cv.put(address,c.getString("adress"));
                cv.put(type, c.getString("type"));
                cv.put(longitude, Double.parseDouble(c.getString("long")));
                cv.put(latitude, Double.parseDouble(c.getString("lat")));
                cv.put(image, c.getString("image"));
                cursor=db.rawQuery("SELECT id FROM food WHERE name='"+c.getString("name")+"'", null);
                if (cursor.getCount()==0) {
                    db.insert(TABLE_LOGIN, null, cv);
                    add++;
                }else{
                        cursor.moveToNext();
                        db.update(TABLE_LOGIN,cv,"id="+cursor.getInt(0),null);
                    updat++;
                }
            }catch (Exception E){

            }
        }
        cursor.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
      s="增加了"+add+"筆更新了"+updat+"筆";
        return  s;
    }
    public ArrayList<structure> select(String city,String location,double latitude, double longitude){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<structure> list=new ArrayList();
        String selectQuery = "select * from food where type like '夜市'";
        String sa,sa1;
        if(city.equals("全部")){
            sa="";
            sa1="";
        }
        else if(location.equals("全部")){
            sa=" and city like '"+city+"'";
            sa1="";
        }else{
            sa=" and city like '"+city+"' and";
            sa1=" location like '"+location+"'";
        }
        selectQuery+=sa+sa1;
        Cursor cursor = db.rawQuery(selectQuery, null);
        while(cursor.moveToNext()){
            float[] results = new float[1];
            structure structure = new structure();
            Location.distanceBetween(latitude, longitude, cursor.getDouble(8), cursor.getDouble(7), results);
            structure.id=cursor.getInt(0);
            structure.name=cursor.getString(1);
            structure.address=cursor.getString(5);
            structure.longitude=cursor.getDouble(7);
            structure.latitude=cursor.getDouble(8);
            structure.image=cursor.getString(9);
            structure.distance=results[0];
            list.add(structure);
        }
        cursor.close();
        db.close(); // Closing database connection
        Collections.sort(list, new Comparator<structure>() {
            @Override
            public int compare(structure o1,
                               structure o2) {
                return Double.compare(o1.distance, o2.distance); // error
            }
        });
        return list;
    }
    public ArrayList<structure> select(double latitude, double longitude,String edit) {
        double R = 6371; // earth radius in km
        double radius = 1; // km
        double longMin = longitude - Math.toDegrees(radius/R/Math.cos(Math.toRadians(latitude)));
        double longMax = longitude + Math.toDegrees(radius/R/Math.cos(Math.toRadians(latitude)));
        double latMax = latitude + Math.toDegrees(radius/R);
        double latMin = latitude - Math.toDegrees(radius/R);
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "select * from food where latitude between '"+latMin+"' and '"+latMax+"' and longitude between '"+longMin+"' and '"+longMax+"'";
        if(edit!=null)
            selectQuery+=" and name like '%"+edit+"%'";
        selectQuery+=" and type !='夜市'";
        Cursor cursor = db.rawQuery(selectQuery, null);

        ArrayList<structure> list=new ArrayList();
        list.clear();
        while(cursor.moveToNext()){
            float[] results = new float[1];
            structure structure = new structure();
            Location.distanceBetween(latitude, longitude, cursor.getDouble(8), cursor.getDouble(7), results);
            structure.id=cursor.getInt(0);
            structure.name=cursor.getString(1);
            structure.address=cursor.getString(5);
            structure.phone=cursor.getString(4);
            structure.longitude=cursor.getDouble(7);
            structure.latitude=cursor.getDouble(8);
            structure.distance=results[0];
            structure.image=cursor.getString(9);
            structure.fv=cursor.getString(10);
            list.add(structure);
        }
        cursor.close();
        db.close(); // Closing database connection
        Collections.sort(list, new Comparator<structure>() {
            @Override
        public int compare(structure o1,
                           structure o2) {
            return Double.compare(o1.distance, o2.distance); // error
        }
        });
        return list;
    }
    public ArrayList<structure> select(  String city,String location,double longitude,double latitude,String edit) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<structure> list=new ArrayList();
        String selectQuery;
        list.clear();
        String sa,sa3=" and type !='夜市'";
        String sa1;
        String sa2=" and name like '%"+edit+"%'";
        if(city.equals("全部")){
            sa="";
            sa1="";
            sa2=" where name like '%"+edit+"%'";
            sa3=" where type !='夜市'";
        }
        else if(location.equals("全部")){
         sa=" where city like '"+city+"'";
            sa1="";
        }else{
            sa=" where city like '"+city+"' and";
            sa1=" location like '"+location+"'";
        }
        if(edit==null)
        selectQuery = "select * from food"+sa+sa1+sa3;
        else{
            sa3=" and type !='夜市'";
        selectQuery = "select * from food"+sa+sa1+sa2+sa3;}
        Cursor cursor = db.rawQuery(selectQuery, null);
        while(cursor.moveToNext()){
            float[] results = new float[1];
            structure structure = new structure();
            structure.id=cursor.getInt(0);
            structure.name=cursor.getString(1);
            Location.distanceBetween(latitude, longitude, cursor.getDouble(8), cursor.getDouble(7), results);
            structure.address=cursor.getString(5);
            structure.phone=cursor.getString(4);
            structure.longitude=cursor.getDouble(7);
            structure.latitude=cursor.getDouble(8);
            structure.distance=results[0];
            structure.image=cursor.getString(9);
            structure.fv=cursor.getString(10);
            list.add(structure);
        }
        cursor.close();
        db.close();
        Collections.sort(list, new Comparator<structure>() {
            @Override
            public int compare(structure o1,
                               structure o2) {
                return Double.compare(o1.distance, o2.distance); // error
            }
        });
        return list;
    }
    public void update(int id,String s){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues(1);
        cv.put(fv,s);
        db.update(TABLE_LOGIN, cv,"id="+id ,null);
        db.close();
    }
    public void update(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues(1);
        String s=null;
        cv.put(fv,s);
        db.update(TABLE_LOGIN, cv,"fv is not null" ,null);
        db.close();
    }
    public String adduser(JSONArray Jarray) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues(1);
        String s="";
            try {
                JSONObject c = Jarray.getJSONObject(0);
                cv.put(KEY_NAME, c.getString("name"));
                s=c.getString("name");
                db.insert(FAVORITE, null, cv);
                if(!c.getString("want").equals("new")){
                    String[] data =c.getString("want").split(",");
                    for(int i = 0;i < data.length;i++) {
                        ContentValues cvs = new ContentValues(1);
                        cvs.put(fv,"想去");
                        db.update(TABLE_LOGIN,cvs,"id="+data[i],null);
                    }
                }
                if(!c.getString("been").equals("new")){
                    String[] data =c.getString("been").split(",");
                    for(int i = 0;i < data.length;i++) {
                        ContentValues cvs = new ContentValues(1);
                        cvs.put(fv,"去過");
                        db.update(TABLE_LOGIN,cvs,"id="+data[i],null);
                    }
                }
                if(!c.getString("col").equals("new")){
                    String[] data =c.getString("col").split(",");
                    for(int i = 0;i < data.length;i++) {
                        ContentValues cvs = new ContentValues(1);
                        cvs.put(fv,"收藏");
                        db.update(TABLE_LOGIN,cvs,"id="+data[i],null);
                    }
                }
            } catch (Exception E) {

            }
          db.close();
          return  s;
    }
    public ArrayList<String> selectuser(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> list=new ArrayList();
     Cursor cursor=db.rawQuery("select * from FAVORITE", null);
        if (cursor.getCount()==0) {
            list.add("no");
        }else {
            cursor.moveToNext();
            list.add(cursor.getString(1));
        }
        cursor.close();
        db.close();
        return list;
    }
    public ArrayList<String> selecfv(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> list=new ArrayList();
        String want="",been="",col="";
        Cursor cursor=db.rawQuery("select id from food where fv='想去'", null);
        while (cursor.moveToNext()){
           want+=cursor.getString(0)+",";
        }
        if(!want.equals(""))
        list.add(want.substring(0,want.length()-1));
        else
            list.add(want);
         cursor=db.rawQuery("select id from food where fv='去過'", null);
        while (cursor.moveToNext()) {
            been+=cursor.getString(0)+",";
        }
        if(!been.equals(""))
            list.add(been.substring(0,been.length()-1));
        else
            list.add(been);
        cursor=db.rawQuery("select id from food where fv='收藏'", null);
        while (cursor.moveToNext()){
            col+=cursor.getString(0)+",";
        }
        if(!col.equals(""))
            list.add(col.substring(0,col.length()-1));
        else
            list.add(col);
        cursor.close();
        db.close();
        return list;
    }

    public ArrayList<structure> select(int i,String edit,String city,String location,double latitude,double longitude){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<structure> list=new ArrayList();
        String sa2="",s,sa,sa1,sa3="";

       switch (i){
           case 1:
               sa2=" where fv is not null";
               break;
           case 2:
               s="想去";
               sa2=" where fv like '"+s+"'";
               break;
           case 3:
               s="去過";
               sa2=" where fv like '"+s+"'";
               break;
           case 4:
               s="收藏";
               sa2=" where fv like '"+s+"'";
               break;
       }
        String selectQuery;
       if(edit==null&&city==null&&location==null)
       selectQuery = "select * from food"+sa2;
        else{
           if(city.equals("全部")){
               sa="";
               sa1="";
               sa3=" and name like '%"+edit+"%'";
           }
           else if(location.equals("全部")){
               sa=" and city like '"+city+"'";
               sa1="";
           }else{
               sa=" and city like '"+city+"' and";
               sa1=" location like '"+location+"'";
           }
           if(edit==null)
               selectQuery = "select * from food"+sa2+sa+sa1;
           else
               selectQuery = "select * from food"+sa2+sa+sa1+sa3;
            }
        Cursor cursor = db.rawQuery(selectQuery, null);
        list.clear();
        while(cursor.moveToNext()){
            float[] results = new float[1];
            structure structure = new structure();
            structure.id=cursor.getInt(0);
            structure.name=cursor.getString(1);
            structure.address=cursor.getString(5);
            Location.distanceBetween(latitude, longitude, cursor.getDouble(8), cursor.getDouble(7), results);
            structure.phone=cursor.getString(4);
            structure.longitude=cursor.getDouble(7);
            structure.latitude=cursor.getDouble(8);
            structure.distance=results[0];
            structure.image=cursor.getString(9);
            structure.fv=cursor.getString(10);
            list.add(structure);
        }
        cursor.close();
        db.close();
        Collections.sort(list, new Comparator<structure>() {
            @Override
            public int compare(structure o1,
                               structure o2) {
                return Double.compare(o1.distance, o2.distance); // error
            }
        });
        return list;
    }
}
