package com.yulin.act.page.base;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import com.yulin.act.page.category.viewmodel.CategoryAdapter;

public class BindingAdapterInterface {

    @BindingAdapter("recyclerAdapter")
    public static void setRecyclerAdapter(RecyclerView recyclerView, CategoryAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }

//    @android.databinding.BindingAdapter({"recyclerData"})
//    public static void setRecyclerData(RecyclerView recyclerView, ObservableArrayList<RankItemViewModel> lstRankItemBean) {
//        recyclerView.getAdapter().notifyDataSetChanged();
//    }

}
