package com.hc.poem.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hc.poem.page.author.model.AuthorBean;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by liulei0905 on 2016/8/12.
 * 管理author表数据
 */
public class AuthorHelper {

    /**
     * 根据id，获取指定作者的内容
     * */
    public static AuthorBean getAuthorById(Context context, int authorId) {
        SQLiteDatabase db = DbHelper.getSQLiteDb(context);

        AuthorBean bean = null;

        try {
            String sql = "SELECT _id, name, intro, profile_pic_url" +
                    " FROM author " +
                    "WHERE _id = ? ";
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(authorId)});
            while (cursor.moveToNext()) {
                bean = new AuthorBean(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)
                );
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null)
                db.close();
        }

        return bean;
    }

    /**
     * 根据姓名，获取指定作者的内容
     * */
    public static Observable<AuthorBean> getAuthorByName(final Context context, final String authorName) {
        return Observable.create(new Observable.OnSubscribe<AuthorBean>() {
            @Override
            public void call(Subscriber<? super AuthorBean> subscriber) {
                SQLiteDatabase db = DbHelper.getSQLiteDb(context);

                try {
                    AuthorBean bean = new AuthorBean();

                    String sql = "SELECT _id, name, intro, profile_pic_url" +
                            " FROM author " +
                            "WHERE name LIKE \"%" + authorName + "%\"";
                    Cursor cursor = db.rawQuery(sql, null);
                    while (cursor.moveToNext()) {
                        bean = new AuthorBean(
                                cursor.getInt(0),
                                cursor.getString(1),
                                cursor.getString(2),
                                cursor.getString(3)
                        );
                    }
                    cursor.close();

                    subscriber.onNext(bean);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    if (db != null)
                        db.close();
                }
            }
        }).subscribeOn(Schedulers.io());
    }

}
