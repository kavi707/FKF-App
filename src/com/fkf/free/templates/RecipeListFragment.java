package com.fkf.free.templates;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.fkf.free.R;
import com.fkf.free.adapter.RecipeListAdapter;
import com.fkf.free.database.LocalDatabaseSQLiteOpenHelper;
import com.fkf.free.database.Recipe;
import com.fkf.free.database.RecipeCategory;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

import java.util.ArrayList;

/**
 * Created by kavi on 6/20/14.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class RecipeListFragment extends Fragment {

    private View myFragmentView;
    private AdView googleAdView;

    private TextView yummyCategoryNameTextView;
    private ListView recipeItemList;

    private ProgressDialog progress;
    private Handler handler;

    private Context context;
    private RecipeCategory recipeCategory;
    private RecipeListAdapter recipeListAdapter;
    private Recipe itemContent;
    private ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
    private String titleHeading = "";

    private LocalDatabaseSQLiteOpenHelper localDatabaseSQLiteOpenHelper;

    public RecipeListFragment(RecipeCategory recipeCategory, Context context) {
        if (recipeCategory != null)
            this.recipeCategory = recipeCategory;
        this.localDatabaseSQLiteOpenHelper = new LocalDatabaseSQLiteOpenHelper(context);
        this.context = context;

        Log.d(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>...", String.valueOf(this.recipeCategory.getCategoryId()));
    }

    public RecipeListFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myFragmentView = inflater.inflate(R.layout.recipe_list_layout, container, false);

        setUpViews();

        return myFragmentView;
    }

    private void setUpViews() {

        yummyCategoryNameTextView = (TextView) myFragmentView.findViewById(R.id.yummyCategoryNameTextView);

        googleAdView = (AdView) myFragmentView.findViewById(R.id.adRecipeList);

        //yummy list view for selected yummy category
        recipeItemList = (ListView) myFragmentView.findViewById(R.id.recipeItemList);

        //set Google Ads bannerx
        AdRequest re = new AdRequest();
        re.setGender(AdRequest.Gender.FEMALE);
        googleAdView.loadAd(re);

        recipeList = localDatabaseSQLiteOpenHelper.getRecipesFromCategoryId(recipeCategory.getCategoryId());
        recipeListAdapter = new RecipeListAdapter(recipeList, this.context);
        recipeItemList.setAdapter(recipeListAdapter);
        yummyCategoryNameTextView.setText("  " + recipeCategory.getCategoryName());

        recipeItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Recipe selectedItemContent = (Recipe) (recipeItemList.getItemAtPosition(i));
                if (selectedItemContent != null) {

                    itemContent = selectedItemContent;

                    progress = ProgressDialog.show(context, "Loading", "Loading the " + itemContent.getName() + " details. Please wait...");
                    handler = new android.os.Handler(context.getMainLooper());

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Intent singleRecipeIntent = new Intent(context, SingleRecipeActivity.class);
                            singleRecipeIntent.putExtra("SELECTED_RECIPE_ID", Integer.parseInt(itemContent.getProductId()));
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
            }
        });
    }
}
