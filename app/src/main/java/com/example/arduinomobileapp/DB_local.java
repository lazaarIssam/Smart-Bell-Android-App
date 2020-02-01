package com.example.arduinomobileapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DB_local extends SQLiteOpenHelper {
    static  final String db_name="foodeals.db";
    public DB_local(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, db_name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table User(username text ,password text,token text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
    //-------------------------------------------------------------------USER
    public void insererUser(String username, String password, String token){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username",username);
        values.put("password",password);
        values.put("token",token);
        database.insert("User",null,values);
    }
    public ArrayList<User> afficherTousUser(){
        ArrayList<User> arrayList = new ArrayList<User>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor =  database.rawQuery("select * from User",null);
        if (cursor.moveToFirst())
            while (!cursor.isAfterLast()) {
                arrayList.add(new User (cursor.getString(0), cursor.getString(1), cursor.getString(2)));
                cursor.moveToNext();
            }
        return arrayList;
    }
    public void supprimerUser(){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("delete from User");
    }
    //-------------------------------------------------------------------Panier
    /*
    public void insererPanier(String idRestau,String restaut_name, String idProduct, String name, int qte,String hCollete,String dateCollete){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idRestau",idRestau);
        values.put("restaut_name",restaut_name);
        values.put("idProduct",idProduct);
        values.put("name",name);
        values.put("qte",qte);
        values.put("hCollete",hCollete);
        values.put("dateCollete",dateCollete);
        database.insert("Panier",null,values);
    }
    //-------------------------------------------------------------------Country

    public void insererCity(String cityname){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("cityName",cityname);
        database.insert("City",null,values);
    }
    public void supprimerCity(){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("delete from City");
    }
    public ArrayList<City> afficherCity(){
        ArrayList<City> arrayList = new ArrayList<City>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor =  database.rawQuery("select * from City",null);
        if (cursor.moveToFirst())
            while (!cursor.isAfterLast()) {
                arrayList.add(new City (cursor.getString(0)));
                cursor.moveToNext();
            }
        return arrayList;
    }

    public void insererProbleme(String id,String obs){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id",id);
        values.put("obs",obs);
        database.insert("probleme",null,values);
    }
    public ArrayList<Probleme> afficherTousProbleme(){
        ArrayList<Probleme> arrayList = new ArrayList<Probleme>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor =  database.rawQuery("select * from probleme",null);
        if (cursor.moveToFirst())
            while (!cursor.isAfterLast()) {
                arrayList.add(new Probleme(cursor.getString(0), cursor.getString(1)));
                cursor.moveToNext();
            }
        return arrayList;
    }
     public void supprimerProbleme(){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("delete from probleme");
     }
     /*
     public void modifier(String r, String d){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values= new ContentValues();
         values.put("ref",r);
         values.put("designation",d);
        database.update("Prod",values,"ref=?",new String[]{r});
     }
     public ArrayList<Produit> afficherPar (String r){
         ArrayList<Produit> arrayList = new ArrayList<Produit>();
         SQLiteDatabase database = getReadableDatabase();
         Cursor cursor =  database.rawQuery("select * from Prod where ref='"+r+"'",null);
         // autre methode
         //Cursor cursor =  database.rawQuery("select * from Prod where ref=?",String[]{r});
         if(cursor.moveToFirst())
             while (!cursor.isAfterLast()){
                 arrayList.add(new Produit(cursor.getString(0),cursor.getString(1)));
                 cursor.moveToNext();
             }
         return arrayList;
     }
    public ArrayList<Produit> commencePar (String r){
        ArrayList<Produit> arrayList = new ArrayList<Produit>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor =  database.rawQuery("select * from Prod where ref like '"+r+"%'",null);
        if(cursor.moveToFirst())
            while (!cursor.isAfterLast()){
                arrayList.add(new Produit(cursor.getString(0),cursor.getString(1)));
                cursor.moveToNext();
            }
        return arrayList;
    }*/
}