package com.hc.poem.page.list.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hc.poem.R;
import com.hc.poem.databinding.ActivityPoemListBinding;
import com.hc.poem.page.base.BaseActivity;
import com.hc.poem.page.list.viewmodel.PoemListAdapter;
import com.hc.poem.page.list.viewmodel.PoemListViewModel;
import com.hc.poem.page.search.view.SearchActivity;

/**
 * Created by houchen on 2016/7/26.
 * Show poem list.
 */
public class PoemListActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPoemListBinding poemBinding = DataBindingUtil.setContentView(this, R.layout.activity_poem_list);

        poemBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        poemBinding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        poemBinding.setModel(new PoemListViewModel(this));
    }

    public void onSearchClick(View view) {
        SearchActivity.startSearch(this);
    }

}
