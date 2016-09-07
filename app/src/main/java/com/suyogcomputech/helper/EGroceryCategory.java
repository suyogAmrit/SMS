package com.suyogcomputech.helper;

import java.util.ArrayList;

/**
 * Created by Suyog on 9/7/2016.
 */
public class EGroceryCategory {
    String catId, caDesc;
    ArrayList<EGrocerySubCategory> subCategories;

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCaDesc() {
        return caDesc;
    }

    public void setCaDesc(String caDesc) {
        this.caDesc = caDesc;
    }

    public ArrayList<EGrocerySubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(ArrayList<EGrocerySubCategory> subCategories) {
        this.subCategories = subCategories;
    }
}
