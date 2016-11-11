package com.yulin.act.model;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;

public class NormalItem extends BaseItem {

    private ObservableField<String> mItemTitle;
    private ObservableInt mItemIconId;
    private int mContentId;

    public NormalItem(short itemType, int contentId, String itemTitle, int itemIconId) {
        this(itemType, itemTitle, itemIconId);
        mContentId = contentId;
    }

    public NormalItem(short itemType, int contentId, String itemTitle) {
        this(itemType, itemTitle, 0);
        mContentId = contentId;
    }

    public NormalItem(short itemType, String itemTitle, int itemIconId) {
        super(itemType);
        mItemTitle = new ObservableField<>(itemTitle);
        mItemIconId = new ObservableInt(itemIconId);
    }

    public String getItemTitle() {
        return mItemTitle.get();
    }

    public int getItemIconId() {
        return mItemIconId.get();
    }

    public int getContentId() {
        return mContentId;
    }

}
