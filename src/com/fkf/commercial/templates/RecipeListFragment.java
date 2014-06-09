package com.fkf.commercial.templates;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.fkf.commercial.R;
import com.fkf.commercial.adapter.RecipeListAdapter;
import com.fkf.commercial.database.LocalDatabaseSQLiteOpenHelper;
import com.fkf.commercial.database.Recipe;
import com.fkf.commercial.database.RecipeCategory;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by kavi on 6/7/14.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class RecipeListFragment extends Fragment {

    private View myFragmentView;
    private TextView yummyCategoryNameTextView;
    private ListView recipeItemList;

    private Context context;
    private RecipeCategory recipeCategory;
    private RecipeListAdapter recipeListAdapter;
    private ArrayList<Recipe> recipeList = new ArrayList<Recipe>();

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
        yummyCategoryNameTextView.setText("  " + recipeCategory.getCategoryName());

        recipeItemList = (ListView) myFragmentView.findViewById(R.id.recipeItemList);
        recipeList = localDatabaseSQLiteOpenHelper.getRecipesFromCategoryId(recipeCategory.getCategoryId());
        recipeListAdapter = new RecipeListAdapter(recipeList, this.context);
        recipeItemList.setAdapter(recipeListAdapter);
    }
}
