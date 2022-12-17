package com.example.csufttally;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.csufttally.logs.LoginActivity;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.account_iv_back:
                finish();
                break;
            case R.id.account_tv_info:
                showInfoDialog();
                break;
            case R.id.account_tv_out:
                SharedPreferences spf = getSharedPreferences("spfRecord", MODE_PRIVATE);
                SharedPreferences.Editor edit = spf.edit();
                edit.putBoolean("isLogin", false);
                edit.apply();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                this.finish();
                break;
        }
    }

    private void showInfoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("状态提示")
                .setMessage("该账号状态正常！")
                .setNegativeButton("取消",null)
                .setPositiveButton("确定", null);
        builder.create().show();
    }
}
