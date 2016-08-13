package com.hc.poem.page.content.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.text.TextUtils;

import com.hc.poem.db.PoemHelper;
import com.hc.poem.page.content.model.PoemContentBean;

public class PoemContentViewModel extends BaseObservable {

    private final PoemContentBean bean;

    public PoemContentViewModel(Context context, int poemId) {
        this.bean = PoemHelper.getPoemById(context, poemId);
    }

    public int getPoemId() {
        return bean == null ? 0 : bean.getPoemId();
    }

    public String getTitle() {
        return bean == null ? null : bean.getTitle();
    }

    public String getSubTitle() {
        if (bean != null) {
            String subTitle = bean.getSubTitle();

            if (!TextUtils.isEmpty(subTitle))
                return "·" + subTitle;
        }

        return null;
    }

    public String getAuthor() {
        return bean == null ? null : bean.getAuthor();
    }

    public String getDynasty() {
        return bean == null ? null : bean.getDynasty();
    }

    public String getGenre() {
        return bean == null ? null : bean.getGenre();
    }

    public String getIntro() {
        return bean == null ? null : bean.getIntro();
    }

    public String getContent() {
        return bean == null ? null : bean.getContent();
    }

}
