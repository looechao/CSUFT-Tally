package com.example.csufttally.logs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.csufttally.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int RESULT_CODE_REGISTER = 0;
    private Button btnRegister;
    private EditText etAccount,etPass,etPassConfirm;
    private CheckBox cbAgree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etAccount = findViewById(R.id.et_account);
        etPass = findViewById(R.id.et_password);
        etPassConfirm = findViewById(R.id.et_password_confirm);
        cbAgree = findViewById(R.id.cb_agree);
        btnRegister = findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String name = etAccount.getText().toString();
        String pass = etPass.getText().toString();
        String passConfirm = etPassConfirm.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(RegisterActivity.this, "用户名不能为空", Toast.LENGTH_LONG).show();

            return;
        }

        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_LONG).show();
            return;
        }

        if (!TextUtils.equals(pass,passConfirm)) {
            Toast.makeText(RegisterActivity.this, "密码不一致", Toast.LENGTH_LONG).show();
            return;
        }

        if (!cbAgree.isChecked()) {
            Toast.makeText(RegisterActivity.this, "请同意用户协议", Toast.LENGTH_LONG).show();
            return;
        }

        // 存储注册的用户名 密码
        SharedPreferences spf = getSharedPreferences("spfRecord", MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString("account", name);
        edit.putString("password", pass);
        edit.apply();


        // 数据回传
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("account",name);
        bundle.putString("password",pass);
        intent.putExtras(bundle);
        setResult(RESULT_CODE_REGISTER,intent);


        Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_LONG).show();
        this.finish();
    }
}
