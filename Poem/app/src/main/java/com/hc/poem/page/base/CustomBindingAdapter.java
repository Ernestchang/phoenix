package com.hc.poem.page.base;

import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.support.v7.widget.RecyclerView;

import com.hc.poem.page.list.viewmodel.PoemItemModel;
import com.hc.poem.page.list.viewmodel.PoemListAdapter;

/**
 * Created by liulei0905 on 2016/8/12.
 * 绑定view和数据，当数据变化时view自动更新
 */
public class CustomBindingAdapter {

    /**
     * 当诗词列表数据变化时，诗词列表界面自动更新
     * 绑定列表和adapter
     * */
    @BindingAdapter({"poemListAdapter"})
    public static void setPoemListAdapter(RecyclerView recyclerView, PoemListAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    /**
     * 当诗词列表数据变化时，诗词列表界面自动更新
     * 绑定数据源，数据源变化时，页面自动刷新
     * */
    @BindingAdapter({"poemListData"})
    public static void setPoemListData(RecyclerView recyclerView, ObservableArrayList<PoemItemModel> lstRankItemBean) {
        recyclerView.getAdapter().notifyDataSetChanged();
    }

}
