package com.fkf.resturent.templates;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.fkf.resturent.R;
import com.fkf.resturent.database.LocalDatabaseSQLiteOpenHelper;
import com.fkf.resturent.database.Recipe;
import com.fkf.resturent.services.ActivityUserPermissionServices;
import com.fkf.resturent.services.connections.ApiConnector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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

    private LinearLayout.LayoutParams contentParams;
    private FrameLayout.LayoutParams favoriteButtonParams;
    private LinearLayout instructionsLinearLayout;
    private Map<String, Integer> layoutWidthAndHeight;

    private LinearLayout secondItemIngredients, thirdItemIngredients;
    private LinearLayout linkedImagesLinearLayout, linkedRecipesLinearLayout;

    private ActivityUserPermissionServices userPermissionServices = new ActivityUserPermissionServices();
    private LocalDatabaseSQLiteOpenHelper localDatabaseSQLiteOpenHelper = new LocalDatabaseSQLiteOpenHelper(this);
    private ApiConnector connector = new ApiConnector();
    private Recipe selectedRecipe;
    private Recipe linkedRecipe;
    private String selectedRecipeProductId = null;
    private boolean isFavorite = false;
    private boolean isOnline = false;
    private Context context = this;

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

        secondItemIngredients = (LinearLayout) findViewById(R.id.secondIngredient);
        thirdItemIngredients = (LinearLayout) findViewById(R.id.thirdIngredient);

        linkedImagesLinearLayout = (LinearLayout) findViewById(R.id.linkedImagesLinearLayout);
        linkedRecipesLinearLayout = (LinearLayout) findViewById(R.id.linkedRecipesLinearLayout);

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
//        float height = (450 * layoutWidthAndHeight.get("width"))/720;
//        contentParams.height = Math.round(height);
//        contentParams.width = layoutWidthAndHeight.get("width");
//        singleRecipeImageViewer.setLayoutParams(contentParams);

        if(layoutWidthAndHeight.get("width") <= 480) {
//            singleRecipeMyFavoriteImageButton.
            favoriteButtonParams = (FrameLayout.LayoutParams)singleRecipeMyFavoriteImageButton.getLayoutParams();
            favoriteButtonParams.height = 43;
            favoriteButtonParams.width = 140;
            singleRecipeMyFavoriteImageButton.setLayoutParams(favoriteButtonParams);
        }

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
                    singleBodyString = singleBodyString + recipeBodyString + "\n\n";
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
                                "First you have to login to application. Then you can add favorite for your account.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Application is in offline mode. Please on your mobile data", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Load linked images if device is in online
        if (isOnline) {
            loadLinkedImages();
        }

        loadLinkedRecipes();
    }

    /**
     * AsyncTask loadImageTask calling method
     */
    private void loadRecipeImage() {
        new loadImageTask().execute();
    }

    /**
     *
     */
    private void loadLinkedRecipes() {

        String jsonLinkedRecipes = selectedRecipe.getLinkRecipeIds();
        Log.d("linked recipes string >>>>>>>>>>>>>>>. ", jsonLinkedRecipes);
        if (!jsonLinkedRecipes.equals("0")) {
            Log.d("Come to here ", "..... +++++++++++++++++ ");
            try {
                JSONArray linkedRecipesJson = new JSONArray(jsonLinkedRecipes);
                for (int recipeCount = 0; recipeCount < linkedRecipesJson.length(); recipeCount++) {
                    Log.d("Recipe Count : ", String.valueOf(recipeCount));
                    Log.d("Recipe ID : ", linkedRecipesJson.getString(recipeCount));
                    setRecipeLinkedTextView(linkedRecipesJson.getString(recipeCount), recipeCount + 1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void setRecipeLinkedTextView(String linkedRecipeId, int recipeCount) {

        TextView textView = new TextView(context);
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(lParams);

        List<Recipe> linkedRecipeList = localDatabaseSQLiteOpenHelper.getRecipeFromRecipeProductId(linkedRecipeId);
        if (linkedRecipeList.size() == 1) {
            linkedRecipe = linkedRecipeList.get(0);

            textView.setText(recipeCount + ". " + linkedRecipe.getName());
            textView.setTextColor(Color.BLUE);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent linkedRecipeIntent = new Intent(SingleRecipeActivity.this, SingleRecipeActivity.class);
                    linkedRecipeIntent.putExtra("SELECTED_RECIPE_ID", Integer.parseInt(linkedRecipe.getProductId()));
                    startActivity(linkedRecipeIntent);
                    finish();
                }
            });

            linkedRecipesLinearLayout.addView(textView);
        } else {
            Log.d("List Count : >>>>>>>>>>>>>>>>>>>> ", String.valueOf(linkedRecipeList.size()));
        }
    }

    /**
     *
     */
    private void loadLinkedImages() {

        String jsonLinkedImages = selectedRecipe.getLinkImages();
        List<String> imageUrls = new ArrayList<String>();
        if (!jsonLinkedImages.equals("0")) {
            try {
                JSONArray linkedImagesJson = new JSONArray(jsonLinkedImages);
                for (int imageCount = 0; imageCount < linkedImagesJson.length(); imageCount++) {
                    imageUrls.add("http://www.fauziaskitchenfun.com"+linkedImagesJson.getString(imageCount));
                    setImageViewsToMainView("http://www.fauziaskitchenfun.com"+linkedImagesJson.getString(imageCount));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * Set the downloaded image bitmap object to dynamically created ImageView
     * Load the created ImageView to LinearLayout
     * @param imageUrl
     */
    private void setImageViewsToMainView(String imageUrl) {

        TextView textView = new TextView(context);
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(lParams);

        ImageView imageView = new ImageView(context);
//        float height = (450 * layoutWidthAndHeight.get("width"))/720;
//        contentParams.height = Math.round(height);
//        contentParams.width = layoutWidthAndHeight.get("width");
//        imageView.setLayoutParams(contentParams);
        imageView.setLayoutParams(lParams);

        loadLinkedImageTask linkedImageTask = new loadLinkedImageTask();
        try {
            Bitmap bitmap = linkedImageTask.execute(imageUrl).get();
            imageView.setImageBitmap(bitmap);

            linkedImagesLinearLayout.addView(imageView);
//            linkedImagesLinearLayout.addView(textView);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
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

    /**
     * Download the linked image from given url and set as a bitmap object.
     * Do this as an asyncTask
     */
    private class loadLinkedImageTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            String imageUrl = strings[0];
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
    }
}
