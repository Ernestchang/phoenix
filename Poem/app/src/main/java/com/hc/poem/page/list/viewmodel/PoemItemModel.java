package com.hc.poem.page.list.viewmodel;

import android.databinding.BaseObservable;
import android.text.TextUtils;

import com.hc.poem.page.list.model.PoemItem;

/**
 * Created by houchen on 2016/7/26.
 * Hold a instance of PoemItem.
 */
public class PoemItemModel extends BaseObservable {

    private final PoemItem poemItem;

    public PoemItemModel(PoemItem poemItem) {
        this.poemItem = poemItem;
    }

    public int getPoemId() {
        return poemItem == null ? 0 : poemItem.getPoemId();
    }

    public String getDynasty() {
        return poemItem == null ? null : poemItem.getDynasty();
    }

    public String getAuthor() {
        return poemItem == null ? null : poemItem.getAuthor();
    }

    public String getTitle() {
        return poemItem == null ? null : poemItem.getTitle();
    }

    public String getSubTitle() {
        if (poemItem != null) {
            String tSubTitle = poemItem.getSubTitle();
            String tAddition = poemItem.getAddition();
            StringBuilder tResult = new StringBuilder();

            if (!TextUtils.isEmpty(tSubTitle) || !TextUtils.isEmpty(tAddition)) {
                tResult.append("·");

                if (!TextUtils.isEmpty(tSubTitle)) {
                    tResult.append(tSubTitle);
                    return tResult.toString();
                }

                if (!TextUtils.isEmpty(tAddition)) {
                    tResult.append(tAddition);
                    return tResult.toString();
                }
            }
        }

        return null;
    }

}
