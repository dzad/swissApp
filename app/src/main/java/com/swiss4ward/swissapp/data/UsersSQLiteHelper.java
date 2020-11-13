package com.swiss4ward.swissapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.swiss4ward.swissapp.models.User;

public class UsersSQLiteHelper extends SQLiteOpenHelper {

    String sqlCreate = "CREATE TABLE Users (id INTEGER, name TEXT, username TEXT, email TEXT, " +
            "street TEXT, suite TEXT, city TEXT, zipcode TEXT, x TEXT, y TEXT, " +
            "companyName TEXT, catchPhrase TEXT, bs TEXT, phone TEXT, website TEXT)";

    public UsersSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory fac, int version){
        super(context, name, fac, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //delete table if exist
        db.execSQL("DROP TABLE IF EXISTS Users");

        //create table
        db.execSQL(sqlCreate);
    }
}
