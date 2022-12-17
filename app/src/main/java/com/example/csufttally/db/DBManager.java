package com.example.csufttally.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.csufttally.adapter.ChartItemAdapter;
import com.example.csufttally.utils.FloatUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/*
负责管理数据库的类，对表中数据进行操作
 */
public class DBManager {
    private static SQLiteDatabase db;
    /*
    初始化数据库对象
     */
    public static void  initDB(Context context){
        DBOpenHelper helper =new DBOpenHelper(context);
        db=helper.getWritableDatabase();
    }
    /**
     * 读取数据库当中的数据，写入内存集合里；
     * 传入kind表示收入或支出
     */
    public static List<TypeBean> getTypeList(int kind){
        List<TypeBean>list=new ArrayList<>();
        //读取typetb中的数据
        String sql="select * from typetb where kind = "+kind;
        Cursor cursor=db.rawQuery(sql,null);
        while(cursor.moveToNext()){
            String typename=cursor.getString(cursor.getColumnIndex("typename"));
            int imageId=cursor.getInt(cursor.getColumnIndex("imageId"));
            int sImageId=cursor.getInt(cursor.getColumnIndex("simageId"));
            int kind1=cursor.getInt(cursor.getColumnIndex("kind"));
            int id=cursor.getInt(cursor.getColumnIndex("id"));
            TypeBean typeBean=new TypeBean(id,typename,imageId,sImageId,kind);
            list.add(typeBean);
        }
        return list;
    }

    /*向记账表中插入一条元素*/
    public static  void insertItemToAccounttb(AccountBean bean){
        ContentValues values = new ContentValues();
        values.put("typename",bean.getTypename());
        values.put("sImageId",bean.getsImageId());
        values.put("beizhu",bean.getBeizhu());
        values.put("money",bean.getMoney());
        values.put("time",bean.getTime());
        values.put("year",bean.getYear());
        values.put("month",bean.getMonth());
        values.put("day",bean.getDay());
        values.put("kind",bean.getKind());
        db.insert("accounttb", null,values);
        Log.i("林大账本插入账表数据","insertItemToAccounttb:成功!!!");
    }

    /*
    * 获取记账表中某一天的所有支出或收入情况
    * */
    public static List<AccountBean>getAccountListOneDayFromAccounttb(int year,int month,int day){
        List<AccountBean>list=new ArrayList<>();
        String sql="select * from accounttb where year=? and month=? and day=? order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "",day + ""});
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            String beizhu = cursor.getString(cursor.getColumnIndex("beizhu"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            int money = Integer.valueOf((int) cursor.getFloat(cursor.getColumnIndex("money")));
            AccountBean accountBean = new AccountBean(id, typename, sImageId, beizhu, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;
    }
    /*
    * 获取某一天的支出或者收入的总金额 kind0 支出 kind 1 收入
    * */
    public static int getSumMoneyOneDay(int year,int month,int day,int kind){
        int total=0;
        String sql="select sum(money) from accounttb where year=? and month=? and day=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + "", kind + ""});//加引号转成字符串型
        //遍历
        if (cursor.moveToNext()) {
            Integer money = Integer.valueOf((int) cursor.getFloat(cursor.getColumnIndex("sum(money)")));
            total=money;
        }
        return total;
    }

    /*
     * 获取某月的支出或者收入的总金额 kind0 支出 kind 1 收入
     * */
    public static int getSumMoneyOneMonth(int year,int month,int kind){
        int total=0;
        String sql="select sum(money) from accounttb where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});//加引号转成字符串型
        //遍历
        if (cursor.moveToNext()) {
            Integer money = Integer.valueOf((int) cursor.getFloat(cursor.getColumnIndex("sum(money)")));
            total=money;
        }
        return total;
    }


    /*统计某月收入和支出的条数 支出--0  收入---1*/
    public static int getCountItemOneMonth(int year,int month,int kind){
        int total=0;
        String sql="select count(money) from accounttb where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(cursor.getColumnIndex("count(money)"));
            total=count;
        }
        return total;
    }


    /*
     * 获取某年的支出或者收入的总金额 kind0 支出 kind 1 收入
     * */
    public static int getSumMoneyOneYear(int year,int kind){
        int total=0;
        String sql="select sum(money) from accounttb where year=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", kind + ""});//加引号转成字符串型
        //遍历
        if (cursor.moveToNext()) {
            Integer money = Integer.valueOf((int) cursor.getFloat(cursor.getColumnIndex("sum(money)")));
            total=money;
        }
        return total;
    }

