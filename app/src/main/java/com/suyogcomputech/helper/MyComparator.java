package com.suyogcomputech.helper;

import java.util.Comparator;

/**
 * Created by office on 9/5/2016.
 */
public class MyComparator implements Comparator<ProductDetails> {
    @Override
    public int compare(ProductDetails pd1, ProductDetails pd2) {
        return Integer.valueOf(pd1.getPrice().compareTo(pd2.getPrice()));
    }
}
