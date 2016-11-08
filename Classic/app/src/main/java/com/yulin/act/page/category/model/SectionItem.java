package com.yulin.act.page.category.model;

import android.databinding.ObservableField;

public class SectionItem extends BaseItem {

    private ObservableField<String> mSectionName;

    public SectionItem(short itemType, String sectionName) {
        super(itemType);
        mSectionName = new ObservableField<>(sectionName);
    }

    public String getSectionName() {
        return mSectionName.get();
    }

}
