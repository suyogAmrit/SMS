package com.suyogcomputech.helper;

import java.util.ArrayList;

/**
 * Created by suyogcomputech on 23/08/16.
 */
public class EShopCategory {
    String catId, caDesc;
    ArrayList<EShopSubCategory> subCategories;

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

    public ArrayList<EShopSubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(ArrayList<EShopSubCategory> subCategories) {
        this.subCategories = subCategories;
    }
}
