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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private TextView getSingleRecipeInstructionLabelTextView;
    private TextView singleRecipeInstructionTextView;
    private TextView singleRecipeContentLabelTextView;
    private TextView singleRecipeIngredientTextView;
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
        singleRecipeInstructionTextView = (TextView) findViewById(R.id.singleRecipeInstructionTextView);
        getSingleRecipeInstructionLabelTextView = (TextView) findViewById(R.id.singleRecipeInstructionLabelTextView);
        singleRecipeContentLabelTextView = (TextView) findViewById(R.id.singleRecipeContentLabelTextView);
        singleRecipeIngredientTextView = (TextView) findViewById(R.id.singleRecipeIngredientTextView);
        singleRecipeImageViewer = (ImageView) findViewById(R.id.singleRecipeImageView);
        singleRecipeMyFavoriteImageButton = (ImageButton) findViewById(R.id.myFavoriteImageButton);

        Bundle extras = getIntent().getExtras();
        int selectedRecipeId = extras.getInt("SELECTED_RECIPE_ID");

        if(selectedRecipeId < 0) {

            switch (selectedRecipeId) {
                //Latest recipe cases
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

                //Popular recipe cases
                case -6:
                    selectedRecipeProductId = localDatabaseSQLiteOpenHelper.getPopularRecipeFromIndex(1);
                    break;
                case -7:
                    selectedRecipeProductId = localDatabaseSQLiteOpenHelper.getPopularRecipeFromIndex(2);
                    break;
                case -8:
                    selectedRecipeProductId = localDatabaseSQLiteOpenHelper.getPopularRecipeFromIndex(3);
                    break;
                case -9:
                    selectedRecipeProductId = localDatabaseSQLiteOpenHelper.getPopularRecipeFromIndex(4);
                    break;
                case -10:
                    selectedRecipeProductId = localDatabaseSQLiteOpenHelper.getPopularRecipeFromIndex(5);
                    break;
                case -11:
                    selectedRecipeProductId = localDatabaseSQLiteOpenHelper.getPopularRecipeFromIndex(6);
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
        singleRecipeInstructionTextView.setText(selectedRecipe.getInstructions());
        singleRecipeContentLabelTextView.setText(selectedRecipe.getName() + " Ingredients");

        String imageUrl = selectedRecipe.getImageUrl_m();
        int loader = R.drawable.default_recipe_image;

        try {
            ImageLoader imageLoader = new ImageLoader(getApplicationContext());
            imageLoader.DisplayImage(imageUrl, loader, singleRecipeImageViewer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int legacy = selectedRecipe.getLegacy();
        if (legacy == 0) {
            String ingredients = selectedRecipe.getIngredients();
            String ingredientString = "";
            try {
                JSONArray ingredientJsonArray = new JSONArray(ingredients);
                JSONObject ingredientJsonObj = null;
                JSONArray itemsJsonArray = null;
                JSONObject itemJsonObj = null;
                if (ingredientJsonArray.length() == 1) {
                    ingredientJsonObj = ingredientJsonArray.getJSONObject(0);
                    itemsJsonArray = ingredientJsonObj.getJSONArray("items");
                    for (int j = 0; j < itemsJsonArray.length(); j++) {
                        itemJsonObj = itemsJsonArray.getJSONObject(j);

                        if (ingredientString.equals("")) {
                            ingredientString = itemJsonObj.getString("name");
                            if (!itemJsonObj.getString("unit").equals("null")) {
                                ingredientString = ingredientString + " " + itemJsonObj.getString("unit");
                            }
                            if (!itemJsonObj.getString("note").equals("null")) {
                                ingredientString = ingredientString + " " + itemJsonObj.getString("note");
                            }
                            ingredientString = ingredientString + "\n";
                        } else {
                            ingredientString = ingredientString + itemJsonObj.getString("name");
                            if (!itemJsonObj.getString("unit").equals("null")) {
                                ingredientString = ingredientString + " " + itemJsonObj.getString("unit");
                            }
                            if (!itemJsonObj.getString("note").equals("null")) {
                                ingredientString = ingredientString + " " + itemJsonObj.getString("note");
                            }
                            ingredientString = ingredientString + "\n";
                        }
                    }

                    singleRecipeIngredientTextView.setText(ingredientString);
                } else {
                    for (int i = 0; i < ingredientJsonArray.length(); i++) {
                        ingredientJsonObj = ingredientJsonArray.getJSONObject(i);
                        if (i == 0) {
                            ingredientString = " * " + ingredientJsonObj.getString("title") + "\n";
                            itemsJsonArray = ingredientJsonObj.getJSONArray("items");
                            for (int k = 0; k < itemsJsonArray.length(); k++) {
                                itemJsonObj = itemsJsonArray.getJSONObject(k);

                                ingredientString = ingredientString + itemJsonObj.getString("name");
                                if (!itemJsonObj.getString("unit").equals("null")) {
                                    ingredientString = ingredientString + " " + itemJsonObj.getString("unit");
                                }
                                if (!itemJsonObj.getString("note").equals("null")) {
                                    ingredientString = ingredientString + " " + itemJsonObj.getString("note");
                                }
                                ingredientString = ingredientString + "\n";
                            }
                        } else {
                            ingredientString = ingredientString + "\n";
                            ingredientString = ingredientString + " * " + ingredientJsonObj.getString("title") + "\n";
                            itemsJsonArray = ingredientJsonObj.getJSONArray("items");
                            for (int m = 0; m < itemsJsonArray.length(); m++) {
                                itemJsonObj = itemsJsonArray.getJSONObject(m);

                                ingredientString = ingredientString + itemJsonObj.getString("name");
                                if (!itemJsonObj.getString("unit").equals("null")) {
                                    ingredientString = ingredientString + " " + itemJsonObj.getString("unit");
                                }
                                if (!itemJsonObj.getString("note").equals("null")) {
                                    ingredientString = ingredientString + " " + itemJsonObj.getString("note");
                                }
                                ingredientString = ingredientString + "\n";
                            }
                        }
                    }

                    singleRecipeIngredientTextView.setText(ingredientString);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (legacy == 1) {
            String recipeBody = selectedRecipe.getBody();
            String[] recipeBodyStrings = recipeBody.split("#");
            String singleBodyString = "";
            for (String recipeBodyString : recipeBodyStrings) {
                if(recipeBodyString.length() > 11) {
                    if(recipeBodyString.substring(0,11).equals("Ingredients")) {
                        singleBodyString = singleBodyString + "\n\n" + recipeBodyString + "\n\n";
                    }else if(recipeBodyString.substring(0,12).equals("INSTRUCTIONS")) {
                        singleBodyString = singleBodyString + "\n\n" + recipeBodyString + "\n\n";
                    } else {
                        singleBodyString = singleBodyString + recipeBodyString + "\n";
                    }
                } else if (recipeBodyString.length() == 11) {
                    if(recipeBodyString.equals("INGREDIENTS")) {
                        singleBodyString = singleBodyString + "\n\n" + recipeBodyString + "\n\n";
                    }
                } else {
                    singleBodyString = singleBodyString + recipeBodyString + "\n";
                }
            }

            singleRecipeIngredientTextView.setText(singleBodyString);
            singleRecipeInstructionTextView.setVisibility(View.GONE);
            getSingleRecipeInstructionLabelTextView.setVisibility(View.GONE);
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

                boolean dbFovStatusBtnClickTest = localDatabaseSQLiteOpenHelper.isUserFavoriteRecipe(selectedRecipe.getProductId(), LoginActivity.LOGGED_USER_ID);
                if (isOnline) {
                    boolean serverFovStatusBtnClickTest = connector.isMyFavorite(selectedRecipe.getProductId(), SingleRecipeActivity.this);

                    if (dbFovStatusBtnClickTest && serverFovStatusBtnClickTest) {
                        isFavorite = true;
                    } else {
                        if (dbFovStatusBtnClickTest) {
                            localDatabaseSQLiteOpenHelper.removeFromUserFavorite(selectedRecipe.getProductId());
                            isFavorite = false;
                        }

                        if (serverFovStatusBtnClickTest) {
                            localDatabaseSQLiteOpenHelper.saveUserFavoriteRecipes(selectedRecipe.getProductId(), LoginActivity.LOGGED_USER_ID);
                            isFavorite = true;
                        }

                        if(!dbFovStatusBtnClickTest && !serverFovStatusBtnClickTest) {
                            isFavorite = false;
                        }
                    }


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
