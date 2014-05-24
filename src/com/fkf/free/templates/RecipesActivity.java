package com.fkf.free.templates;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.*;

import com.fkf.free.R;
import com.fkf.free.adapter.RecipeCategoryListAdapter;
import com.fkf.free.adapter.RecipeListAdapter;
import com.fkf.free.database.LocalDatabaseSQLiteOpenHelper;
import com.fkf.free.database.PopularOrLatestRecipe;
import com.fkf.free.database.Recipe;
import com.fkf.free.database.RecipeCategory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Handler;
import com.fkf.free.services.ActivityUserPermissionServices;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

/**
 * Created by kavi on 6/22/13.
 * Hold the main menu ui for logged user and un-logged user.
 * recipes_layout.xml Activity class
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class RecipesActivity extends Activity implements View.OnClickListener{

    ImageButton menuButton, fullVersionBtn;
    LinearLayout content, menu, loggedUserDetails;

    private ProgressDialog progress;
    private Handler handler;

    private AdView googleAdViewHome;

    LinearLayout firstPopularYummyLinear;
    LinearLayout secondPopularYummyLinear;
    LinearLayout thirdPopularYummyLinear;
    LinearLayout forthPopularYummyLinear;
    LinearLayout fifthPopularYummyLinear;
    LinearLayout sixthPopularYummyLinear;

    LinearLayout myFavoriteItemLinear;

    ListView menuItemList, recipeItemList;
    ScrollView latestAndPopularScrollBar;
    HorizontalScrollView horizontalScroll;
    TextView yummyCategoryNameTextView, homeTextView, myFavoriteTextView;

    TextView latestTitleTextView;
    TextView popularTitleTextView;
    LinearLayout.LayoutParams popularTitleParams;
    RelativeLayout.LayoutParams latestTitleParams;

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
    private AlertDialog messageBalloonAlertDialog;
    private Recipe itemContent;
    public static Map<String, Integer> layoutWidthAndHeight = new HashMap<String, Integer>();

    LinearLayout.LayoutParams contentParams, menuParams, userDetailsLayoutParams;
    TranslateAnimation slide;
    int marginX, animateFromX, animateToX = 0;
    boolean menuOpen = false;

    private LocalDatabaseSQLiteOpenHelper localDatabaseSQLiteOpenHelper = new LocalDatabaseSQLiteOpenHelper(this);
    private ActivityUserPermissionServices userPermissionServices = new ActivityUserPermissionServices();

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

        latestTitleTextView = (TextView) findViewById(R.id.latestTitleText);
        popularTitleTextView = (TextView) findViewById(R.id.popularTitleText);

        googleAdViewHome = (AdView) findViewById(R.id.adHome);

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


        //set Google Ads bannerx
        AdRequest re = new AdRequest();
        re.setGender(AdRequest.Gender.FEMALE);
        googleAdViewHome.loadAd(re);


        /********************************************/
        /********* Latest yummys buttons ************/
        /********************************************/

        //device layout width and height
        layoutWidthAndHeight = userPermissionServices.getDeviceWidthAndHeight(RecipesActivity.this);

        if(layoutWidthAndHeight.get("width") <= 480) {

            latestTitleParams = (RelativeLayout.LayoutParams)latestTitleTextView.getLayoutParams();
            latestTitleParams.width = 400;
//            latestTitleParams.height = ((layoutWidthAndHeight.get("height") * 300)/layoutWidthAndHeight.get("width"));
            latestTitleTextView.setLayoutParams(latestTitleParams);

            popularTitleParams = (LinearLayout.LayoutParams)popularTitleTextView.getLayoutParams();
            popularTitleParams.width = 400;
//            popularTitleParams.height = ((layoutWidthAndHeight.get("height") * 300)/layoutWidthAndHeight.get("width"));
            popularTitleTextView.setLayoutParams(popularTitleParams);

        }

        //Embedded images to yummys buttons
        int deviceWidth = layoutWidthAndHeight.get("width");
        float getHeight;
        if (deviceWidth == 480) {
            getHeight = (480 * layoutWidthAndHeight.get("width"))/640;
        } else if (deviceWidth == 720){
            getHeight = (540 * layoutWidthAndHeight.get("width"))/720;
        } else {
            getHeight = (810 * layoutWidthAndHeight.get("width"))/1080;
        }
