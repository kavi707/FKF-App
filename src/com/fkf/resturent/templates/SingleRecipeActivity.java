package com.fkf.resturent.templates;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.fkf.resturent.R;
import com.fkf.resturent.database.LocalDatabaseSQLiteOpenHelper;
import com.fkf.resturent.database.Recipe;
import com.fkf.resturent.services.ActivityUserPermissionServices;
import com.fkf.resturent.services.connections.ApiConnector;
import com.fkf.resturent.services.image.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

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

    private ActivityUserPermissionServices userPermissionServices = new ActivityUserPermissionServices();
    private LocalDatabaseSQLiteOpenHelper localDatabaseSQLiteOpenHelper = new LocalDatabaseSQLiteOpenHelper(this);
    private ApiConnector connector = new ApiConnector();
    private Recipe selectedRecipe;
    private String selectedRecipeProductId = null;
    private boolean isFavorite = false;
    private boolean isOnline = false;

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
            switch (selectedRecipeId) {
                case -1:
                    selectedRecipeProductId = localDatabaseSQLiteOpenHelper.getLatestRecipeFromIndex(1);
                    break;
                case -2:
                    selectedRecipeProductId = localDatabaseSQLiteOpenHelper.getLatestRecipeFromIndex(2);
                    break;
                case -3:
                    selectedRecipeProductId = localDatabaseSQLiteOpenHelper.getLatestRecipeFromIndex(3);
                    break;
                case -4:
                    selectedRecipeProductId = localDatabaseSQLiteOpenHelper.getLatestRecipeFromIndex(4);
                    break;
                case -5:
                    selectedRecipeProductId = localDatabaseSQLiteOpenHelper.getLatestRecipeFromIndex(5);
                    break;
            }

            List<Recipe> tempRecipeList = localDatabaseSQLiteOpenHelper.getRecipeFromRecipeProductId(selectedRecipeProductId);
            if(!tempRecipeList.isEmpty()) {
                selectedRecipe = tempRecipeList.get(0);
                setUiContents();
            }

        } else {
            ArrayList<Recipe> selectedRecipes = localDatabaseSQLiteOpenHelper.getRecipeFromRecipeProductId(String.valueOf(selectedRecipeId));
            if(selectedRecipes.size() != 1) {
                // Error message abt data querying
            } else {
                selectedRecipe = selectedRecipes.get(0);
                setUiContents();
            }
        }
    }

    private void setUiContents() {

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

        isOnline = userPermissionServices.isOnline(SingleRecipeActivity.this);

        boolean dbFovStatus = localDatabaseSQLiteOpenHelper.isUserFavoriteRecipe(selectedRecipe.getProductId(), LoginActivity.LOGGED_USER_ID);
        if (isOnline) {
            boolean serverFovStatus = connector.isMyFavorite(selectedRecipe.getProductId(), SingleRecipeActivity.this);

            if (dbFovStatus && serverFovStatus) {
                isFavorite = true;
            } else {
                if (dbFovStatus) {
                    localDatabaseSQLiteOpenHelper.removeFromUserFavorite(selectedRecipe.getProductId());
                    isFavorite = false;
                }

                if (serverFovStatus) {
                    localDatabaseSQLiteOpenHelper.saveUserFavoriteRecipes(selectedRecipe.getProductId(), LoginActivity.LOGGED_USER_ID);
                    isFavorite = true;
                }
            }
        } else {
            isFavorite = dbFovStatus;
        }

        if(isFavorite) {
            singleRecipeMyFavoriteImageButton.setImageResource(R.drawable.remove_favorite);
        }

        singleRecipeMyFavoriteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOnline) {
                    if (isFavorite) {

                        boolean removeFavoriteResult = connector.removeFromMyFavorite(selectedRecipe.getProductId(), SingleRecipeActivity.this);
                        if(removeFavoriteResult) {
                            singleRecipeMyFavoriteImageButton.setImageResource(R.drawable.add_favorite);
                            localDatabaseSQLiteOpenHelper.removeFromUserFavorite(selectedRecipe.getProductId());
                            Toast.makeText(getApplicationContext(),
                                    "Removed this recipe from you favorite list", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Removing was failed", Toast.LENGTH_LONG).show();
                        }
                    } else {

                        boolean addFavoriteResult = connector.addToMyFavorite(selectedRecipe.getProductId(), SingleRecipeActivity.this);
                        if(addFavoriteResult) {
                            singleRecipeMyFavoriteImageButton.setImageResource(R.drawable.remove_favorite);
                            localDatabaseSQLiteOpenHelper.saveUserFavoriteRecipes(selectedRecipe.getProductId(), LoginActivity.LOGGED_USER_ID);
                            Toast.makeText(getApplicationContext(),
                                    "This recipe is added to your favorite list", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Adding was failed", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Application is in offline mode. Please on your mobile data", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
