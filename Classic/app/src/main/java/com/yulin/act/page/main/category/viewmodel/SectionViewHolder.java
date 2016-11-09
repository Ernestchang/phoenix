package com.yulin.act.page.main.category.viewmodel;

import android.support.v7.widget.RecyclerView;

import com.yulin.classic.databinding.ItemCategorySectionBinding;

class SectionViewHolder extends RecyclerView.ViewHolder {

    private ItemCategorySectionBinding mBinding;

    SectionViewHolder(ItemCategorySectionBinding binding) {
        super(binding.getRoot());

        mBinding = binding;
    }

    public ItemCategorySectionBinding getBinding() {
        return mBinding;
    }

}
