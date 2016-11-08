package com.yulin.act.page.category.model;

public class BaseItem {

    public static final short ITEM_TYPE_SECTION = 1;
    public static final short ITEM_TYPE_NORMAL = 2;
    public static final short ITEM_TYPE_BOTTOM = 3;

    private short mItemType;

    BaseItem(short itemType) {
        mItemType = itemType;
    }

    public short getItemType() {
        return mItemType;
    }

}
