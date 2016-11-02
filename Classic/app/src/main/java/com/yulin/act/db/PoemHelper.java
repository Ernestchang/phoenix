package com.yulin.act.db;

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
//
//    /**
//     * 根据id，获取指定诗词的内容
//     * */
//    public static PoemContentBean getPoemById(Context context, int poemId) {
//        SQLiteDatabase db = DbHelper.getSQLiteDb(context);
//
//        PoemContentBean bean = null;
//
//        try {
//            String sql = "SELECT p._id, p.title, p.subtitle, a.name, d.name, g.name, p.introduction, p.content" +
//                    " FROM poems p " +
//                    "JOIN author a ON p.author_id = a._id " +
//                    "JOIN genre g ON p.genre_id = g._id " +
//                    "JOIN dynasty d ON p.dynasty_id = d._id " +
//                    "WHERE p._id = ? ";
//            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(poemId)});
//            while (cursor.moveToNext()) {
//                bean = new PoemContentBean(
//                        cursor.getInt(0),
//                        cursor.getString(1),
//                        cursor.getString(2),
//                        cursor.getString(3),
//                        cursor.getString(4),
//                        cursor.getString(5),
//                        cursor.getString(6),
//                        cursor.getString(7)
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
//        return bean;
//    }
//
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
