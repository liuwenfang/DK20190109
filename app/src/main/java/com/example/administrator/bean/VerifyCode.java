package com.example.administrator.bean;

/**
 * Created by Administrator on 2018/12/27.
 */

public class VerifyCode {
    public String Mobile;
    public String MobilePwd;
    public int Identifier;

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getMobilePwd() {
        return MobilePwd;
    }

    public void setMobilePwd(String mobilePwd) {
        MobilePwd = mobilePwd;
    }

    public int getIdentifier() {
        return Identifier;
    }

    public void setIdentifier(int identifier) {
        Identifier = identifier;
    }
}
