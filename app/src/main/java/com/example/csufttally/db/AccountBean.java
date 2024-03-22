package com.example.csufttally.db;

import com.example.csufttally.SearchActivity;

import java.util.List;

/*描述记录一条数据的相关内容类*/
public class AccountBean {
    int id;
    String typename;
    int sImageId;
    String beizhu;
    int money;
    String time;
    int year;
    int month;
    int day;
    int kind;   //收入1 支出0

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public int getsImageId() {
        return sImageId;
    }

    public void setsImageId(int sImageId) {
        this.sImageId = sImageId;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public AccountBean() {
    }

    public AccountBean(int id, String typename, int sImageId, String beizhu, int money, String time, int year, int month, int day, int kind) {
        this.id = id;
        this.typename = typename;
        this.sImageId = sImageId;
        this.beizhu = beizhu;
        this.money = money;
        this.time = time;
        this.year = year;
        this.month = month;
        this.day = day;
        this.kind = kind;
    }
}
