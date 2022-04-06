package com.example.jetpack.util;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class UccOpenHelper extends SQLiteOpenHelper {
    public static final String CREATE_USER = "create table user(" +
            "id integer primary key autoincrement,"+
            "name text," +
            "password text," +
            "phone text," +
            "address text," +
            "photo text)";

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
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_CATEGORY);
        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }

    private static final String DROP_USER = "drop table if exists user";
    private static final String DROP_Category = "drop table if exists Category";
    //当把 version 修改后 onUpgrade 将会执行
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_USER);
        db.execSQL(DROP_Category);
        onCreate(db);
    }


}
