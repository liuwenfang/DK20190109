package com.example.administrator.dk;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/1/2.
 */

public class WXActivity extends AppCompatActivity {
    //    private WebView webview;
    private TextView btncopy;
    private String wxNumber;
    private TextView Number;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx);
        btncopy = findViewById(R.id.btcopy);
        Number= findViewById(R.id.tv_wxnumber);

        String token = getIntent().getStringExtra("token");


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

                                Number.setText(wxNumber + "");
                            }


                        } else {

                        }
                    }
                });

        btncopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", wxNumber+"123");
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);

                Toast.makeText(WXActivity.this, "复制成功", Toast.LENGTH_LONG).show();
            }
        });


    }
}
