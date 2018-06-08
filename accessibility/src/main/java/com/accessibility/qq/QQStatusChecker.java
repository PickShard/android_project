package com.accessibility.qq;

import com.accessibility.utils.Constant;

import java.util.List;

/**
 * 微信状态检查器
 * Created by sc on 2018/3/28.
 */

public class QQStatusChecker {

    public static int STATUS_LOGIN = 1000; // 登录界面
    public static int STATUS_REGISTER_TEL = 2000; //注册输入手机号
    public static int STATUS_REGISTER_CODE = 3000; //输入注册验证码

    public static int STATUS_REGISTER_BUNDLE = 4000; //绑定提示页面
    public static int STATUS_REGISTER_SEND_SMS = 5000; //绑定提示页面



    private QQStatusChecker() {
    }

    private static QQStatusChecker mthis;

    public static QQStatusChecker getInstance() {
        if (mthis == null)
            mthis = new QQStatusChecker();
        return mthis;
    }


    public int checkStatus(List<String> ids) {

        if (ids == null || ids.isEmpty()) {
            return 0;
        }

        if (ids.contains(Constant.QQ_LOGIN_BUTTON) && ids.contains(Constant.QQ_LOGIN_PASSWORD)) {
            return STATUS_LOGIN;
        } else if (ids.contains(Constant.QQ_REGISTER_ACCROD_TEXT) && ids.contains(Constant.QQ_REGISTER_TEL_TEXT)) {
            return STATUS_REGISTER_TEL;
        }else if (ids.contains(Constant.QQ_REGISTER_CODE) && ids.contains(Constant.QQ_REGISTER_RESEND)){
            return STATUS_REGISTER_CODE;
        }else if (ids.contains(Constant.QQ_REGISTER_REGISTER) && ids.contains(Constant.QQ_REGISTER_LOGIN)){
            return STATUS_REGISTER_BUNDLE;
        }else if (ids.contains(Constant.QQ_REGISTER_CODE_INSEPECT) && ids.contains(Constant.QQ_REGISTER_SEND_SMS)){
            return STATUS_REGISTER_SEND_SMS;
        }

        return 0;
    }
}
