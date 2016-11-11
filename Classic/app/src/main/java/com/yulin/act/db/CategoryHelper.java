package com.yulin.act.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.yulin.act.model.BaseItem;
import com.yulin.act.model.BottomItem;
import com.yulin.act.model.NormalItem;
import com.yulin.act.model.SectionItem;
import com.yulin.act.util.Util;
import com.yulin.classic.R;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by liulei0905 on 2016/11/11.
 * 获取分级
 */

public class CategoryHelper {

    /**
     * 查询分类中一级分类和二级分类
     * 一级分类作为section, 二级分类作为normal
     */
    public static Observable<List<BaseItem>> queryFirstTwoCategoryLevel() {
        return Observable.create(
                new Observable.OnSubscribe<List<BaseItem>>() {
                    @Override
                    public void call(Subscriber<? super List<BaseItem>> subscriber) {
                        SQLiteDatabase db = DbHelper.getSQLiteDb(Util.getContext());

                        List<BaseItem> list = new ArrayList<>();

                        try {
                            String sql = "SELECT c1.name, c2.name, c2._id " +
                                    "FROM category c1 " +
                                    "LEFT JOIN category c2 " +
                                    "ON c2.parent_id = c1._id " +
                                    "WHERE c1.parent_id = 0 " +
                                    "ORDER BY c1.parent_id";
                            Cursor cursor = db.rawQuery(sql, null);

                            // 记录前一个item的sectionName，默认为空
                            String preSectionName = "";

                            while (cursor.moveToNext()) {
                                String sectionName = cursor.getString(0);
                                String itemTitle = cursor.getString(1);
                                int itemId = cursor.getInt(2);

                                /*
                                * 如果当前sectionName与前一个sectionName不同，表明新开始了一个section，
                                * 先添加sectionItem开始这个section，然后正常添加normalItem。
                                * 如果preSectionName为空，说明是第一个section开始，只用添加一个sectionItem。
                                * */

                                if (!sectionName.equals(preSectionName)) {
                                    if (!TextUtils.isEmpty(preSectionName)) {
                                        list.add(new BottomItem(BaseItem.ITEM_TYPE_BOTTOM));
                                    }

                                    // 国风·召南
                                    list.add(new SectionItem(BaseItem.ITEM_TYPE_SECTION, sectionName));
                                }
                                list.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, itemId, itemTitle, R.drawable.icon_js));

                                preSectionName = sectionName;
                            }
                            cursor.close();

                            subscriber.onNext(list);
                        } catch (Exception e) {
                            e.printStackTrace();

                            subscriber.onError(e);
                        } finally {
                            if (db != null)
                                db.close();

                            subscriber.onCompleted();
                        }
                    }
                }
        )
                .subscribeOn(Schedulers.io());
    }

}
