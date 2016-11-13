package com.yulin.act.page.menu.list.vm;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yulin.act.model.BaseItem;
import com.yulin.act.model.NormalItem;
import com.yulin.act.model.SectionItem;
import com.yulin.act.page.content.poem.view.PoemContentActivity;
import com.yulin.act.page.main.category.viewmodel.BottomViewHolder;
import com.yulin.act.page.menu.grid.vm.SectionViewHolder;
import com.yulin.applib.page.Page;
import com.yulin.classic.BR;
import com.yulin.classic.R;
import com.yulin.classic.databinding.ItemListMenuNormalBinding;
import com.yulin.classic.databinding.ItemShortMenuNormalBinding;
import com.yulin.classic.databinding.ItemShortMenuSectionBinding;

import java.util.ArrayList;

public class ListMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<BaseItem> mListItems;
    private Page mPage;

    ListMenuAdapter(ArrayList<BaseItem> listItems, Page page) {
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
            ItemShortMenuSectionBinding itemSectionBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_short_menu_section, parent, false);
            return new SectionViewHolder(itemSectionBinding);
        } else {
            ItemListMenuNormalBinding itemItemsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_list_menu_normal, parent, false);
            return new NormalViewHolder(itemItemsBinding);
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

            final int index = position;
            normalViewHolder.getBinding().layoutRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PoemContentActivity.gotoModule(mPage, getContentIds(), getCurrentIndex(index));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mListItems == null ? 0 : mListItems.size();
    }

    private ArrayList<Integer> getContentIds() {
        ArrayList<Integer> listContentIds = new ArrayList<>();

        for (int i = 0; i < mListItems.size(); i++) {
            BaseItem item = mListItems.get(i);
            if (item instanceof NormalItem) {
                NormalItem normalItem = (NormalItem) item;
                listContentIds.add(normalItem.getContentId());
            }
        }

        return listContentIds;
    }

    private int getCurrentIndex(int position) {
        int sectionItemCount = 0;
        for (int i = 0; i < position; i++) {
            BaseItem item = mListItems.get(i);
            if (item instanceof SectionItem) sectionItemCount++;
        }

        return position - sectionItemCount >= 0 ? position - sectionItemCount : 0;
    }

}
