package com.example.csufttally;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.csufttally.adapter.ChartVpAdapter;
import com.example.csufttally.db.DBManager;
import com.example.csufttally.frag_chart.IncomeChartFragment;
import com.example.csufttally.frag_chart.OutcomeChartFragment;
import com.example.csufttally.frag_record.OutcomeFragment;
import com.example.csufttally.utils.CalendarDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonthChartActivity extends AppCompatActivity {
    Button inBtn,outBtn;
    TextView dateTv,inTv,outTv;
    ViewPager chartVp;
    private int year,month;
    int selectPos=-1,selectMonth=-1;
    List<Fragment>chartFragList;
    private IncomeChartFragment incomeChartFragment;
    private OutcomeFragment outcomeFragment;
    private ChartVpAdapter chartVpAdapter;
    private OutcomeChartFragment outcomeChartFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_chart);
        initView();
        initTime();
        initStatistics(year,month);
        initFrag();
        setVpSelectListener();
    }

    private void setVpSelectListener() {
        chartVp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                setButtonStyle(position);
            }
        });
    }

    private void initFrag() {
        chartFragList=new ArrayList<>();
        //添加Fragment对象
        incomeChartFragment = new IncomeChartFragment();
        outcomeChartFragment = new OutcomeChartFragment();
        //添加数据到frag当中
        Bundle bundle = new Bundle();
        bundle.putInt("year",year);
        bundle.putInt("month",month);
        incomeChartFragment.setArguments(bundle);
        outcomeChartFragment.setArguments(bundle);
        //讲frag添加到数据源当中
        chartFragList.add(outcomeChartFragment);
        chartFragList.add(incomeChartFragment);
        //使用适配器
        chartVpAdapter = new ChartVpAdapter(getSupportFragmentManager(), chartFragList);
        chartVp.setAdapter(chartVpAdapter);
        //将fragment加载到activity当中
    }

    /*初始化某年某月的收支情况数据*/
    private void initStatistics(int year,int month) {
        int inMoneyOneMonth = DBManager.getSumMoneyOneMonth(year,month,1);  //收入金额
        int outMoneyOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);  //支出金额
        int incountItemOneMonth = DBManager.getCountItemOneMonth(year, month, 1); //收入条数
        int outcountItemOneMonth = DBManager.getCountItemOneMonth(year, month, 0);  //支出条数
        dateTv.setText(year+"年"+month+"月收支情况");
        inTv.setText("总计"+outcountItemOneMonth+"笔支出  ￥"+outMoneyOneMonth);
        outTv.setText("总计"+incountItemOneMonth+"笔收入  ￥"+inMoneyOneMonth);
    }


    /*初始化时间*/
    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH)+1;   //月份序号从零开始
    }



    //初始化控件
    private void initView() {
        inBtn=findViewById(R.id.chart_btn_in);
        outBtn=findViewById(R.id.chart_btn_out);
        dateTv=findViewById(R.id.chart_tv_date);
        inTv=findViewById(R.id.chart_tv_in);
        outTv=findViewById(R.id.chart_tv_out);
        chartVp=findViewById(R.id.chart_vp);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chart_iv_back:
                finish();
                break;
            case R.id.chart_iv_rili:
                showCalendarDialog();
                break;
            case R.id.chart_btn_out:
                setButtonStyle(0);
                chartVp.setCurrentItem(0);
                break;
            case R.id.chart_btn_in:
                setButtonStyle(1);
                chartVp.setCurrentItem(1);
                break;
        }
    }


    /*显示日历对话框*/
    private void showCalendarDialog() {
        CalendarDialog dialog = new CalendarDialog(this,selectPos,selectMonth);
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnRefreshListener(new CalendarDialog.OnRefreshListener() {
            @Override
            public void onRefresh(int selPos, int year, int month) {
                MonthChartActivity.this.selectPos=selPos;
                MonthChartActivity.this.selectMonth=month;
                initStatistics(year,month);
                incomeChartFragment.setDate(year,month);
                outcomeChartFragment.setDate(year,month);
            }
        });
    }


    /*设置按钮样式的改变 支出--0  收入--1*/
    private void setButtonStyle(int kind){
        if (kind==0) {
            outBtn.setBackgroundResource(R.drawable.main_btn_rcd);
            outBtn.setTextColor(Color.WHITE);
            inBtn.setBackgroundResource(R.drawable.dialog_btn_bg);
            inBtn.setTextColor(Color.BLACK);
        }
        else if(kind==1){
            inBtn.setBackgroundResource(R.drawable.main_btn_rcd);
            inBtn.setTextColor(Color.WHITE);
            outBtn.setBackgroundResource(R.drawable.dialog_btn_bg);
            outBtn.setTextColor(Color.BLACK);
        }
    }
}
