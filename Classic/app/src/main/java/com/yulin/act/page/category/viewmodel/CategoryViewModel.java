package com.yulin.act.page.category.viewmodel;

import android.databinding.ObservableField;

import com.yulin.act.page.category.model.BaseItem;
import com.yulin.act.page.category.model.BottomItem;
import com.yulin.act.page.category.model.NormalItem;
import com.yulin.act.page.category.model.SectionItem;
import com.yulin.classic.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryViewModel {

    private List<BaseItem> mListItems;
    private ObservableField<CategoryAdapter> mCategoryAdapter;

    public CategoryViewModel() {
        mListItems = new ArrayList<>();
        initListItems();
        mCategoryAdapter = new ObservableField<>(new CategoryAdapter());
    }

    public ObservableField<CategoryAdapter> getCategoryAdapter() {
        return mCategoryAdapter;
    }

    private void initListItems() {
        mListItems.add(new SectionItem(BaseItem.ITEM_TYPE_SECTION, "前端开发"));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "HTML/CSS", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "JavaScript", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "jQuery", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "Html5", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "Node.js", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "AngularJs", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "WebApp", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "Css", R.drawable.icon_js));
        mListItems.add(new BottomItem(BaseItem.ITEM_TYPE_BOTTOM));

        mListItems.add(new SectionItem(BaseItem.ITEM_TYPE_SECTION, "后端开发"));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "PHP", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "Java", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "Python", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "C", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "C++", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "Go", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "C#", R.drawable.icon_js));
        mListItems.add(new BottomItem(BaseItem.ITEM_TYPE_BOTTOM));

        mListItems.add(new SectionItem(BaseItem.ITEM_TYPE_SECTION, "移动开发"));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "Android", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "iOS", R.drawable.icon_js));
        mListItems.add(new BottomItem(BaseItem.ITEM_TYPE_BOTTOM));

        mListItems.add(new SectionItem(BaseItem.ITEM_TYPE_SECTION, "数据库"));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "MySQL", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "MongoDB", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "Oracle", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "SQL Server", R.drawable.icon_js));
        mListItems.add(new BottomItem(BaseItem.ITEM_TYPE_BOTTOM));

        mListItems.add(new SectionItem(BaseItem.ITEM_TYPE_SECTION, "云计算&大数据"));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "云计算", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "大数据", R.drawable.icon_js));
        mListItems.add(new BottomItem(BaseItem.ITEM_TYPE_BOTTOM));

        mListItems.add(new SectionItem(BaseItem.ITEM_TYPE_SECTION, "运维&测试"));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "Linux", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "测试", R.drawable.icon_js));
        mListItems.add(new BottomItem(BaseItem.ITEM_TYPE_BOTTOM));

        mListItems.add(new SectionItem(BaseItem.ITEM_TYPE_SECTION, "视觉设计"));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "Photoshop", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "Premiere", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "Maya", R.drawable.icon_js));
        mListItems.add(new NormalItem(BaseItem.ITEM_TYPE_NORMAL, "ZBrush", R.drawable.icon_js));
        mListItems.add(new BottomItem(BaseItem.ITEM_TYPE_BOTTOM));
    }

}
