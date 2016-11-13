package com.yulin.act.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.yulin.classic.BR;

/**
 * Created by liulei0905 on 2016/11/10.
 * <p>
 * 存储诗内容
 */

public class PoemContent extends BaseObservable {

    private int mContentId;
    private String mTitle;
    private String mSubTitle;
    private String mAuthor;
    private String mDynastyName;
    private String mIntroduction;
    private String mContent;

    public PoemContent() {
    }

    // SELECT p._id, p.title, p.subtitle, a.name, d.name, g.name, p.introduction, p.content
    public PoemContent(int mContentId, String mTitle, String subTitle, String mAuthor, String mDynastyName,
                       String introduction, String mContent) {
        this.mContentId = mContentId;
        this.mTitle = mTitle;
        this.mSubTitle = subTitle;
        this.mAuthor = mAuthor;
        this.mDynastyName = mDynastyName;
        this.mIntroduction = introduction;
        this.mContent = mContent;
    }

    public int getContentId() {
        return mContentId;
    }

    public void setContentId(int mContentId) {
        this.mContentId = mContentId;
    }

    @Bindable
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getSubTitle() {
        return mSubTitle;
    }

    public void setSubTitle(String mSubTitle) {
        this.mSubTitle = mSubTitle;
        notifyPropertyChanged(BR.subTitle);
    }

    @Bindable
    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
        notifyPropertyChanged(BR.author);
    }

    @Bindable
    public String getDynastyName() {
        return mDynastyName;
    }

    public void setDynastyName(String mDynastyName) {
        this.mDynastyName = mDynastyName;
        notifyPropertyChanged(BR.dynastyName);
    }

    @Bindable
    public String getIntroduction() {
        return mIntroduction;
    }

    public void setIntroduction(String mIntroduction) {
        this.mIntroduction = mIntroduction;
        notifyPropertyChanged(BR.introduction);
    }

    @Bindable
    public String getContent() {
        return mContent;
    }

    public void setContent(String mContent) {
        this.mContent = mContent;
        notifyPropertyChanged(BR.content);
    }

}
