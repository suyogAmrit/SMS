package com.suyogcomputech.helper;

/**
 * Created by Suyog on 9/8/2016.
 */
public class GroceryProductDetails {
    String product_id;
    String prod_title;
    String prod_short_description;
    String images_path;
    String price;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    public String getProd_title() {
        return prod_title;
    }

    public void setProd_title(String prod_title) {
        this.prod_title = prod_title;
    }

    public String getProd_short_description() {
        return prod_short_description;
    }

    public void setProd_short_description(String prod_short_description) {
        this.prod_short_description = prod_short_description;
    }

    public String getImages_path() {
        return images_path;
    }

    public void setImages_path(String images_path) {
        this.images_path = images_path;
    }
    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
}
