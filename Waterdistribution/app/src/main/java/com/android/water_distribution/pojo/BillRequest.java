package com.android.water_distribution.pojo;

import java.util.ArrayList;

public class BillRequest {

    RequestS requestData;

    public RequestS getRequestData() {
        return requestData;
    }

    public void setRequestData(RequestS requestData) {
        this.requestData = requestData;
    }

    public static class RequestS {

    String billId;
    String billAmount;
    String billAmountReceived;
    String billGenerated;
    String outstandingAmount;
    String customerId;
    String igst;
    String cgst;
    String fromDate;
    String toDate;
    ArrayList<OrderId> orderId;


        public String getBillId() {
            return billId;
        }

        public void setBillId(String billId) {
            this.billId = billId;
        }

        public String getBillAmount() {
            return billAmount;
        }

        public void setBillAmount(String billAmount) {
            this.billAmount = billAmount;
        }

        public String getBillAmountReceived() {
            return billAmountReceived;
        }

        public void setBillAmountReceived(String billAmountReceived) {
            this.billAmountReceived = billAmountReceived;
        }

        public String getBillGenerated() {
            return billGenerated;
        }

        public void setBillGenerated(String billGenerated) {
            this.billGenerated = billGenerated;
        }

        public String getOutstandingAmount() {
            return outstandingAmount;
        }

        public void setOutstandingAmount(String outstandingAmount) {
            this.outstandingAmount = outstandingAmount;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getIgst() {
            return igst;
        }

        public void setIgst(String igst) {
            this.igst = igst;
        }

        public String getCgst() {
            return cgst;
        }

        public void setCgst(String cgst) {
            this.cgst = cgst;
        }

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

        public ArrayList<OrderId> getOrderId() {
            return orderId;
        }

        public void setOrderId(ArrayList<OrderId> orderId) {
            this.orderId = orderId;
        }

        public static class OrderId {

        String orderId;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }
    }
}

}
