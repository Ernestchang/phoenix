package com.yulin.act.page.main.category.viewmodel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;

import com.yulin.act.db.CategoryHelper;
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

public class CategoryViewModel extends BaseViewModel {

    private ObservableArrayList<BaseItem> mListItems;
    private ObservableField<CategoryAdapter> mCategoryAdapter;

    public CategoryViewModel(Page page) {
        mListItems = new ObservableArrayList<>();
        mCategoryAdapter = new ObservableField<>(new CategoryAdapter(mListItems, page));
    }

    /**
     * 查询两级标题列表
     * */
    public void queryFirstTwoCategoryLevel(Observer<Result> observer) {
        mListItems.clear();

        Subscription subscription = CategoryHelper.queryFirstTwoCategoryLevel()
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

    public ObservableField<CategoryAdapter> getCategoryAdapter() {
        return mCategoryAdapter;
    }

    public ObservableArrayList<BaseItem> getListItems() {
        return mListItems;
    }

    public BaseItem getItem(int position) {
        if (mListItems != null && mListItems.size() > position) {
            return mListItems.get(position);
        }

        return null;
    }

}
