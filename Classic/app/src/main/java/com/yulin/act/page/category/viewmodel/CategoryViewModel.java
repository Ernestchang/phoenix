package com.yulin.act.page.category.viewmodel;

import android.databinding.ObservableField;

import com.yulin.act.page.category.model.BaseItem;
import com.yulin.act.page.category.model.BottomItem;
import com.yulin.act.page.category.model.NormalItem;
import com.yulin.act.page.category.model.SectionItem;
import com.yulin.applib.page.Page;
import com.yulin.classic.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryViewModel {

    private List<BaseItem> mListItems;
    private ObservableField<CategoryAdapter> mCategoryAdapter;
    private Page mPage;

    public CategoryViewModel(Page page) {
        mListItems = new ArrayList<>();
        initListItems();
        mPage = page;
        mCategoryAdapter = new ObservableField<>(new CategoryAdapter(mListItems, page));
    }

    public ObservableField<CategoryAdapter> getCategoryAdapter() {
        return mCategoryAdapter;
    }

    private void initListItems() {
        mListItems.add(new SectionItem(BaseItem.ITEM_TYPE_SECTION, "诗词曲赋"));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "诗经", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "古诗三百首", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "唐诗三百首", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "宋词三百首", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "元曲三百首", R.drawable.icon_js));
        mListItems.add(new BottomItem(BaseItem.ITEM_TYPE_BOTTOM));

        mListItems.add(new SectionItem(BaseItem.ITEM_TYPE_SECTION, "先秦诸子"));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "墨子", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "道德经", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "庄子", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "荀子", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "韩非子", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "鬼谷子", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "孙子兵法", R.drawable.icon_js));
        mListItems.add(new BottomItem(BaseItem.ITEM_TYPE_BOTTOM));

        mListItems.add(new SectionItem(BaseItem.ITEM_TYPE_SECTION, "四书五经"));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "大学", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "中庸", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "论语", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "孟子", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "诗经", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "尚书", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "礼记", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "周易", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "春秋", R.drawable.icon_js));
        mListItems.add(new BottomItem(BaseItem.ITEM_TYPE_BOTTOM));

        mListItems.add(new SectionItem(BaseItem.ITEM_TYPE_SECTION, "二十四史"));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "史记", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "汉书", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "后汉书", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "三国志", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "晋书", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "宋书", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "南齐书", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "梁书", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "陈书", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "魏书", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "北齐书", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "周书", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "隋书", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "南史", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "北史", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "旧唐书", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "新唐书", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "旧五代史", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "新五代史", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "宋史", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "辽史", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "金史", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "元史", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "明史", R.drawable.icon_js));
        mListItems.add(new BottomItem(BaseItem.ITEM_TYPE_BOTTOM));

        mListItems.add(new SectionItem(BaseItem.ITEM_TYPE_SECTION, "编年史等"));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "春秋左传", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "战国策", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "资治通鉴", R.drawable.icon_js));
        mListItems.add(new BottomItem(BaseItem.ITEM_TYPE_BOTTOM));

        mListItems.add(new SectionItem(BaseItem.ITEM_TYPE_SECTION, "启蒙经典"));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "三字经", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "百家姓", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "千字文", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "弟子规", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "千家诗", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "增广贤文", R.drawable.icon_js));
        mListItems.add(new BottomItem(BaseItem.ITEM_TYPE_BOTTOM));
    }

    public BaseItem getItem(int position) {
        if (mListItems != null && mListItems.size() > position) {
            return mListItems.get(position);
        }

        return null;
    }

}
