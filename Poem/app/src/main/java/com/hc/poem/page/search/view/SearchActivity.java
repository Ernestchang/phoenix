package com.hc.poem.page.search.view;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.hc.poem.R;
import com.hc.poem.databinding.ActivitySearchBinding;
import com.hc.poem.page.base.BaseActivity;
import com.hc.poem.page.list.view.DividerItemDecoration;
import com.hc.poem.page.search.viewmodel.SearchViewModel;

public class SearchActivity extends BaseActivity {

    private SearchViewModel mSearchModel;
    private ActivitySearchBinding mSearchBinding;

    public static void startSearch(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSearchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);

        mSearchBinding.recyclerSearchList.setLayoutManager(new LinearLayoutManager(this));
        mSearchBinding.recyclerSearchList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        mSearchModel = new SearchViewModel();
        mSearchBinding.setModel(mSearchModel);

        mSearchBinding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search(s.toString());
                displayClearIcon(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mSearchBinding.ivSearchClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchBinding.etSearch.setText("");
            }
        });
    }

    public void onCancelClick(View view) {
        finish();
    }

    public void search(String key) {
        mSearchModel.search(this, key);
    }

    /**
     * 有数据时显示清空图标，无数据是不显示
     * */
    private void displayClearIcon(String msg) {
        if (TextUtils.isEmpty(msg))
            mSearchBinding.ivSearchClear.setVisibility(View.GONE);
        else
            mSearchBinding.ivSearchClear.setVisibility(View.VISIBLE);
    }

}
