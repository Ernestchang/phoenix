package com.yulin.act.page.base;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.yulin.act.page.category.viewmodel.CategoryAdapter;

public class BindingAdapterInterface {

    @BindingAdapter("recyclerAdapter")
    public static void setRecyclerAdapter(RecyclerView recyclerView, CategoryAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }

//    @android.databinding.BindingAdapter({"recyclerData"})
//    public static void setRecyclerData(RecyclerView recyclerView, ObservableArrayList<RankItemViewModel> lstRankItemBean) {
//        recyclerView.getAdapter().notifyDataSetChanged();
//    }

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
    public static void setImageResource(ImageView imageView, int resource){
        imageView.setImageResource(resource);
    }

}
