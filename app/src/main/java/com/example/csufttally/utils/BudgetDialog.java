package com.example.csufttally.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.csufttally.R;

public class BudgetDialog extends Dialog implements View.OnClickListener {
    ImageView cancelIv;
    Button ensureBtn;
    EditText moneyEt;
    public interface OnEnsureListener{
        public void onEnsure(int money);
    }

    OnEnsureListener onEnsureListener;

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public BudgetDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_budget);
        cancelIv=findViewById(R.id.dialog_budget_iv_error);
        ensureBtn=findViewById(R.id.dialog_budget_btn_ensure);
        moneyEt=findViewById(R.id.dialog_budget_et);
        cancelIv.setOnClickListener(this);
        ensureBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_budget_iv_error:
                cancel();
                break;
            case R.id.dialog_budget_btn_ensure:
                //获取输入数据数值：
                String data = moneyEt.getText().toString();
                if (TextUtils.isEmpty(data)) {
                    Toast.makeText(getContext(),"输入预算不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                int money = Integer.parseInt(data);
                if(money<=0){
                    Toast.makeText(getContext(),"预算必须大于零！",Toast.LENGTH_SHORT).show();
                }
                if (onEnsureListener!=null) {
                    onEnsureListener.onEnsure(money);
                }
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
