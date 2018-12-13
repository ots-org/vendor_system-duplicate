
package com.fuso.enterprise.ots.srv.server.model.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "parts_details")
public class ShikeishoPartDetail implements Serializable {

    private static final long serialVersionUID = 8576436602690776920L;

    @Column(name = "id")
    private Integer id;

    @Column(name = "no")
    private Integer no;

    @EmbeddedId
    private ShikeishoPartDetailEmbedded shikeishoPartDetailEmbedded;

    @Column(name = "first_time")
    private String firstTime;

    @Column(name = "district")
    private String district;

    @Column(name = "generation")
    private String generation;

    @Column(name = "part_name")
    private String partName;

    @Column(name = "part_classification")
    private String partClassification;

    @Column(name = "requested_quantity")
    private Integer requestedQuantity;

    @Column(name = "modified_quantity")
    private Integer modifiedQuantity;

    @Column(name = "received_quantity")
    private Integer receivedQuantity;

    @Column(name = "pending_quantity")
    private Integer pendingQuantity;

    @Column(name = "unit_price")
    private Integer unitPrice;

    @Column(name = "desired_date")
    @Convert(converter = LocalDateAttributeConverter.class)
    private LocalDate desired_date;

    @Column(name = "due_date")
    @Convert(converter = LocalDateAttributeConverter.class)
    private LocalDate dueDate;

    @Column(name = "delivery_date")
    @Convert(converter = LocalDateAttributeConverter.class)
    private LocalDate deliveryDate;

    @Column(name = "delivery_location")
    private String deliveryLocation;

    @Column(name = "internal_delivery_time")
    @Convert(converter = LocalDateAttributeConverter.class)
    private LocalDate internalDeliveryTime;

    @Column(name = "implementation_instructions")
    private String implementationInstructions;

    @Column(name = "interior_make")
    private String interiorMake;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "status")
    private String status;

    @Column(name = "reason_for_cancel")
    private String reasonForCancel;

    @Column(name = "drawing_no")
    private String drawing_no;

    @Column(name = "drawing_url")
    private String drawing_url;

    @Column(name = "upg")
    private String upg;

    @Column(name = "pl_no")
    private String pl_no;

    @Column(name = "child_part_available")
    private String child_part_available;

    @Column(name = "buyer_code")
    private String buyerCode;

    @Column(name = "bom_verified_by")
    private String bomVerifiedBy;

    @Column(name = "bom_approved_by")
    private String bomApprovedBy;

    @Column(name = "supplier_code")
    private String supplierCode;

    @Column(name = "is_active")
    private String isActive;

    @Column(name = "pps")
    private String pps;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public ShikeishoPartDetailEmbedded getShikeishoPartDetailEmbedded() {
        return shikeishoPartDetailEmbedded;
    }

    public void setShikeishoPartDetailEmbedded(ShikeishoPartDetailEmbedded shikeishoPartDetailEmbedded) {
        this.shikeishoPartDetailEmbedded = shikeishoPartDetailEmbedded;
    }

    public String getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(String firstTime) {
        this.firstTime = firstTime;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getGeneration() {
        return generation;
    }

    public void setGeneration(String generation) {
        this.generation = generation;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getPartClassification() {
        return partClassification;
    }

    public void setPartClassification(String partClassification) {
        this.partClassification = partClassification;
    }

    public Integer getRequestedQuantity() {
        return requestedQuantity;
    }

    public void setRequestedQuantity(Integer requestedQuantity) {
        this.requestedQuantity = requestedQuantity;
    }

    public Integer getModifiedQuantity() {
        return modifiedQuantity;
    }

    public void setModifiedQuantity(Integer modifiedQuantity) {
        this.modifiedQuantity = modifiedQuantity;
    }

    public Integer getReceivedQuantity() {
        return receivedQuantity;
    }

    public void setReceivedQuantity(Integer receivedQuantity) {
        this.receivedQuantity = receivedQuantity;
    }

    public Integer getPendingQuantity() {
        return pendingQuantity;
    }

    public void setPendingQuantity(Integer pendingQuantity) {
        this.pendingQuantity = pendingQuantity;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }

    public LocalDate getDesired_date() {
        return desired_date;
    }

    public void setDesired_date(LocalDate desired_date) {
        this.desired_date = desired_date;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryLocation() {
        return deliveryLocation;
    }

    public void setDeliveryLocation(String deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    public LocalDate getInternalDeliveryTime() {
        return internalDeliveryTime;
    }

    public void setInternalDeliveryTime(LocalDate internalDeliveryTime) {
        this.internalDeliveryTime = internalDeliveryTime;
    }

    public String getImplementationInstructions() {
        return implementationInstructions;
    }

    public void setImplementationInstructions(String implementationInstructions) {
        this.implementationInstructions = implementationInstructions;
    }

    public String getInteriorMake() {
        return interiorMake;
    }

    public void setInteriorMake(String interiorMake) {
        this.interiorMake = interiorMake;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReasonForCancel() {
        return reasonForCancel;
    }

    public void setReasonForCancel(String reasonForCancel) {
        this.reasonForCancel = reasonForCancel;
    }

    public String getDrawing_no() {
        return drawing_no;
    }

    public void setDrawing_no(String drawing_no) {
        this.drawing_no = drawing_no;
    }

    public String getDrawing_url() {
        return drawing_url;
    }

    public void setDrawing_url(String drawing_url) {
        this.drawing_url = drawing_url;
    }

    public String getUpg() {
        return upg;
    }

    public void setUpg(String upg) {
        this.upg = upg;
    }

    public String getPl_no() {
        return pl_no;
    }

    public void setPl_no(String pl_no) {
        this.pl_no = pl_no;
    }

    public String getChild_part_available() {
        return child_part_available;
    }

    public void setChild_part_available(String child_part_available) {
        this.child_part_available = child_part_available;
    }

    public String getBuyerCode() {
        return buyerCode;
    }

    public void setBuyerCode(String buyerCode) {
        this.buyerCode = buyerCode;
    }

    public String getBomVerifiedBy() {
        return bomVerifiedBy;
    }

    public void setBomVerifiedBy(String bomVerifiedBy) {
        this.bomVerifiedBy = bomVerifiedBy;
    }

    public String getBomApprovedBy() {
        return bomApprovedBy;
    }

    public void setBomApprovedBy(String bomApprovedBy) {
        this.bomApprovedBy = bomApprovedBy;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getPps() {
        return pps;
    }

    public void setPps(String pps) {
        this.pps = pps;
    }

}
