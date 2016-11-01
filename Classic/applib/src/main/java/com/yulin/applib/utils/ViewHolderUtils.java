package com.yulin.applib.utils;

import android.util.SparseArray;
import android.view.View;

/**
 * View控件工具类
 * 一般用于ListView
 * 用于重复利用View控件
 * emoney
 *
 */
public class ViewHolderUtils {

	public ViewHolderUtils() {
		// TODO Auto-generated constructor stub
	}
    public static <T extends View> T get(View view, int id) { 

        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag(); 
        if (viewHolder == null) 
        { 
            viewHolder = new SparseArray<View>(); 
            view.setTag(viewHolder); 
        } 

        View childView = viewHolder.get(id); 
        if (childView == null) 
        { 
            childView = view.findViewById(id); 
            viewHolder.put(id, childView); 
        } 
        return (T) childView; 
    } 
}
