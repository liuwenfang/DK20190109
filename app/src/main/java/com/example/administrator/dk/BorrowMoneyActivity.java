package com.example.administrator.dk;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.bean.JsonCallback;
import com.example.administrator.dialog.ConsumeDialog;
import com.example.administrator.dialog.ConsumeDialogDay;
import com.example.administrator.dialog.ConsumeTypeDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/12/24.
 * 借款
 */

public class BorrowMoneyActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private Button btnApply;
    private RelativeLayout relativeLayout2, relativeLayout3;
    private TextView tvjinge, tvDay, tv_name, tv_yvqi, tv_cost, tv_money, tv_qixian, tv_yicixing;
    private ImageView ivBack, iv_wenhao;
    private String consumeType = "", numMoney;
    private ConsumeDialog consumeDialog;
    private ConsumeDialogDay consumeDialogDay;
    private String token, backM;
    private float Applyfee, Interest, Userfee;
    private Integer moneyID, DayID;
    String jine;
    private int borrowMon;
    private SwipeRefreshLayout srl;
    private Context context;

    /*public BorrowMoneyActivity(Context context) {
        this.context = context;
    }*/

    private boolean run = false;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow);
        setStatusBarFullTransparent();
        setFitSystemWindow(false);
        relativeLayout2 = findViewById(R.id.relativeLayout2);
        relativeLayout3 = findViewById(R.id.relativeLayout3);
        tv_name = findViewById(R.id.tv_name);
        tv_cost = findViewById(R.id.tv_cost);

        iv_wenhao = findViewById(R.id.iv_wenhao);
        tv_yvqi = findViewById(R.id.tv_yvqi);
        tvDay = findViewById(R.id.tv_day);
        tvjinge = findViewById(R.id.textView8);
        ivBack = findViewById(R.id.iv_back);
        btnApply = findViewById(R.id.btn_apply);
        tv_yicixing = findViewById(R.id.tv_yicixing);
        tv_money = findViewById(R.id.tv_money);
        tv_qixian = findViewById(R.id.tv_qixian);


        srl = findViewById(R.id.srl);


        srl.setOnRefreshListener(this);
        token = getSharedPreferences("token", MODE_PRIVATE).getString("token", "");
        //一秒刷新一次
        run = true;
        handler.postDelayed(task, 1000);


        update();


//
///**
// *获取姓名和逾期
// */
//         jine = (String) tvjinge.getText();
//        OkGo.<String>post("http://ts.910ok.com/home/ExtendSign")
//                .params("token", token)
//                .params("Identifier", 9)
//                .params("Loan", jine)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(Response<String> response) {
//                        if (response != null && !response.body().equals("")) {
//                            //数据不为空
//                            JsonParser parser = new JsonParser();
//
//                            JsonObject data = parser.parse(response.body()).getAsJsonObject();
//                            String username = data.get("username").getAsString();
//                            String overdue = data.get("OverdueP").getAsString();
//                            Log.e("tag", "jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjyvqui" + overdue+"vvvvvvvvvvvvvvv"+jine);
//                            tv_name.setText(username);
//                            tv_yvqi.setText("逾期" + overdue);
//
//                        }
//                    }
//
//                    @Override
//                    public void onError(Response<String> response) {
//                        super.onError(response);
//                    }
//                });


