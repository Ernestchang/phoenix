package com.yulin.act.page.category.viewmodel;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.yulin.act.page.category.model.BaseItem;
import com.yulin.classic.R;
import com.yulin.classic.databinding.ItemCategoryItemsBinding;
import com.yulin.classic.databinding.ItemCategorySectionBinding;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BaseItem> mListItems;

    public CategoryAdapter(List<BaseItem> listItems) {
        mListItems = listItems;
    }

    @Override
    public int getItemViewType(int position) {
        BaseItem item = mListItems == null ? null : mListItems.get(position);
        if (item != null) {
            return item.getItemType();
        }

        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == BaseItem.ITEM_TYPE_SECTION) {
            ItemCategorySectionBinding itemSectionBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_category_section, parent, false);
            return new SectionViewHolder(itemSectionBinding);
        } else if (viewType == BaseItem.ITEM_TYPE_NORMAL) {
            ItemCategoryItemsBinding itemItemsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_category_items, parent, false);
            return new ItemViewHolder(itemItemsBinding);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SectionViewHolder) {

        } else if (holder instanceof ItemViewHolder) {
        }
    }

    @Override
    public int getItemCount() {
        return mListItems == null ? 0 : mListItems.size();
    }

}
