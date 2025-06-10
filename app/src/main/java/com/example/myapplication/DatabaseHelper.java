package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.myapplication.model.Msg;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {
    //这个类继承了SQLite轻型数据库
    //1构造函数
    //2onCreate()函数
    //3onUpgrade()函数
    //4-7 对应数据库的增删改查函数

    private  static final String DATABASE_NAME="books.db";
    private static final int DATABASE_VERSION=2;
    private static final String TABLE_BOOKS="books";

    public DatabaseHelper(Context context){//调用父类方法，创建数据库。要求配套onCreate()、onUpgrade()方法
        super(context,DATABASE_NAME,null,DATABASE_VERSION);//Context是应用的环境信息，代表很多
    }
    //1构造函数，创建数据库

    @Override
    public void onCreate(SQLiteDatabase db){//创表
        db.execSQL("create table "+TABLE_BOOKS+
                "(id INTEGER primary key autoincrement," +
                "message text)");
    }//2onCreate()，创建表

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {//
        //db.execSQL("DROP TABLE IF EXISTS books");
        // onCreate(db);
    }//3onUpgrade()，是SQLite数据库要求必须有的

    //增删改查
    public boolean addMsg(String msg){//
        SQLiteDatabase db = this.getWritableDatabase();//就是调用数据库
        ContentValues values =new ContentValues();//ContentValues，键值对
        values.put("message",msg);
        //剩下就是insert语句
        long result = db.insert(TABLE_BOOKS, null, values);
        Log.d("DatabaseHelper", "Insert result: " + result);
        return result != -1;
    }//增加，往数据库插入数据

    public List<Msg> getAllMsg(){
        List<Msg> msgs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_BOOKS, null);
        if(cursor.moveToFirst()){
            do {
                long id = cursor.getLong(0);
                String message = cursor.getString(1);
                msgs.add(new Msg(id, message));
            } while(cursor.moveToNext());
        }
        cursor.close();
        return msgs;
    }
    public boolean deleteMsg(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_BOOKS, "id=?", new String[]{String.valueOf(id)});
        return result > 0;
    }
    public boolean updateMsg(long id, String newMsg) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("message", newMsg);
        int result = db.update(TABLE_BOOKS, values, "id=?", new String[]{String.valueOf(id)});
        return result > 0;
    }
}
