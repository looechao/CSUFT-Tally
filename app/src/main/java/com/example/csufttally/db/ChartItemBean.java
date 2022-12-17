package com.example.csufttally.db;

public class ChartItemBean {
    int sImageId;
    String Type;
    float ratio;
    float totalMoney;

    public void setsImageId(int sImageId) {
        this.sImageId = sImageId;
    }

    public ChartItemBean() {
    }

    public void setType(String type) {
        Type = type;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public void setTotalMoney(float totalMoney) {
        this.totalMoney = totalMoney;
    }

    public int getsImageId() {
        return sImageId;
    }

    public String getType() {
        return Type;
    }

    public float getRatio() {
        return ratio;
    }

    public float getTotalMoney() {
        return totalMoney;
    }

    public ChartItemBean(int sImageId, String type, float ratio, float totalMoney) {
        this.sImageId = sImageId;
        Type = type;
        this.ratio = ratio;
        this.totalMoney = totalMoney;
    }
}
