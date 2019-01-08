package com.example.administrator.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * User: AMQR
 * Date&Time: 2018-09-11 & 10:36
 * Describe: 这个类存在的意义是为了做请求的统一处理
 * 不要conver转对象，原生数据处理起来才可以灵活变通
 */
public class OkGoUtils {
    /**
     * 网络请求的相关参数
     */
    public static class RequestOption{
        public Context context;
        public Map params;
        public String url;
        public AbsPostJsonStringCb iPostJsonStringCb;
        /**
         * 是否同意处理response相应码
         * 一般来说都是默认统一处理，当后端部分接口返回不规范的时候，需要单独处理，
         */
        public boolean isNormalDeal = true;
        public JSONObject mJSONObject;
        //public LoadHelpView loadHelpView;
    }


    /**
     * post请求
     * 以String形式返回数据，以post方式提交,包装这一层，是为了统一处理数据
     */
    public static void postJsonStringCallback(final RequestOption requestOption){


        JSONObject jsonObject;
        if(requestOption.mJSONObject!=null){
            jsonObject = requestOption.mJSONObject;
        }else{
            jsonObject = new JSONObject(requestOption.params);
        }

        OkGo.<String>post(requestOption.url)
                //.tag()
                .upJson(jsonObject)
                .headers("Authorization", "本地存储Token")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String resp = response.body().toString();
                    //    FortuneLogUtils.d(requestOption.url+"  ======= >>>>>> 请求详情:  "+"\n"+"request："+requestOption.url+"\n"+"params： "+requestOption.params+"\n"+"response: "+resp);

                        // 是否需要 统一的处理
                        if(requestOption.isNormalDeal){
                            if(unifiedProcessingCode(resp, requestOption)){
                                backToSuccessNormal(resp, requestOption);
                            }
                        }else{
                            backToSuccessOriginal(resp, requestOption);
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

//                        FtToastUtil.showShort(requestOption.context,"请求失败，请稍后在试");
//                        FortuneLogUtils.d("网络请求异常："+response.toString());

                        if(response.body()!=null){
                            String err = response.body().toString();
//                            FortuneLogUtils.e("======= request faile："+requestOption.url+"\n"+"params： "+requestOption.params+"\n"+"response: "+err);
                        }

                        if(requestOption.iPostJsonStringCb !=null){
                            requestOption.iPostJsonStringCb.onError(response);
                        }

                    }

                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        if(requestOption.iPostJsonStringCb !=null){
                            requestOption.iPostJsonStringCb.onStart(request);
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if(requestOption.iPostJsonStringCb !=null){
                            requestOption.iPostJsonStringCb.onFinish();
                        }
                    }
                });
    }

    /**
     * 返回码的统一处理
     * 当返回 true 时，代表success。
     * @param resp
     * @param requestOption
     * @return
     */
    private static boolean unifiedProcessingCode(String resp, final RequestOption requestOption) {
        if(!TextUtils.isEmpty(resp)){
            try {
                JSONObject jsonObject = new JSONObject(resp);
                String code= jsonObject.optString("code");
                // 假设返回码200为通过
                if(code.equals("200")){
                    return true;
                }else if (code.equals("1001")) { // 其他错误码处理
                    //FtToastUtil.showShort(requestOption.context, "验证码错误");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return  false;
    }

    /**
     * 某些错误码的出现，应该让程序直接抛到登录页面
     * @param requestOption
     * @param content
     */
    private static void gotoLogin(final RequestOption requestOption, String content) {
        // code...
    }


    /**
     * get请求
     * @param requestOption
     */
    public  static void getJsonStringCallback(final RequestOption requestOption){

        if(requestOption.params!=null && requestOption.params.size()>0){
            Map<String,String> map = requestOption.params;
            boolean isFirst = true;
            StringBuffer stringBuffer= new StringBuffer();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if(isFirst){
                    stringBuffer.append("?"+entry.getKey()+"="+entry.getValue());
                    isFirst = false;
                }else{
                    stringBuffer.append("&"+entry.getKey()+"="+entry.getValue());
                }
            }
            requestOption.url = requestOption.url+stringBuffer.toString();
        }


        OkGo.<String>get(requestOption.url)
                // header 参数，如果需要的话
                .headers("Authorization", "本地存储")
                //.tag()
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String resp = response.body().toString();
                      //  FortuneLogUtils.d("======= request："+"\n"+requestOption.url+"\n"+"response: "+resp);
                        Log.i("tt","======= request："+"\n"+requestOption.url+"\n"+"response: "+resp);
                        // 是否需要 统一的处理
                        if(requestOption.isNormalDeal){
                            if(unifiedProcessingCode(resp,requestOption)){
                                backToSuccessNormal(resp, requestOption);
                            }
                        }else{
                            backToSuccessOriginal(resp, requestOption);
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.i("tt",requestOption.context+"请求失败，请稍后在试");
                      //  FtToastUtil.showShort(requestOption.context,"请求失败，请稍后在试");
                        if(response.body()!=null){
                            Log.i("tt",response.toString()+"请求失败，请稍后在试");
                         //   FortuneLogUtils.d("网络请求异常："+response.toString());
                            String err = response.body().toString();
                            Log.i("tt",requestOption.url+err+"请求失败，请稍后在试");
                          //  FortuneLogUtils.e("======= request faile："+requestOption.url+"\n"+"response: "+err);
                        }

                        if(requestOption.iPostJsonStringCb !=null){
                            requestOption.iPostJsonStringCb.onError(response);
                        }

                    }

                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        if(requestOption.iPostJsonStringCb !=null){
                            requestOption.iPostJsonStringCb.onStart(request);
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if(requestOption.iPostJsonStringCb !=null){
                            requestOption.iPostJsonStringCb.onFinish();
                        }
                    }
                });
    }

    /**
     * 请求success 通用处理
     * @param resp
     * @param requestOption
     */
    private static void backToSuccessNormal(String resp, RequestOption requestOption) {

        if(requestOption.iPostJsonStringCb !=null){
            try {
                JSONObject jsonObject = new JSONObject(resp);
                String data = jsonObject.getString("data");
                requestOption.iPostJsonStringCb.onSuccess(resp,data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 请求成功，原生字符串处理
     * @param resp
     * @param requestOption
     */
    private static void backToSuccessOriginal(String resp, RequestOption requestOption) {

        if(requestOption.iPostJsonStringCb !=null){
            requestOption.iPostJsonStringCb.onSuccess(resp,resp);
        }
    }
}