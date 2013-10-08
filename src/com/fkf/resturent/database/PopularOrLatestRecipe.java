package com.fkf.resturent.database;

/**
 * Created by kavi on 10/8/13.
 * popular or latest recipe object for recipes table in sqlite database
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class PopularOrLatestRecipe {

    private int index;
    private String productId;
    private String recipeName;
    private String imageUrlXS;
    private String imageUrlS;
    private String imageUrlM;
    private String imageUrlL;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getImageUrlXS() {
        return imageUrlXS;
    }

    public void setImageUrlXS(String imageUrlXS) {
        this.imageUrlXS = imageUrlXS;
    }

    public String getImageUrlS() {
        return imageUrlS;
    }

    public void setImageUrlS(String imageUrlS) {
        this.imageUrlS = imageUrlS;
    }

    public String getImageUrlM() {
        return imageUrlM;
    }

    public void setImageUrlM(String imageUrlM) {
        this.imageUrlM = imageUrlM;
    }

    public String getImageUrlL() {
        return imageUrlL;
    }

    public void setImageUrlL(String imageUrlL) {
        this.imageUrlL = imageUrlL;
    }
}
