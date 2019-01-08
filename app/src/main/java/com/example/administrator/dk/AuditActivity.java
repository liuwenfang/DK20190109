package com.example.administrator.dk;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/12/25.
 * 借款流程 审核
 */

public class AuditActivity extends AppCompatActivity {
    private RelativeLayout rlWeixinkefu;
    private Context context;
    MyDialog myDialog;
    private ImageView ivBack;
    private String wxNumber;
    String token;

    private SwipeRefreshLayout srlAudit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit);
        setStatusBarFullTransparent();
        setFitSystemWindow(false);
//        ivBack = findViewById(R.id.iv_back);
        rlWeixinkefu = findViewById(R.id.rl_weixinkefu);

        srlAudit = findViewById(R.id.srl_audit);
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        srlAudit.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                //这里写你要刷新的数据，然后更新UI 更新完了之后调用

                srlAudit.setRefreshing(false);
                //这一句是取消转圈的。明白了吧好简单  我说简单吧哈哈哈 恩额

                OkGo.<String>post("http://apk910okinfo.910ok.com/home/ExtendNoComplate")
                        .params("token", token)
                        .params("Identifier", 9)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                JsonParser parser = new JsonParser();

                                JsonObject data = parser.parse(response.body()).getAsJsonObject();

                                int status = data.get("Status").getAsInt();

                                if (status == 4) {
                                    Intent intent = new Intent(AuditActivity.this, ResultActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else if (status == 5) {
                                    Intent i = new Intent(AuditActivity.this, MainActivity.class);
                                    startActivity(i);
                                    finish();

                                }else if (status == 8) {
                                    Intent i = new Intent(AuditActivity.this, MainActivity.class);
                                    startActivity(i);
                                    finish();

                                }
                            }
                        });


            }
        });


        token = getSharedPreferences("token", MODE_PRIVATE).getString("token", "");
        rlWeixinkefu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(AuditActivity.this, R.style.NormalDialogStyle);
                LayoutInflater inflater = LayoutInflater.from(AuditActivity.this);
                View view = inflater.inflate(R.layout.layout_dialog, null);
                RelativeLayout rlKonw = view.findViewById(R.id.rl_yes);
                final TextView tvNumber = view.findViewById(R.id.tv_number);
                Button btn_copy = view.findViewById(R.id.btn_copy);

                OkGo.<String>post("http://apk910okinfo.910ok.com/home/getWxCode")
                        .params("token", token)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                if (response != null && !response.body().equals("")) {
                                    //数据不为空
                                    JsonParser parser = new JsonParser();

                                    JsonObject data = parser.parse(response.body()).getAsJsonObject();
                                    JsonArray datawx = data.get("wxCode").getAsJsonArray();

                                    List<String> mData = new ArrayList<>();

                                    for (JsonElement item : datawx) {

                                        JsonObject data_obj = item.getAsJsonObject();

                                        wxNumber = data_obj.get("wxCode").getAsString();

                                        tvNumber.setText(wxNumber + "");
                                    }


                                } else {

                                }
                            }
                        });


                btn_copy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        // 创建普通字符型ClipData
                        ClipData mClipData = ClipData.newPlainText("Label", wxNumber);
                        // 将ClipData内容放到系统剪贴板里。
                        cm.setPrimaryClip(mClipData);

                        Toast.makeText(AuditActivity.this, "复制成功", Toast.LENGTH_LONG).show();
                    }
                });
                dialog.setContentView(view);
                //使得点击对话框外部不消失对话框
                dialog.setCanceledOnTouchOutside(false);
                //设置对话框的大小
                v.setMinimumHeight((int) (ScreenSizeUtils.getInstance(AuditActivity.this).getScreenHeight() * 0.23f));
                Window dialogWindow = dialog.getWindow();
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                lp.width = (int) (ScreenSizeUtils.getInstance(AuditActivity.this).getScreenWidth() * 0.75f);
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;
                dialogWindow.setAttributes(lp);
                dialog.show();

                dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
                rlKonw.setOnClickListener(new View.OnClickListener() {

                    @SuppressLint("WrongConstant")
                    @Override
                    public void onClick(View arg0) {
                        dialog.dismiss();
                    }
                });


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

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
