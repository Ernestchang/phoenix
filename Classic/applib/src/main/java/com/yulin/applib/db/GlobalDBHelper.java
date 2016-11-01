package com.yulin.applib.db;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * 轻量级数据库 存储简单的全局数据
 * 
 * emoney
 *
 */
public class GlobalDBHelper {
	private Context mContext = null;
	private String mDBName = null;

	public GlobalDBHelper(Context context, String dbName) {
		mDBName = dbName;
		mContext = context;
	}

	private SharedPreferences getDB() {
		SharedPreferences share = mContext.getSharedPreferences(mDBName, Activity.MODE_PRIVATE);
		return share;
	}

	public long getLong(String key, long defValue) {
		return getDB().getLong(key, defValue);
	}

	public boolean setLong(String key, long val) {
		return getDB().edit().putLong(key, val).commit();
	}

	public int getInt(String key, int defValue) {
		return getDB().getInt(key, defValue);
	}

	public boolean setInt(String key, int val) {
		return getDB().edit().putInt(key, val).commit();
	}

	public String getString(String key, String defValue) {
		return getDB().getString(key, defValue);
	}

	public boolean setString(String key, String val) {
		return getDB().edit().putString(key, val).commit();
	}

	public boolean getBoolean(String key, boolean defVal) {
		return getDB().getBoolean(key, defVal);
	}

	public boolean setBoolean(String key, boolean defValue) {
		return getDB().edit().putBoolean(key, defValue).commit();
	}

	public boolean setStringArray(String key, String[] lst) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < lst.length; i++) {
			String val = lst[i];
			if (i == lst.length - 1) {
				sb.append(val);
			} else {
				sb.append(val + ",");
			}
		}
		String result = sb.toString();
		return setString(key, result);
	}

	public String[] getStringArray(String key, String[] defaultLst) {
		String result = getString(key, "");
		if (result.equals("")) {
			return defaultLst;
		} else {
			String[] arr = result.split(",");
			// String[] newLst = new String[arr.length];
			// for(int i = 0; i < arr.length; i++)
			// {
			// String val = arr[i];
			// newLst[i] = val;
			// }
			// return newLst;
			return arr;
		}
	}

	public boolean setIntegerArray(String key, int[] lst) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < lst.length; i++) {
			int val = lst[i];
			if (i == lst.length - 1) {
				sb.append(val);
			} else {
				sb.append(val + ",");
			}
		}
		String result = sb.toString();
		return setString(key, result);
	}
	public boolean setIntegerArray(String key, List<Integer> lst) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < lst.size(); i++) {
			int val = lst.get(i);
			if (i == lst.size() - 1) {
				sb.append(val);
			} else {
				sb.append(val + ",");
			}
		}
		String result = sb.toString();
		return setString(key, result);
	}

	public Integer[] getIntegerArray(String key, Integer[] defaultLst) {
		String result = getString(key, "");
		if (result.equals("")) {
			return defaultLst;
		} else {
			String[] arr = result.split(",");
			Integer[] newLst = new Integer[arr.length];
			for (int i = 0; i < arr.length; i++) {
				int val = Integer.parseInt(arr[i]);
				newLst[i] = val;
			}
			return newLst;
		}
	}

	/**
	 * 清除数据库
	 */
	public void clear() {
		boolean bRet = getDB().edit().clear().commit();
		if (bRet) {
			Log.v("", "清除GlobalDBHelper 成功");
		}
		else {
			Log.e("", "清除GlobalDBHelper 失败");
		}
	}
}
