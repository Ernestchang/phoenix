package com.hc.poem.page.list.viewmodel;

import android.support.v7.widget.RecyclerView;

import com.hc.poem.databinding.LayoutPoemItemBinding;

class PoemItemViewHolder extends RecyclerView.ViewHolder {

    private LayoutPoemItemBinding poemItemBinding;

    public PoemItemViewHolder(LayoutPoemItemBinding itemBinding) {
        super(itemBinding.rootLayout);
        poemItemBinding = itemBinding;
    }

    public LayoutPoemItemBinding getPoemItemBinding() {
        return poemItemBinding;
    }

}
