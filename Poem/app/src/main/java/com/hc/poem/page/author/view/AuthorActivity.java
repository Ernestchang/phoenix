package com.hc.poem.page.author.view;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.hc.poem.R;
import com.hc.poem.databinding.ActivityAuthorBinding;
import com.hc.poem.page.author.viewmodel.AuthorViewModel;
import com.hc.poem.page.base.BaseActivity;
import com.hc.poem.page.list.view.DividerItemDecoration;
import com.hc.poem.util.HtmlUtil;

import rx.Observer;

/**
 * Created by liulei0905 on 2016/8/12.
 * 显示作者详情及其作品列表
 */
public class AuthorActivity extends BaseActivity {

    private static final String EXTRA_KEY_NAME = "extra_author_name";

    private String mAuthorName;
    private ActivityAuthorBinding authorBinding;
    private AuthorViewModel mModel;

    public static void start(Context context, String authorName) {
        Bundle extras = new Bundle();
        extras.putString(EXTRA_KEY_NAME, authorName);

        Intent intent = new Intent(context, AuthorActivity.class);
        intent.putExtras(extras);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authorBinding = DataBindingUtil.setContentView(this, R.layout.activity_author);

        authorBinding.recyclerAuthorPoemList.setLayoutManager(new LinearLayoutManager(this));
        authorBinding.recyclerAuthorPoemList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        authorBinding.recyclerAuthorPoemListHeader.attachTo(authorBinding.recyclerAuthorPoemList);

        // close activity when click back
        authorBinding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthorActivity.this.finish();
            }
        });

        // Get author name
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mAuthorName = extras.getString(EXTRA_KEY_NAME);
        }

        mModel = new AuthorViewModel();
        authorBinding.setModel(mModel);

        mModel.queryAuthorInfoByName(this, mAuthorName, new Observer<String>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(String s) {
//                if (TextUtils.isEmpty(mModel.getProfileUrl())) {
//                    authorBinding.layoutProfile.setVisibility(View.GONE);
//                } else {
//                    authorBinding.imgProfile.setImageURI(Uri.parse(mModel.getProfileUrl()));
//                    authorBinding.layoutProfile.setVisibility(View.VISIBLE);
//                }

                if (!TextUtils.isEmpty(mModel.getAuthorIntro())) {
                    String htmlCode = HtmlUtil.generateAuthorInfo(mModel.getProfileUrl(), mModel.getAuthorIntro());
                    authorBinding.webAuthorInfo.loadDataWithBaseURL(null, htmlCode, "text/html", "utf-8", null);
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mModel.queryAuthorPoems(AuthorActivity.this);
                    }
                }, 100);
            }
        });
    }

}
