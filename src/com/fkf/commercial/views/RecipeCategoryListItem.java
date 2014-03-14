package com.fkf.commercial.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fkf.commercial.R;
import com.fkf.commercial.database.RecipeCategory;

/**
 * Created by kavi on 9/6/13.
 * Object for recipes_category_list_item in recipes_category_layout.xml recipes list
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class RecipeCategoryListItem extends LinearLayout {

    private TextView recipeCategoryNameTextView;

    private RecipeCategory recipeCategory;

    public RecipeCategoryListItem(Context context,AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        recipeCategoryNameTextView = (TextView) findViewById(R.id.recipeCategoryNameTextView);
    }

    public RecipeCategory getRecipeCategory() {
        return recipeCategory;
    }

    public void setRecipeCategory(RecipeCategory recipeCategory) {
        this.recipeCategory = recipeCategory;

        recipeCategoryNameTextView.setText(recipeCategory.getCategoryName());
    }
}
