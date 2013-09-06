package com.fkf.resturent.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.fkf.resturent.R;
import com.fkf.resturent.database.RecipeCategory;
import com.fkf.resturent.views.RecipeCategoryListItem;

import java.util.ArrayList;

/**
 * Created by kavi on 9/6/13.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class RecipeCategoryListAdapter extends BaseAdapter {

    private ArrayList<RecipeCategory> recipeCategoryList;
    private Context context;

    public RecipeCategoryListAdapter(ArrayList<RecipeCategory> recipeCategoryList, Context context) {
        super();
        this.recipeCategoryList = recipeCategoryList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return recipeCategoryList.size();
    }

    @Override
    public Object getItem(int i) {
        return (recipeCategoryList == null)? null:recipeCategoryList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        RecipeCategoryListItem recipeCategoryListItem;
        if(view == null) {
            recipeCategoryListItem = (RecipeCategoryListItem) View.inflate(context, R.layout.recipe_category_list_item, null);
        } else {
            recipeCategoryListItem = (RecipeCategoryListItem) view;
        }

        recipeCategoryListItem.setRecipeCategory(recipeCategoryList.get(i));
        return recipeCategoryListItem;
    }
}
