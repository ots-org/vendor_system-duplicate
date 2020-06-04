package com.ortusolis.subhaksha.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class OrderResponse {

    String responseCode;
    String responseDescription;
    OrderRequest responseData;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public OrderRequest getResponseData() {
        return responseData;
    }

    public void setResponseData(OrderRequest responseData) {
        this.responseData = responseData;
    }


    public class OrderRequest {

        List<RequestS> orderList;
        //
        String pdf;

        public String getPdf() {
            return pdf;
        }

        public void setPdf(String pdf) {
            this.pdf = pdf;
        }

        //
        public List<RequestS> getOrderList() {
            return orderList;
        }

        public void setOrderList(List<RequestS> orderList) {
            this.orderList = orderList;
        }
    }



    public static class RequestS implements Parcelable {

    String customerId;
    String orderDate;
    String delivaryDate;
    String distributorId;
    String orderCost;
    String assignedId;
    String orderStatus;
    String orderId;
    String orderNumber;
    String deliverdDate;
    String delivaredDate;
    //code raghuram ots
    String amountRecived;
    //code raghuram ots
    String orderOutStanding;
    String outStandingAmount;
    UserInfo employeeDetails;
    UserInfo customerDetails;
    UserInfo distributorDetails;
    ArrayList<ProductOrder> orderdProducts;

        protected RequestS(Parcel in) {
            customerId = in.readString();
            orderDate = in.readString();
            delivaryDate = in.readString();
            distributorId = in.readString();
            orderCost = in.readString();
            assignedId = in.readString();
            orderStatus = in.readString();
            orderId = in.readString();
            orderNumber = in.readString();
            deliverdDate = in.readString();
            delivaredDate = in.readString();
            //code raghuram ots
            amountRecived = in.readString();
            //code raghuram ots
            orderOutStanding = in.readString();
            outStandingAmount = in.readString();
            employeeDetails = in.readParcelable(UserInfo.class.getClassLoader());
            customerDetails = in.readParcelable(UserInfo.class.getClassLoader());
            distributorDetails = in.readParcelable(UserInfo.class.getClassLoader());
            orderdProducts = in.createTypedArrayList(ProductOrder.CREATOR);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(customerId);
            dest.writeString(orderDate);
            dest.writeString(delivaryDate);
            dest.writeString(distributorId);
            dest.writeString(orderCost);
            dest.writeString(assignedId);
            dest.writeString(orderStatus);
            dest.writeString(orderId);
            dest.writeString(orderNumber);
            dest.writeString(deliverdDate);
            dest.writeString(delivaredDate);
            //code raghuram ots
            dest.writeString(amountRecived);
            //code raghuram ots
            dest.writeString(orderOutStanding);
            dest.writeString(outStandingAmount);
            dest.writeParcelable(employeeDetails, flags);
            dest.writeParcelable(customerDetails, flags);
            dest.writeParcelable(distributorDetails, flags);
            dest.writeTypedList(orderdProducts);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<RequestS> CREATOR = new Creator<RequestS>() {
            @Override
            public RequestS createFromParcel(Parcel in) {
                return new RequestS(in);
            }

            @Override
            public RequestS[] newArray(int size) {
                return new RequestS[size];
            }
        };

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

        public String getDelivaryDate() {
            return delivaryDate;
        }

        public void setDelivaryDate(String delivaryDate) {
            this.delivaryDate = delivaryDate;
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

        public String getAssignedId() {
            return assignedId;
        }

        public void setAssignedId(String assignedId) {
            this.assignedId = assignedId;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public String getDeliverdDate() {
            return deliverdDate;
        }

        public void setDeliverdDate(String deliverdDate) {
            this.deliverdDate = deliverdDate;
        }

        public String getDelivaredDate() {
            return delivaredDate;
        }

        public void setDelivaredDate(String delivaredDate) {
            this.delivaredDate = delivaredDate;
        }
//code raghuram ots
        public String getAmountRecived() {
            return amountRecived;
        }

        public void setAmountRecived(String amountRecived) {
            this.amountRecived = amountRecived;
        }
//code raghuram ots

        public String getOrderOutStanding() {
            return orderOutStanding;
        }

        public void setOrderOutStanding(String orderOutStanding) {
            this.orderOutStanding = orderOutStanding;
        }

        public String getOutStandingAmount() {
            return outStandingAmount;
        }

        public void setOutStandingAmount(String outStandingAmount) {
            this.outStandingAmount = outStandingAmount;
        }

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

        public ArrayList<ProductOrder> getOrderdProducts() {
            return orderdProducts;
        }

        public void setOrderdProducts(ArrayList<ProductOrder> orderdProducts) {
            this.orderdProducts = orderdProducts;
        }

        public static class ProductOrder implements Parcelable{

        String otsOrderProductId;
        String otsOrderedQty;
        String otsDeliveredQty;
        String otsOrderProductCost;
        String otsOrderProductStatus;
        String otsOrderId;
        String otsProductId;
        String  productImage;
        String productName;
        String balanceCan;
        String outSbalanceCan;
        String canRecieved;
        String emptyCanRecived;
        String type;

            protected ProductOrder(Parcel in) {
                otsOrderProductId = in.readString();
                otsOrderedQty = in.readString();
                otsDeliveredQty = in.readString();
                otsOrderProductCost = in.readString();
                otsOrderProductStatus = in.readString();
                otsOrderId = in.readString();
                otsProductId = in.readString();
                productName = in.readString();
                productImage = in.readString();
                balanceCan = in.readString();
                outSbalanceCan = in.readString();
                canRecieved = in.readString();
                emptyCanRecived = in.readString();
                type = in.readString();
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(otsOrderProductId);
                dest.writeString(otsOrderedQty);
                dest.writeString(otsDeliveredQty);
                dest.writeString(otsOrderProductCost);
                dest.writeString(otsOrderProductStatus);
                dest.writeString(otsOrderId);
                dest.writeString(otsProductId);
                dest.writeString(productName);
                dest.writeString(productImage);
                dest.writeString(balanceCan);
                dest.writeString(outSbalanceCan);
                dest.writeString(canRecieved);
                dest.writeString(emptyCanRecived);
                dest.writeString(type);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<ProductOrder> CREATOR = new Creator<ProductOrder>() {
                @Override
                public ProductOrder createFromParcel(Parcel in) {
                    return new ProductOrder(in);
                }

                @Override
                public ProductOrder[] newArray(int size) {
                    return new ProductOrder[size];
                }
            };

            public String getOtsOrderProductId() {
                return otsOrderProductId;
            }

            public void setOtsOrderProductId(String otsOrderProductId) {
                this.otsOrderProductId = otsOrderProductId;
            }

            public String getOtsOrderedQty() {
                return otsOrderedQty;
            }

            public void setOtsOrderedQty(String otsOrderedQty) {
                this.otsOrderedQty = otsOrderedQty;
            }

            public String getOtsDeliveredQty() {
                return otsDeliveredQty;
            }

            public void setOtsDeliveredQty(String otsDeliveredQty) {
                this.otsDeliveredQty = otsDeliveredQty;
            }

            public String getOtsOrderProductCost() {
                return otsOrderProductCost;
            }

            public void setOtsOrderProductCost(String otsOrderProductCost) {
                this.otsOrderProductCost = otsOrderProductCost;
            }

            public String getOtsOrderProductStatus() {
                return otsOrderProductStatus;
            }

            public void setOtsOrderProductStatus(String otsOrderProductStatus) {
                this.otsOrderProductStatus = otsOrderProductStatus;
            }

            public String getOtsOrderId() {
                return otsOrderId;
            }

            public void setOtsOrderId(String otsOrderId) {
                this.otsOrderId = otsOrderId;
            }

            public String getOtsProductId() {
                return otsProductId;
            }

            public void setOtsProductId(String otsProductId) {
                this.otsProductId = otsProductId;
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

            public String getBalanceCan() {
                return balanceCan;
            }

            public void setBalanceCan(String balanceCan) {
                this.balanceCan = balanceCan;
            }

            public String getOutSbalanceCan() {
                return outSbalanceCan;
            }

            public void setOutSbalanceCan(String outSbalanceCan) {
                this.outSbalanceCan = outSbalanceCan;
            }

            public String getCanRecieved() {
                return canRecieved;
            }

            public void setCanRecieved(String canRecieved) {
                this.canRecieved = canRecieved;
            }

            public String getEmptyCanRecived() {
                return emptyCanRecived;
            }

            public void setEmptyCanRecived(String emptyCanRecived) {
                this.emptyCanRecived = emptyCanRecived;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
}

}
