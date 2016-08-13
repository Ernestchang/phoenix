package com.hc.poem.page.author.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;

import com.hc.poem.BR;
import com.hc.poem.db.AuthorHelper;
import com.hc.poem.db.PoemHelper;
import com.hc.poem.page.author.model.AuthorBean;
import com.hc.poem.page.list.model.PoemItem;
import com.hc.poem.page.list.viewmodel.PoemItemModel;
import com.hc.poem.page.list.viewmodel.PoemListAdapter;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by liulei0905 on 2016/8/12.
 * 管理作者详情界面的数据和业务逻辑
 */
public class AuthorViewModel extends BaseObservable {

    /**
     * 该作者的基本信息，id、姓名、简介、头像
     */
    private AuthorBean mAuthorBean;
    private String mAuthorName;
    private String mAuthorIntro;
    private String mProfileUrl;

    private ArrayList<Integer> mListPoemIds = new ArrayList<>();
    public ObservableArrayList<PoemItemModel> listPoems = new ObservableArrayList<>();
    public ObservableField<PoemListAdapter> poemAdapter = new ObservableField<>();

    public AuthorViewModel() {
        poemAdapter.set(new PoemListAdapter(listPoems, mListPoemIds));
    }

    /**
     * 根据作者名字，从数据库中查询作者信息
     */
    public void queryAuthorInfoByName(final Context context, String authorName, Observer<String> observer) {
        AuthorHelper.getAuthorByName(context, authorName)
                .map(new Func1<AuthorBean, String>() {
                    @Override
                    public String call(AuthorBean bean) {
                        mAuthorBean = bean;
                        setAuthorName(bean.getName());
                        setAuthorIntro(bean.getIntro());
                        setProfileUrl(bean.getProfileUrl());

                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 从poem表根据authorId查询该作者对应的诗词列表
     */
    public void queryAuthorPoems(Context context) {
        int authorId = mAuthorBean == null ? 0 : mAuthorBean.getId();

        // clear old data.
        listPoems.clear();
        mListPoemIds.clear();

        // get new data.
        List<PoemItem> listPoemItems = PoemHelper.queryPoemsByAuthorId(context, authorId);
        for (PoemItem item : listPoemItems) {
            listPoems.add(new PoemItemModel(item));
            mListPoemIds.add(item.getPoemId());
        }
    }

    @Bindable
    public String getAuthorName() {
        return mAuthorName;
    }

    public void setAuthorName(String mAuthorName) {
        this.mAuthorName = mAuthorName;
        notifyPropertyChanged(BR.authorName);
    }

    @Bindable
    public String getAuthorIntro() {
        return mAuthorIntro;
    }

    public void setAuthorIntro(String mAuthorIntro) {
        this.mAuthorIntro = mAuthorIntro;
        notifyPropertyChanged(BR.authorIntro);
    }

    @Bindable
    public String getProfileUrl() {
        return mProfileUrl;
    }

    public void setProfileUrl(String mProfileUrl) {
        this.mProfileUrl = mProfileUrl;
        notifyPropertyChanged(BR.profileUrl);
    }

}
