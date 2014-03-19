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
    private String imageUrlXL;
    private String imageUrlT;

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

    public String getImageUrlXL() {
        return imageUrlXL;
    }

    public void setImageUrlXL(String imageUrlXL) {
        this.imageUrlXL = imageUrlXL;
    }

    public String getImageUrlT() {
        return imageUrlT;
    }

    public void setImageUrlT(String imageUrlT) {
        this.imageUrlT = imageUrlT;
    }
}
