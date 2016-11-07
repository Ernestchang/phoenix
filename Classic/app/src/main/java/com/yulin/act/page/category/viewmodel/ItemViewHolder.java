package com.yulin.act.page.category.viewmodel;

import android.support.v7.widget.RecyclerView;

import com.yulin.classic.databinding.ItemCategoryItemsBinding;

class ItemViewHolder extends RecyclerView.ViewHolder {

    private ItemCategoryItemsBinding mBinding;

    public ItemViewHolder(ItemCategoryItemsBinding binding) {
        super(binding.getRoot());

        mBinding = binding;
    }

    ItemCategoryItemsBinding getBinding() {
        return mBinding;
    }

}
