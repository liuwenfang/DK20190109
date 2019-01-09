package com.example.administrator.dk;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.bean.JsonCallback;
import com.example.administrator.bean.VerifyCode;
import com.example.administrator.dialog.ConsumeTypeDialog;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.zhouyou.view.seekbar.SignSeekBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button next, btn_nothing;
    private String consumeType = "";
    private ConsumeTypeDialog consumeTypeDialog;

    private RelativeLayout rl_jiekuan;
    private List<BannerBean> bannerBeans;
    private List<Integer> bannerUrls;
    private int status;
    private int llog;
    private String token;
    private Banner banner;
    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;
    private Button btn_huankuan;
    private int state = 0;
    private String loan;
    private int result;
    private RelativeLayout rl_repayment, rel_borrow_money;

    private BorrowMoneyActivity fatherActivity;
    private SignSeekBar msignSeekBar1, msignSeekBar2;
    private TextView tv_repay, tv_date_repay, tv_exit;
    float backm;

    private SwipeRefreshLayout srlMain;

    @SuppressLint({"ClickableViewAccessibility", "LongLogTag"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        next = findViewById(R.id.btn_next);
        rl_jiekuan = findViewById(R.id.rl_jiekuan);
        banner = findViewById(R.id.banner);
        btn_huankuan = findViewById(R.id.btn_huankuan);
        rl_repayment = findViewById(R.id.rl_repayment);
        btn_nothing = findViewById(R.id.btn_nothing);
        tv_exit = findViewById(R.id.tv_exit);


        tv_date_repay = findViewById(R.id.tv_date_repay);
        rel_borrow_money = findViewById(R.id.rel_borrow_money);
        srlMain = findViewById(R.id.srl_main);

        tv_repay = findViewById(R.id.tv_repay);
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");


        tv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        srlMain.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                srlMain.setRefreshing(false);
                getStatus();


            }
        });


        if (token.equals("") || token == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

        } else {
            getStatus();
        }
        Log.e("wwwwwwwwwww000000000wwwwwww", token);


        OkGo.<String>post("http://apk910okinfo.910ok.com/home/GetBanner")
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
                        Toast.makeText(MainActivity.this, "网络异常请稍后重试", Toast.LENGTH_LONG).show();
                    }
                });
        bannerUrls = new ArrayList<>();
        bannerUrls.add(R.mipmap.ban);
        bannerUrls.add(R.mipmap.banner_icon);


        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(bannerUrls);
        banner.setDelayTime(3000);
        //banner设置方法全部调用完毕时最后调用
        banner.start();

