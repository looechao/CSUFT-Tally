package com.example.csufttally.logs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.csufttally.MainActivity;
import com.example.csufttally.R;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "tag";
    public static final int REQUEST_CODE_REGISTER = 1;
    private Button btnLogin;
    private EditText etAccount,etPassword;
    private CheckBox cbRemember,cbAutoLogin;

    private String userName = "fxjzzyo";
    private String pass = "123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

        initData();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = etAccount.getText().toString();
                String password = etPassword.getText().toString();

                Log.d(TAG, "onClick: -------------" + account);
                Log.d(TAG, "password: -------------" + password);
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(LoginActivity.this, "还没有注册账号！", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.equals(account, userName)) {
                    if (TextUtils.equals(password, pass)) {

                        Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_LONG).show();
                        if (cbRemember.isChecked()) {
                            SharedPreferences spf = getSharedPreferences("spfRecord", MODE_PRIVATE);
                            SharedPreferences.Editor edit = spf.edit();
                            edit.putString("account", account);
                            edit.putString("password", password);
                            edit.putBoolean("isRemember", true);
                            if (cbAutoLogin.isChecked()) {
                                edit.putBoolean("isLogin", true);
                            }else {
                                edit.putBoolean("isLogin", false);
                            }
                            edit.apply();

                        }else {
                            SharedPreferences spf = getSharedPreferences("spfRecord", MODE_PRIVATE);
                            SharedPreferences.Editor edit = spf.edit();
                            edit.putBoolean("isRemember", false);
                            edit.apply();
                        }


                        Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("account", account);
                        startActivity(intent);
                        LoginActivity.this.finish();

                    } else {
                        Toast.makeText(LoginActivity.this, "密码错误！", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "用户名错误！", Toast.LENGTH_LONG).show();
                }

            }
        });

        cbAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbRemember.setChecked(true);
                }
            }
        });

        cbRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    cbAutoLogin.setChecked(false);
                }
            }
        });

    }



    private void initView() {
        btnLogin = findViewById(R.id.btn_login);
        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);
        cbRemember = findViewById(R.id.cb_remember);
        cbAutoLogin = findViewById(R.id.cb_auto_login);

    }

    private void initData() {
        SharedPreferences spf = getSharedPreferences("spfRecord", MODE_PRIVATE);
        boolean isRemember = spf.getBoolean("isRemember", false);
        boolean isLogin = spf.getBoolean("isLogin", false);
        String account = spf.getString("account", "");
        String password = spf.getString("password", "");


        if (isLogin) {
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            intent.putExtra("account", account);
            startActivity(intent);
            LoginActivity.this.finish();
        }


        userName = account;
        pass = password;

        if (isRemember) {
            etAccount.setText(account);
            etPassword.setText(password);
            cbRemember.setChecked(true);
        }


    }

    public void toRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);

        startActivityForResult(intent, REQUEST_CODE_REGISTER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_REGISTER && resultCode == RegisterActivity.RESULT_CODE_REGISTER && data != null) {
            Bundle extras = data.getExtras();

            String account = extras.getString("account", "");
            String password = extras.getString("password", "");

            etAccount.setText(account);
            etPassword.setText(password);

            userName = account;
            pass = password;
        }
    }
}