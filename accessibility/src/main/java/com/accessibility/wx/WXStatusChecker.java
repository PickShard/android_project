package com.accessibility.wx;

import com.accessibility.utils.Constant;

import java.util.List;

/**
 * 微信状态检查器
 * Created by sc on 2018/3/28.
 */

public class WXStatusChecker {

    public static int STATUS_WX = 1000; // 首页
    public static int STATUS_CHAT = 2000; //聊天窗口
    public static int STATUS_CHAT_MORE = 2100; //聊天窗口显示更多面板
    public static int STATUS_CHAT_KEYBORD = 2200; //聊天窗口显示服务面板
    public static int STATUS_COLLECT = 3000; //收藏界面
    public static int STATUS_COLLECT_DIALOG = 3100; //收藏界面显示发送面板
    public static int STATUS_TYT = 6000; //收藏界面显示发送面板


    private WXStatusChecker() {
    }

    private static WXStatusChecker mthis;

    public static WXStatusChecker getInstance() {
        if (mthis == null)
            mthis = new WXStatusChecker();
        return mthis;
    }


    public int checkStatus(List<String> ids) {

        if (ids == null || ids.isEmpty()) {
            return 0;
        }

        if (ids.contains(Constant.WX_ID_TAB) && ids.contains(Constant.WX_ID_TITLE)) {
            return STATUS_WX;
        } else if (ids.contains(Constant.WX_ID_CHAT_BACK) && ids.contains(Constant.WX_ID_CHAT_KEYBORD)) {
            return STATUS_CHAT_KEYBORD;
        }else if (ids.contains(Constant.WX_ID_CHAT_BACK) && ids.contains(Constant.WX_ID_CHAT_MORE_ITEM)) {
            return STATUS_CHAT_MORE;
        }else if (ids.contains(Constant.WX_ID_CHAT_BACK) && ids.contains(Constant.WX_ID_CHAT_TITIL)) {
            return STATUS_CHAT;
        }else if (ids.contains(Constant.WX_ID_TITLE) && ids.contains(Constant.WX_ID_COLLOCT_ITEM)) {
            return STATUS_COLLECT;
        }else if (ids.contains(Constant.WX_ID_SEND) && ids.contains(Constant.WX_ID_CANCLE)) {
            return STATUS_COLLECT_DIALOG;
        }else if (ids.contains(Constant.WX_ID_TYT_GS) && ids.contains(Constant.WX_ID_TYT_KH)) {
            return STATUS_TYT;
        }

        return 0;
    }
}
