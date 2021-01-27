package com.ortusolis.etaaranavkf.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class BillReportModel implements Parcelable {

    UserInfo employeeDetails;
    UserInfo customerDetails;
    UserInfo distributorDetails;
    List<ProductDetails> productDeatils;

    protected BillReportModel(Parcel in) {
        employeeDetails = in.readParcelable(UserInfo.class.getClassLoader());
        customerDetails = in.readParcelable(UserInfo.class.getClassLoader());
        distributorDetails = in.readParcelable(UserInfo.class.getClassLoader());
        productDeatils = in.createTypedArrayList(ProductDetails.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(employeeDetails, flags);
        dest.writeParcelable(customerDetails, flags);
        dest.writeParcelable(distributorDetails, flags);
        dest.writeTypedList(productDeatils);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BillReportModel> CREATOR = new Creator<BillReportModel>() {
        @Override
        public BillReportModel createFromParcel(Parcel in) {
            return new BillReportModel(in);
        }

        @Override
        public BillReportModel[] newArray(int size) {
            return new BillReportModel[size];
        }
    };

    public UserInfo getEmployeeDetails() {
        return employeeDetails;
    }

    public void setEmployeeDetails(UserInfo employeeDetails) {
        this.employeeDetails = employeeDetails;
    }

    public UserInfo getCustomerDetails() {
        return customerDetails;
    }

    public void setCustomerDetails(UserInfo customerDetails) {
        this.customerDetails = customerDetails;
    }

    public UserInfo getDistributorDetails() {
        return distributorDetails;
    }

    public void setDistributorDetails(UserInfo distributorDetails) {
        this.distributorDetails = distributorDetails;
    }

    public List<ProductDetails> getProductDeatils() {
        return productDeatils;
    }

    public void setProductDeatils(List<ProductDetails> productDeatils) {
        this.productDeatils = productDeatils;
    }
}
