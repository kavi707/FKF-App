package com.fkf.commercial.templates;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fkf.commercial.R;
import com.fkf.commercial.adapter.RecipeCategoryListAdapter;
import com.fkf.commercial.adapter.RecipeListAdapter;
import com.fkf.commercial.database.LocalDatabaseSQLiteOpenHelper;
import com.fkf.commercial.database.Recipe;
import com.fkf.commercial.database.RecipeCategory;
import com.fkf.commercial.services.image.loader.ImageLoader;
import com.fkf.commercial.views.RecipeCategoryListItem;
import com.fkf.commercial.views.RecipeListItem;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kavi on 6/6/14.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class RecipesMenuActivity extends Activity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    private Context context = this;

    private TextView menuHomeTextView;
    private TextView loggedOutButtonSeparatorTextView, loggedUserNameTextView, loggedUserTextView;
    private LinearLayout menuMyFavoriteLinearLayout;
    private AlertDialog messageBalloonAlertDialog;
    private ImageButton logoutButton;
    private ImageView profileImageView;

    private RecipeCategoryListAdapter recipeCategoryListAdapter;
    private LocalDatabaseSQLiteOpenHelper localDatabaseSQLiteOpenHelper = new LocalDatabaseSQLiteOpenHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        setUpViews(savedInstanceState);
    }

    private void setUpViews(Bundle savedInstanceState) {

        mTitle = mDrawerTitle = getTitle();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        menuHomeTextView = (TextView) findViewById(R.id.menuHomeTextView);
        menuMyFavoriteLinearLayout = (LinearLayout) findViewById(R.id.menuMyFavoriteLinearLayout);

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        //add items to category menu list
        ArrayList<RecipeCategory> menuItems = localDatabaseSQLiteOpenHelper.getAllCategories();
        recipeCategoryListAdapter = new RecipeCategoryListAdapter(menuItems, context);
        mDrawerList.setAdapter(recipeCategoryListAdapter);

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
//            displayView(0);
            Fragment fragment = new HomeFragment(context);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            setTitle(R.string.app_name);
        }

        menuHomeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new HomeFragment(context);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, fragment).commit();

                setTitle(R.string.app_name);
                mDrawerLayout.closeDrawers();
            }
        });

        if (LoginActivity.LOGGED_STATUS == 1) {

            menuMyFavoriteLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<Recipe> myFavorite;

                    List<String> favoriteIds = localDatabaseSQLiteOpenHelper.getLoggedUserFavoriteRecipeIds();
                    List<Recipe> tempRecipeList;
                    myFavorite = new ArrayList<Recipe>();
                    for (String favoriteId : favoriteIds) {
                        tempRecipeList = localDatabaseSQLiteOpenHelper.getRecipeFromRecipeProductId(favoriteId);
                        if (!tempRecipeList.isEmpty()) {
                            Recipe tempRecipe = tempRecipeList.get(0);
                            myFavorite.add(tempRecipe);
                        }
                    }

                    Fragment fragment = new RecipeListFragment(myFavorite, context, "  My Favourites");
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container, fragment).commit();

                    setTitle(R.string.app_name);
                    mDrawerLayout.closeDrawers();
                }
            });
        } else {
            ViewGroup.LayoutParams myFavoriteTextViewLayoutParams = menuMyFavoriteLinearLayout.getLayoutParams();
            myFavoriteTextViewLayoutParams.height = 0;
        }

        //logout button & profile image
        logoutButton = (ImageButton) findViewById(R.id.logoutButton);
        profileImageView = (ImageView) findViewById(R.id.userIconImageView);
        loggedOutButtonSeparatorTextView = (TextView) findViewById(R.id.logoutButtonSeparator);
        loggedUserNameTextView = (TextView) findViewById(R.id.userTextView);
        loggedUserTextView = (TextView) findViewById(R.id.userNameTextView);
        if(LoginActivity.LOGGED_STATUS == 0) {
            logoutButton.setVisibility(View.GONE);
            loggedOutButtonSeparatorTextView.setVisibility(View.GONE);
            profileImageView.setImageResource(R.drawable.login_icon);
            loggedUserTextView.setVisibility(View.GONE);

            LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lParams.setMargins(0, 20, 0, 0);
            loggedUserNameTextView.setText(" Login");
            loggedUserNameTextView.setTextSize(18);
            loggedUserNameTextView.setLayoutParams(lParams);

            loggedUserNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent loginIntent = new Intent(RecipesMenuActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                    finish();
                }
            });

            profileImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent loginIntent = new Intent(RecipesMenuActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                    finish();
                }
            });
        } else if (LoginActivity.LOGGED_STATUS == 1) {
            //Embedded images to profile pic image view
            int loader = R.drawable.default_recipe_image_squre;
            ImageLoader imageLoader = new ImageLoader(context);
            imageLoader.DisplayImage(LoginActivity.LOGGED_USER_PIC_URL, loader, profileImageView);

            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    messageBalloonAlertDialog = new AlertDialog.Builder(context)
                            .setTitle(R.string.warning)
                            .setMessage("Do you need to logout?")
                            .setPositiveButton(R.string.yes, new AlertDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    messageBalloonAlertDialog.show();
                                    LoginActivity.LOGGED_STATUS = 0;
                                    LoginActivity.LOGGED_USER = null;
                                    LoginActivity.LOGGED_USER_PASSWORD = null;
                                    localDatabaseSQLiteOpenHelper.deleteLoginDetails();
                                    Intent loginIntent = new Intent(RecipesMenuActivity.this, LoginActivity.class);
                                    startActivity(loginIntent);
                                    finish();
                                }
                            })
                            .setNegativeButton(R.string.no, new AlertDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    messageBalloonAlertDialog.cancel();
                                }
                            }).create();
                    messageBalloonAlertDialog.show();
                }
            });

            //Logged user textView
            loggedUserNameTextView.setText("Welcome " + LoginActivity.LOGGED_USER_NAME);
            loggedUserTextView.setText(LoginActivity.LOGGED_USER);
        }
    }

    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.welcome_single, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* *
     * Called when invalidateOptionsMenu() is triggered
     */
    /*@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }*/

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        RecipeCategory recipeCategory = (RecipeCategory) mDrawerList.getItemAtPosition(position);
        fragment = new RecipeListFragment(recipeCategory, context);

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(R.string.app_name);
            mDrawerLayout.closeDrawers();
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {

                messageBalloonAlertDialog = new AlertDialog.Builder(context)
                        .setTitle(R.string.warning)
                        .setMessage("Do you want to close Fauzia's Kitchen Fun ?")
                        .setPositiveButton(R.string.yes, new AlertDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RecipesMenuActivity.this.finish();
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
        return super.onKeyDown(keyCode, keyEvent);
    }
}
