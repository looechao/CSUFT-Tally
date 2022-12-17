package com.example.csufttally.utils;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.csufttally.AboutActivity;
import com.example.csufttally.AccountActivity;
import com.example.csufttally.HistoryActivity;
import com.example.csufttally.MonthChartActivity;
import com.example.csufttally.R;
import com.example.csufttally.SettingActivity;
import com.example.csufttally.TreehollowActivity;

public class MoreDialog extends Dialog implements View.OnClickListener {
   Button accountBtn,aboutBtn,clearBtn,historyBtn,infoBtn,treehollowBtn;
   ImageView errorIv;
    public MoreDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_more);
        accountBtn=findViewById(R.id.dialog_more_btn_account);
        aboutBtn=findViewById(R.id.dialog_more_btn_about);
        clearBtn=findViewById(R.id.dialog_more_btn_clear);
        historyBtn=findViewById(R.id.dialog_more_btn_record);
        infoBtn=findViewById(R.id.dialog_more_btn_info);
        errorIv=findViewById(R.id.dialog_more_iv);
        treehollowBtn=findViewById(R.id.dialog_more_btn_treehollow);

        accountBtn.setOnClickListener(this);
        aboutBtn.setOnClickListener(this);
        clearBtn.setOnClickListener(this);
        historyBtn.setOnClickListener(this);
        infoBtn.setOnClickListener(this);
        errorIv.setOnClickListener(this);
        treehollowBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
        switch (v.getId()) {
            case R.id.dialog_more_btn_treehollow:
                intent.setClass(getContext(), TreehollowActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.dialog_more_btn_account:
                intent.setClass(getContext(), AccountActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.dialog_more_btn_about:
                intent.setClass(getContext(), AboutActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.dialog_more_btn_clear:
                intent.setClass(getContext(), SettingActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.dialog_more_btn_record:
                intent.setClass(getContext(), HistoryActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.dialog_more_btn_info:
                intent.setClass(getContext(), MonthChartActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.dialog_more_iv:
                cancel();
                break;
        }
    }



    //设置dialog显示的宽度和屏幕尺寸一致：
    public void setDialogSize(){
        //获得当前窗口对象
        Window window = getWindow();
        //获取窗口对象的宽度
        WindowManager.LayoutParams wlp = window.getAttributes();
        //获取整个屏幕的宽度
        Display d = window.getWindowManager().getDefaultDisplay();
        wlp.height=(int)(d.getHeight()); //对话框为屏幕高度
        wlp.gravity= Gravity.LEFT;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
    }

}
