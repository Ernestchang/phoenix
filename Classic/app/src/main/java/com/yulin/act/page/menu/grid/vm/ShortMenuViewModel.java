package com.yulin.act.page.menu.grid.vm;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;

import com.yulin.act.db.PoemHelper;
import com.yulin.act.model.BaseItem;
import com.yulin.act.model.Result;
import com.yulin.act.page.base.BaseViewModel;
import com.yulin.applib.page.Page;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

public class ShortMenuViewModel extends BaseViewModel {

    private ObservableArrayList<BaseItem> mListItems;
    private ObservableField<ShortMenuAdapter> mShortMenuAdapter;

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
    public void queryMenu(Observer<Result> observer, int categoryId) {
        mListItems.clear();

        Subscription subscription = PoemHelper.queryMenu(categoryId)
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

        addSubscription(subscription);
    }

    public BaseItem getItem(int position) {
        if (mListItems != null && mListItems.size() > position) {
            return mListItems.get(position);
        }

        return null;
    }

}
