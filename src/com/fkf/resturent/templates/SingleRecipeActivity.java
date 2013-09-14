package com.fkf.resturent.templates;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fkf.resturent.R;
import com.fkf.resturent.database.LocalDatabaseSQLiteOpenHelper;
import com.fkf.resturent.database.Recipe;
import com.fkf.resturent.services.connections.ApiConnector;
import com.fkf.resturent.services.image.loader.ImageLoader;

import java.util.ArrayList;

/**
 * Created by kavi on 6/29/13.
 * Hold the new single recipe view functionalities.
 * single_recipe_layout.xml Activity class
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class SingleRecipeActivity extends Activity {

    private TextView singleRecipeNameTextView;
    private RatingBar singleRecipeRatingBar;
    private TextView singleRecipeDescriptionTextView;
    private TextView singleRecipeContentLabelTextView;
    private ImageView singleRecipeImageViewer;
    private ImageButton singleRecipeMyFavoriteImageButton;

    private LocalDatabaseSQLiteOpenHelper localDatabaseSQLiteOpenHelper = new LocalDatabaseSQLiteOpenHelper(this);
    private ApiConnector connector = new ApiConnector();
    private Recipe selectedRecipe;
    private boolean isFavorite = false;

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
        singleRecipeImageViewer = (ImageView) findViewById(R.id.singleRecipeImageView);
        singleRecipeMyFavoriteImageButton = (ImageButton) findViewById(R.id.myFavoriteImageButton);

        Bundle extras = getIntent().getExtras();
        int selectedRecipeId = extras.getInt("SELECTED_RECIPE_ID");

        if(selectedRecipeId < 0) {

        } else {
            ArrayList<Recipe> selectedRecipes = localDatabaseSQLiteOpenHelper.getRecipeFromRecipeId(selectedRecipeId);
            if(selectedRecipes.size() != 1) {
                // Error message abt data querying
            } else {
                selectedRecipe = selectedRecipes.get(0);

                singleRecipeNameTextView.setText(selectedRecipe.getName());
                singleRecipeRatingBar.setRating(selectedRecipe.getRatings());
                singleRecipeDescriptionTextView.setText(selectedRecipe.getDescription());
                singleRecipeContentLabelTextView.setText(selectedRecipe.getName() + " contents");

                String imageUrl = selectedRecipe.getImageUrl();
                int loader = R.drawable.default_recipe_image;

                try {
                    ImageLoader imageLoader = new ImageLoader(getApplicationContext());
                    imageLoader.DisplayImage(imageUrl, loader, singleRecipeImageViewer);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                isFavorite = localDatabaseSQLiteOpenHelper.isUserFavoriteRecipe(selectedRecipe.getProductId(), LoginActivity.LOGGED_USER_ID);

                if(isFavorite) {
                    singleRecipeMyFavoriteImageButton.setImageResource(R.drawable.remove_favorite);
                }

                singleRecipeMyFavoriteImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isFavorite) {
                            singleRecipeMyFavoriteImageButton.setImageResource(R.drawable.add_favorite);
                            localDatabaseSQLiteOpenHelper.removeFromUserFavorite(selectedRecipe.getProductId());
                            //TODO remove favorite function must be added to here.
                        } else {
                            singleRecipeMyFavoriteImageButton.setImageResource(R.drawable.remove_favorite);
                            localDatabaseSQLiteOpenHelper.saveUserFavoriteRecipes(selectedRecipe.getProductId(), LoginActivity.LOGGED_USER_ID);
                            boolean addFavoriteResult = connector.addToMyFavorite(selectedRecipe.getProductId());
                        }
                    }
                });
            }
        }
    }
}