//        File firstImageFile = new File("/sdcard/fauzias/latest_yummys/icon_1");
        File firstImageFile = new File(getFilesDir()+"/fauzias/latest_yummys/icon_1");
        if(firstImageFile.exists()) {
            Bitmap firstBitmap = BitmapFactory.decodeFile(firstImageFile.getAbsolutePath());
            firstYummyImageButton.setImageBitmap(firstBitmap);
            contentParams = (LinearLayout.LayoutParams)firstYummyImageButton.getLayoutParams();
            contentParams.height = Math.round(getHeight);
            contentParams.width = layoutWidthAndHeight.get("width");
            firstYummyImageButton.setLayoutParams(contentParams);
            firstYummyTextView.setText("  " + latestRecipeNames[1]);
        } else {
            firstYummyImageButton.setImageResource(R.drawable.fkf_xs);
            firstYummyTextView.setText("  " + latestRecipeNames[1]);
        }


//        File secondImageFile = new File("/sdcard/fauzias/latest_yummys/icon_2");
        File secondImageFile = new File(getFilesDir()+"/fauzias/latest_yummys/icon_2");
        if(secondImageFile.exists()) {
            Bitmap secondBitmap = BitmapFactory.decodeFile(secondImageFile.getAbsolutePath());
            secondYummyImageButton.setImageBitmap(secondBitmap);
            contentParams = (LinearLayout.LayoutParams)secondYummyImageButton.getLayoutParams();
            contentParams.height = Math.round(getHeight);
            contentParams.width = layoutWidthAndHeight.get("width");
            secondYummyImageButton.setLayoutParams(contentParams);
            secondYummyTextView.setText("  " + latestRecipeNames[2]);
        } else {
            secondYummyImageButton.setImageResource(R.drawable.fkf_xs);
            secondYummyTextView.setText("  " + latestRecipeNames[2]);
        }


//        File thirdImageFile = new File("/sdcard/fauzias/latest_yummys/icon_3");
        File thirdImageFile = new File(getFilesDir()+"/fauzias/latest_yummys/icon_3");
        if(thirdImageFile.exists()) {
            Bitmap thirdBitmap = BitmapFactory.decodeFile(thirdImageFile.getAbsolutePath());
            thirdYummyImageButton.setImageBitmap(thirdBitmap);
            contentParams = (LinearLayout.LayoutParams)thirdYummyImageButton.getLayoutParams();
            contentParams.height = Math.round(getHeight);
            contentParams.width = layoutWidthAndHeight.get("width");
            thirdYummyImageButton.setLayoutParams(contentParams);
            thirdYummyTextView.setText(latestRecipeNames[3]);
        } else {
            thirdYummyImageButton.setImageResource(R.drawable.fkf_xs);
            thirdYummyTextView.setText(latestRecipeNames[3]);
        }


//        File forthImageFile = new File("/sdcard/fauzias/latest_yummys/icon_4");
        File forthImageFile = new File(getFilesDir()+"/fauzias/latest_yummys/icon_4");
        if(forthImageFile.exists()) {
            Bitmap forthBitmap = BitmapFactory.decodeFile(forthImageFile.getAbsolutePath());
            forthYummyImageButton.setImageBitmap(forthBitmap);
            contentParams = (LinearLayout.LayoutParams)forthYummyImageButton.getLayoutParams();
            contentParams.height = Math.round(getHeight);
            contentParams.width = layoutWidthAndHeight.get("width");
            forthYummyImageButton.setLayoutParams(contentParams);
            forthYummyTextView.setText("  " + latestRecipeNames[4]);
        } else {
            forthYummyImageButton.setImageResource(R.drawable.fkf_xs);
            forthYummyTextView.setText("  " + latestRecipeNames[4]);
        }


