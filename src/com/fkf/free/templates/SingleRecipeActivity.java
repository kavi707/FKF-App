package com.fkf.free.templates;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.AndroidCharacter;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.fkf.free.R;
import com.fkf.free.database.LocalDatabaseSQLiteOpenHelper;
import com.fkf.free.database.Recipe;
import com.fkf.free.database.dbprovider.ContentProviderAccessor;
import com.fkf.free.services.ActivityUserPermissionServices;
import com.fkf.free.services.connections.ApiConnector;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.font.TextAttribute;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.jar.Attributes;

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

    private TextView relatedRecipeLabel;

    private ImageView singleRecipeImageViewer;
    private ImageButton singleRecipeMyFavoriteImageButton;

    private AdView googleAdView;

    private LinearLayout.LayoutParams contentParams;
    private FrameLayout.LayoutParams favoriteButtonParams;
    private LinearLayout instructionsLinearLayout;
    private LinearLayout relatedRecipeLinearLayout;
    private Map<String, Integer> layoutWidthAndHeight;

    private LinearLayout secondItemIngredients, thirdItemIngredients;
    private LinearLayout linkedImagesLinearLayout, linkedRecipesLinearLayout;

    private ActivityUserPermissionServices userPermissionServices = new ActivityUserPermissionServices();
    private LocalDatabaseSQLiteOpenHelper localDatabaseSQLiteOpenHelper = new LocalDatabaseSQLiteOpenHelper(this);
    private ContentProviderAccessor contentProviderAccessor = new ContentProviderAccessor();

    private ApiConnector connector = new ApiConnector();
    private Recipe selectedRecipe;
    private Recipe linkedRecipe;
    private List<String> linkedRecipesIdList = new ArrayList<String>();
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
        Log.d("onPost Create Override : >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ", "onPostCreate executing");
        if(userPermissionServices.isOnline(SingleRecipeActivity.this)) {
            //calling recipe image loading in background
            this.loadRecipeImage();
            //Set favorite button image
            this.setFavoriteButtonImage();
        }
    }

    private void setUpView() {

        instructionsLinearLayout = (LinearLayout) findViewById(R.id.instructionsLinearLayout);
        relatedRecipeLinearLayout = (LinearLayout) findViewById(R.id.relatedRecipeLinearLayout);

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

        relatedRecipeLabel = (TextView) findViewById(R.id.relatedRecipeLabel);

        singleRecipeImageViewer = (ImageView) findViewById(R.id.singleRecipeImageView);
        singleRecipeMyFavoriteImageButton = (ImageButton) findViewById(R.id.myFavoriteImageButton);

        googleAdView = (AdView) findViewById(R.id.ad);

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

        //set Google Ads bannerx
        AdRequest re = new AdRequest();
        re.setGender(AdRequest.Gender.FEMALE);
        googleAdView.loadAd(re);

        relatedRecipeLabel.setVisibility(View.GONE);
        relatedRecipeLinearLayout.setVisibility(View.GONE);

        singleRecipeNameTextView.setText(selectedRecipe.getName());
        singleRecipeRatingBar.setRating(selectedRecipe.getRatings());

        String descriptionString = selectedRecipe.getDescription();
        String finalDescriptionString = "";
        if (!descriptionString.equals("") && !descriptionString.equals(null)) {
            try {
                JSONArray descriptionJsonArray = new JSONArray(descriptionString);
                for (int descStringCount = 0; descStringCount < descriptionJsonArray.length(); descStringCount++) {
                    finalDescriptionString = finalDescriptionString + descriptionJsonArray.getString(descStringCount).replace("#","") + "\n\n";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        singleRecipeDescriptionTextView.setText(finalDescriptionString);
        singleRecipeContentLabelTextView.setText(/*selectedRecipe.getName() + */"Ingredients");

        //device layout width and height
        layoutWidthAndHeight = userPermissionServices.getDeviceWidthAndHeight(SingleRecipeActivity.this);
        contentParams = (LinearLayout.LayoutParams)singleRecipeImageViewer.getLayoutParams();
        float height = (810 * layoutWidthAndHeight.get("width"))/1080;
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

        int legacy = selectedRecipe.getLegacy();
        if (legacy == 0) {
            //Hide two extra ingredient linear layouts
            secondItemIngredients.setVisibility(View.GONE);
            thirdItemIngredients.setVisibility(View.GONE);

            String instructionString = selectedRecipe.getInstructions();
            String finalInstructionString = "";
            if (!instructionString.equals("") && !instructionString.equals(null)){
                try {
                    JSONArray instructionJsonArray = new JSONArray(instructionString);
                    for (int instructionStringCount = 0; instructionStringCount < instructionJsonArray.length(); instructionStringCount++) {
                        finalInstructionString = finalInstructionString + instructionJsonArray.getString(instructionStringCount)+ "\n\n";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                    singleBodyString = singleBodyString + recipeBodyString + "\n";
                }
            }

            singleRecipeIngredientTextView.setText(singleBodyString);
            singleRecipeInstructionTextView.setVisibility(View.GONE);
            getSingleRecipeInstructionLabelTextView.setVisibility(View.GONE);
            instructionsLinearLayout.setVisibility(View.GONE);
        }

        isOnline = userPermissionServices.isOnline(SingleRecipeActivity.this);

        singleRecipeMyFavoriteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                boolean dbFovStatusBtnClickTest = localDatabaseSQLiteOpenHelper.isUserFavoriteRecipe(selectedRecipe.getProductId(), LoginActivity.LOGGED_USER_ID);
                boolean dbFovStatusBtnClickTest = contentProviderAccessor.isUserFavoriteRecipe(context, selectedRecipe.getProductId(), LoginActivity.LOGGED_USER_ID);
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

        loadLinkedRecipes();
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
//            String imageUrl = selectedRecipe.getImageUrl_l();
            String imageUrl = selectedRecipe.getImageUrl_xl();
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

            //Load linked images if device is in online
            loadLinkedImages();
        }
    }

    /**
     * Load linked recipes to view calling method
     */
    private void loadLinkedRecipes() {

        String jsonLinkedRecipes = selectedRecipe.getLinkRecipeIds();
        if (!jsonLinkedRecipes.equals("0")) {
            try {
                relatedRecipeLabel.setVisibility(View.VISIBLE);
                relatedRecipeLinearLayout.setVisibility(View.VISIBLE);
                JSONArray linkedRecipesJson = new JSONArray(jsonLinkedRecipes);
                for (int recipeCount = 0; recipeCount < linkedRecipesJson.length(); recipeCount++) {
                    Log.d("Recipe Count : ", String.valueOf(recipeCount));
                    Log.d("Recipe ID : ", linkedRecipesJson.getString(recipeCount));
                    linkedRecipesIdList.add(linkedRecipesJson.getString(recipeCount));
                    setLinkedRecipeTextView(linkedRecipesJson.getString(recipeCount), recipeCount);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Create TextView to each liked recipe and load it to view
     * @param linkedRecipeId
     * @param recipeCount
     */
    public void setLinkedRecipeTextView(String linkedRecipeId, int recipeCount) {

        TextView textView = new TextView(context);
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lParams.setMargins(20,0,0,0);
        textView.setLayoutParams(lParams);

        List<Recipe> linkedRecipeList = localDatabaseSQLiteOpenHelper.getRecipeFromRecipeProductId(linkedRecipeId);
        if (linkedRecipeList.size() == 1) {
            linkedRecipe = linkedRecipeList.get(0);

            textView.setText((recipeCount + 1) + ". " + linkedRecipe.getName());
            textView.setTextColor(Color.GRAY);
            textView.setTextSize(18);
            textView.setId(recipeCount);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent linkedRecipeIntent = new Intent(SingleRecipeActivity.this, SingleRecipeActivity.class);
                    linkedRecipeIntent.putExtra("SELECTED_RECIPE_ID", Integer.parseInt(linkedRecipesIdList.get(v.getId())));
                    startActivity(linkedRecipeIntent);
                    finish();
                }
            });

            linkedRecipesLinearLayout.addView(textView);
        } else {
            Log.d("List Count : ", String.valueOf(linkedRecipeList.size()));
        }
    }

    /**
     * Create linked images url list and call linked image loading method
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

        loadLinkedImageTask linkedImageTask = new loadLinkedImageTask();
        try {
            linkedImageTask.execute(imageUrl).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
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

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(lParams);

            imageView.setImageBitmap(bitmap);

            linkedImagesLinearLayout.addView(imageView);
        }
    }

    /**
     * This method is for set the favorite button image.
     * Because if recipe is favorite then it must be red heart, if not it must be white heart
     */
    private void setFavoriteButtonImage() {

        boolean dbFovStatus = contentProviderAccessor.isUserFavoriteRecipe(context, selectedRecipe.getProductId(), LoginActivity.LOGGED_USER_ID);
        isFavorite = dbFovStatus;
        /*if (isOnline) {
            isFavoriteCheckTask isFavoriteCheckTask = new isFavoriteCheckTask();
            boolean serverFovStatus = false;
            try {
                serverFovStatus = isFavoriteCheckTask.execute().get();
                serverFovStatus = false;
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
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            isFavorite = dbFovStatus;
        }*/

        if(isFavorite) {
            singleRecipeMyFavoriteImageButton.setImageResource(R.drawable.fav_remove);
        }
    }

    /**
     * AsyncTask isFavoriteCheckTask
     * This check recipe is favorite or not
     */
    private class isFavoriteCheckTask extends AsyncTask<Void, Void, Boolean> {

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p/>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Boolean doInBackground(Void... params) {
            return connector.isMyFavorite(selectedRecipe.getProductId(), SingleRecipeActivity.this);
        }
    }
}
