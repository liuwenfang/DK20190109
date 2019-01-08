package com.example.administrator.http;

import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

public abstract class  AbsPostJsonStringCb implements IPostJsonStringCb{

    // 抽象类里面复写的方法后面作为 非必选方法。
    @Override
    public void onError(Response<String> response) {

    }

    @Override
    public void onStart(Request<String, ? extends Request> str) {

    }
}
