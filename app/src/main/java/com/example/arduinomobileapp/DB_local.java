package com.example.arduinomobileapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DB_local extends SQLiteOpenHelper {
    static  final String db_name="arduinoapp.db";
    public DB_local(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, db_name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table User(idmac text,username text ,password text,token text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
    //-------------------------------------------------------------------USER
    public void insererUser(String idmac,String username, String password, String token){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idmac",idmac);
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
                arrayList.add(new User (cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
                cursor.moveToNext();
            }
        return arrayList;
    }
    public void supprimerUser(){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("delete from User");
    }
}