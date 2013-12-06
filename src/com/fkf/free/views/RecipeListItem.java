package com.fkf.free.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fkf.free.R;
import com.fkf.free.database.Recipe;
import com.fkf.free.services.image.loader.ImageLoader;
import com.fkf.free.templates.RecipesActivity;

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

        int legacy = recipe.getLegacy();
        String description = "";
        if(legacy == 0) {
            description = recipe.getDescription();
        } else if(legacy == 1) {
            description = recipe.getBody();
        }

        int deviceWidth = RecipesActivity.layoutWidthAndHeight.get("width");
        String shortDesc = "";
        int shortDescLimit = 0;
        if(deviceWidth < 500) {
            shortDescLimit = 60;
            recipeNameTextView.setTextSize(16);
            recipeDescriptionTextView.setTextSize(12);
        } else {
            shortDescLimit = 70;
            recipeNameTextView.setTextSize(18);
            recipeDescriptionTextView.setTextSize(16);
        }
        if(description.length() > shortDescLimit) {
            shortDesc = description.substring(0, shortDescLimit);
            shortDesc = shortDesc.replace("#", "").replace("\\","").replace("/", "");
        } else {
            shortDesc = description;
            shortDesc = shortDesc.replace("#", "").replace("\\","").replace("/", "");
        }
        recipeDescriptionTextView.setText(shortDesc);
        recipeRatingBar.setRating(recipe.getRatings());

        int loader = R.drawable.fkf_m;
        try {
            ImageLoader imageLoader = new ImageLoader(context);
            imageLoader.DisplayImage(recipe.getImageUrlT(), loader, recipeImageView);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
