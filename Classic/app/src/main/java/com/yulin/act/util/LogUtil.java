package com.yulin.act.util;

import android.text.TextUtils;

import com.yulin.classic.BuildConfig;

/**
 * Created by liulei0905 on 2016/11/9.
 *
 * print log
 */

public class LogUtil {

    private static final int indexStack = 3;

    public static String customTagPrefix = "";

    public static void easylog(String tag, String content) {
        if (!BuildConfig.DEBUG)
            return;

        StackTraceElement caller = Thread.currentThread().getStackTrace()[indexStack];
        String pathtag = generateTag(caller);
        System.out.println(pathtag + "-> [" + tag + "]:" + content);
    }

    private static String generateTag(StackTraceElement caller) {
        // 例: cn.emoney.acg.page.MainPage$3.run(MainPage.java:252)
        String tag = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":" + tag;
        return tag;
    }

}
