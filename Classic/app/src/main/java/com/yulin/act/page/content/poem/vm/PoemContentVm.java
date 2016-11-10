package com.yulin.act.page.content.poem.vm;

import com.yulin.act.db.PoemHelper;
import com.yulin.act.model.PoemContent;
import com.yulin.act.model.Result;
import com.yulin.act.page.base.BaseViewModel;

import rx.Observer;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by liulei0905 on 2016/11/10.
 * <p>
 * 查询诗词内容
 */
public class PoemContentVm extends BaseViewModel {

    private PoemContent mPoemContent;

    public PoemContentVm() {
        mPoemContent = new PoemContent();
    }

    public void queryPoemContent(Observer<Result> observer, int poemId) {
        Subscription subscription = PoemHelper.getPoemContentById(poemId)
                .map(new Func1<PoemContent, Result>() {
                    @Override
                    public Result call(PoemContent poemContent) {
                        // SELECT p._id, p.title, p.subtitle, a.name, d.name, g.name, p.introduction, p.content
                        mPoemContent.setTitle(poemContent.getTitle());
                        mPoemContent.setSubTitle(poemContent.getSubTitle());
                        mPoemContent.setAuthor(poemContent.getAuthor());
                        mPoemContent.setDynastyName(poemContent.getDynastyName());
                        mPoemContent.setGenreName(poemContent.getGenreName());
                        mPoemContent.setIntroduction(poemContent.getIntroduction());
                        mPoemContent.setContent(poemContent.getContent());

                        return new Result(Result.RESULT_OK);
                    }
                })
                .subscribe(observer);

        addSubscription(subscription);
    }

    public PoemContent getPoemContent() {
        return mPoemContent;
    }

}
