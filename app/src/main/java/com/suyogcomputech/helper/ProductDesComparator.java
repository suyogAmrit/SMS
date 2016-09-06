package com.suyogcomputech.helper;

import java.util.Comparator;

/**
 * Created by office on 9/6/2016.
 */
public class ProductDesComparator implements Comparator<ProductDetails> {
    @Override
    public int compare(ProductDetails o1, ProductDetails o2) {
        if (Double.valueOf(o1.getPrice()) < Double.valueOf(o2.getPrice())){
            return 1;
        }else if (Double.valueOf(o1.getPrice()) > Double.valueOf(o2.getPrice())){
            return -1;
        }else {
            return 0;
        }
    }
}