//        File fifthImageFile = new File("/sdcard/fauzias/latest_yummys/icon_5");
        File fifthImageFile = new File(getFilesDir()+"/fauzias/latest_yummys/icon_5");
        if(fifthImageFile.exists()) {
            Bitmap fifthBitmap = BitmapFactory.decodeFile(fifthImageFile.getAbsolutePath());
            fifthYummyImageButton.setImageBitmap(fifthBitmap);
            contentParams = (LinearLayout.LayoutParams)fifthYummyImageButton.getLayoutParams();
            contentParams.height = Math.round(getHeight);
            contentParams.width = layoutWidthAndHeight.get("width");
            fifthYummyImageButton.setLayoutParams(contentParams);
            fifthYummyTextView.setText("  " + latestRecipeNames[5]);
        } else {
            fifthYummyImageButton.setImageResource(R.drawable.fkf_xs);
            fifthYummyTextView.setText("  " + latestRecipeNames[5]);
        }


        //latest yummys image button click events
        firstYummyImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                progress = ProgressDialog.show(RecipesActivity.this, "Loading", "Loading the selected recipe details. Please wait...");
                handler = new android.os.Handler(context.getMainLooper());

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Intent singleRecipeIntent = new Intent(RecipesActivity.this, SingleRecipeActivity.class);
                        singleRecipeIntent.putExtra("SELECTED_RECIPE_ID", -1);
                        singleRecipeIntent.putExtra("OLD_RECIPE_ID", -1);
                        startActivity(singleRecipeIntent);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.dismiss();
                            }
                        });
                    }
                });
            }
        });

        secondYummyImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress = ProgressDialog.show(RecipesActivity.this, "Loading", "Loading the selected recipe details. Please wait...");
                handler = new android.os.Handler(context.getMainLooper());

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Intent singleRecipeIntent = new Intent(RecipesActivity.this, SingleRecipeActivity.class);
                        singleRecipeIntent.putExtra("SELECTED_RECIPE_ID", -2);
                        singleRecipeIntent.putExtra("OLD_RECIPE_ID", -1);
                        startActivity(singleRecipeIntent);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.dismiss();
                            }
                        });
                    }
                });
            }
        });

        thirdYummyImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress = ProgressDialog.show(RecipesActivity.this, "Loading", "Loading the selected recipe details. Please wait...");
                handler = new android.os.Handler(context.getMainLooper());

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Intent singleRecipeIntent = new Intent(RecipesActivity.this, SingleRecipeActivity.class);
                        singleRecipeIntent.putExtra("SELECTED_RECIPE_ID", -3);
                        singleRecipeIntent.putExtra("OLD_RECIPE_ID", -1);
                        startActivity(singleRecipeIntent);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.dismiss();
                            }
                        });
                    }
                });
            }
        });

        forthYummyImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress = ProgressDialog.show(RecipesActivity.this, "Loading", "Loading the selected recipe details. Please wait...");
                handler = new android.os.Handler(context.getMainLooper());

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Intent singleRecipeIntent = new Intent(RecipesActivity.this, SingleRecipeActivity.class);
                        singleRecipeIntent.putExtra("SELECTED_RECIPE_ID", -4);
                        singleRecipeIntent.putExtra("OLD_RECIPE_ID", -1);
                        startActivity(singleRecipeIntent);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.dismiss();
                            }
                        });
                    }
                });
            }
        });

        fifthYummyImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress = ProgressDialog.show(RecipesActivity.this, "Loading", "Loading the selected recipe details. Please wait...");
                handler = new android.os.Handler(context.getMainLooper());

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Intent singleRecipeIntent = new Intent(RecipesActivity.this, SingleRecipeActivity.class);
                        singleRecipeIntent.putExtra("SELECTED_RECIPE_ID", -5);
                        singleRecipeIntent.putExtra("OLD_RECIPE_ID", -1);
                        startActivity(singleRecipeIntent);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.dismiss();
                            }
                        });
                    }
                });
            }
        });






        /***********************************************/
        /********* Popular yummys row views ************/
        /***********************************************/

        //popular yummys linear layout click events
        //first popular yummy
        firstPopularYummyImageView = (ImageView) findViewById(R.id.firstPopularRecipeImageView);
//        File firstPopularImageFile = new File("/sdcard/fauzias/popular_yummys/icon_1");
        File firstPopularImageFile = new File(getFilesDir()+"/fauzias/popular_yummys/icon_1");
        if(firstPopularImageFile.exists()) {
            Bitmap firstPopularBitmap = BitmapFactory.decodeFile(firstPopularImageFile.getAbsolutePath());
            firstPopularYummyImageView.setImageBitmap(firstPopularBitmap);
        } else {
            firstPopularYummyImageView.setImageResource(R.drawable.default_recipe_image_squre);
        }

        firstPopularYummyLinear = (LinearLayout) findViewById(R.id.firstPopularYummyLinear);
        firstPopularYummyLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress = ProgressDialog.show(RecipesActivity.this, "Loading", "Loading the selected recipe details. Please wait...");
                handler = new android.os.Handler(context.getMainLooper());

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Intent singleRecipeIntent = new Intent(RecipesActivity.this, SingleRecipeActivity.class);
                        singleRecipeIntent.putExtra("SELECTED_RECIPE_ID", -6);
                        singleRecipeIntent.putExtra("OLD_RECIPE_ID", -1);
                        startActivity(singleRecipeIntent);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.dismiss();
                            }
                        });
                    }
                });
            }
        });

        //second popular yummy
        secondPopularYummyImageView = (ImageView) findViewById(R.id.secondPopularRecipeImageView);
