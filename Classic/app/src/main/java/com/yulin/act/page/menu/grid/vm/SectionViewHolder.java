package com.yulin.act.page.menu.grid.vm;

import android.support.v7.widget.RecyclerView;

import com.yulin.classic.databinding.ItemShortMenuSectionBinding;

class SectionViewHolder extends RecyclerView.ViewHolder {

    private ItemShortMenuSectionBinding mBinding;

    SectionViewHolder(ItemShortMenuSectionBinding binding) {
        super(binding.getRoot());

        mBinding = binding;
    }

    public ItemShortMenuSectionBinding getBinding() {
        return mBinding;
    }

}
