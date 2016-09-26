package com.suyogcomputech.helper;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by suyogcomputech on 31/08/16.
 */
public class ProductDetails implements Parcelable{
    String offerDesc;
    String offerPer;
    String fromDate;
    String toDate;
    String offerStatus;
    String imageStatus;
    String sellerName;
    String orderplaceHolderName;

    public String getOrderedDate() {
        return orderedDate;
    }

    public void setOrderedDate(String orderedDate) {
        this.orderedDate = orderedDate;
    }

    String orderedDate;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    String uniqueId;

    public String getOrderPlaceHolderEmail() {
        return orderPlaceHolderEmail;
    }

    public void setOrderPlaceHolderEmail(String orderPlaceHolderEmail) {
        this.orderPlaceHolderEmail = orderPlaceHolderEmail;
    }

    public String getOrderPlaceHolderAddr() {
        return orderPlaceHolderAddr;
    }

    public void setOrderPlaceHolderAddr(String orderPlaceHolderAddr) {
        this.orderPlaceHolderAddr = orderPlaceHolderAddr;
    }

    public String getOrderPlaceHolderPhone() {
        return orderPlaceHolderPhone;
    }

    public void setOrderPlaceHolderPhone(String orderPlaceHolderPhone) {
        this.orderPlaceHolderPhone = orderPlaceHolderPhone;
    }

    String orderPlaceHolderEmail;
    String orderPlaceHolderAddr;
    String orderPlaceHolderPhone;
    public String getOrderplaceHolderName() {
        return orderplaceHolderName;
    }

    public void setOrderplaceHolderName(String orderplaceHolderName) {
        this.orderplaceHolderName = orderplaceHolderName;
    }



    public String getSerielNo() {
        return serielNo;
    }

    public void setSerielNo(String serielNo) {
        this.serielNo = serielNo;
    }

    String serielNo;

    public String getSizeProduct() {
        return sizeProduct;
    }

    public void setSizeProduct(String sizeProduct) {
        this.sizeProduct = sizeProduct;
    }

    String sizeProduct;
    ArrayList<String> images;
    ArrayList<String> sizes;
    ArrayList<Integer> sizeAvailable;
    int rating;
    String id, subCatId, title, shortDesc, desc, selId, status, brand, price, aliasName, mainImage, quantity, maxQtyInCart, shippingFee, services, features, otherDetails;

    public ProductDetails() {
        super();
    }

    public ProductDetails(Parcel in) {
        offerDesc = in.readString();
        offerPer = in.readString();
        fromDate = in.readString();
        toDate = in.readString();
        offerStatus = in.readString();
        imageStatus = in.readString();
        sellerName = in.readString();
        images = in.createStringArrayList();
        sizes = in.createStringArrayList();
        rating = in.readInt();
        id = in.readString();
        subCatId = in.readString();
        title = in.readString();
        shortDesc = in.readString();
        desc = in.readString();
        selId = in.readString();
        status = in.readString();
        brand = in.readString();
        price = in.readString();
        aliasName = in.readString();
        mainImage = in.readString();
        quantity = in.readString();
        maxQtyInCart = in.readString();
        shippingFee = in.readString();
        services = in.readString();
        features = in.readString();
        otherDetails = in.readString();
        sizeAvailable = (ArrayList<Integer>) in.readSerializable();
        sizeProduct = in.readString();
        serielNo = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(offerDesc);
        dest.writeString(offerPer);
        dest.writeString(fromDate);
        dest.writeString(toDate);
        dest.writeString(offerStatus);
        dest.writeString(imageStatus);
        dest.writeString(sellerName);
        dest.writeStringList(images);
        dest.writeStringList(sizes);
        dest.writeInt(rating);
        dest.writeString(id);
        dest.writeString(subCatId);
        dest.writeString(title);
        dest.writeString(shortDesc);
        dest.writeString(desc);
        dest.writeString(selId);
        dest.writeString(status);
        dest.writeString(brand);
        dest.writeString(price);
        dest.writeString(aliasName);
        dest.writeString(mainImage);
        dest.writeString(quantity);
        dest.writeString(maxQtyInCart);
        dest.writeString(shippingFee);
        dest.writeString(services);
        dest.writeString(features);
        dest.writeString(otherDetails);
        dest.writeSerializable(sizeAvailable);
        dest.writeString(sizeProduct);
        dest.writeString(serielNo);
    }
    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductDetails> CREATOR = new Creator<ProductDetails>() {
        @Override
        public ProductDetails createFromParcel(Parcel in) {
            return new ProductDetails(in);
        }

        @Override
        public ProductDetails[] newArray(int size) {
            return new ProductDetails[size];
        }
    };

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(String subCatId) {
        this.subCatId = subCatId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSelId() {
        return selId;
    }

    public void setSelId(String selId) {
        this.selId = selId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMaxQtyInCart() {
        return maxQtyInCart;
    }

    public void setMaxQtyInCart(String maxQtyInCart) {
        this.maxQtyInCart = maxQtyInCart;
    }

    public String getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(String shippingFee) {
        this.shippingFee = shippingFee;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getOtherDetails() {
        return otherDetails;
    }

    public void setOtherDetails(String otherDetails) {
        this.otherDetails = otherDetails;
    }

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
