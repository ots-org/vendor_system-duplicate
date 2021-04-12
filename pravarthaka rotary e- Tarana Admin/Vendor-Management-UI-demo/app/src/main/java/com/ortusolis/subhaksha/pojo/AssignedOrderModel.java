package com.ortusolis.subhaksha.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class AssignedOrderModel implements Parcelable {

    UserInfo employeeDetails;
    UserInfo customerDetails;
    UserInfo distributorDetails;
    List<OrderResponse.RequestS.ProductOrder> orderdProducts;
    String addressToBePlaced;
    String outStandingAmount;
    String orderStatus;
    String delivaryDate;
    String orderId;
    String assignedId;
    String distributorId;
    String orderCost;
    String customerId;
    String orderDate;
    String delivaredDate;

    protected AssignedOrderModel(Parcel in) {
        employeeDetails = in.readParcelable(UserInfo.class.getClassLoader());
        customerDetails = in.readParcelable(UserInfo.class.getClassLoader());
        distributorDetails = in.readParcelable(UserInfo.class.getClassLoader());
        orderdProducts = in.createTypedArrayList(OrderResponse.RequestS.ProductOrder.CREATOR);
        addressToBePlaced= in.readString();
        outStandingAmount = in.readString();
        orderStatus = in.readString();
        delivaryDate = in.readString();
        orderId = in.readString();
        assignedId = in.readString();
        distributorId = in.readString();
        orderCost = in.readString();
        customerId = in.readString();
        orderDate = in.readString();
        delivaredDate = in.readString();
    }

    public static final Creator<AssignedOrderModel> CREATOR = new Creator<AssignedOrderModel>() {
        @Override
        public AssignedOrderModel createFromParcel(Parcel in) {
            return new AssignedOrderModel(in);
        }

        @Override
        public AssignedOrderModel[] newArray(int size) {
            return new AssignedOrderModel[size];
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

    public List<OrderResponse.RequestS.ProductOrder> getOrderdProducts() {
        return orderdProducts;
    }

    public void setOrderdProducts(List<OrderResponse.RequestS.ProductOrder> orderdProducts) {
        this.orderdProducts = orderdProducts;
    }

        public String getAddressToBePlaced() {
        return addressToBePlaced;
    }

    public void setAddressToBePlaced(String addressToBePlaced) {
        this.addressToBePlaced = addressToBePlaced;
    }

    public String getOutStandingAmount() {
        return outStandingAmount;
    }

    public void setOutStandingAmount(String outStandingAmount) {
        this.outStandingAmount = outStandingAmount;
    }




    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getDelivaryDate() {
        return delivaryDate;
    }

    public void setDelivaryDate(String delivaryDate) {
        this.delivaryDate = delivaryDate;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAssignedId() {
        return assignedId;
    }

    public void setAssignedId(String assignedId) {
        this.assignedId = assignedId;
    }

    public String getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(String distributorId) {
        this.distributorId = distributorId;
    }

    public String getOrderCost() {
        return orderCost;
    }

    public void setOrderCost(String orderCost) {
        this.orderCost = orderCost;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getDelivaredDate() {
        return delivaredDate;
    }

    public void setDelivaredDate(String delivaredDate) {
        this.delivaredDate = delivaredDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(employeeDetails, flags);
        dest.writeParcelable(customerDetails, flags);
        dest.writeParcelable(distributorDetails, flags);
        dest.writeTypedList(orderdProducts);
        dest.writeString(addressToBePlaced);
        dest.writeString(outStandingAmount);
        dest.writeString(orderStatus);
        dest.writeString(delivaryDate);
        dest.writeString(orderId);
        dest.writeString(assignedId);
        dest.writeString(distributorId);
        dest.writeString(orderCost);
        dest.writeString(customerId);
        dest.writeString(orderDate);
        dest.writeString(delivaredDate);
    }
}
