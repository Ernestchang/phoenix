package com.hc.poem.page.list.model;

/**
 * Created by houchen on 2016/7/26.
 * Cash data in every item.
 */
public class PoemItem {

    private int mPoemId;
    private String mDynasty;
    private String mAuthor;
    private String mTitle;
    private String mSubTitle;
    private String mAddition;    // 显示在列表中，不显示在详情，通常为诗词第一句

    public PoemItem() {
    }

    public PoemItem(int poemId, String mDynasty, String mAuthor, String mTitle, String mSubTitle, String mAddition) {
        this.mPoemId = poemId;
        this.mDynasty = mDynasty;
        this.mAuthor = mAuthor;
        this.mTitle = mTitle;
        this.mSubTitle = mSubTitle;
        this.mAddition = mAddition;
    }

    public int getPoemId() {
        return mPoemId;
    }

    public void setPoemId(int mPoemId) {
        this.mPoemId = mPoemId;
    }

    public String getDynasty() {
        return mDynasty;
    }

    public void setDynasty(String mDynasty) {
        this.mDynasty = mDynasty;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getSubTitle() {
        return mSubTitle;
    }

    public void setSubTitle(String mSubTitle) {
        this.mSubTitle = mSubTitle;
    }

    public String getAddition() {
        return mAddition;
    }

    public void setAddition(String mAddition) {
        this.mAddition = mAddition;
    }

    @Override
    public String toString() {
        return "PoemItem{" +
                "mPoemId=" + mPoemId +
                ", mDynasty='" + mDynasty + '\'' +
                ", mAuthor='" + mAuthor + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mSubTitle='" + mSubTitle + '\'' +
                '}';
    }

}
