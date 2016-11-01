package com.yulin.applib.page;

import android.os.Bundle;

/**
 * Page跳转意图 模拟了Intent 通过对PageIntent的设置，实现强大的跳转目的
 * 
 * @version 1.0
 */
public class PageIntent {

    // 每次启动都会新增一个目标page，入栈
    public final static int FLAG_PAGE_STANDARD = 0;// 默认启动方式
    // 每次启动用一个目标page替换指定的视图，不入栈
    public final static int FLAG_PAGE_REPLACE = 1;// 多用于替换视图
    // 如果栈中已存在该意图，则直接跳转至目标page，并销毁该目标page以上的page
    public final static int FLAG_PAGE_CLEAR_TOP = 2;
    // 启动一个新的page，不入栈，随着新启动的page的创建而销毁
    public final static int FLAG_PAGE_NO_HISTORY = 3;
    public final static int FLAG_PAGE_REPLACE_AND_NO_HISTORY = 4;

    private Class<? extends Page> mTargetClass;
    private Page mTargetInstance;
    private int mFlags = FLAG_PAGE_STANDARD;

    private Bundle mArguments;

    public PageIntent() {
    }

    public PageIntent(PageContext context, String className) {
        try {
            Class<? extends Page> clz = (Class<? extends Page>) Class.forName(className);
            mTargetClass = clz;
            mTargetInstance = clz.newInstance();
            mTargetInstance.mLinkedContext = context;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public PageIntent(PageContext context, Page page) {
        mTargetClass = page.getClass();
        mTargetInstance = page;
        mTargetInstance.mLinkedContext = context;
    }

    public PageIntent(PageContext context, Class<? extends Page> cls) {
        mTargetClass = cls;
        try {
            mTargetInstance = mTargetClass.newInstance();
            mTargetInstance.mLinkedContext = context;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public void setClass(PageContext context, Class<? extends Page> cls) {
        mTargetClass = cls;
        try {
            mTargetInstance = mTargetClass.newInstance();
            mTargetInstance.mLinkedContext = context;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public void setArguments(Bundle bundle) {
        mArguments = bundle;
    }

    public Bundle getArguments() {
        return mArguments;
    }

    public void setFlags(int flags) {
        mFlags = flags;
    }

    public int getFlags() {
        return mFlags;
    }

    public Class<? extends Page> getTargetClass() {
        return mTargetClass;
    }

    public Page getTargetInstance() {
        return mTargetInstance;
    }

    public boolean equals(PageIntent intent) {
        if (intent == null) {
            return false;
        }
        if (this == intent) {
            return true;
        }
        Class<? extends Page> targetClass = intent.getTargetClass();
        if (targetClass == mTargetClass) {
            return true;
        }
        return false;
    }

    public void needPringLog(boolean flag) {
        if (mTargetInstance != null) {
            mTargetInstance.needPringLog(flag);
        }
    }

    public void setSupportAnimation(boolean isSupported) {
        if (mTargetInstance != null) {
            mTargetInstance.setSupportAnimation(isSupported);
        }
    }

    public String toString() {
        return "Page:" + mTargetInstance.getClass().getSimpleName() + " Flags:" + mFlags;
    }

}
