package com.fkf.resturent.templates;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.*;

import com.fkf.resturent.R;
import com.fkf.resturent.adapter.RecipeListAdapter;
import com.fkf.resturent.database.LocalDatabaseSQLiteOpenHelper;
import com.fkf.resturent.database.Recipe;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by kavi on 6/22/13.
 * Hold the main menu ui for logged user and un-logged user.
 * recipes_layout.xml Activity class
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class RecipesActivity extends Activity implements View.OnClickListener{

    ImageButton menuButton, logoutButton, searchRecipeButton;
    LinearLayout content, menu, firstPopularYummyLinear, secondPopularYummyLinear, thirdPopularYummyLinear;
    ListView menuItemList, recipeItemList;
    ScrollView latestAndPopularScrollBar;
    HorizontalScrollView horizontalScroll;
    TextView loggedUserTextView, yummyCategoryNameTextView;
    ImageView profileImageView;
    EditText searchRecipeEditText;

    //yummy image buttons in the horizontal scroll
    ImageButton firstYummyImageButton;
    ImageButton secondYummyImageButton;
    ImageButton thirdYummyImageButton;
    ImageButton forthYummyImageButton;
    ImageButton fifthYummyImageButton;

    //popular yummy imageViews
    ImageView firstPopularYummyImageView;
    ImageView secondPopularYummyImageView;
    ImageView thirdPopularYummyImageView;

    private ArrayAdapter<String> menuItemListAdapter;
    private RecipeListAdapter recipeListAdapter;
    private ArrayList<Recipe> recipeList;
    private Context context = this;

    LinearLayout.LayoutParams contentParams;
    TranslateAnimation slide;
    int marginX, animateFromX, animateToX = 0;
    boolean menuOpen = false;

    private LocalDatabaseSQLiteOpenHelper localDatabaseSQLiteOpenHelper = new LocalDatabaseSQLiteOpenHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipes_layout);

        setUpViews();
    }

    private void setUpViews() {
        ArrayList<String> menuItems;

        horizontalScroll = (HorizontalScrollView) findViewById(R.id.horizontalScroll);
        latestAndPopularScrollBar = (ScrollView) findViewById(R.id.latestAndPopularScrollBar);

        firstYummyImageButton = (ImageButton) findViewById(R.id.firstYummyImageButton);
        secondYummyImageButton = (ImageButton) findViewById(R.id.secondYummyImageButton);
        thirdYummyImageButton = (ImageButton) findViewById(R.id.thirdYummyImageButton);
        forthYummyImageButton = (ImageButton) findViewById(R.id.forthYummyImageButton);
        fifthYummyImageButton = (ImageButton) findViewById(R.id.fifthYummyImageButton);


        //Embedded images to yummys buttons
        File firstImageFile = new File("/sdcard/fauzias/latest_yummys/icon_1.jpg");
        Bitmap firstBitmap = BitmapFactory.decodeFile(firstImageFile.getAbsolutePath());
        firstYummyImageButton.setImageBitmap(firstBitmap);

        File secondImageFile = new File("/sdcard/fauzias/latest_yummys/icon_2.png");
        Bitmap secondBitmap = BitmapFactory.decodeFile(secondImageFile.getAbsolutePath());
        secondYummyImageButton.setImageBitmap(secondBitmap);

        File thirdImageFile = new File("/sdcard/fauzias/latest_yummys/icon_3.png");
        Bitmap thirdBitmap = BitmapFactory.decodeFile(thirdImageFile.getAbsolutePath());
        thirdYummyImageButton.setImageBitmap(thirdBitmap);

        File forthImageFile = new File("/sdcard/fauzias/latest_yummys/icon_4.png");
        Bitmap forthBitmap = BitmapFactory.decodeFile(forthImageFile.getAbsolutePath());
        forthYummyImageButton.setImageBitmap(forthBitmap);

        File fifthImageFile = new File("/sdcard/fauzias/latest_yummys/icon_5.png");
        Bitmap fifthBitmap = BitmapFactory.decodeFile(fifthImageFile.getAbsolutePath());
        fifthYummyImageButton.setImageBitmap(fifthBitmap);


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


        //popular yummys linear layout click events
        //first popular yummy
        firstPopularYummyImageView = (ImageView) findViewById(R.id.firstPopularRecipeImageView);
        File firstPopularImageFile = new File("/sdcard/fauzias/popular_yummys/icon_1.jpg");
        Bitmap firstPopularBitmap = BitmapFactory.decodeFile(firstPopularImageFile.getAbsolutePath());
        firstPopularYummyImageView.setImageBitmap(firstPopularBitmap);

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
        Bitmap secondPopularBitmap = BitmapFactory.decodeFile(secondPopularImageFile.getAbsolutePath());
        secondPopularYummyImageView.setImageBitmap(secondPopularBitmap);

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
        Bitmap thirdPopularBitmap = BitmapFactory.decodeFile(thirdPopularImageFile.getAbsolutePath());
        thirdPopularYummyImageView.setImageBitmap(thirdPopularBitmap);

        thirdPopularYummyLinear = (LinearLayout) findViewById(R.id.thirdPopularYummyLinear);
        thirdPopularYummyLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent singleRecipeIntent = new Intent(RecipesActivity.this, SingleRecipeActivity.class);
                singleRecipeIntent.putExtra("SELECTED_RECIPE_ID", -8);
                startActivity(singleRecipeIntent);
            }
        });



        //add items to category menu list
        menuItems = localDatabaseSQLiteOpenHelper.getAllCategories();

        menu = (LinearLayout)findViewById(R.id.menu);
        menuItemList = (ListView) findViewById(R.id.menu_item_list);
        menuItemListAdapter = new ArrayAdapter<String>(this, R.layout.menu_button_view, menuItems);
        menuItemList.setAdapter(menuItemListAdapter);


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

        //selected yummy category list view
        //category list on item click
        menuItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String itemContent = (String) (menuItemList.getItemAtPosition(i));

                ViewGroup.LayoutParams scrollParams = latestAndPopularScrollBar.getLayoutParams();

                if(itemContent.equals("Latest Yummys")){
                    scrollParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    latestAndPopularScrollBar.setLayoutParams(scrollParams);
                } else {
                    scrollParams.height = 0;
                    latestAndPopularScrollBar.setLayoutParams(scrollParams);

                    if(itemContent.equals("My Favorites")) {
                        yummyCategoryNameTextView.setText("My Favorite yummys");
                        recipeList = localDatabaseSQLiteOpenHelper.getRecipesFromCategoryId(1); //need to get my favorite yummys from api
                    } else {
                        String[] itemContentArray = itemContent.split(":");
                        yummyCategoryNameTextView.setText(itemContentArray[1]);
                        recipeList = localDatabaseSQLiteOpenHelper.getRecipesFromCategoryId(Integer.parseInt(itemContentArray[0]));
                    }

                    recipeListAdapter = new RecipeListAdapter(recipeList, context);
                    recipeItemList.setAdapter(recipeListAdapter);
                }
            }
        });

        recipeItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Recipe itemContent = (Recipe) (recipeItemList.getItemAtPosition(i));

                if(itemContent != null) {
                    Intent singleRecipeIntent = new Intent(RecipesActivity.this, SingleRecipeActivity.class);
                    singleRecipeIntent.putExtra("SELECTED_RECIPE_ID", itemContent.getId());
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
