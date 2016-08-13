package com.hc.poem.page.content.model;

public class PoemContentBean {

    private int mPoemId;
    private String mTitle;
    private String mSubTitle;
    private String mAuthor;
    private String mDynasty;
    private String mGenre;
    private String mIntro;
    private String mContent;

    public PoemContentBean() {
    }

    public PoemContentBean(int mPoemId, String mTitle, String mSubTitle
            , String mAuthor, String mDynasty, String mGenre, String mIntro, String mContent) {
        this.mPoemId = mPoemId;
        this.mTitle = mTitle;
        this.mSubTitle = mSubTitle;
        this.mAuthor = mAuthor;
        this.mDynasty = mDynasty;
        this.mGenre = mGenre;
        this.mIntro = mIntro;
        this.mContent = mContent;
    }

    public int getPoemId() {
        return mPoemId;
    }

    public void setPoemId(int mPoemId) {
        this.mPoemId = mPoemId;
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

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getDynasty() {
        return mDynasty;
    }

    public void setDynasty(String mDynasty) {
        this.mDynasty = mDynasty;
    }

    public String getGenre() {
        return mGenre;
    }

    public void setGenre(String mGenre) {
        this.mGenre = mGenre;
    }

    public String getIntro() {
        return mIntro;
    }

    public void setIntro(String mIntro) {
        this.mIntro = mIntro;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String mContent) {
        this.mContent = mContent;
    }

    @Override
    public String toString() {
        return "PoemContentBean{" +
                "mPoemId=" + mPoemId +
                ", mTitle='" + mTitle + '\'' +
                ", mSubTitle='" + mSubTitle + '\'' +
                ", mAuthor='" + mAuthor + '\'' +
                ", mDynasty='" + mDynasty + '\'' +
                ", mGenre='" + mGenre + '\'' +
                ", mIntro='" + mIntro + '\'' +
                ", mContent='" + mContent + '\'' +
                '}';
    }

}
