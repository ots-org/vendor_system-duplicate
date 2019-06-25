package com.android.water_distribution.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductDetails implements Parcelable {

    String productId;
    String productName;
    String productImage;
    String productDescription;
    String productStatus;
    String productqty;
    String productPrice;
    String otsProductOpenBalance;
    String otsProductStockAddition;
    String otsProductOrderDelivered;
    String stock;
    String totalProductPrice;

    protected ProductDetails(Parcel in) {
        productId = in.readString();
        productName = in.readString();
        productImage = in.readString();
        productDescription = in.readString();
        productStatus = in.readString();
        productqty = in.readString();
        productPrice = in.readString();
        otsProductOpenBalance = in.readString();
        otsProductStockAddition = in.readString();
        otsProductOrderDelivered = in.readString();
        stock = in.readString();
        totalProductPrice = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productId);
        dest.writeString(productName);
        dest.writeString(productImage);
        dest.writeString(productDescription);
        dest.writeString(productStatus);
        dest.writeString(productqty);
        dest.writeString(productPrice);
        dest.writeString(otsProductOpenBalance);
        dest.writeString(otsProductStockAddition);
        dest.writeString(otsProductOrderDelivered);
        dest.writeString(stock);
        dest.writeString(totalProductPrice);
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public String getProductqty() {
        return productqty;
    }

    public void setProductqty(String productqty) {
        this.productqty = productqty;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getOtsProductOpenBalance() {
        return otsProductOpenBalance;
    }

    public void setOtsProductOpenBalance(String otsProductOpenBalance) {
        this.otsProductOpenBalance = otsProductOpenBalance;
    }

    public String getOtsProductStockAddition() {
        return otsProductStockAddition;
    }

    public void setOtsProductStockAddition(String otsProductStockAddition) {
        this.otsProductStockAddition = otsProductStockAddition;
    }

    public String getOtsProductOrderDelivered() {
        return otsProductOrderDelivered;
    }

    public void setOtsProductOrderDelivered(String otsProductOrderDelivered) {
        this.otsProductOrderDelivered = otsProductOrderDelivered;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getTotalProductPrice() {
        return totalProductPrice;
    }

    public void setTotalProductPrice(String totalProductPrice) {
        this.totalProductPrice = totalProductPrice;
    }
}
