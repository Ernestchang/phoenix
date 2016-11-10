package com.yulin.act.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yulin.act.model.BaseItem;
import com.yulin.act.model.NormalItem;
import com.yulin.act.model.SectionItem;
import com.yulin.act.util.LogUtil;
import com.yulin.act.util.Util;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by liulei0905 on 2016/11/9.
 * <p>
 * 查询诗经
 */

public class ShiJingHelper {

    /**
     * 查询诗经类型和标题
     * 用来显示诗经标题列表
     */
    public static Observable<List<BaseItem>> queryMenu() {
        return Observable.create(
                new Observable.OnSubscribe<List<BaseItem>>() {
                    @Override
                    public void call(Subscriber<? super List<BaseItem>> subscriber) {
                        SQLiteDatabase db = DbHelper.getSQLiteDb(Util.getContext());

                        List<BaseItem> list = new ArrayList<>();

                        try {
                            /*
                            * SELECT d.name, c.name, p.title FROM poems p JOIN collection c ON p.shijing_sub3 = c._id
                            * JOIN collection d ON p.shijing_sub2 = d._id WHERE collection_id == 3
                            * */
                            String sql = "SELECT c.name, d.name, p.title, p._id FROM poems p " +
                                    "JOIN collection c ON p.shijing_sub2 = c._id " +
                                    "JOIN collection d ON p.shijing_sub3 = d._id " +
                                    "WHERE collection_id == 3";
                            Cursor cursor = db.rawQuery(sql, null);

                            // 记录前一个item的sectionName，默认为空
                            String preSectionName = "";

                            while (cursor.moveToNext()) {
                                String sectionTitle = cursor.getString(0);
                                String sectionName = cursor.getString(1);
                                String itemTitle = cursor.getString(2);
                                int itemId = cursor.getInt(3);
                                LogUtil.easylog("shijing_herple", "section: " + sectionName + ", title: " + itemTitle);

                                /*
                                * 如果当前sectionName与前一个sectionName不同，表明新开始了一个section，
                                * 先添加sectionItem开始这个section，然后正常添加normalItem。
                                * 如果preSectionName为空，说明是第一个section开始，只用添加一个sectionItem。
                                * */

                                if (!sectionName.equals(preSectionName)) {
                                    // 国风·召南
                                    list.add(new SectionItem(BaseItem.ITEM_TYPE_SECTION, sectionTitle + "·" + sectionName));
                                }
                                list.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, itemId, itemTitle));

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
