package com.fkf.resturent.templates;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.*;

import com.fkf.resturent.R;
import com.fkf.resturent.adapter.RecipeCategoryListAdapter;
import com.fkf.resturent.adapter.RecipeListAdapter;
import com.fkf.resturent.database.LocalDatabaseSQLiteOpenHelper;
import com.fkf.resturent.database.PopularOrLatestRecipe;
import com.fkf.resturent.database.Recipe;
import com.fkf.resturent.database.RecipeCategory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kavi on 6/22/13.
 * Hold the main menu ui for logged user and un-logged user.
 * recipes_layout.xml Activity class
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class RecipesActivity extends Activity implements View.OnClickListener{

    ImageButton menuButton, logoutButton, searchRecipeButton;
    LinearLayout content, menu;

    LinearLayout firstPopularYummyLinear;
    LinearLayout secondPopularYummyLinear;
    LinearLayout thirdPopularYummyLinear;
    LinearLayout forthPopularYummyLinear;
    LinearLayout fifthPopularYummyLinear;
    LinearLayout sixthPopularYummyLinear;

    ListView menuItemList, recipeItemList;
    ScrollView latestAndPopularScrollBar;
    HorizontalScrollView horizontalScroll;
    TextView loggedUserTextView, yummyCategoryNameTextView, homeTextView, myFavoriteTextView;
    ImageView profileImageView;
    EditText searchRecipeEditText;

    //yummy image buttons in the horizontal scroll
    ImageButton firstYummyImageButton;
    ImageButton secondYummyImageButton;
    ImageButton thirdYummyImageButton;
    ImageButton forthYummyImageButton;
    ImageButton fifthYummyImageButton;

    //latest yummy text views
    TextView firstYummyTextView;
    TextView secondYummyTextView;
    TextView thirdYummyTextView;
    TextView forthYummyTextView;
    TextView fifthYummyTextView;

    //popular yummy imageViews
    ImageView firstPopularYummyImageView;
    ImageView secondPopularYummyImageView;
    ImageView thirdPopularYummyImageView;
    ImageView forthPopularYummyImageView;
    ImageView fifthPopularYummyImageView;
    ImageView sixthPopularYummyImageView;

    //popular yummy name text views
    TextView firstPopularRecipeNameTextView;
    TextView secondPopularRecipeNameTextView;
    TextView thirdPopularRecipeNameTextView;
    TextView forthPopularRecipeNameTextView;
    TextView fifthPopularRecipeNameTextView;
    TextView sixthPopularRecipeNameTextView;

    //popular yummy desc text views
    TextView firstPopularRecipeDescTextView;
    TextView secondPopularRecipeDescTextView;
    TextView thirdPopularRecipeDescTextView;
    TextView forthPopularRecipeDescTextView;
    TextView fifthPopularRecipeDescTextView;
    TextView sixthPopularRecipeDescTextView;

    //popular yummy ratings bars
    RatingBar firstPopularRecipeRatingBar;
    RatingBar secondPopularRecipeRatingBar;
    RatingBar thirdPopularRecipeRatingBar;
    RatingBar forthPopularRecipeRatingBar;
    RatingBar fifthPopularRecipeRatingBar;
    RatingBar sixthPopularRecipeRatingBar;


    private RecipeCategoryListAdapter recipeCategoryListAdapter;
    private RecipeListAdapter recipeListAdapter;
    private ArrayList<Recipe> recipeList;
    private Context context = this;

    LinearLayout.LayoutParams contentParams;
    TranslateAnimation slide;
    int marginX, animateFromX, animateToX = 0;
    boolean menuOpen = false;

    View selectedView, lastSelectedView;

    private LocalDatabaseSQLiteOpenHelper localDatabaseSQLiteOpenHelper = new LocalDatabaseSQLiteOpenHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipes_layout);

        setUpViews();
    }

    private void setUpViews() {
        ArrayList<RecipeCategory> menuItems;

        horizontalScroll = (HorizontalScrollView) findViewById(R.id.horizontalScroll);
        latestAndPopularScrollBar = (ScrollView) findViewById(R.id.latestAndPopularScrollBar);

        firstYummyImageButton = (ImageButton) findViewById(R.id.firstYummyImageButton);
        secondYummyImageButton = (ImageButton) findViewById(R.id.secondYummyImageButton);
        thirdYummyImageButton = (ImageButton) findViewById(R.id.thirdYummyImageButton);
        forthYummyImageButton = (ImageButton) findViewById(R.id.forthYummyImageButton);
        fifthYummyImageButton = (ImageButton) findViewById(R.id.fifthYummyImageButton);

        firstYummyTextView = (TextView) findViewById(R.id.firstYummyTextView);
        secondYummyTextView = (TextView) findViewById(R.id.secondYummyTextView);
        thirdYummyTextView = (TextView) findViewById(R.id.thirdYummyTextView);
        forthYummyTextView = (TextView) findViewById(R.id.forthYummyTextView);
        fifthYummyTextView = (TextView) findViewById(R.id.fifthYummyTextView);

        firstPopularRecipeNameTextView = (TextView) findViewById(R.id.firstPopularRecipeNameTextView);
        secondPopularRecipeNameTextView = (TextView) findViewById(R.id.secondPopularRecipeNameTextView);
        thirdPopularRecipeNameTextView = (TextView) findViewById(R.id.thirdPopularRecipeNameTextView);
        forthPopularRecipeNameTextView = (TextView) findViewById(R.id.forthPopularRecipeNameTextView);
        fifthPopularRecipeNameTextView = (TextView) findViewById(R.id.fifthPopularRecipeNameTextView);
        sixthPopularRecipeNameTextView = (TextView) findViewById(R.id.sixthPopularRecipeNameTextView);

        firstPopularRecipeDescTextView = (TextView) findViewById(R.id.firstPopularRecipeDescriptionTextView);
        secondPopularRecipeDescTextView = (TextView) findViewById(R.id.secondPopularRecipeDescriptionTextView);
        thirdPopularRecipeDescTextView = (TextView) findViewById(R.id.thirdPopularRecipeDescriptionTextView);
        forthPopularRecipeDescTextView = (TextView) findViewById(R.id.forthPopularRecipeDescriptionTextView);
        fifthPopularRecipeDescTextView = (TextView) findViewById(R.id.fifthPopularRecipeDescriptionTextView);
        sixthPopularRecipeDescTextView = (TextView) findViewById(R.id.sixthPopularRecipeDescriptionTextView);

        firstPopularRecipeRatingBar = (RatingBar) findViewById(R.id.firstPopularRecipeRatingBar);
        secondPopularRecipeRatingBar = (RatingBar) findViewById(R.id.secondPopularRecipeRatingBar);
        thirdPopularRecipeRatingBar = (RatingBar) findViewById(R.id.thirdPopularRecipeRatingBar);
        forthPopularRecipeRatingBar = (RatingBar) findViewById(R.id.forthPopularRecipeRatingBar);
        fifthPopularRecipeRatingBar = (RatingBar) findViewById(R.id.fifthPopularRecipeRatingBar);
        sixthPopularRecipeRatingBar = (RatingBar) findViewById(R.id.sixthPopularRecipeRatingBar);


        List<PopularOrLatestRecipe> latestRecipes = localDatabaseSQLiteOpenHelper.getAllLatestRecipes();
        String[] latestRecipeNames = new String[10];
        if(latestRecipes != null) {
            for (PopularOrLatestRecipe latestRecipe : latestRecipes) {
                latestRecipeNames[latestRecipe.getIndex()] = latestRecipe.getRecipeName();
            }
        }




        /********************************************/
        /********* Latest yummys buttons ************/
        /********************************************/

        //Embedded images to yummys buttons
        File firstImageFile = new File("/sdcard/fauzias/latest_yummys/icon_1.jpg");
        if(firstImageFile.exists()) {
            Bitmap firstBitmap = BitmapFactory.decodeFile(firstImageFile.getAbsolutePath());
            firstYummyImageButton.setImageBitmap(firstBitmap);
            firstYummyTextView.setText("  " + latestRecipeNames[1]);
        } else {
            firstYummyImageButton.setImageResource(R.drawable.fkf_xs);
        }


        File secondImageFile = new File("/sdcard/fauzias/latest_yummys/icon_2.jpg");
        if(secondImageFile.exists()) {
            Bitmap secondBitmap = BitmapFactory.decodeFile(secondImageFile.getAbsolutePath());
            secondYummyImageButton.setImageBitmap(secondBitmap);
            secondYummyTextView.setText("  " + latestRecipeNames[2]);
        } else {
            secondYummyImageButton.setImageResource(R.drawable.fkf_xs);
        }


        File thirdImageFile = new File("/sdcard/fauzias/latest_yummys/icon_3.jpg");
        if(thirdImageFile.exists()) {
            Bitmap thirdBitmap = BitmapFactory.decodeFile(thirdImageFile.getAbsolutePath());
            thirdYummyImageButton.setImageBitmap(thirdBitmap);
            thirdYummyTextView.setText(latestRecipeNames[3]);
        } else {
            thirdYummyImageButton.setImageResource(R.drawable.fkf_xs);
        }


        File forthImageFile = new File("/sdcard/fauzias/latest_yummys/icon_4.jpg");
        if(forthImageFile.exists()) {
            Bitmap forthBitmap = BitmapFactory.decodeFile(forthImageFile.getAbsolutePath());
            forthYummyImageButton.setImageBitmap(forthBitmap);
            forthYummyTextView.setText("  " + latestRecipeNames[4]);
        } else {
            forthYummyImageButton.setImageResource(R.drawable.fkf_xs);
        }


        File fifthImageFile = new File("/sdcard/fauzias/latest_yummys/icon_5.jpg");
        if(fifthImageFile.exists()) {
            Bitmap fifthBitmap = BitmapFactory.decodeFile(fifthImageFile.getAbsolutePath());
            fifthYummyImageButton.setImageBitmap(fifthBitmap);
            fifthYummyTextView.setText("  " + latestRecipeNames[5]);
        } else {
            fifthYummyImageButton.setImageResource(R.drawable.fkf_xs);
        }


        //latest yummys image button click events
        firstYummyImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent singleRecipeIntent = new Intent(RecipesActivity.this, SingleRecipeActivity.class);
                singleRecipeIntent.putExtra("SELECTED_RECIPE_ID", -1);
                startActivity(singleRecipeIntent);
            }
        });

        secondYummyImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent singleRecipeIntent = new Intent(RecipesActivity.this, SingleRecipeActivity.class);
                singleRecipeIntent.putExtra("SELECTED_RECIPE_ID", -2);
                startActivity(singleRecipeIntent);
            }
        });

        thirdYummyImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent singleRecipeIntent = new Intent(RecipesActivity.this, SingleRecipeActivity.class);
                singleRecipeIntent.putExtra("SELECTED_RECIPE_ID", -3);
                startActivity(singleRecipeIntent);
            }
        });

        forthYummyImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent singleRecipeIntent = new Intent(RecipesActivity.this, SingleRecipeActivity.class);
                singleRecipeIntent.putExtra("SELECTED_RECIPE_ID", -4);
                startActivity(singleRecipeIntent);
            }
        });

        fifthYummyImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent singleRecipeIntent = new Intent(RecipesActivity.this, SingleRecipeActivity.class);
                singleRecipeIntent.putExtra("SELECTED_RECIPE_ID", -5);
                startActivity(singleRecipeIntent);
            }
        });






        /***********************************************/
        /********* Popular yummys row views ************/
        /***********************************************/

        //popular yummys linear layout click events
        //first popular yummy
        firstPopularYummyImageView = (ImageView) findViewById(R.id.firstPopularRecipeImageView);
        File firstPopularImageFile = new File("/sdcard/fauzias/popular_yummys/icon_1.jpg");
        if(firstPopularImageFile.exists()) {
            Bitmap firstPopularBitmap = BitmapFactory.decodeFile(firstPopularImageFile.getAbsolutePath());
            firstPopularYummyImageView.setImageBitmap(firstPopularBitmap);
        } else {
            firstPopularYummyImageView.setImageResource(R.drawable.fkf_m);
        }

        firstPopularYummyLinear = (LinearLayout) findViewById(R.id.firstPopularYummyLinear);
        firstPopularYummyLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent singleRecipeIntent = new Intent(RecipesActivity.this, SingleRecipeActivity.class);
                singleRecipeIntent.putExtra("SELECTED_RECIPE_ID", -6);
                startActivity(singleRecipeIntent);
            }
        });

        //second popular yummy
        secondPopularYummyImageView = (ImageView) findViewById(R.id.secondPopularRecipeImageView);
        File secondPopularImageFile = new File("/sdcard/fauzias/popular_yummys/icon_2.jpg");
        if(secondPopularImageFile.exists()) {
            Bitmap secondPopularBitmap = BitmapFactory.decodeFile(secondPopularImageFile.getAbsolutePath());
            secondPopularYummyImageView.setImageBitmap(secondPopularBitmap);
        } else {
            secondPopularYummyImageView.setImageResource(R.drawable.fkf_m);
        }

        secondPopularYummyLinear = (LinearLayout) findViewById(R.id.secondPopularYummyLinear);
        secondPopularYummyLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent singleRecipeIntent = new Intent(RecipesActivity.this, SingleRecipeActivity.class);
                singleRecipeIntent.putExtra("SELECTED_RECIPE_ID", -7);
                startActivity(singleRecipeIntent);
            }
        });

        //third popular yummy
        thirdPopularYummyImageView = (ImageView) findViewById(R.id.thirdPopularRecipeImageView);
        File thirdPopularImageFile = new File("/sdcard/fauzias/popular_yummys/icon_3.jpg");
        if(thirdPopularImageFile.exists()) {
            Bitmap thirdPopularBitmap = BitmapFactory.decodeFile(thirdPopularImageFile.getAbsolutePath());
            thirdPopularYummyImageView.setImageBitmap(thirdPopularBitmap);
        } else {
            thirdPopularYummyImageView.setImageResource(R.drawable.fkf_m);
        }

        thirdPopularYummyLinear = (LinearLayout) findViewById(R.id.thirdPopularYummyLinear);
        thirdPopularYummyLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent singleRecipeIntent = new Intent(RecipesActivity.this, SingleRecipeActivity.class);
                singleRecipeIntent.putExtra("SELECTED_RECIPE_ID", -8);
                startActivity(singleRecipeIntent);
            }
        });

        //forth popular yummy
        forthPopularYummyImageView = (ImageView) findViewById(R.id.forthPopularRecipeImageView);
        File forthPopularImageFile = new File("/sdcard/fauzias/popular_yummys/icon_4.jpg");
        if(forthPopularImageFile.exists()) {
            Bitmap forthPopularBitmap = BitmapFactory.decodeFile(forthPopularImageFile.getAbsolutePath());
            forthPopularYummyImageView.setImageBitmap(forthPopularBitmap);
        } else {
            forthPopularYummyImageView.setImageResource(R.drawable.fkf_m);
        }

        forthPopularYummyLinear = (LinearLayout) findViewById(R.id.forthPopularYummyLinear);
        forthPopularYummyLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent singleRecipeIntent = new Intent(RecipesActivity.this, SingleRecipeActivity.class);
                singleRecipeIntent.putExtra("SELECTED_RECIPE_ID", -9);
                startActivity(singleRecipeIntent);
            }
        });

        //fifth popular yummy
        fifthPopularYummyImageView = (ImageView) findViewById(R.id.fifthPopularRecipeImageView);
        File fifthPopularImageFile = new File("/sdcard/fauzias/popular_yummys/icon_5.jpg");
        if(fifthPopularImageFile.exists()) {
            Bitmap fifthPopularBitmap = BitmapFactory.decodeFile(fifthPopularImageFile.getAbsolutePath());
            fifthPopularYummyImageView.setImageBitmap(fifthPopularBitmap);
        } else {
            fifthPopularYummyImageView.setImageResource(R.drawable.fkf_m);
        }

        fifthPopularYummyLinear = (LinearLayout) findViewById(R.id.fifthPopularYummyLinear);
        fifthPopularYummyLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent singleRecipeIntent = new Intent(RecipesActivity.this, SingleRecipeActivity.class);
                singleRecipeIntent.putExtra("SELECTED_RECIPE_ID", -10);
                startActivity(singleRecipeIntent);
            }
        });

        //sixth popular yummy
        sixthPopularYummyImageView = (ImageView) findViewById(R.id.sixthPopularRecipeImageView);
        File sixthPopularImageFile = new File("/sdcard/fauzias/popular_yummys/icon_6.jpg");
        if(sixthPopularImageFile.exists()) {
            Bitmap sixthPopularBitmap = BitmapFactory.decodeFile(sixthPopularImageFile.getAbsolutePath());
            sixthPopularYummyImageView.setImageBitmap(sixthPopularBitmap);
        } else {
            sixthPopularYummyImageView.setImageResource(R.drawable.fkf_m);
        }

        sixthPopularYummyLinear = (LinearLayout) findViewById(R.id.sixthPopularYummyLinear);
        sixthPopularYummyLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent singleRecipeIntent = new Intent(RecipesActivity.this, SingleRecipeActivity.class);
                singleRecipeIntent.putExtra("SELECTED_RECIPE_ID", -11);
                startActivity(singleRecipeIntent);
            }
        });


        List<PopularOrLatestRecipe> allPopularRecipeList = localDatabaseSQLiteOpenHelper.getAllPopularRecipes();
        if (allPopularRecipeList != null && !allPopularRecipeList.isEmpty()) {
            for (PopularOrLatestRecipe popularOrLatestRecipe : allPopularRecipeList) {

                List<Recipe> tempRecipesList = localDatabaseSQLiteOpenHelper.getRecipeFromRecipeProductId(popularOrLatestRecipe.getProductId());
                Recipe selectedRecipe = null;
                if(tempRecipesList != null && !tempRecipesList.isEmpty()) {
                    selectedRecipe = tempRecipesList.get(0);
                }

                switch (popularOrLatestRecipe.getIndex()) {
                    case 1:
                        firstPopularRecipeNameTextView.setText(popularOrLatestRecipe.getRecipeName());
                        if(selectedRecipe != null) {
                            String description = selectedRecipe.getDescription();
                            String shortDesc = "";
                            if(description.length() > 60) {
                                shortDesc = description.substring(0, 60);
                            } else {
                                shortDesc = description;
                            }
                            firstPopularRecipeDescTextView.setText(shortDesc);
                            firstPopularRecipeRatingBar.setRating(selectedRecipe.getRatings());
                        }
                        break;
                    case 2:
                        secondPopularRecipeNameTextView.setText(popularOrLatestRecipe.getRecipeName());
                        if(selectedRecipe != null) {
                            String description = selectedRecipe.getDescription();
                            String shortDesc = "";
                            if(description.length() > 60) {
                                shortDesc = description.substring(0, 60);
                            } else {
                                shortDesc = description;
                            }
                            secondPopularRecipeDescTextView.setText(shortDesc);
                            secondPopularRecipeRatingBar.setRating(selectedRecipe.getRatings());
                        }
                        break;
                    case 3:
                        thirdPopularRecipeNameTextView.setText(popularOrLatestRecipe.getRecipeName());
                        if(selectedRecipe != null) {
                            String description = selectedRecipe.getDescription();
                            String shortDesc = "";
                            if(description.length() > 60) {
                                shortDesc = description.substring(0, 60);
                            } else {
                                shortDesc = description;
                            }
                            thirdPopularRecipeDescTextView.setText(shortDesc);
                            thirdPopularRecipeRatingBar.setRating(selectedRecipe.getRatings());
                        }
                        break;
                    case 4:
                        forthPopularRecipeNameTextView.setText(popularOrLatestRecipe.getRecipeName());
                        if(selectedRecipe != null) {
                            String description = selectedRecipe.getDescription();
                            String shortDesc = "";
                            if(description.length() > 60) {
                                shortDesc = description.substring(0, 60);
                            } else {
                                shortDesc = description;
                            }
                            forthPopularRecipeDescTextView.setText(shortDesc);
                            forthPopularRecipeRatingBar.setRating(selectedRecipe.getRatings());
                        }
                        break;
                    case 5:
                        fifthPopularRecipeNameTextView.setText(popularOrLatestRecipe.getRecipeName());
                        if(selectedRecipe != null) {
                            String description = selectedRecipe.getDescription();
                            String shortDesc = "";
                            if(description.length() > 60) {
                                shortDesc = description.substring(0, 60);
                            } else {
                                shortDesc = description;
                            }
                            fifthPopularRecipeDescTextView.setText(shortDesc);
                            fifthPopularRecipeRatingBar.setRating(selectedRecipe.getRatings());
                        }
                        break;
                    case 6:
                        sixthPopularRecipeNameTextView.setText(popularOrLatestRecipe.getRecipeName());
                        if(selectedRecipe != null) {
                            String description = selectedRecipe.getDescription();
                            String shortDesc = "";
                            if(description.length() > 60) {
                                shortDesc = description.substring(0, 60);
                            } else {
                                shortDesc = description;
                            }
                            sixthPopularRecipeDescTextView.setText(shortDesc);
                            sixthPopularRecipeRatingBar.setRating(selectedRecipe.getRatings());
                        }
                        break;
                }
            }
        }

        //Following items are always in menu list
        homeTextView = (TextView) findViewById(R.id.menuHomeTextView);
        myFavoriteTextView = (TextView) findViewById(R.id.menuMyFavoriteTextView);

        //add items to category menu list
        menuItems = localDatabaseSQLiteOpenHelper.getAllCategories();

        menu = (LinearLayout)findViewById(R.id.menu);
        menuItemList = (ListView) findViewById(R.id.menu_item_list);
        recipeCategoryListAdapter = new RecipeCategoryListAdapter(menuItems, context);
        menuItemList.setAdapter(recipeCategoryListAdapter);


        //content view
        content = (LinearLayout)findViewById(R.id.content);
        contentParams = (LinearLayout.LayoutParams)content.getLayoutParams();
        contentParams.width = getWindowManager().getDefaultDisplay().getWidth();	// Ensures constant width of content during menu sliding
        contentParams.leftMargin = -(menu.getLayoutParams().width);		// Position the content at the start of the screen
        content.setLayoutParams(contentParams);

        //menu view button click actions
        menuButton = (ImageButton)findViewById(R.id.menu_button);
        menuButton.setOnClickListener(this);

        //yummy list view for selected yummy category
        recipeItemList = (ListView) findViewById(R.id.recipeItemList);

        //selected yummy's category name textView
        yummyCategoryNameTextView = (TextView) findViewById(R.id.yummyCategoryNameTextView);

        //Home list item for get back to latest and popular yummys
        homeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup.LayoutParams scrollParams = latestAndPopularScrollBar.getLayoutParams();
                scrollParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                latestAndPopularScrollBar.setLayoutParams(scrollParams);
                homeTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.selected_menu_item_background));
                myFavoriteTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.category_list_background));

                animateFromX = 0;
                animateToX = -(menu.getLayoutParams().width);
                marginX = -(menu.getLayoutParams().width);
                menuOpen = false;

                slideMenuIn(animateFromX, animateToX, marginX);
            }
        });

        //Favorite list item for view user favorite yummys
        //If user logged in to application
        if (LoginActivity.LOGGED_STATUS == 1) {

            myFavoriteTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myFavoriteTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.selected_menu_item_background));
                    homeTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.category_list_background));

                    ViewGroup.LayoutParams scrollParams = latestAndPopularScrollBar.getLayoutParams();

                    scrollParams.height = 0;
                    latestAndPopularScrollBar.setLayoutParams(scrollParams);

                    yummyCategoryNameTextView.setText("  My Favorite yummys");
                    List<String> favoriteIds = localDatabaseSQLiteOpenHelper.getLoggedUserFavoriteRecipeIds();
                    List<Recipe> tempRecipeList;
                    recipeList = new ArrayList<Recipe>();
                    for (String favoriteId : favoriteIds) {
                        tempRecipeList = localDatabaseSQLiteOpenHelper.getRecipeFromRecipeProductId(favoriteId);
                        if (!tempRecipeList.isEmpty()) {
                            Recipe tempRecipe = tempRecipeList.get(0);
                            recipeList.add(tempRecipe);
                        }
                    }

                    recipeListAdapter = new RecipeListAdapter(recipeList, context);
                    recipeItemList.setAdapter(recipeListAdapter);

                    animateFromX = 0;
                    animateToX = -(menu.getLayoutParams().width);
                    marginX = -(menu.getLayoutParams().width);
                    menuOpen = false;

                    slideMenuIn(animateFromX, animateToX, marginX);
                }
            });
        } else {
            ViewGroup.LayoutParams myFavoriteTextViewLayoutParams = myFavoriteTextView.getLayoutParams();
            myFavoriteTextViewLayoutParams.height = 0;
        }

        //selected yummy category list view
        //category list on item click
        menuItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                RecipeCategory itemContent = (RecipeCategory) (menuItemList.getItemAtPosition(i));

                if(itemContent != null) {

                    /*if (lastSelectedView != null) {
                        lastSelectedView.findViewById(R.id.recipeCategoryNameTextView).setBackgroundDrawable(getResources()
                                .getDrawable(R.drawable.category_list_background));
                    }

                    TextView tempSelecting = (TextView) view.findViewById(R.id.recipeCategoryNameTextView);
                    if(tempSelecting.getText().toString().equals(itemContent.getCategoryName())) {
                        selectedView = tempSelecting;
                        selectedView.setBackgroundDrawable(getResources().getDrawable(R.drawable.selected_menu_item_background));
                        lastSelectedView = selectedView;
                    }*/
                    myFavoriteTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.category_list_background));
                    homeTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.category_list_background));

                    ViewGroup.LayoutParams scrollParams = latestAndPopularScrollBar.getLayoutParams();

                    scrollParams.height = 0;
                    latestAndPopularScrollBar.setLayoutParams(scrollParams);

                    yummyCategoryNameTextView.setText("  " + itemContent.getCategoryName());
                    recipeList = localDatabaseSQLiteOpenHelper.getRecipesFromCategoryId(itemContent.getCategoryId());

                    recipeListAdapter = new RecipeListAdapter(recipeList, context);
                    recipeItemList.setAdapter(recipeListAdapter);

                    animateFromX = 0;
                    animateToX = -(menu.getLayoutParams().width);
                    marginX = -(menu.getLayoutParams().width);
                    menuOpen = false;

                    slideMenuIn(animateFromX, animateToX, marginX);
                }
            }
        });

        recipeItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Recipe itemContent = (Recipe) (recipeItemList.getItemAtPosition(i));
                if(itemContent != null) {
                    Intent singleRecipeIntent = new Intent(RecipesActivity.this, SingleRecipeActivity.class);
                    singleRecipeIntent.putExtra("SELECTED_RECIPE_ID", Integer.parseInt(itemContent.getProductId()));
                    startActivity(singleRecipeIntent);
                }
            }
        });

        //logout button & profile image
        logoutButton = (ImageButton) findViewById(R.id.logoutButton);
        profileImageView = (ImageView) findViewById(R.id.userIconImageView);
        if(LoginActivity.LOGGED_STATUS == 0) {
            logoutButton.setVisibility(View.GONE);
        } else if(LoginActivity.LOGGED_STATUS == 1){
            //Embedded images to profile pic image view
            File profilePicImage = new File("/sdcard/fauzias/user/profile_pic.png");
            Bitmap profilePicBitmap = BitmapFactory.decodeFile(profilePicImage.getAbsolutePath());
            profileImageView.setImageBitmap(profilePicBitmap);

            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LoginActivity.LOGGED_STATUS = 0;
                    LoginActivity.LOGGED_USER = null;
                    LoginActivity.LOGGED_USER_PASSWORD = null;
                    localDatabaseSQLiteOpenHelper.deleteLoginDetails();
                    Intent loginIntent = new Intent(RecipesActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                    finish();
                }
            });

            //Logged user textView
            loggedUserTextView = (TextView) findViewById(R.id.userNameTextView);
            loggedUserTextView.setText("Welcome " + LoginActivity.LOGGED_USER);
        }

        //recipes search from given name
        searchRecipeButton = (ImageButton) findViewById(R.id.searchRecipeImageButton);
        searchRecipeEditText = (EditText) findViewById(R.id.searchRecipeEditText);
        searchRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchKey = searchRecipeEditText.getText().toString();

                recipeList = localDatabaseSQLiteOpenHelper.searchRecipesFromGivenRecipeName(searchKey);

                ViewGroup.LayoutParams scrollParams = latestAndPopularScrollBar.getLayoutParams();
                scrollParams.height = 0;
                latestAndPopularScrollBar.setLayoutParams(scrollParams);

                recipeListAdapter = new RecipeListAdapter(recipeList, context);
                recipeItemList.setAdapter(recipeListAdapter);
            }
        });

    }

    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if(menuOpen) {
                slideMenuIn(0, -(menu.getLayoutParams().width), -(menu.getLayoutParams().width)); 	// Pass slide in paramters
                menuOpen = false;
                return true;
            }
        }
        return super.onKeyDown(keyCode, keyEvent);
    }

    public void slideMenuIn(int animateFromX, int animateToX, final int marginX){
        slide = new TranslateAnimation(animateFromX, animateToX, 0, 0);
        slide.setDuration(300);
        slide.setFillEnabled(true);
        slide.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {		// Make movement of content permanent after animation has completed
                contentParams.setMargins(marginX, 0, 0, 0);			// by positioning its left margin
                content.setLayoutParams(contentParams);
            }

            public void onAnimationRepeat(Animation animation) { }
            public void onAnimationStart(Animation animation) { }
        });
        content.startAnimation(slide);		// Slide menu in or out
    }

    @Override
    public void onClick(View view) {
        //To change body of implemented methods use File | Settings | File Templates.
        if(contentParams.leftMargin == -(menu.getLayoutParams().width)) {	// Menu is hidden (slide out parameters)
            animateFromX = 0;
            animateToX = (menu.getLayoutParams().width);
            marginX = 0;
            menuOpen = true;
        } else {	// Menu is visible (slide in parameter)
            animateFromX = 0;
            animateToX = -(menu.getLayoutParams().width);
            marginX = -(menu.getLayoutParams().width);
            menuOpen = false;
        }
        slideMenuIn(animateFromX, animateToX, marginX);
    }
}
