package com.example.ravi.hiltonadmin1;

public class Items {


    private String ImageUrl;
    private String ItemName;
    private String ItemCategory;
    private String ItemNumber;
    private String ItemDescription;
    private String ItemPrice;
    private String ItemId;

    /**getters and setters***/


    public String getItemNumber() {
        return ItemNumber;
    }

    public void setItemNumber(String itemNumber) {
        ItemNumber = itemNumber;
    }

    public String getItemCategory() {
        return ItemCategory;
    }

    public void setItemCategory(String itemCategory) {
        ItemCategory = itemCategory;
    }

    public String getItemId() {
        return ItemId;
    }

    public String getItemName() {
        return ItemName;
    }

    public String getItemDescription() {
        return ItemDescription;
    }

    public String getItemPrice() {
        return ItemPrice;
    }

    Items(String ItemId, String ItemName,String ItemCategory,String ItemNumber, String ItemDescription, String ItemPrice,String ImageUrl)
    {
        this.ImageUrl=ImageUrl;
        this.ItemId=ItemId;
        this.ItemName=ItemName;
        this.ItemCategory=ItemCategory;
        this.ItemDescription=ItemDescription;
        this.ItemNumber=ItemNumber;

        this.ItemPrice= ItemPrice;

    }
}
