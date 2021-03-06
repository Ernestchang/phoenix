package com.yulin.act.page.base;

import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.ListView;

import com.yulin.act.model.BaseItem;
import com.yulin.act.page.main.category.viewmodel.CategoryAdapter;
import com.yulin.act.page.menu.grid.vm.GridMenuAdapter;
import com.yulin.act.page.menu.list.vm.ListMenuAdapter;
import com.yulin.act.page.menu.list.vm.MenuListAdapter;
import com.yulin.applib.widget.PinnedSectionListView;

public class BindingAdapterInterface {

    @BindingAdapter("recyclerAdapter")
    public static void setRecyclerAdapter(RecyclerView recyclerView, CategoryAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter("recyclerAdapter")
    public static void setRecyclerAdapter(RecyclerView recyclerView, GridMenuAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter("recyclerAdapter")
    public static void setRecyclerAdapter(RecyclerView recyclerView, ListMenuAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter({"recyclerData"})
    public static void setRecyclerData(RecyclerView recyclerView, ObservableArrayList<BaseItem> listItems) {
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @BindingAdapter("listAdapter")
    public static void setListAdapter(PinnedSectionListView listView, MenuListAdapter adapter) {
        listView.setAdapter(adapter);
    }

    @BindingAdapter({"recyclerData"})
    public static void setRecyclerData(PinnedSectionListView recyclerView, ObservableArrayList<BaseItem> listItems) {
//        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @BindingAdapter("android:src")
    public static void setImageUri(ImageView view, String imageUri) {
        if (imageUri == null) {
            view.setImageURI(null);
        } else {
            view.setImageURI(Uri.parse(imageUri));
        }
    }

    @BindingAdapter("android:src")
    public static void setImageUri(ImageView view, Uri imageUri) {
        view.setImageURI(imageUri);
    }

    @BindingAdapter("android:src")
    public static void setImageDrawable(ImageView view, Drawable drawable) {
        view.setImageDrawable(drawable);
    }

    @BindingAdapter("android:src")
    public static void setImageResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }

}
