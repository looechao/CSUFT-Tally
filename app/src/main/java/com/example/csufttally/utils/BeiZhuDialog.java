package com.example.csufttally.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.csufttally.R;

import java.util.Calendar;

public class BeiZhuDialog extends Dialog implements View.OnClickListener{
    EditText et;
    Button cancelBtn,ensureBtn;
    OnEsureListener onEsureListener;
//设定回调接口
    public void setOnEsureListener(OnEsureListener onEsureListener) {
        this.onEsureListener = onEsureListener;
    }

    public BeiZhuDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_beizhu);
        et=findViewById(R.id.dialog_beizhu_et);
        cancelBtn=findViewById(R.id.dialog_beizhu_btn_cancel);
        ensureBtn=findViewById(R.id.dialog_beizhu_btn_ensure);
        cancelBtn.setOnClickListener(this);
        ensureBtn.setOnClickListener(this);
    }

    public interface OnEsureListener{
        public void onEnsure();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_beizhu_btn_cancel:
                cancel();
                break;
            case R.id.dialog_beizhu_btn_ensure:
                if (onEsureListener!=null) {
                    onEsureListener.onEnsure();  //接口回调
                }
                break;
        }
    }


    //获取输入数据的方法：
    public String getEditText(){
        return et.getText().toString().trim();
    }

    //设置dialog显示的宽度和屏幕尺寸一致：
    public void setDialogSize(){
        //获得当前窗口对象
        Window window = getWindow();
        //获取窗口对象的宽度
        WindowManager.LayoutParams wlp = window.getAttributes();
        //获取整个屏幕的宽度
        Display d = window.getWindowManager().getDefaultDisplay();
        wlp.width=(int)(d.getWidth()); //对话框为屏幕宽度
        wlp.gravity= Gravity.BOTTOM;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
        handler.sendEmptyMessageDelayed(1,1);
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            //自动弹出软键盘
            InputMethodManager inputMethodManager= (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    };
}
