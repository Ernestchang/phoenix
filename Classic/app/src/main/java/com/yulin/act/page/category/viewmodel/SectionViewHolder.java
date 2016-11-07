package com.yulin.act.page.category.viewmodel;

import android.support.v7.widget.RecyclerView;

import com.yulin.classic.databinding.ItemCategorySectionBinding;

class SectionViewHolder extends RecyclerView.ViewHolder {

    private ItemCategorySectionBinding mBinding;

    public SectionViewHolder(ItemCategorySectionBinding binding) {
        super(binding.getRoot());

        mBinding = binding;
    }

    ItemCategorySectionBinding getBinding() {
        return mBinding;
    }

}