//        OkGo.<String>post("http://ts.910ok.com/home/TermList")
//                .params("token", token)
//                .execute(new StringCallback() {
//
//                    @Override
//                    public void onSuccess(Response<String> response) {
//                        if (response != null && !response.body().equals("")) {
//                            //数据不为空
//                            JsonParser parser = new JsonParser();
//
//                            JsonObject data = parser.parse(response.body()).getAsJsonObject();
//
//                            if (data.get("result").getAsInt() == 1) {
//                                //获取贷款金额成功
//
//                                JsonArray dataList = data.get("dataList").getAsJsonArray();
//
//                                 mData1 = new ArrayList<>();
//
//                                for (JsonElement item : dataList) {
//
//                                    JsonObject data_obj = item.getAsJsonObject();
//                                    DayID = data_obj.get("ID").getAsInt();
//                                    mData1.add(data_obj.get("Num").getAsString() + "(天)");
//
//
//                                }
//
//                                OkGo.<String>post("http://ts.910ok.com/home/ExtendGetfee")
//                                        .params("LoanId", moneyID)
//                                        .params("TermId", 7)
//                                        .params("token", token)
//                                        .params("Identifier",9)
//                                        .execute(new StringCallback() {
//                                            @Override
//                                            public void onSuccess(Response<String> response) {
//                                                if (response.body() != null && !response.body().equals("")) {
//
//                                                    JsonParser parser = new JsonParser();
//
//                                                    JsonObject data = parser.parse(response.body()).getAsJsonObject();
//
//                                                    if (data.get("result").getAsInt() == 1) {
//                                                        //获取基本费用成功
//                                                        //   data = data.get("data").getAsJsonObject();
//
//                                                        Applyfee = data.get("Applyfee").getAsFloat();
//
//                                                        Interest = data.get("Interest").getAsFloat();
//
//                                                        Userfee = data.get("Userfee").getAsFloat();
//                                                        // lixi = Integer.valueOf((int) (Interest * 1000 * 7 / 100));
//                                                        int all = (int) (Applyfee + Interest + Userfee);
//                                                        tv_cost.setText(all + "元");
//
//
//                                                    } else {
//                                                        Toast.makeText(BorrowMoneyActivity.this, "获取失败请检查网络连接", Toast.LENGTH_SHORT).show();
//                                                    }
//                                                }
//
//                                            }
//                                        });
//                            }
//                        }
//                    }
//                });


        // backM = (String) tvjinge.getText();

        relativeLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OkGo.<String>post("http://apk910okinfo.910ok.com/home/TermList")
                        .params("token", token)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                if (response != null && !response.body().equals("")) {
                                    //数据不为空
                                    JsonParser parser = new JsonParser();

                                    JsonObject data = parser.parse(response.body()).getAsJsonObject();

                                    if (data.get("result").getAsInt() == 1) {
                                        //获取贷款金额成功

                                        JsonArray dataList = data.get("dataList").getAsJsonArray();

                                        List<String> mData = new ArrayList<>();

                                        for (JsonElement item : dataList) {

                                            JsonObject data_obj = item.getAsJsonObject();
                                            DayID = data_obj.get("ID").getAsInt();
                                            mData.add(data_obj.get("Num").getAsString() + "(天)");


                                        }
                                        if (mData.size() == 0)
                                            return;
                                        if (consumeDialogDay == null) {
                                            consumeDialogDay = new ConsumeDialogDay(BorrowMoneyActivity.this, mData);
                                            consumeDialogDay.setSelType(new ConsumeDialogDay.selType() {
                                                @Override
                                                public void getSelType(String selType) {
                                                    consumeType = selType;
                                                    tvDay.setText(consumeType);
                                                }
                                            });
                                        }
                                        consumeDialogDay.show();


                                    } else {

                                        Toast.makeText(BorrowMoneyActivity.this, "获取失败！", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });


            }
        });

        tv_qixian.setText(tvDay.getText());

        relativeLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = getSharedPreferences("token", MODE_PRIVATE).getString("token", "");
                OkGo.<String>post("http://apk910okinfo.910ok.com/home/ExtendLoanList")
                        .params("token", token)
                        .params("Identifier",9)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                if (response != null && !response.body().equals("")) {
                                    //数据不为空
                                    JsonParser parser = new JsonParser();

                                    JsonObject data = parser.parse(response.body()).getAsJsonObject();

                                    if (data.get("result").getAsInt() == 1) {
                                        //获取贷款金额成功

                                        JsonArray dataList = data.get("dataList").getAsJsonArray();

                                        List<String> mData = new ArrayList<>();

                                        for (JsonElement item : dataList) {

                                            JsonObject data_obj = item.getAsJsonObject();
                                            mData.add(data_obj.get("Num").getAsString());


                                        }
                                        if (mData.size() == 0)
                                            return;
                                        if (consumeDialog == null) {
                                            consumeDialog = new ConsumeDialog(BorrowMoneyActivity.this, mData);
                                            consumeDialog.setSelType(new ConsumeDialog.selType() {
                                                @Override
                                                public void getSelType(String selTypeo) {
                                                    consumeType = selTypeo;
                                                    tvjinge.setText(consumeType);
                                                }
                                            });
                                        }
                                        consumeDialog.show();
                                        tv_money.setText(tvjinge.getText());


                                    } else {

                                        Toast.makeText(BorrowMoneyActivity.this, "获取失败！", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });


            }
        });

        tv_money.setText(tvjinge.getText());


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        iv_wenhao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                OkGo.<String>post("http://ts.910ok.com/home/ExtendGetfee")
//                        .params("LoanId", 3)
//                        .params("TermId", 7)
//                        .params("token", token)
//                        .params("Identifier", 9)
//                        .execute(new StringCallback() {
//                            @Override
//                            public void onSuccess(Response<String> response) {
//                                if (response.body() != null && !response.body().equals("")) {
//
//                                    JsonParser parser = new JsonParser();
//
//                                    JsonObject data = parser.parse(response.body()).getAsJsonObject();
//
//                                    if (data.get("result").getAsInt() == 1) {
//                                        //获取基本费用成功
//                                        //   data = data.get("data").getAsJsonObject();
//
//                                        Applyfee = data.get("Applyfee").getAsFloat();
//
//                                        Interest = data.get("Interest").getAsFloat();
//
//                                        Userfee = data.get("Userfee").getAsFloat();
//
//                                        int all = (int) (Applyfee + Interest + Userfee);
//                                        tv_cost.setText(all + "元");
//
//
//                                    } else {
//                                        Toast.makeText(BorrowMoneyActivity.this, "获取失败请检查网络连接", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//
//                            }
//                        });
                final Dialog dialog = new Dialog(BorrowMoneyActivity.this, R.style.NormalDialogStyle);
                LayoutInflater inflater = LayoutInflater.from(BorrowMoneyActivity.this);
                View view = inflater.inflate(R.layout.layout_dialog_query, null);

                LinearLayout llKonw = view.findViewById(R.id.ll_yes);
                TextView tvTotal = view.findViewById(R.id.tv_total);
                TextView tvApplyfee = view.findViewById(R.id.tv_Applyfee);
                TextView tvInterest = view.findViewById(R.id.tv_Interest);
                TextView tvUserfee = view.findViewById(R.id.tv_Userfee);

                tvApplyfee.setText(Applyfee + "元");
                tvInterest.setText(Interest + "元");
                tvUserfee.setText(Userfee + "元");
                int all = (int) (Applyfee + Interest + Userfee);
                tvTotal.setText(all + "元");

                llKonw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                dialog.setContentView(view);
                dialog.show();


            }
        });
    }

    /**
     * 获取获取基本费用
     */
    private void getCost() {
        String tv = (String) tvjinge.getText();
        if (tv.equals("600.00")) {
            moneyID = 35;
        } else if (tv.equals("1000.00")) {
            moneyID = 3;
        }
        OkGo.<String>post("http://apk910okinfo.910ok.com/home/ExtendGetfee")
                .params("LoanId", moneyID)
                .params("TermId", 1)
                .params("token", token)
                .params("Identifier", 9)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.body() != null && !response.body().equals("")) {

                            JsonParser parser = new JsonParser();

                            JsonObject data = parser.parse(response.body()).getAsJsonObject();

                            if (data.get("result").getAsInt() == 1) {
                                //获取基本费用成功
                                //   data = data.get("data").getAsJsonObject();

                                Applyfee = data.get("Applyfee").getAsFloat();

                                Interest = data.get("Interest").getAsFloat();

                                Userfee = data.get("Userfee").getAsFloat();
                                int all = (int) (Applyfee + Interest + Userfee);
                                tv_cost.setText(all + "元");


                            } else {
                                Toast.makeText(BorrowMoneyActivity.this, "获取失败请检查网络连接", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
    }

    /**
     * 申请借款
     */
    public void loanBorrow() {
        backM = (String) tvjinge.getText();
        if (backM.equals("1000.00")) {
            moneyID = 3;
        } else if (backM.equals("600.00")) {
            moneyID = 35;
        }

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkGo.<String>post("http://apk910okinfo.910ok.com/home/ExtendLoadSave")
                        .params("LoanId", moneyID)
                        .params("TermId", 1)
                        .params("token", token)
                        .params("Applyfee", Applyfee)
                        .params("Interest", Interest)
                        .params("Userfee", Userfee)
                        .params("BackM", backM)
                        .params("Identifier", 9)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                JsonParser parser = new JsonParser();
                                JsonObject data = parser.parse(response.body()).getAsJsonObject();
                                String toast = data.get("message").getAsString();
                                Toast.makeText(BorrowMoneyActivity.this, toast, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Response<String> response) {
                                super.onError(response);
                                Toast.makeText(BorrowMoneyActivity.this, "您已提交申请了哦！", Toast.LENGTH_SHORT).show();
                            }
                        });
                Intent intent = new Intent(BorrowMoneyActivity.this, AuditActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * 获取姓名和逾期
     */
    public void getName() {


        OkGo.<String>post("http://apk910okinfo.910ok.com/home/ExtendSign")
                .params("token", token)
                .params("Identifier", 9)
                .params("Loan", borrowMon)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response != null && !response.body().equals("")) {
                            //数据不为空
                            JsonParser parser = new JsonParser();

                            JsonObject data = parser.parse(response.body()).getAsJsonObject();
                            String username = data.get("username").getAsString();
                            String overdue = data.get("OverdueP").getAsString();

                            String RongP = data.get("RongP").getAsString();
                            Log.e("tag", "jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjyvqui" + overdue + "vvvvvvvvvvvvvvv" + borrowMon);
                            tv_name.setText(username);
                            tv_yvqi.setText("七天容时期，" + RongP + "元每天   " + "七天后逾期，" + overdue + "元每天");

                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }
                });

    }

    /**
     * 全透状态栏
     */
    protected void setStatusBarFullTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 如果需要内容紧贴着StatusBar
     * 应该在对应的xml布局文件中，设置根布局fitsSystemWindows=true。
     */
    private View contentViewGroup;

    protected void setFitSystemWindow(boolean fitSystemWindow) {
        if (contentViewGroup == null) {
            contentViewGroup = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        }
        contentViewGroup.setFitsSystemWindows(fitSystemWindow);
    }

    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (run) {
//                doSomething();
                handler.postDelayed(this, 1000);
            }
        }
    };


    public void update() {
        OkGo.<String>post("http://apk910okinfo.910ok.com/home/LoanList")
                .params("token", token)
                .execute(new StringCallback() {

                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response != null && !response.body().equals("")) {
                            //数据不为空
                            JsonParser parser = new JsonParser();

                            JsonObject data = parser.parse(response.body()).getAsJsonObject();

                            if (data.get("result").getAsInt() == 1) {
                                //获取贷款金额成功

                                JsonArray dataList = data.get("dataList").getAsJsonArray();

                                List<String> mData = new ArrayList<>();

                                for (JsonElement item : dataList) {

                                    JsonObject data_obj = item.getAsJsonObject();
                                    moneyID = data_obj.get("ID").getAsInt();
                                    String nu = data_obj.get("Num").getAsString();
                                    tvjinge.setText(nu);

                                    tv_money.setText(tvjinge.getText());
                                    tv_yicixing.setText("一次性还清" + tvjinge.getText() + "元");
                                    Log.e("tag", "jjjjjjjjjjjjjjjjjjnunununununu" + nu);
                                    mData.add(data_obj.get("Num").getAsString());
                                    jine = String.valueOf(tvjinge.getText());
                                    borrowMon = (int) Float.parseFloat(jine);
                                    getName();
                                    getCost();
                                    loanBorrow();

                                }


                            } else {
                                Toast.makeText(BorrowMoneyActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }

    @Override
    public void onRefresh() {
        /**
         * 下拉刷新
         */


        //这里写你要刷新的数据，然后更新UI 更新完了之后调用

        srl.setRefreshing(false);
        //这一句是取消转圈的。明白了吧好简单  我说简单吧哈哈哈 恩额
        getCost();
        tv_money.setText(tvjinge.getText());
        tv_yicixing.setText("一次性还清" + tvjinge.getText() + "元");

        jine = String.valueOf(tvjinge.getText());
        borrowMon = (int) Float.parseFloat(jine);
        getName();
        getCost();
        // loanBorrow();


    }
}
