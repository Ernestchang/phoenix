package com.yulin.act.page.menu.grid.vm;

import android.support.v7.widget.RecyclerView;

import com.yulin.classic.databinding.ItemShortMenuSectionBinding;

public class SectionViewHolder extends RecyclerView.ViewHolder {

    private ItemShortMenuSectionBinding mBinding;

    public SectionViewHolder(ItemShortMenuSectionBinding binding) {
        super(binding.getRoot());

        mBinding = binding;
    }

    public ItemShortMenuSectionBinding getBinding() {
        return mBinding;
    }

}