    /*根据传入的id，删除accounttb当中的一条数据*/




    public static int deleteItemFromAccounttbById(int id){
        int i = db.delete("accounttb", "id=?", new String[]{id + ""});
        return i;
    }

    /*
    * 根据备注搜索收入或支出的情况列表（搜索）*/
    public static List<AccountBean>getAccountListByRemarkFromAccounttb(String beizhu){
       List<AccountBean>list=new ArrayList<>();
       String sql="select * from accounttb where beizhu like '%"+beizhu+"%'";   //模糊查询
       Cursor cursor=db.rawQuery(sql,null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            String bz = cursor.getString(cursor.getColumnIndex("beizhu"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            int money = Integer.valueOf((int) cursor.getFloat(cursor.getColumnIndex("money")));
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            int month = cursor.getInt(cursor.getColumnIndex("month"));
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            AccountBean accountBean = new AccountBean(id, typename, sImageId, bz, money, time, year, month, day, kind);
            list.add(accountBean);
        }
       return list;
    }


    /*
     * 获取记账表中某月的所有支出或收入情况
     * */
    public static List<AccountBean>getAccountListOneMonthFromAccounttb(int year,int month){
        List<AccountBean>list=new ArrayList<>();
        String sql="select * from accounttb where year=? and month=? order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + ""});
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            String beizhu = cursor.getString(cursor.getColumnIndex("beizhu"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            int money = Integer.valueOf((int) cursor.getFloat(cursor.getColumnIndex("money")));
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            AccountBean accountBean = new AccountBean(id, typename, sImageId, beizhu, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;
    }


    /**
     * 查询记账的表当中有几个年份信息
     * */
    public static List<Integer>getYearListFromAccounttb(){
        List<Integer>list = new ArrayList<>();
        String sql = "select distinct(year) from accounttb order by year asc";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            list.add(year);
        }
        return list;
    }


    public static void deleteAllAccount() {
        String sql="delete from accounttb";
        db.execSQL(sql);
    }

    /*
    * 查询指定年份的收入或者支出每一种类型的总钱数*/
    public static List<ChartItemBean>getChartListFromAccounttb(int year,int month,int kind){
        List<ChartItemBean>list=new ArrayList<>();
        int sumMoneyOneMonth = getSumMoneyOneMonth(year, month, kind);  //求出支出或者收入的总钱数
        String sql="select typename,sImageId,sum(money)as total from accounttb where year=? and month=? and kind=? group by typename order by total desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        while (cursor.moveToNext()) {
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            float total = cursor.getFloat(cursor.getColumnIndex("total"));
            //计算所占百分比 total/summonth
            float ratio= FloatUtils.div(total,sumMoneyOneMonth);
            ChartItemBean bean = new ChartItemBean(sImageId, typename, ratio, total);
            list.add(bean);
        }
        return list;
    }

    /**
     * 获取这个月收入或支出最高的一天
     * */
    public static int getMaxMoneyOneDay(int year,int month, int kind){
        String sql = "select sum(money) from accounttb where year=? and month=? and kind=? group by day order by sum(money) desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        if (cursor.moveToFirst()) {
            int money = cursor.getInt(cursor.getColumnIndex("sum(money)"));
            return money;
        }
        return 0;
    }

    /** 根据指定月份每一日收入或者支出的总钱数的集合*/
    public static List<BarChartItemBean>getSumMoneyOneDayInMonth(int year,int month,int kind){
        String sql = "select day,sum(money) from accounttb where year=? and month=? and kind=? group by day";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        List<BarChartItemBean>list = new ArrayList<>();
        while (cursor.moveToNext()) {
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            float smoney = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            BarChartItemBean itemBean = new BarChartItemBean(year, month, day, smoney);
            list.add(itemBean);
        }
        return list;
    }


}


