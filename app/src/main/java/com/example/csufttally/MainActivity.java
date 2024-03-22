package com.example.csufttally;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.csufttally.adapter.AccountAdapter;
import com.example.csufttally.db.AccountBean;
import com.example.csufttally.db.DBManager;
import com.example.csufttally.utils.BudgetDialog;
import com.example.csufttally.utils.MoreDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ListView todayLv;//展示今日收支情况的ListView
    ImageView serchIv;
    ImageButton editBtn;
    ImageButton moreBtn;
    //头布局相关控件
    View headerView;
    TextView topOutTv,topInTv,topbudgetTv,topConTv,userName;
    SharedPreferences preferences;
    //声明数据源
    List<AccountBean>mDatas;
    AccountAdapter adapter;
    int year,month,day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTime();
        initView();
        preferences=getSharedPreferences("budget", Context.MODE_PRIVATE);
        //添加listView的头布局；
        addLVHeaderView();
        mDatas=new ArrayList<>();
        //设置适配器，加载每一行数据到列表中
        adapter = new AccountAdapter(this,mDatas);
        todayLv.setAdapter(adapter);
        userName = findViewById(R.id.main_tv_temp);
        Intent intent = getIntent();
        String account = intent.getStringExtra("account");
        userName.setText("当前用户："+account);
    }

    /**初始化自带View的方法*/
    private void initView() {
        todayLv=findViewById(R.id.main_lv);
        editBtn=findViewById(R.id.main_btn_rcd);
        moreBtn=findViewById(R.id.main_btn_more);
        serchIv=findViewById(R.id.main_iv_search);
        editBtn.setOnClickListener(this);
        moreBtn.setOnClickListener(this);
        serchIv.setOnClickListener(this);
        //给ListView设定长按事件
        setLVLongClickListener();
    }

    /*给LISTview添加头布局*/
    private void addLVHeaderView() {
        //将布局转换成View对象
        headerView = getLayoutInflater().inflate(R.layout.item_mainlv_top, null);
        //设置头布局
        todayLv.addHeaderView(headerView);
        //查找头布局可用控件
        topOutTv=headerView.findViewById(R.id.item_mainlv_top_tv_out);
        topInTv=headerView.findViewById(R.id.item_mainlv_top_tv_in);
        topbudgetTv=headerView.findViewById(R.id.item_mainlv_top_tv_budget);
        topConTv=headerView.findViewById(R.id.item_mainlv_top_tv_day);

        topbudgetTv.setOnClickListener(this);
        headerView.setOnClickListener(this);

    }


/*
* 给ListView设定长按事件
* */
    private void setLVLongClickListener() {
        todayLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){ //点击了头布局
                    return false;
                }
                int pos=position-1;
                AccountBean clickBean = mDatas.get(pos);  //获取正在被点击的这条数据
                //弹出是否删除的对话框；
                showDeleteItemDialog(clickBean);
                return false;
            }
        });
    }


    //弹出是否删除某一条记录的对话框；
    private void showDeleteItemDialog(final AccountBean clickBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("要删除这条记录吗？")
                .setNegativeButton("还是算了",null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int click_id=clickBean.getId();
                //执行删除的操作
                DBManager.deleteItemFromAccounttbById(click_id);
                mDatas.remove(clickBean);  //实时刷新，移除集合当中的对象
                adapter.notifyDataSetChanged();  //提示适配器更新数据
                setTopTvShow();  //改变头布局TextView现实的内容；
            }
        });
        builder.create().show();
    }



    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    //当activity获取焦点时会调用方法
    @Override
    protected void onResume() {
        super.onResume();
        loadDBData();//载入数据库数据
        setTopTvShow();
    }

    private void setTopTvShow() {
        //获取今日支出和今日收入总金额，显示在view中；
        int incomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 1);//当日收入
        int outcomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 0);//当日支出
        String infoOneDay="今日支出 ￥"+outcomeOneDay+"  收入 ￥"+incomeOneDay;
        topConTv.setText(infoOneDay);
        //获取本月支出和本月收入总金额，显示在view中；
        int incomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 1);
        int outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);//本月支出
        topOutTv.setText("￥"+outcomeOneMonth);
        topInTv.setText("￥"+incomeOneMonth);
        //显示运算剩余：
        int bmoney = preferences.getInt("bmoney", 0);
        if(bmoney==0){
            topbudgetTv.setText("￥0");
        }else {
            int symoney=bmoney-outcomeOneDay;
            topbudgetTv.setText("￥"+symoney);
        }
    }

    private void loadDBData() {
        List<AccountBean> list = DBManager.getAccountListOneDayFromAccounttb(year, month, day);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_iv_search:
                Intent it=new Intent(this,SearchActivity.class);  //跳转界面
                startActivity(it);
                break;
            case R.id.main_btn_rcd :
                Intent it1=new Intent(this,RecordActivity.class);
                startActivity(it1);
                break;
            case R.id.main_btn_more:
                MoreDialog moreDialog = new MoreDialog(this);
                moreDialog.show();
                moreDialog.setDialogSize();
                break;

            case R.id.item_mainlv_top_tv_budget:
                showBudgetDialog();
                break;
        }
        if(v==headerView){
            //头布局被点击了
            Intent intent = new Intent();
            intent.setClass(this, MonthChartActivity.class);
            startActivity(intent);
        }
    }
/*显示预算设置对话框*/
    private void showBudgetDialog() {
        BudgetDialog dialog = new BudgetDialog(this);
        dialog.show();
        dialog.setDialogSize();

        dialog.setOnEnsureListener(new BudgetDialog.OnEnsureListener() {
            @Override
            public void onEnsure(int money) {
                SharedPreferences.Editor editor=preferences.edit();
                editor.putInt("bmoney",money);
                editor.commit();
                //计算剩余运算；
                int outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);//本月支出；
                int syMoney=money-outcomeOneMonth;
                topbudgetTv.setText("￥"+syMoney);
            }
        });
    }
}
