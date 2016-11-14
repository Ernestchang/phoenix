package com.yulin.act.page.menu.list.vm;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yulin.act.model.BaseItem;
import com.yulin.act.model.NormalItem;
import com.yulin.act.model.SectionItem;
import com.yulin.act.page.menu.grid.vm.SectionViewHolder;
import com.yulin.act.util.Util;
import com.yulin.applib.page.Page;
import com.yulin.applib.widget.PinnedSectionListView;
import com.yulin.classic.BR;
import com.yulin.classic.R;
import com.yulin.classic.databinding.ItemListMenuNormalBinding;
import com.yulin.classic.databinding.ItemShortMenuSectionBinding;

import java.util.ArrayList;

public class MenuListAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {

    private ArrayList<BaseItem> mListItems;
    private Page mPage;

    MenuListAdapter(ArrayList<BaseItem> listItems, Page page) {
        mListItems = listItems;
        mPage = page;
    }

    @Override
    public int getItemViewType(int position) {
        return mListItems.get(position).getItemType();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == BaseItem.ITEM_TYPE_SECTION;
    }

    @Override
    public int getCount() {
        return mListItems.size();
    }

    @Override
    public BaseItem getItem(int position) {
        return mListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);

        if (viewType == BaseItem.ITEM_TYPE_SECTION) {
            SectionViewHolder vh;
            if (convertView == null) {
                ItemShortMenuSectionBinding itemSectionBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_short_menu_section, parent, false);
                convertView = itemSectionBinding.getRoot();
                vh = new SectionViewHolder(itemSectionBinding);
                convertView.setTag(vh);
            } else {
                vh = (SectionViewHolder) convertView.getTag();
            }

            SectionItem sectionItem = (SectionItem) getItem(position);
            vh.getBinding().setVariable(BR.model, sectionItem);
            vh.getBinding().executePendingBindings();
        } else if (viewType == BaseItem.ITEM_TYPE_NORMAL) {
            NormalViewHolder vh;
            if (convertView == null) {
                ItemListMenuNormalBinding itemNormalBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_list_menu_normal, parent, false);
                convertView = itemNormalBinding.getRoot();
                vh = new NormalViewHolder(itemNormalBinding);
                convertView.setTag(vh);
            } else {
                vh = (NormalViewHolder) convertView.getTag();
            }

            NormalItem normalItem = (NormalItem) getItem(position);
            vh.getBinding().setVariable(BR.model, normalItem);
            vh.getBinding().executePendingBindings();
        }

        return convertView;
    }

    private class SectionViewHolder {

        private ItemShortMenuSectionBinding mBinding;

        SectionViewHolder(ItemShortMenuSectionBinding binding) {
            mBinding = binding;
        }

        public ItemShortMenuSectionBinding getBinding() {
            return mBinding;
        }

    }

    private class NormalViewHolder {

        private ItemListMenuNormalBinding mBinding;

        NormalViewHolder(ItemListMenuNormalBinding binding) {
            mBinding = binding;
        }

        ItemListMenuNormalBinding getBinding() {
            return mBinding;
        }

    }

}
