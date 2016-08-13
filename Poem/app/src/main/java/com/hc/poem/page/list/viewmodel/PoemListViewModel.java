package com.hc.poem.page.list.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;

import com.hc.poem.db.PoemHelper;
import com.hc.poem.page.list.model.PoemItem;
import com.hc.poem.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class PoemListViewModel extends BaseObservable {

    private ObservableArrayList<PoemItemModel> mListPoems = new ObservableArrayList<>();
    private ArrayList<Integer> mListPoemIds = new ArrayList<>();
    private ObservableField<PoemListAdapter> mPoemAdapter;

    public PoemListViewModel(Context context) {
        mPoemAdapter = new ObservableField<>(new PoemListAdapter(mListPoems, mListPoemIds));

        query(context);
    }

    /**
     * Query poem list from database.
     */
    public void query(Context context) {
        LogUtil.log("houchen", "query");
        // clear old data.
        mListPoems.clear();
        mListPoemIds.clear();

        // get new data.
        List<PoemItem> listPoemItems = PoemHelper.query(context);
        for (PoemItem item : listPoemItems) {
            mListPoems.add(new PoemItemModel(item));
            mListPoemIds.add(item.getPoemId());
        }
    }

    public ObservableArrayList<PoemItemModel> getListPoems() {
        return mListPoems;
    }

    public ObservableField<PoemListAdapter> getPoemAdapter() {
        return mPoemAdapter;
    }

}
