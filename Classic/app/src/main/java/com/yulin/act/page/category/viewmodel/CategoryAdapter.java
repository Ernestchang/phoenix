package com.yulin.act.page.category.viewmodel;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yulin.act.page.category.model.BaseItem;
import com.yulin.act.page.category.model.NormalItem;
import com.yulin.act.page.category.model.SectionItem;
import com.yulin.act.page.smenu.view.ShortMenuActivity;
import com.yulin.act.util.Util;
import com.yulin.applib.page.Page;
import com.yulin.classic.BR;
import com.yulin.classic.R;
import com.yulin.classic.databinding.ItemCategoryItemsBinding;
import com.yulin.classic.databinding.ItemCategorySectionBinding;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BaseItem> mListItems;
    private Page mPage;

    CategoryAdapter(List<BaseItem> listItems, Page page) {
        mListItems = listItems;
        mPage = page;
    }

    @Override
    public int getItemViewType(int position) {
        return mListItems.get(position).getItemType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == BaseItem.ITEM_TYPE_SECTION) {
            ItemCategorySectionBinding itemSectionBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_category_section, parent, false);
            return new SectionViewHolder(itemSectionBinding);
        } else if (viewType == BaseItem.ITEM_TYPE_NORMAL) {
            ItemCategoryItemsBinding itemItemsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_category_items, parent, false);
            return new NormalViewHolder(itemItemsBinding);
        } else {
            View bottomView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_bottom, parent, false);
            return new BottomViewHolder(bottomView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SectionViewHolder) {
            SectionItem sectionItem = (SectionItem) mListItems.get(position);
            SectionViewHolder sectionViewHolder = (SectionViewHolder) holder;
            sectionViewHolder.getBinding().setVariable(BR.model, sectionItem);
            sectionViewHolder.getBinding().executePendingBindings();
        } else if (holder instanceof NormalViewHolder) {
            final NormalItem normalItem = (NormalItem) mListItems.get(position);
            NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
            normalViewHolder.getBinding().setVariable(BR.model, normalItem);
            normalViewHolder.getBinding().executePendingBindings();

            normalViewHolder.getBinding().layoutSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShortMenuActivity.gotoModule(mPage);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mListItems == null ? 0 : mListItems.size();
    }

}