//        File secondPopularImageFile = new File("/sdcard/fauzias/popular_yummys/icon_2");
        File secondPopularImageFile = new File(getFilesDir()+"/fauzias/popular_yummys/icon_2");
        if(secondPopularImageFile.exists()) {
            Bitmap secondPopularBitmap = BitmapFactory.decodeFile(secondPopularImageFile.getAbsolutePath());
            secondPopularYummyImageView.setImageBitmap(secondPopularBitmap);
        } else {
            secondPopularYummyImageView.setImageResource(R.drawable.default_recipe_image_squre);
        }

        secondPopularYummyLinear = (LinearLayout) findViewById(R.id.secondPopularYummyLinear);
        secondPopularYummyLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress = ProgressDialog.show(RecipesActivity.this, "Loading", "Loading the selected recipe details. Please wait...");
                handler = new android.os.Handler(context.getMainLooper());

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Intent singleRecipeIntent = new Intent(RecipesActivity.this, SingleRecipeActivity.class);
                        singleRecipeIntent.putExtra("SELECTED_RECIPE_ID", -7);
                        singleRecipeIntent.putExtra("OLD_RECIPE_ID", -1);
                        startActivity(singleRecipeIntent);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.dismiss();
                            }
                        });
                    }
                });
            }
        });

        //third popular yummy
        thirdPopularYummyImageView = (ImageView) findViewById(R.id.thirdPopularRecipeImageView);
//        File thirdPopularImageFile = new File("/sdcard/fauzias/popular_yummys/icon_3");
        File thirdPopularImageFile = new File(getFilesDir()+"/fauzias/popular_yummys/icon_3");
        if(thirdPopularImageFile.exists()) {
            Bitmap thirdPopularBitmap = BitmapFactory.decodeFile(thirdPopularImageFile.getAbsolutePath());
            thirdPopularYummyImageView.setImageBitmap(thirdPopularBitmap);
        } else {
            thirdPopularYummyImageView.setImageResource(R.drawable.default_recipe_image_squre);
        }

        thirdPopularYummyLinear = (LinearLayout) findViewById(R.id.thirdPopularYummyLinear);
        thirdPopularYummyLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress = ProgressDialog.show(RecipesActivity.this, "Loading", "Loading the selected recipe details. Please wait...");
                handler = new android.os.Handler(context.getMainLooper());

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Intent singleRecipeIntent = new Intent(RecipesActivity.this, SingleRecipeActivity.class);
                        singleRecipeIntent.putExtra("SELECTED_RECIPE_ID", -8);
                        singleRecipeIntent.putExtra("OLD_RECIPE_ID", -1);
                        startActivity(singleRecipeIntent);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.dismiss();
                            }
                        });
                    }
                });
            }
        });

        //forth popular yummy
        forthPopularYummyImageView = (ImageView) findViewById(R.id.forthPopularRecipeImageView);
//        File forthPopularImageFile = new File("/sdcard/fauzias/popular_yummys/icon_4");
        File forthPopularImageFile = new File(getFilesDir()+"/fauzias/popular_yummys/icon_4");
        if(forthPopularImageFile.exists()) {
            Bitmap forthPopularBitmap = BitmapFactory.decodeFile(forthPopularImageFile.getAbsolutePath());
            forthPopularYummyImageView.setImageBitmap(forthPopularBitmap);
        } else {
            forthPopularYummyImageView.setImageResource(R.drawable.default_recipe_image_squre);
        }

        forthPopularYummyLinear = (LinearLayout) findViewById(R.id.forthPopularYummyLinear);
        forthPopularYummyLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress = ProgressDialog.show(RecipesActivity.this, "Loading", "Loading the selected recipe details. Please wait...");
                handler = new android.os.Handler(context.getMainLooper());

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Intent singleRecipeIntent = new Intent(RecipesActivity.this, SingleRecipeActivity.class);
                        singleRecipeIntent.putExtra("SELECTED_RECIPE_ID", -9);
                        singleRecipeIntent.putExtra("OLD_RECIPE_ID", -1);
                        startActivity(singleRecipeIntent);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.dismiss();
                            }
                        });
                    }
                });
            }
        });

        //fifth popular yummy
        fifthPopularYummyImageView = (ImageView) findViewById(R.id.fifthPopularRecipeImageView);
