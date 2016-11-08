package com.yulin.act.page.category.model;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;

public class NormalItem extends BaseItem {

    private ObservableField<String> mItemTitle;
    private ObservableInt mItemIconId;

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

    @Override
    public boolean isNormalItem() {
        return true;
    }

}
