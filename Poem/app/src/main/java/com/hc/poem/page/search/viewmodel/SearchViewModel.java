package com.hc.poem.page.search.viewmodel;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;

import com.hc.poem.db.PoemHelper;
import com.hc.poem.page.list.model.PoemItem;
import com.hc.poem.page.list.viewmodel.PoemItemModel;
import com.hc.poem.page.list.viewmodel.PoemListAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchViewModel {

    private ObservableArrayList<PoemItemModel> mListPoems = new ObservableArrayList<>();
    private ArrayList<Integer> mListPoemIds = new ArrayList<>();
    private ObservableField<PoemListAdapter> mPoemAdapter;

    public SearchViewModel() {
        mPoemAdapter = new ObservableField<>(new PoemListAdapter(mListPoems, mListPoemIds));
    }

    /**
     * Search key word in database.
     * */
    public void search(Context context, String keyWord) {
        // clear old data.
        mListPoems.clear();
        mListPoemIds.clear();

        // get new data.
        List<PoemItem> listPoemItems = PoemHelper.search(context, keyWord);
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
