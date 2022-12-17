package com.example.csufttally.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.csufttally.R;

public class DBOpenHelper extends SQLiteOpenHelper {
    public DBOpenHelper(@Nullable Context context) {
        super(context, "csufttally", null , 2);
    }

    //创建数据库的方法，只有第一次运行项目时会被调用；
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表示类型的表
//        int id;//      String typename;//    int imageId;//  int simageId;

        String sql="create table typetb(id integer primary key autoincrement,typename varchar(10),imageId integer,simageId integer,kind integer)";
        db.execSQL(sql);
        insertType(db);

        //记账表：
        sql = "create table accounttb(id integer primary key autoincrement,typename varchar(10),sImageId integer,beizhu varchar(80),money int,time varchar(60),year integer,month integer,day integer,kind integer)";
        db.execSQL(sql);
    }

    private void insertType(SQLiteDatabase db) {
 //向typetb中插入元素；
        String sql="insert into typetb(typename,imageId,sImageId,kind)values(?,?,?,?)";
        db.execSQL(sql,new Object[]{"其它", R.mipmap.ic_else,R.mipmap.ic_else_fs,0});
        db.execSQL(sql,new Object[]{"饮食", R.mipmap.ic_food,R.mipmap.ic_food_fs,0});
        db.execSQL(sql,new Object[]{"生活用品", R.mipmap.ic_daily,R.mipmap.ic_daily_fs,0});
        db.execSQL(sql,new Object[]{"游戏", R.mipmap.ic_game,R.mipmap.ic_game_fs,0});
        db.execSQL(sql,new Object[]{"交通", R.mipmap.ic_traffic,R.mipmap.ic_traffic_fs,0});
        db.execSQL(sql,new Object[]{"学习", R.mipmap.ic_study,R.mipmap.ic_study_fs,0});
        db.execSQL(sql,new Object[]{"零食", R.mipmap.ic_snacks,R.mipmap.ic_snacks_fs,0});
        db.execSQL(sql,new Object[]{"饮品", R.mipmap.ic_drinks,R.mipmap.ic_drinks_fs,0});
        db.execSQL(sql,new Object[]{"娱乐活动", R.mipmap.ic_entertainment,R.mipmap.ic_entertainment_fs,0});
        db.execSQL(sql,new Object[]{"购物", R.mipmap.ic_shopping,R.mipmap.ic_shopping_fs,0});
        db.execSQL(sql,new Object[]{"住宿", R.mipmap.ic_house,R.mipmap.ic_house_fs,0});
        db.execSQL(sql,new Object[]{"通讯费用", R.mipmap.ic_communicate,R.mipmap.ic_communicate_fs,0});
        db.execSQL(sql,new Object[]{"服饰", R.mipmap.ic_cloth,R.mipmap.ic_cloth_fs,0});
        db.execSQL(sql,new Object[]{"水电费", R.mipmap.ic_utility,R.mipmap.ic_utility_fs,0});
        db.execSQL(sql,new Object[]{"医疗", R.mipmap.ic_medicine,R.mipmap.ic_medicine_fs,0});
        db.execSQL(sql,new Object[]{"人情往来", R.mipmap.ic_redpocket,R.mipmap.ic_redpocket_fs,0});

        db.execSQL(sql,new Object[]{"其它", R.mipmap.ic_in_else,R.mipmap.ic_in_else_fs,1});
        db.execSQL(sql,new Object[]{"生活费", R.mipmap.ic_in_life,R.mipmap.ic_in_life_fs,1});
        db.execSQL(sql,new Object[]{"工资", R.mipmap.ic_in_salary,R.mipmap.ic_in_salary_fs,1});
        db.execSQL(sql,new Object[]{"奖学金", R.mipmap.ic_in_scholorship,R.mipmap.ic_in_scholorship_fs,1});
        db.execSQL(sql,new Object[]{"理财产品", R.mipmap.ic_in_licai,R.mipmap.ic_in_licai_fs,1});
        db.execSQL(sql,new Object[]{"银行利息", R.mipmap.ic_in_lixi,R.mipmap.ic_in_lixi_fs,1});
        db.execSQL(sql,new Object[]{"借入", R.mipmap.ic_in_borrow,R.mipmap.ic_in_borrow_fs,1});
        db.execSQL(sql,new Object[]{"旧物处理", R.mipmap.ic_in_ershou,R.mipmap.ic_in_ershou_fs,1});
    }

    private void insertAccount(SQLiteDatabase db) {
        //向account中插入元素；
        String sql="insert into accounttb(typename,sImageId,beizhu,money ,time ,year ,month ,day ,kind )values(?,?,?,?,?,?,?,?,?)";
        db.execSQL(sql,new Object[]{"人情往来",R.mipmap.ic_redpocket_fs,"0101",50,"16:16",2022,12,11,1});
    }
    //数据库版本更新时会改变时，调用此方法
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
