package com.fkf.resturent.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fkf.resturent.R;
import com.fkf.resturent.database.Recipe;
import com.fkf.resturent.services.image.loader.ImageLoader;

/**
 * Created by kavi on 6/26/13.
 * Object for recipes_list_item in recipes_layout.xml recipes list
 * @author Kavimal Wijewardana <kavi707@gmail.com>
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

    public void setRecipe(Recipe recipe, Context context) {
        this.recipe = recipe;

        recipeNameTextView.setText(recipe.getName());
        String description = recipe.getDescription();
        String shortDesc = "";
        if(description.length() > 60) {
            shortDesc = description.substring(0, 60);
        } else {
            shortDesc = description;
        }
        recipeDescriptionTextView.setText(shortDesc);
        recipeRatingBar.setRating(recipe.getRatings());

        int loader = R.drawable.fkf;
        try {
            ImageLoader imageLoader = new ImageLoader(context);
            imageLoader.DisplayImage(recipe.getImageUrl(), loader, recipeImageView);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
