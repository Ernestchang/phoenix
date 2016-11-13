package com.yulin.act.page.menu.list.vm;

import android.support.v7.widget.RecyclerView;

import com.yulin.classic.databinding.ItemListMenuNormalBinding;
import com.yulin.classic.databinding.ItemShortMenuNormalBinding;

class NormalViewHolder extends RecyclerView.ViewHolder {

    private ItemListMenuNormalBinding mBinding;

    NormalViewHolder(ItemListMenuNormalBinding binding) {
        super(binding.getRoot());

        mBinding = binding;
    }

    ItemListMenuNormalBinding getBinding() {
        return mBinding;
    }

}
