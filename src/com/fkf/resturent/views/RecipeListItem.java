package com.fkf.resturent.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fkf.resturent.R;
import com.fkf.resturent.database.Recipe;

/**
 * Created by kavi on 6/26/13.
 */
public class RecipeListItem extends LinearLayout {

    private ImageView recipeImageView;
    private TextView recipeNameTextView;
    private TextView recipeDescriptionTextView;
    private RatingBar recipeRatingBar;

    private Recipe recipe;

    public RecipeListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        recipeImageView = (ImageView) findViewById(R.id.recipeImageView);
        recipeNameTextView = (TextView) findViewById(R.id.recipeNameTextView);
        recipeDescriptionTextView = (TextView) findViewById(R.id.recipeDescriptionTextView);
        recipeRatingBar = (RatingBar) findViewById(R.id.recipeRatingBar);
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;

        recipeNameTextView.setText(recipe.getName());
        recipeDescriptionTextView.setText(recipe.getDescription());
        recipeRatingBar.setRating(recipe.getRatings());
    }
}