//        File fifthPopularImageFile = new File("/sdcard/fauzias/popular_yummys/icon_5");
        File fifthPopularImageFile = new File(getFilesDir()+"/fauzias/popular_yummys/icon_5");
        if(fifthPopularImageFile.exists()) {
            Bitmap fifthPopularBitmap = BitmapFactory.decodeFile(fifthPopularImageFile.getAbsolutePath());
            fifthPopularYummyImageView.setImageBitmap(fifthPopularBitmap);
        } else {
            fifthPopularYummyImageView.setImageResource(R.drawable.default_recipe_image_squre);
        }

        fifthPopularYummyLinear = (LinearLayout) findViewById(R.id.fifthPopularYummyLinear);
        fifthPopularYummyLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress = ProgressDialog.show(RecipesActivity.this, "Loading", "Loading the selected recipe details. Please wait...");
                handler = new android.os.Handler(context.getMainLooper());

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Intent singleRecipeIntent = new Intent(RecipesActivity.this, SingleRecipeActivity.class);
                        singleRecipeIntent.putExtra("SELECTED_RECIPE_ID", -10);
                        singleRecipeIntent.putExtra("OLD_RECIPE_ID", -1);
                        startActivity(singleRecipeIntent);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.dismiss();
                            }
                        });
                    }
                });
            }
        });

        //sixth popular yummy
        sixthPopularYummyImageView = (ImageView) findViewById(R.id.sixthPopularRecipeImageView);
