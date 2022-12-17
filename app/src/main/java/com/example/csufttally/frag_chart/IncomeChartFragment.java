package com.example.csufttally.frag_chart;


import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.csufttally.R;
import com.example.csufttally.adapter.ChartItemAdapter;
import com.example.csufttally.db.BarChartItemBean;
import com.example.csufttally.db.ChartItemBean;
import com.example.csufttally.db.DBManager;
import com.example.csufttally.utils.FloatUtils;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class IncomeChartFragment extends BaseChartFragment {
    int kind=1;
    @Override
    public void onResume() {
        super.onResume();
        loadData(year,month,kind);
    }

    @Override
    protected void setPieData(int year, int month) {
        List<PieEntry> pieEntries = new ArrayList<>();
        List<ChartItemBean>list=DBManager.getChartListFromAccounttb(year,month,kind);
        for(int i=0;i<list.size();i++){
            final ChartItemBean itemBean=list.get(i);
            float value= FloatUtils.ratio1(itemBean.getRatio());
            PieEntry pieEntry = new PieEntry(value, itemBean.getType());
            pieEntries.add(pieEntry);
            PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
            pieDataSet.setColor(Color.parseColor("#FFA500"));
            pieDataSet.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    // 此处的value就是PieEntry（）中第一个参数的value
                    if (value == 0) {
                        return " ";
                    } else {
                        return value + "%";
                    }
                }
            });

            pieDataSet.setValueTextSize(12f);
            pieDataSet.setSliceSpace(2f); // 设置扇区中的间隔
            // 设置饼图显示的线
            pieDataSet.setValueLineColor(Color.parseColor("#FFA500"));
            pieDataSet.setValueLinePart1OffsetPercentage(30); // 第一条线离圆心的百分比
            pieDataSet.setValueLinePart1Length(0.3f); // 第一条线长度
            pieDataSet.setValueLinePart2Length(0.35f); // 第二条线长度
            pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE); // 设置值显示的位置

            PieData pieData = new PieData(pieDataSet);
            pieChart.setData(pieData); // 为饼图设置数据
            Legend legend=pieChart.getLegend();
            legend.setEnabled(false);
        }
    }

    @Override
    protected void setAxisData(int year, int month) {
        List<IBarDataSet> sets = new ArrayList<>();
//获取这个月每天的支出总金额
        List<BarChartItemBean> list = DBManager.getSumMoneyOneDayInMonth(year, month, kind);
        if (list.size()==0) {
            pieChart.setVisibility(View.GONE);
            barChart.setVisibility(View.GONE);
            chartTv.setVisibility(View.VISIBLE);
        }else{
            pieChart.setVisibility(View.VISIBLE);
            barChart.setVisibility(View.VISIBLE);
            chartTv.setVisibility(View.GONE);
//设置有多少根柱子
            List<BarEntry> barEntries1 = new ArrayList<>();
            for (int i=0;i<31;i++){
                //初始化每一根柱子，添加到柱状图当中
                BarEntry entry = new BarEntry(i, 0.0f);
                barEntries1.add(entry);
            }

            for (int i=0;i<list.size();i++){
                BarChartItemBean itemBean = list.get(i);
                int day=itemBean.getDay();  //获取日期
                //根据天数，获取x轴的位置
                int xIndex=day-1;
                BarEntry barEntry = barEntries1.get(xIndex);
                barEntry.setY(itemBean.getSummoney());
            }
            BarDataSet barDataSet1 = new BarDataSet(barEntries1, "");
            barDataSet1.setValueTextColor(Color.BLACK); // 值的颜色
            barDataSet1.setValueTextSize(8f); // 值的大小
            barDataSet1.setColor(Color.parseColor("#FFD700"));   // 柱子的颜色


            // 设置柱子上数据显示的格式
            barDataSet1.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    // 此处的value默认保存一位小数
                    if (value==0) {
                        return "";
                    }
                    return value + "";
                }
            });

            sets.add(barDataSet1);

            BarData barData = new BarData(sets);
            barData.setBarWidth(0.2f); // 设置柱子的宽度
            barChart.setData(barData);
        }
    }

    @Override
    protected void setYAxis(int year, int month) {
        //获取本月收入最高的一天为多少，将他设定为y轴的最大值；
        int maxMoney = DBManager.getMaxMoneyOneDay(year, month, kind);
        //设置y轴
        YAxis yAxis_right = barChart.getAxisRight();
        yAxis_right.setAxisMaximum(maxMoney);  // 设置y轴的最大值
        yAxis_right.setAxisMinimum(0f);  // 设置y轴的最小值
        yAxis_right.setEnabled(false);  // 不显示右边的y轴

        YAxis yAxis_left = barChart.getAxisLeft();
        yAxis_left.setAxisMaximum(maxMoney);
        yAxis_left.setAxisMinimum(0f);
        yAxis_left.setEnabled(false);

        //设置不显示图例
        Legend legend = barChart.getLegend();
        legend.setEnabled(false);
    }


    @Override
    public void setDate(int year, int month) {
        super.setDate(year, month);
        loadData(year,month,kind);
    }
}