//        //增加点击事件
//        banner.setOnBannerListener(new OnBannerListener() {
//            @Override
//            public void OnBannerClick(int position) {
//                Toast.makeText(MainActivity.this, "position" + position, Toast.LENGTH_SHORT).show();
//            }
//        });
        msignSeekBar1 = findViewById(R.id.mSignSeekBar1);
        msignSeekBar2 = findViewById(R.id.mSignSeekBar2);
        msignSeekBar1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("ACETEST", "监听");
                return true;
            }
        });
        msignSeekBar2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("ACETEST", "监听");
                return true;
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStatus();
                if (result == -1) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this,"登录失败",Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }else {
                    Intent intent = new Intent(MainActivity.this, BorrowMoneyActivity.class);
                    Intent intent1 = getIntent();
                    String loginState = intent1.getStringExtra("token");
                    Log.e("ACETEST", "============" + loginState);
                    startActivity(intent);
//                finish();
                }

            }
        });
    }

    /**
     * 获取状态
     */
    private void getStatus() {

        OkGo.<String>post("http://apk910okinfo.910ok.com/home/ExtendNoComplate")
                .params("token", token)
                .params("Identifier", 9)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        JsonParser parser = new JsonParser();

                        JsonObject data = parser.parse(response.body()).getAsJsonObject();
                        result = data.get("result").getAsInt();
                        if (result == -1) {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            return;
                        }

                        status = data.get("Status").getAsInt();

                        llog = data.get("LoanlogID").getAsInt();

                        Log.e("dddddddddddddddd", "dfffffffffffffffsf" + llog);
                        loan = String.valueOf(llog);
                        if (status == -1) {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            Toast.makeText(MainActivity.this,"登录失败",Toast.LENGTH_LONG).show();
                            finish();
                            return;
                        }
                        if (llog != 0) {
                            rl_repayment.setVisibility(View.VISIBLE);
                            rel_borrow_money.setVisibility(View.GONE);
                            OkGo.<String>post("http://apk910okinfo.910ok.com/Member/LoanDetail")
                                    .params("LoanlogID", llog)
                                    .params("token", token)
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(Response<String> response) {
                                            JsonParser parser = new JsonParser();
                                            JsonObject data = parser.parse(response.body()).getAsJsonObject();

                                            JsonObject data1 = data.get("data").getAsJsonObject();
                                            Log.e("dddddddddddddddd", "dffffffftokennnnnfffsf" + token);
                                            Log.e("dddddddddddddddd", "dsf" + data);
                                            backm = data1.get("BackM").getAsFloat();

                                            String date = data1.get("RepayTime").getAsString();
                                            tv_date_repay.setText("还款日期：" + date);

                                            /**
                                             * 如果状态是5获取到期应还
                                             */
                                            if (status == 5) {

                                                rl_repayment.setVisibility(View.VISIBLE);

                                                next.setVisibility(View.GONE);


                                                tv_repay.setVisibility(View.VISIBLE);
                                                tv_repay.setText(backm + "");


                                                Log.e("hdfjksf", "sddddddddddddsssss" + llog);
                                                btn_huankuan.setVisibility(View.VISIBLE);
                                                btn_huankuan.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        final Dialog dialog = new Dialog(MainActivity.this, R.style.NormalDialogStyle);
                                                        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                                                        View view = inflater.inflate(R.layout.layout_dialog_refund, null);
                                                        final ImageView iv_yinhangka = view.findViewById(R.id.iv_yinhangka);
                                                        final ImageView iv_weixin = view.findViewById(R.id.iv_weixin);

                                                        RelativeLayout rl_weixin = view.findViewById(R.id.rl_weixin);
                                                        RelativeLayout rl_bankCard = view.findViewById(R.id.rl_bankCard);
                                                        LinearLayout ll_woyaoRefund = view.findViewById(R.id.ll_woyaoRefund);


                                                        dialog.setContentView(view);
                                                        //使得点击对话框外部不消失对话框
                                                        dialog.setCanceledOnTouchOutside(true);
                                                        //设置对话框的大小
                                                        v.setMinimumHeight((int) (ScreenSizeUtils.getInstance(MainActivity.this).getScreenHeight() * 0.23f));
                                                        Window dialogWindow = dialog.getWindow();
                                                        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                                                        lp.width = (int) (ScreenSizeUtils.getInstance(MainActivity.this).getScreenWidth() * 0.75f);
                                                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                                        lp.gravity = Gravity.CENTER;
                                                        dialogWindow.setAttributes(lp);
                                                        dialog.show();

                                                        dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置


                                                        rl_weixin.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                state = 1;
                                                                iv_weixin.setBackgroundResource(R.mipmap.yuan);
                                                                iv_yinhangka.setBackgroundResource(R.mipmap.weigou);
                                                            }
                                                        });
                                                        rl_bankCard.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                state = 2;
                                                                iv_weixin.setBackgroundResource(R.mipmap.weigou);
                                                                iv_yinhangka.setBackgroundResource(R.mipmap.yuan);
                                                            }
                                                        });
                                                        ll_woyaoRefund.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                if (state == 1) {
                                                                    Toast.makeText(MainActivity.this, "该功能正在维护中......", Toast.LENGTH_SHORT).show();
//                                                                    Intent i = new Intent(MainActivity.this, WXActivity.class);
//                                                                    i.putExtra("token", token);
//                                                                    startActivity(i);
                                                                    dialog.dismiss();

                                                                } else if (state == 2) {
                                                                    Intent i = new Intent(MainActivity.this, CardActivity.class);
                                                                    i.putExtra("loan", loan);
                                                                    startActivity(i);
                                                                    dialog.dismiss();
                                                                } else if (state == 0) {
                                                                    Toast.makeText(MainActivity.this, "请选择还款方式", Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                        });

                                                    }
                                                });

                                            }
                                        }
                                    });
                        } else {
                            rl_repayment.setVisibility(View.GONE);
                            rel_borrow_money.setVisibility(View.VISIBLE);
                            btn_huankuan.setVisibility(View.GONE);
                            next.setVisibility(View.VISIBLE);

                        }
                        if (llog == 0) {
                            return;
                        } else if (status == 0) {
                            Intent intent = new Intent(MainActivity.this, AuditActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (status == 4) {
                            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (status == 8) {
                            rl_repayment.setVisibility(View.GONE);
                            rel_borrow_money.setVisibility(View.VISIBLE);
                            int day = data.get("LimitDay").getAsInt();
                            if (day > 0) {
                                next.setVisibility(View.GONE);
                                btn_nothing.setVisibility(View.VISIBLE);
                            } else {
                                next.setVisibility(View.VISIBLE);
                                btn_nothing.setVisibility(View.GONE);
                            }
                        } else {
                            return;
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                    }
                });
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

}

