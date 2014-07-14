package com.fkf.commercial.templates;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fkf.commercial.R;
import com.fkf.commercial.WelcomeActivity;
import com.fkf.commercial.adapter.RecipeListAdapter;
import com.fkf.commercial.database.LocalDatabaseSQLiteOpenHelper;
import com.fkf.commercial.database.PopularOrLatestRecipe;
import com.fkf.commercial.database.Recipe;
import com.fkf.commercial.database.dbprovider.ContentProviderAccessor;
import com.fkf.commercial.services.ActivityUserPermissionServices;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kavi on 5/5/14.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class HomeFragment extends Fragment {

    private View myFragmentView;

    ImageButton searchRecipeButton;
    EditText searchRecipeEditText;

    LinearLayout firstPopularYummyLinear;
    LinearLayout secondPopularYummyLinear;
    LinearLayout thirdPopularYummyLinear;
    LinearLayout forthPopularYummyLinear;
    LinearLayout fifthPopularYummyLinear;
    LinearLayout sixthPopularYummyLinear;

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

    TextView latestTitleTextView;
    TextView popularTitleTextView;
    LinearLayout.LayoutParams popularTitleParams;
    RelativeLayout.LayoutParams latestTitleParams;

    LinearLayout.LayoutParams contentParams;

    private ProgressDialog progress;
    private Handler handler;

    private Context context;
    private LocalDatabaseSQLiteOpenHelper localDatabaseSQLiteOpenHelper;
    private ContentProviderAccessor contentProviderAccessor = new ContentProviderAccessor();
    private ActivityUserPermissionServices activityUserPermissionServices = new ActivityUserPermissionServices();
    private ArrayList<Recipe> recipeList;
    private int deviceWidth;

    public HomeFragment(){}

    public HomeFragment(Context context){
        this.localDatabaseSQLiteOpenHelper = new LocalDatabaseSQLiteOpenHelper(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myFragmentView = inflater.inflate(R.layout.home_layout, container, false);

        setUpViews();

        return myFragmentView;
    }

    private void setUpViews() {

        latestTitleTextView = (TextView) myFragmentView.findViewById(R.id.latestTitleText);
        popularTitleTextView = (TextView) myFragmentView.findViewById(R.id.popularTitleText);

        firstYummyImageButton = (ImageButton) myFragmentView.findViewById(R.id.firstYummyImageButton);
        secondYummyImageButton = (ImageButton) myFragmentView.findViewById(R.id.secondYummyImageButton);
        thirdYummyImageButton = (ImageButton) myFragmentView.findViewById(R.id.thirdYummyImageButton);
        forthYummyImageButton = (ImageButton) myFragmentView.findViewById(R.id.forthYummyImageButton);
        fifthYummyImageButton = (ImageButton) myFragmentView.findViewById(R.id.fifthYummyImageButton);

        firstYummyTextView = (TextView) myFragmentView.findViewById(R.id.firstYummyTextView);
        secondYummyTextView = (TextView) myFragmentView.findViewById(R.id.secondYummyTextView);
        thirdYummyTextView = (TextView) myFragmentView.findViewById(R.id.thirdYummyTextView);
        forthYummyTextView = (TextView) myFragmentView.findViewById(R.id.forthYummyTextView);
        fifthYummyTextView = (TextView) myFragmentView.findViewById(R.id.fifthYummyTextView);

        firstPopularRecipeNameTextView = (TextView) myFragmentView.findViewById(R.id.firstPopularRecipeNameTextView);
        secondPopularRecipeNameTextView = (TextView) myFragmentView.findViewById(R.id.secondPopularRecipeNameTextView);
        thirdPopularRecipeNameTextView = (TextView) myFragmentView.findViewById(R.id.thirdPopularRecipeNameTextView);
        forthPopularRecipeNameTextView = (TextView) myFragmentView.findViewById(R.id.forthPopularRecipeNameTextView);
        fifthPopularRecipeNameTextView = (TextView) myFragmentView.findViewById(R.id.fifthPopularRecipeNameTextView);
        sixthPopularRecipeNameTextView = (TextView) myFragmentView.findViewById(R.id.sixthPopularRecipeNameTextView);

        firstPopularRecipeDescTextView = (TextView) myFragmentView.findViewById(R.id.firstPopularRecipeDescriptionTextView);
        secondPopularRecipeDescTextView = (TextView) myFragmentView.findViewById(R.id.secondPopularRecipeDescriptionTextView);
        thirdPopularRecipeDescTextView = (TextView) myFragmentView.findViewById(R.id.thirdPopularRecipeDescriptionTextView);
        forthPopularRecipeDescTextView = (TextView) myFragmentView.findViewById(R.id.forthPopularRecipeDescriptionTextView);
        fifthPopularRecipeDescTextView = (TextView) myFragmentView.findViewById(R.id.fifthPopularRecipeDescriptionTextView);
        sixthPopularRecipeDescTextView = (TextView) myFragmentView.findViewById(R.id.sixthPopularRecipeDescriptionTextView);

        firstPopularRecipeRatingBar = (RatingBar) myFragmentView.findViewById(R.id.firstPopularRecipeRatingBar);
        secondPopularRecipeRatingBar = (RatingBar) myFragmentView.findViewById(R.id.secondPopularRecipeRatingBar);
        thirdPopularRecipeRatingBar = (RatingBar) myFragmentView.findViewById(R.id.thirdPopularRecipeRatingBar);
        forthPopularRecipeRatingBar = (RatingBar) myFragmentView.findViewById(R.id.forthPopularRecipeRatingBar);
        fifthPopularRecipeRatingBar = (RatingBar) myFragmentView.findViewById(R.id.fifthPopularRecipeRatingBar);
        sixthPopularRecipeRatingBar = (RatingBar) myFragmentView.findViewById(R.id.sixthPopularRecipeRatingBar);

        String[] latestRecipeNames = new String[10];
        try {
            //List<PopularOrLatestRecipe> latestRecipes = localDatabaseSQLiteOpenHelper.getAllLatestRecipes();
            List<PopularOrLatestRecipe> latestRecipes = contentProviderAccessor.getAllLatestRecipes(context);
            if (latestRecipes != null) {
                for (PopularOrLatestRecipe latestRecipe : latestRecipes) {
                    latestRecipeNames[latestRecipe.getIndex()] = latestRecipe.getRecipeName();
                }
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }

        /********************************************/
        /********* Latest yummys buttons ************/
        /********************************************/

        try {
            deviceWidth = WelcomeActivity.widthAndHeight.get("width");
        } catch (NullPointerException ex) {
            Log.d("NullPointerException","Null pointer at recipes for category Id");
            int getDeviceSize = activityUserPermissionServices.getDeviceScreenSize(context);
            switch(getDeviceSize) {
                case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                    deviceWidth = 1600;
                    break;
                case Configuration.SCREENLAYOUT_SIZE_LARGE:
                    deviceWidth = 1080;
                    break;
                case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                    deviceWidth = 720;
                    break;
                case Configuration.SCREENLAYOUT_SIZE_SMALL:
                    deviceWidth = 480;
                    break;
            }
            ex.printStackTrace();
        }

        if(deviceWidth <= 480) {

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
        float getHeight;
        if (deviceWidth == 480) {
            getHeight = (480 * deviceWidth)/640;
        } else if (deviceWidth == 720){
            getHeight = (540 * deviceWidth)/720;
        } else {
            getHeight = (810 * deviceWidth)/1080;
        }

        try {
            File firstImageFile = new File(context.getFilesDir() + "/fauzias/latest_yummys/icon_1");
            if (firstImageFile.exists()) {
                Bitmap firstBitmap = BitmapFactory.decodeFile(firstImageFile.getAbsolutePath());
                firstYummyImageButton.setImageBitmap(firstBitmap);
                contentParams = (LinearLayout.LayoutParams) firstYummyImageButton.getLayoutParams();
                contentParams.height = Math.round(getHeight);
                contentParams.width = deviceWidth;
                firstYummyImageButton.setLayoutParams(contentParams);
                firstYummyTextView.setText("  " + latestRecipeNames[1]);
            } else {
                firstYummyImageButton.setImageResource(R.drawable.fkf_xs);
                firstYummyTextView.setText("  " + latestRecipeNames[1]);
            }
        } catch (NullPointerException ex) {
            Log.d("HomeFragment/NullPointerException:", "First Latest Recipe");
            setDefaultImageToLatestRecipe(firstYummyImageButton, firstYummyTextView);
            ex.printStackTrace();
        }

        try {
            File secondImageFile = new File(context.getFilesDir() + "/fauzias/latest_yummys/icon_2");
            if (secondImageFile.exists()) {
                Bitmap secondBitmap = BitmapFactory.decodeFile(secondImageFile.getAbsolutePath());
                secondYummyImageButton.setImageBitmap(secondBitmap);
                contentParams = (LinearLayout.LayoutParams) secondYummyImageButton.getLayoutParams();
                contentParams.height = Math.round(getHeight);
                contentParams.width = deviceWidth;
                secondYummyImageButton.setLayoutParams(contentParams);
                secondYummyTextView.setText("  " + latestRecipeNames[2]);
            } else {
                secondYummyImageButton.setImageResource(R.drawable.fkf_xs);
                secondYummyTextView.setText("  " + latestRecipeNames[2]);
            }
        } catch (NullPointerException ex) {
            Log.d("HomeFragment/NullPointerException:", "Second Latest Recipe");
            setDefaultImageToLatestRecipe(secondYummyImageButton, secondYummyTextView);
            ex.printStackTrace();
        }

        try {
            File thirdImageFile = new File(context.getFilesDir() + "/fauzias/latest_yummys/icon_3");
            if (thirdImageFile.exists()) {
                Bitmap thirdBitmap = BitmapFactory.decodeFile(thirdImageFile.getAbsolutePath());
                thirdYummyImageButton.setImageBitmap(thirdBitmap);
                contentParams = (LinearLayout.LayoutParams) thirdYummyImageButton.getLayoutParams();
                contentParams.height = Math.round(getHeight);
                contentParams.width = deviceWidth;
                thirdYummyImageButton.setLayoutParams(contentParams);
                thirdYummyTextView.setText(latestRecipeNames[3]);
            } else {
                thirdYummyImageButton.setImageResource(R.drawable.fkf_xs);
                thirdYummyTextView.setText(latestRecipeNames[3]);
            }
        } catch (NullPointerException ex) {
            Log.d("HomeFragment/NullPointerException:", "Third Latest Recipe");
            setDefaultImageToLatestRecipe(thirdYummyImageButton, thirdYummyTextView);
            ex.printStackTrace();
        }

        try {
            File forthImageFile = new File(context.getFilesDir() + "/fauzias/latest_yummys/icon_4");
            if (forthImageFile.exists()) {
                Bitmap forthBitmap = BitmapFactory.decodeFile(forthImageFile.getAbsolutePath());
                forthYummyImageButton.setImageBitmap(forthBitmap);
                contentParams = (LinearLayout.LayoutParams) forthYummyImageButton.getLayoutParams();
                contentParams.height = Math.round(getHeight);
                contentParams.width = deviceWidth;
                forthYummyImageButton.setLayoutParams(contentParams);
                forthYummyTextView.setText("  " + latestRecipeNames[4]);
            } else {
                forthYummyImageButton.setImageResource(R.drawable.fkf_xs);
                forthYummyTextView.setText("  " + latestRecipeNames[4]);
            }
        } catch (NullPointerException ex) {
            Log.d("HomeFragment/NullPointerException:", "Forth Latest Recipe");
            setDefaultImageToLatestRecipe(forthYummyImageButton, forthYummyTextView);
            ex.printStackTrace();
        }

        try {
            File fifthImageFile = new File(context.getFilesDir() + "/fauzias/latest_yummys/icon_5");
            if (fifthImageFile.exists()) {
                Bitmap fifthBitmap = BitmapFactory.decodeFile(fifthImageFile.getAbsolutePath());
                fifthYummyImageButton.setImageBitmap(fifthBitmap);
                contentParams = (LinearLayout.LayoutParams) fifthYummyImageButton.getLayoutParams();
                contentParams.height = Math.round(getHeight);
                contentParams.width = deviceWidth;
                fifthYummyImageButton.setLayoutParams(contentParams);
                fifthYummyTextView.setText("  " + latestRecipeNames[5]);
            } else {
                fifthYummyImageButton.setImageResource(R.drawable.fkf_xs);
                fifthYummyTextView.setText("  " + latestRecipeNames[5]);
            }
        } catch (NullPointerException ex) {
            Log.d("HomeFragment/NullPointerException:", "Fifth Latest Recipe");
            setDefaultImageToLatestRecipe(fifthYummyImageButton, fifthYummyTextView);
            ex.printStackTrace();
        }

        //latest yummys image button click events
        firstYummyImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                progress = ProgressDialog.show(context, "Loading", "Loading the selected recipe details. Please wait...");
                handler = new android.os.Handler(context.getMainLooper());

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Intent singleRecipeIntent = new Intent(context, SingleRecipeActivity.class);
                        singleRecipeIntent.putExtra("SELECTED_RECIPE_ID", -1);
                        singleRecipeIntent.putExtra("OLD_RECIPE_ID", -1);
                        startActivity(singleRecipeIntent);

                        getActivity().runOnUiThread(new Runnable() {
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

                progress = ProgressDialog.show(context, "Loading", "Loading the selected recipe details. Please wait...");
                handler = new android.os.Handler(context.getMainLooper());

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Intent singleRecipeIntent = new Intent(context, SingleRecipeActivity.class);
                        singleRecipeIntent.putExtra("SELECTED_RECIPE_ID", -2);
                        singleRecipeIntent.putExtra("OLD_RECIPE_ID", -1);
                        startActivity(singleRecipeIntent);

                        getActivity().runOnUiThread(new Runnable() {
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

                progress = ProgressDialog.show(context, "Loading", "Loading the selected recipe details. Please wait...");
                handler = new android.os.Handler(context.getMainLooper());

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Intent singleRecipeIntent = new Intent(context, SingleRecipeActivity.class);
                        singleRecipeIntent.putExtra("SELECTED_RECIPE_ID", -3);
                        singleRecipeIntent.putExtra("OLD_RECIPE_ID", -1);
                        startActivity(singleRecipeIntent);

                        getActivity().runOnUiThread(new Runnable() {
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

                progress = ProgressDialog.show(context, "Loading", "Loading the selected recipe details. Please wait...");
                handler = new android.os.Handler(context.getMainLooper());

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Intent singleRecipeIntent = new Intent(context, SingleRecipeActivity.class);
                        singleRecipeIntent.putExtra("SELECTED_RECIPE_ID", -4);
                        singleRecipeIntent.putExtra("OLD_RECIPE_ID", -1);
                        startActivity(singleRecipeIntent);

                        getActivity().runOnUiThread(new Runnable() {
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

                progress = ProgressDialog.show(context, "Loading", "Loading the selected recipe details. Please wait...");
                handler = new android.os.Handler(context.getMainLooper());

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Intent singleRecipeIntent = new Intent(context, SingleRecipeActivity.class);
                        singleRecipeIntent.putExtra("SELECTED_RECIPE_ID", -5);
                        singleRecipeIntent.putExtra("OLD_RECIPE_ID", -1);
                        startActivity(singleRecipeIntent);

                        getActivity().runOnUiThread(new Runnable() {
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
        try {
            firstPopularYummyImageView = (ImageView) myFragmentView.findViewById(R.id.firstPopularRecipeImageView);
            File firstPopularImageFile = new File(context.getFilesDir() + "/fauzias/popular_yummys/icon_1");
            if (firstPopularImageFile.exists()) {
                Bitmap firstPopularBitmap = BitmapFactory.decodeFile(firstPopularImageFile.getAbsolutePath());
                firstPopularYummyImageView.setImageBitmap(firstPopularBitmap);
            } else {
                firstPopularYummyImageView.setImageResource(R.drawable.default_recipe_image_squre);
            }
        } catch (NullPointerException ex) {
            Log.d("HomeFragment/NullPointerException:", "First Popular Recipe");
            firstPopularYummyImageView.setImageResource(R.drawable.default_recipe_image_squre);
            ex.printStackTrace();
        }

        firstPopularYummyLinear = (LinearLayout) myFragmentView.findViewById(R.id.firstPopularYummyLinear);
        firstPopularYummyLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress = ProgressDialog.show(context, "Loading", "Loading the selected recipe details. Please wait...");
                handler = new android.os.Handler(context.getMainLooper());

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Intent singleRecipeIntent = new Intent(context, SingleRecipeActivity.class);
                        singleRecipeIntent.putExtra("SELECTED_RECIPE_ID", -6);
                        singleRecipeIntent.putExtra("OLD_RECIPE_ID", -1);
                        startActivity(singleRecipeIntent);

                        getActivity().runOnUiThread(new Runnable() {
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
        try {
            secondPopularYummyImageView = (ImageView) myFragmentView.findViewById(R.id.secondPopularRecipeImageView);
            File secondPopularImageFile = new File(context.getFilesDir() + "/fauzias/popular_yummys/icon_2");
            if (secondPopularImageFile.exists()) {
                Bitmap secondPopularBitmap = BitmapFactory.decodeFile(secondPopularImageFile.getAbsolutePath());
                secondPopularYummyImageView.setImageBitmap(secondPopularBitmap);
            } else {
                secondPopularYummyImageView.setImageResource(R.drawable.default_recipe_image_squre);
            }
        } catch (NullPointerException ex) {
            Log.d("HomeFragment/NullPointerException:", "Second Popular Recipe");
            secondPopularYummyImageView.setImageResource(R.drawable.default_recipe_image_squre);
            ex.printStackTrace();
        }

        secondPopularYummyLinear = (LinearLayout) myFragmentView.findViewById(R.id.secondPopularYummyLinear);
        secondPopularYummyLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress = ProgressDialog.show(context, "Loading", "Loading the selected recipe details. Please wait...");
                handler = new android.os.Handler(context.getMainLooper());

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Intent singleRecipeIntent = new Intent(context, SingleRecipeActivity.class);
                        singleRecipeIntent.putExtra("SELECTED_RECIPE_ID", -7);
                        singleRecipeIntent.putExtra("OLD_RECIPE_ID", -1);
                        startActivity(singleRecipeIntent);

                        getActivity().runOnUiThread(new Runnable() {
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
        try {
            thirdPopularYummyImageView = (ImageView) myFragmentView.findViewById(R.id.thirdPopularRecipeImageView);
            File thirdPopularImageFile = new File(context.getFilesDir() + "/fauzias/popular_yummys/icon_3");
            if (thirdPopularImageFile.exists()) {
                Bitmap thirdPopularBitmap = BitmapFactory.decodeFile(thirdPopularImageFile.getAbsolutePath());
                thirdPopularYummyImageView.setImageBitmap(thirdPopularBitmap);
            } else {
                thirdPopularYummyImageView.setImageResource(R.drawable.default_recipe_image_squre);
            }
        } catch (NullPointerException ex) {
            Log.d("HomeFragment/NullPointerException:", "Third Popular Recipe");
            thirdPopularYummyImageView.setImageResource(R.drawable.default_recipe_image_squre);
            ex.printStackTrace();
        }

        thirdPopularYummyLinear = (LinearLayout) myFragmentView.findViewById(R.id.thirdPopularYummyLinear);
        thirdPopularYummyLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress = ProgressDialog.show(context, "Loading", "Loading the selected recipe details. Please wait...");
                handler = new android.os.Handler(context.getMainLooper());

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Intent singleRecipeIntent = new Intent(context, SingleRecipeActivity.class);
                        singleRecipeIntent.putExtra("SELECTED_RECIPE_ID", -8);
                        singleRecipeIntent.putExtra("OLD_RECIPE_ID", -1);
                        startActivity(singleRecipeIntent);

                        getActivity().runOnUiThread(new Runnable() {
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
        try {
            forthPopularYummyImageView = (ImageView) myFragmentView.findViewById(R.id.forthPopularRecipeImageView);
            File forthPopularImageFile = new File(context.getFilesDir() + "/fauzias/popular_yummys/icon_4");
            if (forthPopularImageFile.exists()) {
                Bitmap forthPopularBitmap = BitmapFactory.decodeFile(forthPopularImageFile.getAbsolutePath());
                forthPopularYummyImageView.setImageBitmap(forthPopularBitmap);
            } else {
                forthPopularYummyImageView.setImageResource(R.drawable.default_recipe_image_squre);
            }
        } catch (NullPointerException ex) {
            Log.d("HomeFragment/NullPointerException:", "Forth Popular Recipe");
            forthPopularYummyImageView.setImageResource(R.drawable.default_recipe_image_squre);
            ex.printStackTrace();
        }

        forthPopularYummyLinear = (LinearLayout) myFragmentView.findViewById(R.id.forthPopularYummyLinear);
        forthPopularYummyLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress = ProgressDialog.show(context, "Loading", "Loading the selected recipe details. Please wait...");
                handler = new android.os.Handler(context.getMainLooper());

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Intent singleRecipeIntent = new Intent(context, SingleRecipeActivity.class);
                        singleRecipeIntent.putExtra("SELECTED_RECIPE_ID", -9);
                        singleRecipeIntent.putExtra("OLD_RECIPE_ID", -1);
                        startActivity(singleRecipeIntent);

                        getActivity().runOnUiThread(new Runnable() {
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
        try {
            fifthPopularYummyImageView = (ImageView) myFragmentView.findViewById(R.id.fifthPopularRecipeImageView);
            File fifthPopularImageFile = new File(context.getFilesDir() + "/fauzias/popular_yummys/icon_5");
            if (fifthPopularImageFile.exists()) {
                Bitmap fifthPopularBitmap = BitmapFactory.decodeFile(fifthPopularImageFile.getAbsolutePath());
                fifthPopularYummyImageView.setImageBitmap(fifthPopularBitmap);
            } else {
                fifthPopularYummyImageView.setImageResource(R.drawable.default_recipe_image_squre);
            }
        } catch (NullPointerException ex) {
            Log.d("HomeFragment/NullPointerException:", "Fifth Popular Recipe");
            fifthPopularYummyImageView.setImageResource(R.drawable.default_recipe_image_squre);
            ex.printStackTrace();
        }

        fifthPopularYummyLinear = (LinearLayout) myFragmentView.findViewById(R.id.fifthPopularYummyLinear);
        fifthPopularYummyLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress = ProgressDialog.show(context, "Loading", "Loading the selected recipe details. Please wait...");
                handler = new android.os.Handler(context.getMainLooper());

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Intent singleRecipeIntent = new Intent(context, SingleRecipeActivity.class);
                        singleRecipeIntent.putExtra("SELECTED_RECIPE_ID", -10);
                        singleRecipeIntent.putExtra("OLD_RECIPE_ID", -1);
                        startActivity(singleRecipeIntent);

                        getActivity().runOnUiThread(new Runnable() {
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
        try {
            sixthPopularYummyImageView = (ImageView) myFragmentView.findViewById(R.id.sixthPopularRecipeImageView);
            File sixthPopularImageFile = new File(context.getFilesDir() + "/fauzias/popular_yummys/icon_6");
            if (sixthPopularImageFile.exists()) {
                Bitmap sixthPopularBitmap = BitmapFactory.decodeFile(sixthPopularImageFile.getAbsolutePath());
                sixthPopularYummyImageView.setImageBitmap(sixthPopularBitmap);
            } else {
                sixthPopularYummyImageView.setImageResource(R.drawable.default_recipe_image_squre);
            }
        } catch (NullPointerException ex) {
            Log.d("HomeFragment/NullPointerException:", "Sixth Popular Recipe");
            sixthPopularYummyImageView.setImageResource(R.drawable.default_recipe_image_squre);
            ex.printStackTrace();
        }

        sixthPopularYummyLinear = (LinearLayout) myFragmentView.findViewById(R.id.sixthPopularYummyLinear);
        sixthPopularYummyLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress = ProgressDialog.show(context, "Loading", "Loading the selected recipe details. Please wait...");
                handler = new android.os.Handler(context.getMainLooper());

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Intent singleRecipeIntent = new Intent(context, SingleRecipeActivity.class);
                        singleRecipeIntent.putExtra("SELECTED_RECIPE_ID", -11);
                        singleRecipeIntent.putExtra("OLD_RECIPE_ID", -1);
                        startActivity(singleRecipeIntent);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.dismiss();
                            }
                        });
                    }
                });
            }
        });

        //List<PopularOrLatestRecipe> allPopularRecipeList = localDatabaseSQLiteOpenHelper.getAllPopularRecipes();
        List<PopularOrLatestRecipe> allPopularRecipeList = contentProviderAccessor.getAllPopularRecipes(context);
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

        //recipes search from given name
        searchRecipeButton = (ImageButton) myFragmentView.findViewById(R.id.searchRecipeImageButton);
        searchRecipeEditText = (EditText) myFragmentView.findViewById(R.id.searchRecipeEditText);
        if (deviceWidth <= 480) {
            searchRecipeEditText.setTextSize(14);
        }
        searchRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchKey = searchRecipeEditText.getText().toString();

                if(searchKey.equals(null) || !searchKey.equals("")) {
                    recipeList = localDatabaseSQLiteOpenHelper.searchRecipesFromGivenRecipeName(searchKey);

                    Fragment fragment = new RecipeListFragment(recipeList, context, "   Search Results");

                    if (fragment != null) {
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_container, fragment).commit();
                    } else {
                        // error in creating fragment
                        Log.e("MainActivity", "Error in creating fragment");
                    }
                } else {
                    Toast.makeText(context,
                            "Please enter key word for search", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setDefaultImageToLatestRecipe(ImageButton latestRecipeImageButton, TextView latestRecipeTextView) {
        if (deviceWidth <= 480) {
            latestRecipeImageButton.setImageResource(R.drawable.fkf_xs);
            latestRecipeTextView.setText("  Recipe Name");
        } else if (deviceWidth <= 720) {
            latestRecipeImageButton.setImageResource(R.drawable.fkf_m);
            latestRecipeTextView.setText("  Recipe Name");
        } else if (deviceWidth <= 1080) {
            latestRecipeImageButton.setImageResource(R.drawable.fkf_s);
            latestRecipeTextView.setText("  Recipe Name");
        }
    }
}
