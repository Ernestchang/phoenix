package com.yulin.act.page.menu.grid.vm;

import android.support.v7.widget.RecyclerView;

import com.yulin.classic.databinding.ItemShortMenuNormalBinding;

class NormalViewHolder extends RecyclerView.ViewHolder {

    private ItemShortMenuNormalBinding mBinding;

    NormalViewHolder(ItemShortMenuNormalBinding binding) {
        super(binding.getRoot());

        mBinding = binding;
    }

    ItemShortMenuNormalBinding getBinding() {
        return mBinding;
    }

}
