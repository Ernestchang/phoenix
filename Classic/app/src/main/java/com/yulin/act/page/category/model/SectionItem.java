package com.yulin.act.page.category.model;

public class SectionItem extends BaseItem {

    private String mSectionName;

    public SectionItem(short itemType, String sectionName) {
        super(itemType);
        mSectionName = sectionName;
    }

    public String getSectionName() {
        return mSectionName;
    }

}
