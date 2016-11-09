package com.yulin.act.page.menu.grid.vm;

import android.databinding.ObservableField;

import com.yulin.act.db.ShiJingHelper;
import com.yulin.act.model.BaseItem;
import com.yulin.applib.page.Page;

import java.util.ArrayList;
import java.util.List;

public class ShortMenuViewModel {

    private List<BaseItem> mListItems;
    private ObservableField<ShortMenuAdapter> mShortMenuAdapter;

    public ShortMenuViewModel(Page page) {
        mListItems = new ArrayList<>();
        initListItems();
        mShortMenuAdapter = new ObservableField<>(new ShortMenuAdapter(mListItems, page));
    }

    public ObservableField<ShortMenuAdapter> getShortMenuAdapter() {
        return mShortMenuAdapter;
    }

    private void initListItems() {
        mListItems.addAll(ShiJingHelper.query());
    }

    public BaseItem getItem(int position) {
        if (mListItems != null && mListItems.size() > position) {
            return mListItems.get(position);
        }

        return null;
    }

}
