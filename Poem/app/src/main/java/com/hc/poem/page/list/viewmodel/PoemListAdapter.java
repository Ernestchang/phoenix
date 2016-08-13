package com.hc.poem.page.list.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hc.poem.BR;
import com.hc.poem.R;
import com.hc.poem.databinding.LayoutPoemItemBinding;
import com.hc.poem.page.content.view.PoemContentActivity;

import java.util.ArrayList;

public class PoemListAdapter extends RecyclerView.Adapter<PoemItemViewHolder> {

    private ArrayList<PoemItemModel> mListPoemItems;
    private ArrayList<Integer> mListPoemIds;
    private Context mContext;

    public PoemListAdapter(ArrayList<PoemItemModel> listItemModels, ArrayList<Integer> listPoemIds) {
        this.mListPoemItems = listItemModels;
        this.mListPoemIds = listPoemIds;
    }

    @Override
    public PoemItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutPoemItemBinding itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.layout_poem_item, parent, false);
        mContext = parent.getContext();
        return new PoemItemViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(PoemItemViewHolder holder, final int position) {
        holder.getPoemItemBinding().setVariable(BR.poem, mListPoemItems.get(position));
        holder.getPoemItemBinding().executePendingBindings();
        
        holder.getPoemItemBinding().rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show content
                startActivity(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListPoemItems == null ? 0 : mListPoemItems.size();
    }

    private void startActivity(int position) {
        Intent intent = new Intent(mContext, PoemContentActivity.class);
        Bundle extras = new Bundle();
        extras.putInt(PoemContentActivity.EXTRA_POEM_CURRENT_POSITION, position);
        extras.putIntegerArrayList(PoemContentActivity.EXTRA_POEM_IDS, mListPoemIds);
        intent.putExtras(extras);
        mContext.startActivity(intent);
    }

}
