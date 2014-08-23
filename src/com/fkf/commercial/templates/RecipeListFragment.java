package com.fkf.commercial.templates;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fkf.commercial.R;
import com.fkf.commercial.WelcomeActivity;
import com.fkf.commercial.adapter.RecipeListAdapter;
import com.fkf.commercial.database.LocalDatabaseSQLiteOpenHelper;
import com.fkf.commercial.database.Recipe;
import com.fkf.commercial.database.RecipeCategory;
import com.fkf.commercial.database.dbprovider.ContentProviderAccessor;

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
    private ImageButton searchRecipeListImageButton;
    private EditText searchRecipeListEditText;

    private ProgressDialog progress;
    private Handler handler;

    private Context context;
    private RecipeCategory recipeCategory;
    private RecipeListAdapter recipeListAdapter;
    private Recipe itemContent;
    private ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
    private boolean isSearchResult = false;
    private String titleHeading = "";

    private LocalDatabaseSQLiteOpenHelper localDatabaseSQLiteOpenHelper;
    private ContentProviderAccessor contentProviderAccessor = new ContentProviderAccessor();

    public RecipeListFragment(RecipeCategory recipeCategory, Context context) {
        if (recipeCategory != null)
            this.recipeCategory = recipeCategory;
        this.localDatabaseSQLiteOpenHelper = new LocalDatabaseSQLiteOpenHelper(context);
        this.context = context;

        Log.d(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>...", String.valueOf(this.recipeCategory.getCategoryId()));
    }

    public RecipeListFragment(ArrayList<Recipe> searchedRecipeList, Context context, String heading) {
        this.isSearchResult = true;
        recipeList = searchedRecipeList;
        this.context = context;
        this.titleHeading = heading;

        Log.d("recipe count >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ", String.valueOf(recipeList.size()));
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

        //yummy list view for selected yummy category
        recipeItemList = (ListView) myFragmentView.findViewById(R.id.recipeItemList);

        if (isSearchResult) {
            if (!recipeList.isEmpty()) {
                recipeListAdapter = new RecipeListAdapter(recipeList, this.context);
                recipeItemList.setAdapter(recipeListAdapter);
            }
            yummyCategoryNameTextView.setText(titleHeading);
        } else {
            //recipeList = localDatabaseSQLiteOpenHelper.getRecipesFromCategoryId(recipeCategory.getCategoryId());
            try {
                recipeList = contentProviderAccessor.getRecipesFromCategoryId(recipeCategory.getCategoryId(), context);
                recipeListAdapter = new RecipeListAdapter(recipeList, this.context);
                recipeItemList.setAdapter(recipeListAdapter);
                yummyCategoryNameTextView.setText("  " + recipeCategory.getCategoryName());
            } catch (NullPointerException ex) {
                Log.d("NullPointerException","Null pointer at recipes for category Id");
                yummyCategoryNameTextView.setText("  Category");
                Toast.makeText(context, "Please reload the category again", Toast.LENGTH_LONG);
                ex.printStackTrace();
            }
        }

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

        //recipes search from given name
        searchRecipeListImageButton = (ImageButton) myFragmentView.findViewById(R.id.searchRecipeListImageButton);
        searchRecipeListEditText = (EditText) myFragmentView.findViewById(R.id.searchRecipeListEditText);
        if (WelcomeActivity.widthAndHeight.get("width") <= 480) {
            searchRecipeListEditText.setTextSize(14);
        }
        searchRecipeListImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchKey = searchRecipeListEditText.getText().toString();

                if(searchKey.equals(null) || !searchKey.equals("")) {
                    try {
                        recipeList = localDatabaseSQLiteOpenHelper.searchRecipesFromGivenRecipeName(searchKey);

                        recipeListAdapter = new RecipeListAdapter(recipeList, context);
                        recipeItemList.setAdapter(recipeListAdapter);

                        yummyCategoryNameTextView.setText("   Search Results");
                    } catch (NullPointerException ex) {
                        Log.d("LOG_TAG:", "NullPointerException thrown");
                    }
                } else {
                    Toast.makeText(context,
                            "Please enter key word for search", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
