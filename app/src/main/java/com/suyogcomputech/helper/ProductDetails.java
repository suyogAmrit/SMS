package com.suyogcomputech.helper;

import java.util.ArrayList;

/**
 * Created by suyogcomputech on 31/08/16.
 */
public class ProductDetails extends Product {
    String offerDesc, offerPer, fromDate, toDate, offerStatus, imageStatus, sellerName;
    ArrayList<String> images;
    ArrayList<String> sizes;
    ArrayList<Integer> sizeAvailable;
    int rating;

    public ArrayList<String> getSizes() {
        return sizes;
    }

    public void setSizes(ArrayList<String> sizes) {
        this.sizes = sizes;
    }

    public ArrayList<Integer> getSizeAvailable() {
        return sizeAvailable;
    }

    public void setSizeAvailable(ArrayList<Integer> sizeAvailable) {
        this.sizeAvailable = sizeAvailable;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getOfferDesc() {
        return offerDesc;
    }

    public void setOfferDesc(String offerDesc) {
        this.offerDesc = offerDesc;
    }

    public String getOfferPer() {
        return offerPer;
    }

    public void setOfferPer(String offerPer) {
        this.offerPer = offerPer;
    }

    @Override
    public String getFromDate() {
        return fromDate;
    }

    @Override
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    @Override
    public String getToDate() {
        return toDate;
    }

    @Override
    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getOfferStatus() {
        return offerStatus;
    }

    public void setOfferStatus(String offerStatus) {
        this.offerStatus = offerStatus;
    }

    public String getImageStatus() {
        return imageStatus;
    }

    public void setImageStatus(String imageStatus) {
        this.imageStatus = imageStatus;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }
}
