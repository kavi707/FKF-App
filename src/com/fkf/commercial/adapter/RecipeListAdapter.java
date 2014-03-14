package com.fkf.commercial.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fkf.commercial.R;
import com.fkf.commercial.database.Recipe;
import com.fkf.commercial.views.RecipeListItem;

import java.util.ArrayList;

/**
 * Created by kavi on 6/26/13.
 *
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class RecipeListAdapter extends BaseAdapter {

    private ArrayList<Recipe> recipeList;
    private Context context;

    public RecipeListAdapter(ArrayList<Recipe> recipeList, Context context) {
        super();
        this.recipeList = recipeList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return recipeList.size();
    }

    @Override
    public Recipe getItem(int i) {
        return (recipeList == null) ? null : recipeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        RecipeListItem recipeListItem;
        if (view == null) {
            recipeListItem = (RecipeListItem) View.inflate(context, R.layout.recipe_list_item, null);
        } else {
            recipeListItem = (RecipeListItem) view;
        }

        recipeListItem.setRecipe(recipeList.get(i), this.context);
        return recipeListItem;
    }
}
