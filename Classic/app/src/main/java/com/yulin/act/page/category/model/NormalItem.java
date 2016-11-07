package com.yulin.act.page.category.model;

public class NormalItem extends BaseItem {

    private String mItemTitle;
    private int mItemIconId;

    public NormalItem(short itemType, String itemTitle, int itemIconId) {
        super(itemType);
        mItemTitle = itemTitle;
        mItemIconId = itemIconId;
    }

    public String getItemTitle() {
        return mItemTitle;
    }

    public int getItemIconId() {
        return mItemIconId;
    }

    @Override
    public boolean isNormalItem() {
        return true;
    }

}
