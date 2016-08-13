package com.hc.poem.page.author.model;

/**
 * Created by liulei0905 on 2016/8/12.
 * 存放从数据表中查询到的作者信息
 */
public class AuthorBean {

    private int mId;
    private String mName;
    private String mIntro;
    private String mProfileUrl;

    public AuthorBean() {
    }

    public AuthorBean(int mId, String mName, String mIntro, String mProfileUrl) {
        this.mId = mId;
        this.mName = mName;
        this.mIntro = mIntro;
        this.mProfileUrl = mProfileUrl;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getIntro() {
        return mIntro;
    }

    public void setIntro(String mIntro) {
        this.mIntro = mIntro;
    }

    public String getProfileUrl() {
        return mProfileUrl;
    }

    public void setProfileUrl(String mProfileUrl) {
        this.mProfileUrl = mProfileUrl;
    }

    @Override
    public String toString() {
        return "AuthorBean{" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                ", mIntro='" + mIntro + '\'' +
                ", mProfileUrl='" + mProfileUrl + '\'' +
                '}';
    }

}
