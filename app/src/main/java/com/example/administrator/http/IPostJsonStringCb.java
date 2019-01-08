package com.example.administrator.http;

import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

public interface IPostJsonStringCb {
    void onSuccess(String str,String data);
    void onError(Response<String> response);
    void onStart(Request<String, ? extends Request> str);
    void onFinish();
}
