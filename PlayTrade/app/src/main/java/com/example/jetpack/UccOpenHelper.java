package com.example.jetpack;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class UccOpenHelper extends SQLiteOpenHelper {
    public static final String CREATE_BOOK = "create table Book(" +
            "name text," +
            "password text)";

    private static final String CREATE_CATEGORY = "create table Category(" +
            "name text," +
            "password text)";

    private Context mContext;

    public UccOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK);
        db.execSQL(CREATE_CATEGORY);
        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }

    private static final String DROP_BOOK = "drop table if exists Book";
    private static final String DROP_Category = "drop table if exists Category";
    //当把 version 修改后 onUpgrade 将会执行
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_BOOK);
        db.execSQL(DROP_Category);
        onCreate(db);
    }


}
