package com.yulin.act.page.menu.grid.vm;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;

import com.yulin.act.db.ShiJingHelper;
import com.yulin.act.model.BaseItem;
import com.yulin.act.model.Result;
import com.yulin.applib.page.Page;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ShortMenuViewModel {

    private ObservableArrayList<BaseItem> mListItems;
    private ObservableField<ShortMenuAdapter> mShortMenuAdapter;
    private Subscription mSubscription;

    public ShortMenuViewModel(Page page) {
        mListItems = new ObservableArrayList<>();
        mShortMenuAdapter = new ObservableField<>(new ShortMenuAdapter(mListItems, page));
    }

    public ObservableField<ShortMenuAdapter> getShortMenuAdapter() {
        return mShortMenuAdapter;
    }

    public ObservableArrayList<BaseItem> getListItems() {
        return mListItems;
    }

    /**
     * 查询标题列表
     * */
    public void queryMenu(Observer<Result> observer) {
        mSubscription = ShiJingHelper.queryMenu()
                .flatMap(new Func1<List<BaseItem>, Observable<BaseItem>>() {
                    @Override
                    public Observable<BaseItem> call(List<BaseItem> baseItems) {
                        return Observable.from(baseItems);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<BaseItem, Result>() {
                    @Override
                    public Result call(BaseItem baseItem) {
                        mListItems.add(baseItem);

                        return new Result(Result.RESULT_OK);
                    }
                })
                .subscribe(observer);
    }

    public void clearSubscription() {
        if (mSubscription != null && mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    public BaseItem getItem(int position) {
        if (mListItems != null && mListItems.size() > position) {
            return mListItems.get(position);
        }

        return null;
    }

}