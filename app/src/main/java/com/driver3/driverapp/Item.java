package com.driver3.driverapp;

public class Item {

    private String productId = "";
    private String description = "";
    private String pictureURL = "";
    private int price = 0;
    private String title = "";
    public Item(String productId, String description, String pictureURL, int price, String title){
        this.productId = productId;
        this.description = description;
        this.pictureURL = pictureURL;
        this.price = price;
        this.title = title;
    }

    public String getProductId(){
        return productId;
    }

    public String getPictureURL(){
        return pictureURL;
    }

    public int getPrice(){
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle(){
        return title;
    }
}
