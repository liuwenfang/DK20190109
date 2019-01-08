package com.example.administrator.dk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

/**
 * Created by Administrator on 2019/1/2.
 */

public class ResultActivity extends AppCompatActivity {

    private SwipeRefreshLayout srlResult;
    String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        srlResult = findViewById(R.id.srl_result);
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        srlResult.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                //这里写你要刷新的数据，然后更新UI 更新完了之后调用

                srlResult.setRefreshing(false);
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
                                if (status == 5) {
                                    Intent i = new Intent(ResultActivity.this, MainActivity.class);
                                    startActivity(i);
                                    finish();

                                }
                            }
                        });


            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
