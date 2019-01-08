package com.example.administrator.dk;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.bean.JsonCallback;
import com.example.administrator.bean.MD5Utils;
import com.example.administrator.bean.VerifyCode;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.concurrent.CountDownLatch;

/**
 * 作者 刘文芳
 *
 * @email 1348058915@qq.com
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_phone_number, et_yanzm;
    private Button btn_login, btn_yanzhengma;

    private int Identifier = 9;

    private User user;
    //private Integer codenumber;
    //  private NetworkHelper helper = new NetworkHelper(this);
    private String codeNumberc;
    private String phoneNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_phone_number = findViewById(R.id.et_phone_number);
        et_yanzm = findViewById(R.id.et_yanzm);
        btn_login = findViewById(R.id.btn_login);
        btn_yanzhengma = findViewById(R.id.btn_yanzhengma);

        et_phone_number.setOnClickListener(this);
        btn_yanzhengma.setOnClickListener(this);
        et_yanzm.setOnClickListener(this);
        btn_login.setOnClickListener(this);

    }


    int timeOut = 59;
    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler() {
        @SuppressLint("ResourceAsColor")
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case 1:
                    btn_yanzhengma.setText(timeOut + "秒后重新获取");
                    timeOut--;
                    if (timeOut == 0) {
                        btn_yanzhengma.setText("重新获取");
                        btn_yanzhengma.setEnabled(true);
                        timeOut = 59;

                    }
            }
            super.handleMessage(msg);
        }
    };


    @SuppressLint("LongLogTag")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yanzhengma://验证码
                btn_yanzhengma.requestFocus();
                phoneNumber = et_phone_number.getText().toString();
                if (phoneNumber.isEmpty() || phoneNumber.length() < 11) {
                    Toast.makeText(this, "手机号不能为空或者手机号非法，请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                btn_yanzhengma.setEnabled(false);
                final CountDownLatch cd = new CountDownLatch(1);
                Thread thread = new Thread(new MyThread());
                thread.start();
//                codenumber = getRadrom();
                sendCode(phoneNumber);

                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        /**
                         *  user = helper.getUser(phoneNumber);
                         */

                        cd.countDown();
                    }
                }.start();

                try {
                    cd.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                break;

            case R.id.btn_login:
                final String phoneNumber = et_phone_number.getText().toString().trim();


                codeNumberc = et_yanzm.getText().toString();
                if (phoneNumber.isEmpty() || phoneNumber.length() < 11) {
                    Toast.makeText(this, "手机号不能为空或者手机号非法，请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (codeNumberc.isEmpty()) {
                    Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();

                    return;
                }
                String urlcode = "http://apk910okinfo.910ok.com/home/ExtendLogin";

                OkGo.<String>post(urlcode)
                        .params("Mobile", phoneNumber)
                        .params("Code", codeNumberc)
                        .params("Identifier", 9)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                JsonParser parser = new JsonParser();

                                JsonObject data = parser.parse(response.body()).getAsJsonObject();
                                if (response != null && !response.body().equals("")) {

                                    //真正上线的时候，在这里判断一下返回的字符串是不是空或者是不是一个Json字符串
                                    if (data.get("result").getAsInt()==0) {
                                       String mes = data.get("message").getAsString();
                                        Toast.makeText(LoginActivity.this, mes, Toast.LENGTH_SHORT).show();

                                    } else if (data.get("message").getAsString().equals("登录成功")) {
                                        //登录成功


                                        String token = data.get("token").getAsString();

                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                                        SharedPreferences sp = getSharedPreferences("token", MODE_PRIVATE);
                                        //存入数据
                                        SharedPreferences.Editor editor = sp.edit();

                                        editor.putString("token", token);

                                        editor.commit();
                                        startActivity(intent);
                                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                        finish();

                                    } else if(data.get("message").getAsString().equals("手机验证码不正确")){
                                        //登录失败
                                        Toast.makeText(LoginActivity.this, "手机验证码不正确", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }
                        });

        }
    }

    public class MyThread implements Runnable {      // thread
        @Override
        public void run() {
            for (int i = 0; i < 59; i++) {
                try {
                    Thread.sleep(1000);     // sleep 1000ms
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                } catch (Exception e) {
                }
            }
        }
    }


    /*******
     * @fuction 发送验证码
     * @param  mobile,number  手机号和6位验证码
     *
     * *********/

    public void sendCode(String mobile) {

        String url = "http://apk910okinfo.910ok.com/home/ExtendSendCode/";
        final VerifyCode[] jounal = {new VerifyCode()};

        jounal[0].setMobile(mobile);
        jounal[0].setMobilePwd(MD5Utils.MD5Encode(mobile + "|_sc901@Pwmd5", "utf8"));
        jounal[0].setIdentifier(9);
        Gson gson = new Gson();

        // String jsonString = gson.toJson(jounal[0]);
        String mobilePwd = MD5Utils.MD5Encode(mobile + "|_sc901@Pwmd5", "utf8");

        OkGo.<String>post(url)
//                .upJson(jsonString)
                .params("Mobile", mobile)
                .params("MobilePwd", mobilePwd)
                .params("Identifier", 9)
                .execute(new JsonCallback<String>(VerifyCode.class) {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onSuccess(Response<String> response) {
                        response.body();
                        Log.e("wwwwwwwwwwwwwwwwwwwwwwwww", String.valueOf(response.code()));


                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Toast.makeText(LoginActivity.this, "网络异常请稍后重试", Toast.LENGTH_LONG).show();
                    }
                });


    }
}
