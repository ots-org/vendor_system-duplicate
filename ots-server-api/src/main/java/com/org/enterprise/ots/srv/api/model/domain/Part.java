
package com.org.enterprise.ots.srv.api.model.domain;

import javax.validation.constraints.Size;

public class Part {

    @Size(max = 10)
    private String prototypeCode;

    @Size(max = 10)
    private String district;

    @Size(max = 20)
    private String generation;

    @Size(max = 30)
    private String partNumber;

    @Size(max = 50)
    private String partName;

    private Integer quantity;

    private String internalDeliveryDate;

    @Size(max = 100)
    private String implementationInstruction;

    @Size(max = 20)
    private String upg;

    @Size(max = 20)
    private String interiorMakerName;

    @Size(max = 200)
    private String partRemarks;

    private String drawingNo;

    private String pps;

    private String status;

    private String deliverLocation;

    private String drawingUrl;

    private String dueDate;

    public String getPrototypeCode() {
        return prototypeCode;
    }

    public void setPrototypeCode(String prototypeCode) {
        this.prototypeCode = prototypeCode;
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

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getInternalDeliveryDate() {
        return internalDeliveryDate;
    }

    public void setInternalDeliveryDate(String internalDeliveryDate) {
        this.internalDeliveryDate = internalDeliveryDate;
    }

    public String getImplementationInstruction() {
        return implementationInstruction;
    }

    public void setImplementationInstruction(String implementationInstruction) {
        this.implementationInstruction = implementationInstruction;
    }

    public String getUpg() {
        return upg;
    }

    public void setUpg(String upg) {
        this.upg = upg;
    }

    public String getInteriorMakerName() {
        return interiorMakerName;
    }

    public void setInteriorMakerName(String interiorMakerName) {
        this.interiorMakerName = interiorMakerName;
    }

    public String getPartRemarks() {
        return partRemarks;
    }

    public void setPartRemarks(String partRemarks) {
        this.partRemarks = partRemarks;
    }

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    public String getPps() {
        return pps;
    }

    public void setPps(String pps) {
        this.pps = pps;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeliverLocation() {
        return deliverLocation;
    }

    public void setDeliverLocation(String deliverLocation) {
        this.deliverLocation = deliverLocation;
    }

    public String getDrawingUrl() {
        return drawingUrl;
    }

    public void setDrawingUrl(String drawingUrl) {
        this.drawingUrl = drawingUrl;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

}
