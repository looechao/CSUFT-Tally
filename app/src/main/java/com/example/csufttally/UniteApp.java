package com.example.csufttally;

import android.app.Application;

import com.example.csufttally.db.DBManager;
import com.example.csufttally.db.TypeBean;

import java.util.ArrayList;
import java.util.List;

/*表示全局应用的类*/
public class UniteApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化数据可对象
        DBManager.initDB(getApplicationContext());
    }
}
