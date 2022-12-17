package com.example.csufttally.frag_chart;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.csufttally.R;
import com.example.csufttally.adapter.ChartItemAdapter;
import com.example.csufttally.db.ChartItemBean;
import com.example.csufttally.db.DBManager;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseChartFragment extends Fragment {
    ListView chartLv;
    PieChart pieChart;  //代表头布局就当中的饼图控件
    BarChart barChart;  //代表头布局就当中的柱形图控件
    TextView chartTv; //如果没有收支情况，显示的文本控件；
    public int year,month;
    List<ChartItemBean> mDatas;  //数据源
    private ChartItemAdapter itemAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_income_chart, container, false);
        chartLv=view.findViewById(R.id.frag_chart_lv);
        //获取activity传递的数据；
        Bundle bundle = getArguments();
        year = bundle.getInt("year");
        month = bundle.getInt("month");
        //设置数据源
        mDatas=new ArrayList<>();
        //设置适配器
        itemAdapter = new ChartItemAdapter(getContext(), mDatas);
        chartLv.setAdapter(itemAdapter);
        //添加头布局
        addLvHeaderView();
        return view;
    }

    protected void addLvHeaderView() {
        //将布局转换成View对象
        View headerView=getLayoutInflater().inflate(R.layout.item_chart_frag_top,null);
        //将View添加到Listview头布局上
        chartLv.addHeaderView(headerView);
        //查找头部将当中所包含的控件
        pieChart=headerView.findViewById(R.id.item_chartfrag_piechart);
        barChart=headerView.findViewById(R.id.item_chartfrag_barchart);
        chartTv=headerView.findViewById(R.id.item_chart_top_tv);
        //设置图表不显示描述
        barChart.getDescription().setEnabled(false);
        pieChart.getDescription().setEnabled(false);
        //设置图表的内边距
        barChart.setExtraOffsets(20,20,20,20);
        //设置坐标轴
        setAxis(year,month);
        //设置坐标轴显示的数据;
        setAxisData(year,month);
        setPieData(year,month);
    }

    //设置饼图数据
    protected abstract void setPieData(int year, int month);

    //设置坐标轴显示的数据（柱形图）
    protected abstract void setAxisData(int year, int month);


    /**设置柱状图坐标轴的显示 y轴不同，方法必须重写*/
    protected void setAxis(int year, final int month) {
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);  //X轴在下方
        xAxis.setDrawGridLines(true); //绘制网格线
        //设置x轴的标签个数
        xAxis.setLabelCount(31);  //一个月最多31天
        xAxis.setTextSize(12f);  //x轴文字大小

        //设置x轴显示的值的格式
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int val= (int) value;
                if (val==0) {
                    return month+"-1";
                }

                if (val==14) {
                    return month+"-15";
                }

                //根据月份不同，显示最后一天的位置
                if (month==2) {
                    if (val==27)
                    return month+"-28";
                }else if(month==1||month==3||month==5||month==7||month==8||month==10||month==12){
                    if (val == 30) {
                        return month+"-31";
                    }
                }else if(month==4||month==6||month==9||month==11){
                    if (val==29) {
                        return month+"-30";
                    }
                }
                return "";
            }
        });

        xAxis.setYOffset(10); // 设置标签对x轴的偏移量，垂直方向

        //y轴在子类的设置
        setYAxis(year,month);

    }

    //设置y轴，最高坐标不确定，在子类中确定；
    protected abstract void setYAxis(int year,int month);

    public void setDate(int year,int month){
        this.year=year;
        this.month=month;
        //清空柱状图的数据
        pieChart.clear();
        barChart.clear();
        barChart.invalidate();
        setPieData(year,month);
        setAxis(year,month);
        setAxisData(year, month);
    }



    public void loadData(int year,int month,int kind) {
        List<ChartItemBean> list = DBManager.getChartListFromAccounttb(year, month, kind);
        mDatas.clear();
        mDatas.addAll(list);
        itemAdapter.notifyDataSetChanged();
    }
}
