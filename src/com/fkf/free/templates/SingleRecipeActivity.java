package com.fkf.free.templates;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.fkf.free.R;
import com.fkf.free.database.LocalDatabaseSQLiteOpenHelper;
import com.fkf.free.database.Recipe;
import com.fkf.free.services.ActivityUserPermissionServices;
import com.fkf.free.services.connections.ApiConnector;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private TextView secondItemIngredientTitleTextView;
    private TextView thirdItemIngredientTitleTextView;

    private TextView singleRecipeIngredientTextView;
    private TextView secondItemIngredientsTextView;
    private TextView thirdItemIngredientsTextView;

    private ImageView singleRecipeImageViewer;
    private ImageButton singleRecipeMyFavoriteImageButton;

    private AdView googleAdView;

    private LinearLayout.LayoutParams contentParams;
    private FrameLayout.LayoutParams favoriteButtonParams;
    private LinearLayout instructionsLinearLayout;
    private Map<String, Integer> layoutWidthAndHeight;

    private LinearLayout secondItemIngredients, thirdItemIngredients;

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

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        //calling recipe image loading in background
        if(userPermissionServices.isOnline(SingleRecipeActivity.this)) {
            this.loadRecipeImage();
        }
    }

    private void setUpView() {

        instructionsLinearLayout = (LinearLayout) findViewById(R.id.instructionsLinearLayout);
        singleRecipeNameTextView = (TextView) findViewById(R.id.singleRecipeNameTextView);
        singleRecipeRatingBar = (RatingBar) findViewById(R.id.singleRecipeRatingBar);
        singleRecipeDescriptionTextView = (TextView) findViewById(R.id.singleRecipeDescriptionTextView);
        singleRecipeInstructionTextView = (TextView) findViewById(R.id.singleRecipeInstructionTextView);
        getSingleRecipeInstructionLabelTextView = (TextView) findViewById(R.id.singleRecipeInstructionLabelTextView);

        singleRecipeContentLabelTextView = (TextView) findViewById(R.id.singleRecipeContentLabelTextView);
        secondItemIngredientTitleTextView = (TextView) findViewById(R.id.secondItemIngredientTitleTextView);
        thirdItemIngredientTitleTextView = (TextView) findViewById(R.id.thirdItemIngredientTitleTextView);

        singleRecipeIngredientTextView = (TextView) findViewById(R.id.singleRecipeIngredientTextView);
        secondItemIngredientsTextView = (TextView) findViewById(R.id.secondItemIngredientsTextView);
        thirdItemIngredientsTextView = (TextView) findViewById(R.id.thirdItemIngredientsTextView);

        singleRecipeImageViewer = (ImageView) findViewById(R.id.singleRecipeImageView);
        singleRecipeMyFavoriteImageButton = (ImageButton) findViewById(R.id.myFavoriteImageButton);

        googleAdView = (AdView) findViewById(R.id.ad);

        secondItemIngredients = (LinearLayout) findViewById(R.id.secondIngredient);
        thirdItemIngredients = (LinearLayout) findViewById(R.id.thirdIngredient);

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

        //set Google Ads bannerx
        AdRequest re = new AdRequest();
        re.setGender(AdRequest.Gender.FEMALE);
        googleAdView.loadAd(re);

        singleRecipeNameTextView.setText(selectedRecipe.getName());
        singleRecipeRatingBar.setRating(selectedRecipe.getRatings());

        String descriptionString = selectedRecipe.getDescription();
        String[] descriptionArray = descriptionString.split("#");
        String finalDescriptionString = "";
        for (int descriptionCount = 0; descriptionCount < descriptionArray.length; descriptionCount++) {
            finalDescriptionString = finalDescriptionString + descriptionArray[descriptionCount].replace("#","") + "\n\n";
        }

        singleRecipeDescriptionTextView.setText(finalDescriptionString);
        singleRecipeContentLabelTextView.setText(/*selectedRecipe.getName() + */"Ingredients");

        //device layout width and height
        layoutWidthAndHeight = userPermissionServices.getDeviceWidthAndHeight(SingleRecipeActivity.this);
        contentParams = (LinearLayout.LayoutParams)singleRecipeImageViewer.getLayoutParams();
        float height = (450 * layoutWidthAndHeight.get("width"))/720;
        contentParams.height = Math.round(height);
        contentParams.width = layoutWidthAndHeight.get("width");
        singleRecipeImageViewer.setLayoutParams(contentParams);

        if(layoutWidthAndHeight.get("width") <= 480) {
//            singleRecipeMyFavoriteImageButton.
            favoriteButtonParams = (FrameLayout.LayoutParams)singleRecipeMyFavoriteImageButton.getLayoutParams();
            favoriteButtonParams.height = 43;
            favoriteButtonParams.width = 140;
            singleRecipeMyFavoriteImageButton.setLayoutParams(favoriteButtonParams);
        }

       /*String imageUrl = selectedRecipe.getImageUrl_l();
        int loader = R.drawable.default_recipe_image;

        try {
            ImageLoader imageLoader = new ImageLoader(getApplicationContext());
            imageLoader.DisplayImage(imageUrl, loader, singleRecipeImageViewer);
        } catch (Exception ex) {
            ex.printStackTrace();
        }*/

        int legacy = selectedRecipe.getLegacy();
        if (legacy == 0) {
            //Hide two extra ingredient linear layouts
            secondItemIngredients.setVisibility(View.GONE);
            thirdItemIngredients.setVisibility(View.GONE);

            String instructionString = selectedRecipe.getInstructions();
            String[] instructionsArray = instructionString.split("#");
            String finalInstructionString = "";
            for (int instructCount = 0; instructCount < instructionsArray.length; instructCount++) {
                finalInstructionString = finalInstructionString + instructionsArray[instructCount] + "\n\n";
            }

            singleRecipeInstructionTextView.setText(finalInstructionString);

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
                            if (!itemJsonObj.getString("unit").equals("null")) {
                                ingredientString = ingredientString + " " + itemJsonObj.getString("unit");
                            }
                            if (!itemJsonObj.getString("name").equals("null")) {
                                ingredientString = ingredientString + " " + itemJsonObj.getString("name");
                            }
                            if (!itemJsonObj.getString("note").equals("null")) {
                                ingredientString = ingredientString + " " + itemJsonObj.getString("note");
                            }
                            ingredientString = ingredientString + "\n";
                        } else {
                            if (!itemJsonObj.getString("unit").equals("null")) {
                                ingredientString = ingredientString + " " + itemJsonObj.getString("unit");
                            }
                            if (!itemJsonObj.getString("name").equals("null")) {
                                ingredientString = ingredientString + " " + itemJsonObj.getString("name");
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
                        /*if (i == 0) {
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
                        }*/
                        if (i == 0) {
                            singleRecipeContentLabelTextView.setText(ingredientJsonObj.getString("title"));
                            itemsJsonArray = ingredientJsonObj.getJSONArray("items");
                            for (int k = 0; k < itemsJsonArray.length(); k++) {
                                itemJsonObj = itemsJsonArray.getJSONObject(k);

                                if (!itemJsonObj.getString("unit").equals("null")) {
                                    ingredientString = ingredientString + itemJsonObj.getString("unit");
                                }
                                if (!itemJsonObj.getString("name").equals("null")) {
                                    ingredientString = ingredientString + " " + itemJsonObj.getString("name");
                                }
                                if (!itemJsonObj.getString("note").equals("null")) {
                                    ingredientString = ingredientString + " " + itemJsonObj.getString("note");
                                }
                                ingredientString = ingredientString + "\n";
                            }
                            singleRecipeIngredientTextView.setText(ingredientString);
                        } else if (i == 1) {
                            secondItemIngredients.setVisibility(View.VISIBLE);
                            secondItemIngredientTitleTextView.setText(ingredientJsonObj.getString("title"));
                            ingredientString = "";
                            itemsJsonArray = ingredientJsonObj.getJSONArray("items");
                            for (int k = 0; k < itemsJsonArray.length(); k++) {
                                itemJsonObj = itemsJsonArray.getJSONObject(k);

                                if (!itemJsonObj.getString("unit").equals("null")) {
                                    ingredientString = ingredientString + itemJsonObj.getString("unit");
                                }
                                if (!itemJsonObj.getString("name").equals("null")) {
                                    ingredientString = ingredientString + " " + itemJsonObj.getString("name");
                                }
                                if (!itemJsonObj.getString("note").equals("null")) {
                                    ingredientString = ingredientString + " " + itemJsonObj.getString("note");
                                }
                                ingredientString = ingredientString + "\n";
                            }
                            secondItemIngredientsTextView.setText(ingredientString);
                        } else if (i == 2) {
                            thirdItemIngredients.setVisibility(View.VISIBLE);
                            thirdItemIngredientTitleTextView.setText(ingredientJsonObj.getString("title"));
                            ingredientString = "";
                            itemsJsonArray = ingredientJsonObj.getJSONArray("items");
                            for (int k = 0; k < itemsJsonArray.length(); k++) {
                                itemJsonObj = itemsJsonArray.getJSONObject(k);

                                if (!itemJsonObj.getString("unit").equals("null")) {
                                    ingredientString = ingredientString + itemJsonObj.getString("unit");
                                }
                                if (!itemJsonObj.getString("name").equals("null")) {
                                    ingredientString = ingredientString + " " + itemJsonObj.getString("name");
                                }
                                if (!itemJsonObj.getString("note").equals("null")) {
                                    ingredientString = ingredientString + " " + itemJsonObj.getString("note");
                                }
                                ingredientString = ingredientString + "\n";
                            }
                            thirdItemIngredientsTextView.setText(ingredientString);
                        }
                    }

//                    singleRecipeIngredientTextView.setText(ingredientString);
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
                    } else if (recipeBodyString.substring(0,12).equals("Instructions")) {
                        singleBodyString = singleBodyString + "\n\n" + recipeBodyString + "\n\n";
                    }else {
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
            instructionsLinearLayout.setVisibility(View.GONE);
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
            singleRecipeMyFavoriteImageButton.setImageResource(R.drawable.fav_remove);
        }

        singleRecipeMyFavoriteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean dbFovStatusBtnClickTest = localDatabaseSQLiteOpenHelper.isUserFavoriteRecipe(selectedRecipe.getProductId(), LoginActivity.LOGGED_USER_ID);
                if (isOnline) {

                    Map<String, String> lastLoginDetails = localDatabaseSQLiteOpenHelper.getLoginDetails();
                    if (!lastLoginDetails.isEmpty()) {

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

                            if (!dbFovStatusBtnClickTest && !serverFovStatusBtnClickTest) {
                                isFavorite = false;
                            }
                        }


                        if (isFavorite) {

                            boolean removeFavoriteResult = connector.removeFromMyFavorite(selectedRecipe.getProductId(), SingleRecipeActivity.this);
                            if (removeFavoriteResult) {
                                singleRecipeMyFavoriteImageButton.setImageResource(R.drawable.fav_add);
                                localDatabaseSQLiteOpenHelper.removeFromUserFavorite(selectedRecipe.getProductId());
                                Toast.makeText(getApplicationContext(),
                                        "Removed this recipe from you favorite list", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Removing was failed", Toast.LENGTH_LONG).show();
                            }
                        } else {

                            boolean addFavoriteResult = connector.addToMyFavorite(selectedRecipe.getProductId(), SingleRecipeActivity.this);
                            if (addFavoriteResult) {
                                singleRecipeMyFavoriteImageButton.setImageResource(R.drawable.fav_remove);
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
                                "Buy commercial version to create account and add your own favorite recipes.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Application is in offline mode. Please on your mobile data", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * AsyncTask loadImageTask calling method
     */
    private void loadRecipeImage() {
        new loadImageTask().execute();
    }

    /**
     * AsyncTask loadImageTask
     * This load the image recipe from given url in background
     */
    private class loadImageTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... voids) {
            String imageUrl = selectedRecipe.getImageUrl_l();
            Bitmap bmp = null;
            try {
                URL url = new URL(imageUrl);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            singleRecipeImageViewer.setImageBitmap(bitmap);
        }
    }
}
