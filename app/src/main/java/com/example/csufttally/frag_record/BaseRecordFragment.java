package com.example.csufttally.frag_record;


import android.inputmethodservice.KeyboardView;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.csufttally.R;
import com.example.csufttally.db.AccountBean;
import com.example.csufttally.db.DBManager;
import com.example.csufttally.db.TypeBean;
import com.example.csufttally.utils.BeiZhuDialog;
import com.example.csufttally.utils.KeyBoardUtils;
import com.example.csufttally.utils.KeyBoardUtils.OnEnsureListener;
import com.example.csufttally.utils.SelectTimeDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseRecordFragment extends Fragment implements View.OnClickListener{


    KeyboardView keyboardView;
    EditText moneyEt;
    ImageView typeIv;
    TextView typeTv,beizhuTv,timeTv;
    GridView typeGv;
    List<TypeBean>typeList;
    TypeBaseAdapter adapter;
    AccountBean accountBean; //将需要插入到记账本的数据保存成对象的形式

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountBean = new AccountBean();
        this.accountBean.setTypename("其它");
        this.accountBean.setsImageId(R.mipmap.ic_else_fs);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_outcome, container, false);
        initView(view);
        setInitTime();
        //给Gripview填充数据
        loadDataToGv();
        setGVListener(); //设置gridview点击事件
        return view;
    }
/*获取当前时间，显示在timetv上*/
    private void setInitTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String time = sdf.format(date);
        timeTv.setText(time);
        accountBean.setTime(time);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        accountBean.setYear(year);
        accountBean.setMonth(month);
        accountBean.setDay(day);
    }

    private void setGVListener() {
        typeGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.selectPos=position;
                adapter.notifyDataSetInvalidated();  //提示绘制放生变化；

                TypeBean typeBean = typeList.get(position);
                String typename = typeBean.getTypename();
                typeTv.setText(typename);
                accountBean.setTypename(typename);
                int simageId = typeBean.getSimageId();
                typeIv.setImageResource(simageId);
                accountBean.setsImageId(simageId);
            }
        });
    }

    //给Gripview填充数据
    public void loadDataToGv() {
        typeList=new ArrayList<>();
        adapter = new TypeBaseAdapter(getContext(), typeList);
        typeGv.setAdapter(adapter);
    }

    private void initView(View view) {
        keyboardView=view.findViewById(R.id.frag_record_keyboard);
        moneyEt=view.findViewById(R.id.frag_record_et_money);
        typeIv=view.findViewById(R.id.frag_record_iv);
        typeTv=view.findViewById(R.id.frag_record_tv_type);
        typeGv=view.findViewById(R.id.frag_record_gv);
        beizhuTv=view.findViewById(R.id.frag_record_tv_beizhu);
        timeTv=view.findViewById(R.id.frag_record_tv_time);
        beizhuTv.setOnClickListener(this);
        timeTv.setOnClickListener(this);
        //显示自定义软键盘；
        KeyBoardUtils boardUtils=new KeyBoardUtils(keyboardView,moneyEt);
        boardUtils.showKeyboard();
        //设置接口，确定按钮被点击；
        boardUtils.setOnEnsureListener(new OnEnsureListener() {
            @Override
            public void onEnsure() {
                //点击了确定按钮
                //获取输入钱数
                String moneyStr = moneyEt.getText().toString();
                if(TextUtils.isEmpty(moneyStr)||moneyStr.equals("0")){
                    getActivity().finish();
                    return;
                }
                int money=Integer.parseInt(moneyStr);
                accountBean.setMoney(money);
                //获取记录的信息，保留在数据库
                saveAccountToDB();
                //返回上一级页面
                getActivity().finish();
            }
        });
    }

    /*让子类一定重写这个方法*/
    public abstract void saveAccountToDB();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frag_record_tv_time:
                showTimeDialog();
                break;
            case R.id.frag_record_tv_beizhu:
                showBZDialog();
                break;
        }
    }

    /*弹出显示时间的对话框*/
    private void showTimeDialog() {
        SelectTimeDialog dialog = new SelectTimeDialog(getContext());
        dialog.show();
        //确定按钮被点击的监听器
        dialog.setOnEnsureListener(new SelectTimeDialog.OnEnsureListener() {
            @Override
            public void onEnsure(String time, int year, int month, int day) {
                timeTv.setText(time);
                accountBean.setTime(time);
                accountBean.setYear(year);
                accountBean.setMonth(month);
                accountBean.setDay(day);
            }
        });
    }

    /*弹出备注对话框*/
    public void showBZDialog(){
        final BeiZhuDialog dialog = new BeiZhuDialog(getContext());
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEsureListener(new BeiZhuDialog.OnEsureListener() {
            @Override
            public void onEnsure() {
                String msg = dialog.getEditText();
                if (!TextUtils.isEmpty(msg)) {
                    beizhuTv.setText(msg);
                    accountBean.setBeizhu(msg);
                }
                dialog.cancel();
            }
        });
    }
}
