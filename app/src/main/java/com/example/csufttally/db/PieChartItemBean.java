package com.example.csufttally.db;

public class PieChartItemBean {
    int year;
    int month;
    int day;
    String Type;
    float ratio;
    float totalMoney;

    public PieChartItemBean() {
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public PieChartItemBean(int year, int month, int day, String type, float ratio, float totalMoney) {
        this.year = year;
        this.month = month;
        this.day = day;
        Type = type;
        this.ratio = ratio;
        this.totalMoney = totalMoney;
    }
}
