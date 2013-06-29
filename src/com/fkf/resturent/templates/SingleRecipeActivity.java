package com.fkf.resturent.templates;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fkf.resturent.R;
import com.fkf.resturent.database.LocalDatabaseSQLiteOpenHelper;
import com.fkf.resturent.database.Recipe;

import java.util.ArrayList;

/**
 * Created by kavi on 6/29/13.
 */
public class SingleRecipeActivity extends Activity {

    private TextView singleRecipeNameTextView;
    private RatingBar singleRecipeRatingBar;
    private TextView singleRecipeDescriptionTextView;
    private TextView singleRecipeContentLabelTextView;

    private LocalDatabaseSQLiteOpenHelper localDatabaseSQLiteOpenHelper = new LocalDatabaseSQLiteOpenHelper(this);
    private Recipe selectedRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_recipe_layout);

        setUpView();
    }

    private void setUpView() {

        singleRecipeNameTextView = (TextView) findViewById(R.id.singleRecipeNameTextView);
        singleRecipeRatingBar = (RatingBar) findViewById(R.id.singleRecipeRatingBar);
        singleRecipeDescriptionTextView = (TextView) findViewById(R.id.singleRecipeDescriptionTextView);
        singleRecipeContentLabelTextView = (TextView) findViewById(R.id.singleRecipeContentLabelTextView);

        Bundle extras = getIntent().getExtras();
        int selectedRecipeId = extras.getInt("SELECTED_RECIPE_ID");

        ArrayList<Recipe> selectedRecipes = localDatabaseSQLiteOpenHelper.getRecipeFromRecipeId(selectedRecipeId);
        if(selectedRecipes.size() != 1) {
            // Error message abt data querying
        } else {
            selectedRecipe = selectedRecipes.get(0);

            singleRecipeNameTextView.setText(selectedRecipe.getName());
            singleRecipeRatingBar.setRating(selectedRecipe.getRatings());
            singleRecipeDescriptionTextView.setText(selectedRecipe.getDescription());
            singleRecipeContentLabelTextView.setText(selectedRecipe.getName() + " contents");
        }
    }
}
