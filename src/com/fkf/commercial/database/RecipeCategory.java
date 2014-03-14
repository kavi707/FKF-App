package com.fkf.commercial.database;

/**
 * Created by kavi on 9/6/13.
 * Recipe Category object for recipes recipe_category table in sqlite database
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class RecipeCategory {

    private int categoryId;
    private String categoryName;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