//        File sixthPopularImageFile = new File("/sdcard/fauzias/popular_yummys/icon_6");
        File sixthPopularImageFile = new File(getFilesDir()+"/fauzias/popular_yummys/icon_6");
        if(sixthPopularImageFile.exists()) {
            Bitmap sixthPopularBitmap = BitmapFactory.decodeFile(sixthPopularImageFile.getAbsolutePath());
            sixthPopularYummyImageView.setImageBitmap(sixthPopularBitmap);
        } else {
            sixthPopularYummyImageView.setImageResource(R.drawable.default_recipe_image_squre);
        }

        sixthPopularYummyLinear = (LinearLayout) findViewById(R.id.sixthPopularYummyLinear);
        sixthPopularYummyLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress = ProgressDialog.show(RecipesActivity.this, "Loading", "Loading the selected recipe details. Please wait...");
                handler = new android.os.Handler(context.getMainLooper());

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Intent singleRecipeIntent = new Intent(RecipesActivity.this, SingleRecipeActivity.class);
                        singleRecipeIntent.putExtra("SELECTED_RECIPE_ID", -11);
                        singleRecipeIntent.putExtra("OLD_RECIPE_ID", -1);
                        startActivity(singleRecipeIntent);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.dismiss();
                            }
                        });
                    }
                });
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

                int shortDescLimit = 0;
                switch (popularOrLatestRecipe.getIndex()) {
                    case 1:
                        firstPopularRecipeNameTextView.setText(popularOrLatestRecipe.getRecipeName());
                        if(selectedRecipe != null) {
                            String description = selectedRecipe.getDescription();
                            String shortDesc = "";

                            if(deviceWidth < 500) {
                                shortDescLimit = 50;
                                firstPopularRecipeNameTextView.setTextSize(16);
                                firstPopularRecipeDescTextView.setTextSize(12);
                            } else {
                                shortDescLimit = 60;
                                firstPopularRecipeNameTextView.setTextSize(18);
                                firstPopularRecipeDescTextView.setTextSize(16);
                            }

                            if(description.length() > shortDescLimit) {
                                shortDesc = description.substring(0, shortDescLimit);
                            } else {
                                shortDesc = description;
                            }
                            firstPopularRecipeDescTextView.setText(shortDesc.
                                    replace("#","").replace("[","").replace("]","").replace("\"",""));
                            firstPopularRecipeRatingBar.setRating(selectedRecipe.getRatings());
                        }
                        break;
                    case 2:
                        secondPopularRecipeNameTextView.setText(popularOrLatestRecipe.getRecipeName());
                        if(selectedRecipe != null) {
                            String description = selectedRecipe.getDescription();
                            String shortDesc = "";

                            if(deviceWidth < 500) {
                                shortDescLimit = 50;
                                secondPopularRecipeNameTextView.setTextSize(16);
                                secondPopularRecipeDescTextView.setTextSize(12);
                            } else {
                                shortDescLimit = 60;
                                secondPopularRecipeNameTextView.setTextSize(18);
                                secondPopularRecipeDescTextView.setTextSize(16);
                            }

                            if(description.length() > shortDescLimit) {
                                shortDesc = description.substring(0, shortDescLimit);
                            } else {
                                shortDesc = description;
                            }
                            secondPopularRecipeDescTextView.setText(shortDesc.
                                    replace("#","").replace("[","").replace("]","").replace("\"",""));
                            secondPopularRecipeRatingBar.setRating(selectedRecipe.getRatings());
                        }
                        break;
                    case 3:
                        thirdPopularRecipeNameTextView.setText(popularOrLatestRecipe.getRecipeName());
                        if(selectedRecipe != null) {
                            String description = selectedRecipe.getDescription();
                            String shortDesc = "";

                            if(deviceWidth < 500) {
                                shortDescLimit = 50;
                                thirdPopularRecipeNameTextView.setTextSize(16);
                                thirdPopularRecipeDescTextView.setTextSize(12);
                            } else {
                                shortDescLimit = 60;
                                thirdPopularRecipeNameTextView.setTextSize(18);
                                thirdPopularRecipeDescTextView.setTextSize(16);
                            }

                            if(description.length() > shortDescLimit) {
                                shortDesc = description.substring(0, shortDescLimit);
                            } else {
                                shortDesc = description;
                            }
                            thirdPopularRecipeDescTextView.setText(shortDesc.
                                    replace("#","").replace("[","").replace("]","").replace("\"",""));
                            thirdPopularRecipeRatingBar.setRating(selectedRecipe.getRatings());
                        }
                        break;
                    case 4:
                        forthPopularRecipeNameTextView.setText(popularOrLatestRecipe.getRecipeName());
                        if(selectedRecipe != null) {
                            String description = selectedRecipe.getDescription();
                            String shortDesc = "";

                            if(deviceWidth < 500) {
                                shortDescLimit = 50;
                                forthPopularRecipeNameTextView.setTextSize(16);
                                forthPopularRecipeDescTextView.setTextSize(12);
                            } else {
                                shortDescLimit = 60;
                                forthPopularRecipeNameTextView.setTextSize(18);
                                forthPopularRecipeDescTextView.setTextSize(16);
                            }

                            if(description.length() > shortDescLimit) {
                                shortDesc = description.substring(0, shortDescLimit);
                            } else {
                                shortDesc = description;
                            }
                            forthPopularRecipeDescTextView.setText(shortDesc.
                                    replace("#","").replace("[","").replace("]","").replace("\"",""));
                            forthPopularRecipeRatingBar.setRating(selectedRecipe.getRatings());
                        }
                        break;
                    case 5:
                        fifthPopularRecipeNameTextView.setText(popularOrLatestRecipe.getRecipeName());
                        if(selectedRecipe != null) {
                            String description = selectedRecipe.getDescription();
                            String shortDesc = "";

                            if(deviceWidth < 500) {
                                shortDescLimit = 50;
                                fifthPopularRecipeNameTextView.setTextSize(16);
                                fifthPopularRecipeDescTextView.setTextSize(12);
                            } else {
                                shortDescLimit = 60;
                                fifthPopularRecipeNameTextView.setTextSize(18);
                                fifthPopularRecipeDescTextView.setTextSize(16);
                            }

                            if(description.length() > shortDescLimit) {
                                shortDesc = description.substring(0, shortDescLimit);
                            } else {
                                shortDesc = description;
                            }
                            fifthPopularRecipeDescTextView.setText(shortDesc.
                                    replace("#","").replace("[","").replace("]","").replace("\"",""));
                            fifthPopularRecipeRatingBar.setRating(selectedRecipe.getRatings());
                        }
                        break;
                    case 6:
                        sixthPopularRecipeNameTextView.setText(popularOrLatestRecipe.getRecipeName());
                        if(selectedRecipe != null) {
                            String description = selectedRecipe.getDescription();
                            String shortDesc = "";

                            if(deviceWidth < 500) {
                                shortDescLimit = 50;
                                sixthPopularRecipeNameTextView.setTextSize(16);
                                sixthPopularRecipeDescTextView.setTextSize(12);
                            } else {
                                shortDescLimit = 60;
                                sixthPopularRecipeNameTextView.setTextSize(18);
                                sixthPopularRecipeDescTextView.setTextSize(16);
                            }

                            if(description.length() > shortDescLimit) {
                                shortDesc = description.substring(0, shortDescLimit);
                            } else {
                                shortDesc = description;
                            }
                            sixthPopularRecipeDescTextView.setText(shortDesc.
                                    replace("#","").replace("[","").replace("]","").replace("\"",""));
                            sixthPopularRecipeRatingBar.setRating(selectedRecipe.getRatings());
                        }
                        break;
                }
            }
        }

        //Following items are always in menu list
        homeTextView = (TextView) findViewById(R.id.menuHomeTextView);
        myFavoriteTextView = (TextView) findViewById(R.id.menuMyFavoriteTextView);
        myFavoriteItemLinear = (LinearLayout) findViewById(R.id.menuMyFavoriteLinearLayout);

        //add items to category menu list
        menuItems = localDatabaseSQLiteOpenHelper.getAllCategories();

        menu = (LinearLayout)findViewById(R.id.menu);
        int layoutWidth = layoutWidthAndHeight.get("width");
        menuParams = (LinearLayout.LayoutParams)menu.getLayoutParams();
        menuParams.width = (layoutWidth/10)*8;
        menu.setLayoutParams(menuParams);

        loggedUserDetails = (LinearLayout) findViewById(R.id.userDetailsLayout);
        userDetailsLayoutParams = (LinearLayout.LayoutParams)loggedUserDetails.getLayoutParams();
        userDetailsLayoutParams.width = ((layoutWidth/10)*8) - ((layoutWidth/10)*3);
        loggedUserDetails.setLayoutParams(userDetailsLayoutParams);

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
        yummyCategoryNameTextView.setEnabled(false);
        yummyCategoryNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Status : ", "Category Title Clicked");
            }
        });

        //Home list item for get back to latest and popular yummys
        homeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup.LayoutParams scrollParams = latestAndPopularScrollBar.getLayoutParams();
                scrollParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                latestAndPopularScrollBar.setLayoutParams(scrollParams);

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

            myFavoriteItemLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

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
            ViewGroup.LayoutParams myFavoriteTextViewLayoutParams = myFavoriteItemLinear.getLayoutParams();
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
                    Recipe selectedItemContent = (Recipe) (recipeItemList.getItemAtPosition(i));
                    if (selectedItemContent != null) {

                        itemContent = selectedItemContent;

                        progress = ProgressDialog.show(RecipesActivity.this, "Loading", "Loading the " + itemContent.getName() + " details. Please wait...");
                        handler = new android.os.Handler(context.getMainLooper());

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Intent singleRecipeIntent = new Intent(RecipesActivity.this, SingleRecipeActivity.class);
                                singleRecipeIntent.putExtra("SELECTED_RECIPE_ID", Integer.parseInt(itemContent.getProductId()));
                                singleRecipeIntent.putExtra("OLD_RECIPE_ID", -1);
                                startActivity(singleRecipeIntent);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progress.dismiss();
                                    }
                                });
                            }
                        });
                    }
            }
        });

        //Get full version button
        fullVersionBtn = (ImageButton) findViewById(R.id.fullVersionBtn);
        fullVersionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String appPackageName = "com.fkf.commercial";

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException ex) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if(menuOpen) {
                slideMenuIn(0, -(menu.getLayoutParams().width), -(menu.getLayoutParams().width)); 	// Pass slide in paramters
                menuOpen = false;
                return true;
            } else {
                messageBalloonAlertDialog = new AlertDialog.Builder(context)
                        .setTitle(R.string.warning)
                        .setMessage("Do you want to close Fauzia's Kitchen Fun ?")
                        .setPositiveButton(R.string.yes, new AlertDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RecipesActivity.this.finish();
                            }
                        })
                        .setNegativeButton(R.string.no, new AlertDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                messageBalloonAlertDialog.cancel();
                            }
                        }).create();
                messageBalloonAlertDialog.show();
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
