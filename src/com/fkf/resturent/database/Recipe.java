package com.fkf.resturent.database;

import java.util.List;

/**
 * Created by kavi on 6/26/13.
 * Recipe object for recipes table in sqlite database
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class Recipe {

    private int id;
    private String productId;
    private String name;
    private String description;
    private String ingredients;
    private String instructions;
    private int categoryId;
    private String addedDate;
    private int ratings;
    private String imageUrl;
    private String imageUrlT;
    private String imageUrl_xs;
    private String imageUrl_s;
    private String imageUrl_m;
    private String imageUrl_l;
    private String linkImages;
    private String linkRecipeIds;
    private int legacy;
    private String body;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    public int getRatings() {
        return ratings;
    }

    public void setRatings(int ratings) {
        this.ratings = ratings;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrlT() {
        return imageUrlT;
    }

    public void setImageUrlT(String imageUrlT) {
        this.imageUrlT = imageUrlT;
    }

    public String getImageUrl_xs() {
        return imageUrl_xs;
    }

    public void setImageUrl_xs(String imageUrl_xs) {
        this.imageUrl_xs = imageUrl_xs;
    }

    public String getImageUrl_s() {
        return imageUrl_s;
    }

    public void setImageUrl_s(String imageUrl_s) {
        this.imageUrl_s = imageUrl_s;
    }

    public String getImageUrl_m() {
        return imageUrl_m;
    }

    public void setImageUrl_m(String imageUrl_m) {
        this.imageUrl_m = imageUrl_m;
    }

    public String getImageUrl_l() {
        return imageUrl_l;
    }

    public void setImageUrl_l(String imageUrl_l) {
        this.imageUrl_l = imageUrl_l;
    }

    public String getLinkImages() {
        return linkImages;
    }

    public void setLinkImages(String linkImages) {
        this.linkImages = linkImages;
    }

    public String getLinkRecipeIds() {
        return linkRecipeIds;
    }

    public void setLinkRecipeIds(String linkRecipeIds) {
        this.linkRecipeIds = linkRecipeIds;
    }

    public int getLegacy() {
        return legacy;
    }

    public void setLegacy(int legacy) {
        this.legacy = legacy;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
