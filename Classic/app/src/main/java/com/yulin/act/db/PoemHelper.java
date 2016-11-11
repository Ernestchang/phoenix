package com.yulin.act.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yulin.act.model.BaseItem;
import com.yulin.act.model.NormalItem;
import com.yulin.act.model.PoemContent;
import com.yulin.act.model.SectionItem;
import com.yulin.act.util.Util;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * handle table poems
 */
public class PoemHelper {

//    /**
//     * 查询所有诗词
//     * */
//    public static List<PoemItem> query(Context context) {
//        SQLiteDatabase db = DbHelper.getSQLiteDb(context);
//
//        List<PoemItem> list = new ArrayList<>();
//
//        try {
//            String sql = "SELECT p._id, d.name, a.name, p.title, p.subtitle, p.addition FROM poems p " +
//                    "JOIN author a ON p.author_id = a._id " +
//                    "JOIN dynasty d ON p.dynasty_id = d._id";
//            Cursor cursor = db.rawQuery(sql, null);
//            while (cursor.moveToNext()) {
//                list.add(
//                        new PoemItem(
//                                cursor.getInt(0),
//                                cursor.getString(1),
//                                cursor.getString(2),
//                                cursor.getString(3),
//                                cursor.getString(4),
//                                cursor.getString(5)
//                        )
//                );
//            }
//            cursor.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (db != null)
//                db.close();
//        }
//
//        return list;
//    }

    /**
     * 根据诗词id，获取指定诗词的内容
     * */
    public static Observable<PoemContent> getPoemContentById(final int poemId) {
        return Observable.create(
                new Observable.OnSubscribe<PoemContent>() {
                    @Override
                    public void call(Subscriber<? super PoemContent> subscriber) {
                        SQLiteDatabase db = DbHelper.getSQLiteDb(Util.getContext());

                        try {
                            String sql = "SELECT p._id, p.title, p.subtitle, a.name, d.name, g.name, p.introduction, p.content" +
                                    " FROM poems p " +
                                    "JOIN author a ON p.author_id = a._id " +
                                    "JOIN genre g ON p.genre_id = g._id " +
                                    "JOIN dynasty d ON p.dynasty_id = d._id " +
                                    "WHERE p._id = ? ";
                            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(poemId)});

                            PoemContent poemContent = null;
                            while (cursor.moveToNext()) {
                                poemContent = new PoemContent(
                                        cursor.getInt(0),
                                        cursor.getString(1),
                                        cursor.getString(2),
                                        cursor.getString(3),
                                        cursor.getString(4),
                                        cursor.getString(5),
                                        cursor.getString(6),
                                        cursor.getString(7)
                                );
                            }
                            cursor.close();

                            if (poemContent == null) poemContent = new PoemContent();

                            subscriber.onNext(poemContent);
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

    /**
     * 查询类型和标题
     * 用来显示标题列表
     */
    public static Observable<List<BaseItem>> queryMenu(final int categoryId) {
        return Observable.create(
                new Observable.OnSubscribe<List<BaseItem>>() {
                    @Override
                    public void call(Subscriber<? super List<BaseItem>> subscriber) {
                        SQLiteDatabase db = DbHelper.getSQLiteDb(Util.getContext());

                        List<BaseItem> list = new ArrayList<>();

                        try {
                            String sql = "SELECT p._id, p.title, c1.name, c2.name " +
                                    "FROM poems p " +
                                    "JOIN category c1 ON c1._id = p.category_id " +
                                    "LEFT JOIN category c2 ON c1.parent_id = c2._id " +
                                    "WHERE collection_id == " + categoryId;
                            Cursor cursor = db.rawQuery(sql, null);

                            // 记录前一个item的sectionName，默认为空
                            String preSectionName = "";

                            while (cursor.moveToNext()) {
                                int itemId = cursor.getInt(0);
                                String itemTitle = cursor.getString(1);
                                String sectionName = cursor.getString(2);
                                String sectionTitle = cursor.getString(3);

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

//    /**
//     * 根据模糊查询条件，获取诗词列表
//     * 模糊查询诗人、标题
//     * */
//    public static List<PoemItem> search(Context context, String key) {
//        SQLiteDatabase db = DbHelper.getSQLiteDb(context);
//
//        List<PoemItem> list = new ArrayList<>();
//
//        try {
//            String sql = "SELECT p._id, d.name, a.name, p.title, p.subtitle, p.addition FROM poems p " +
//                    "JOIN author a ON p.author_id = a._id " +
//                    "JOIN dynasty d ON p.dynasty_id = d._id " +
//                    "WHERE a.name LIKE \"%" + key + "%\" " +
//                    "OR p.title LIKE \"%" + key + "%\"";
//            Cursor cursor = db.rawQuery(sql, null);
//            while (cursor.moveToNext()) {
//                list.add(
//                        new PoemItem(
//                                cursor.getInt(0),
//                                cursor.getString(1),
//                                cursor.getString(2),
//                                cursor.getString(3),
//                                cursor.getString(4),
//                                cursor.getString(5)
//                        )
//                );
//            }
//            cursor.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (db != null)
//                db.close();
//        }
//
//        return list;
//    }
//
//    /**
//     * 根据作者id
//     * 查询指定作者对应的诗词列表
//     * */
//    public static List<PoemItem> queryPoemsByAuthorId(Context context, int authorId) {
//        SQLiteDatabase db = DbHelper.getSQLiteDb(context);
//
//        List<PoemItem> list = new ArrayList<>();
//
//        try {
//            String sql = "SELECT p._id, d.name, a.name, p.title, p.subtitle, p.addition FROM poems p " +
//                    "JOIN author a ON p.author_id = a._id " +
//                    "JOIN dynasty d ON p.dynasty_id = d._id " +
//                    "WHERE a._id = " + authorId;
//            Cursor cursor = db.rawQuery(sql, null);
//            while (cursor.moveToNext()) {
//                list.add(
//                        new PoemItem(
//                                cursor.getInt(0),
//                                cursor.getString(1),
//                                cursor.getString(2),
//                                cursor.getString(3),
//                                cursor.getString(4),
//                                cursor.getString(5)
//                        )
//                );
//            }
//            cursor.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (db != null)
//                db.close();
//        }
//
//        return list;
//    }

}
