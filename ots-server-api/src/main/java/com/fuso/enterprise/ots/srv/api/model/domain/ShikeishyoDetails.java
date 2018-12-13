
package com.fuso.enterprise.ots.srv.api.model.domain;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ShikeishyoDetails {

    @NotNull
    @Size(max = 8)
    private String shikeisyoNo;

    @Size(max = 200)
    private String shikeishoDescription;

    @Size(max = 10)
    private String orderNo;

    @Size(max = 10)
    private String machineType;

    private String rdDesiredDate;

    private String dataOfIssue;

    @Size(max = 10)
    private String requestNo;

    private Integer productionVolumeUnits;

    @Size(max = 10)
    private String payout1;

    @Size(max = 10)
    private String payout2;

    @Size(max = 10)
    private String payout3;

    @Size(max = 20)
    private String requestCode;

    @Size(max = 6)
    private String partsDestination;

    @Size(max = 6)
    private String managementNo;

    @Size(max = 10)
    private String networkNumber;

    private Integer materialCost;

    @Size(max = 50)
    private String designerName;

    @Size(max = 10)
    private String department;

    @Size(max = 10)
    private String group;

    @Size(max = 100)
    private String responsiblePersonL4;

    @Size(max = 50)
    private String personInchrge;

    @Size(max = 200)
    private String remarks;

    private String eoNo;

    @Valid
    private List<Part> parts;

    private String shikeishoState;

    private String rdApprovalImageBase64Data;

    private String requesterID;

    private String fundingApprovalLink;

    public String getShikeisyoNo() {
        return shikeisyoNo;
    }

    public void setShikeisyoNo(String shikeisyoNo) {
        this.shikeisyoNo = shikeisyoNo;
    }

    public String getShikeishoDescription() {
        return shikeishoDescription;
    }

    public void setShikeishoDescription(String shikeishoDescription) {
        this.shikeishoDescription = shikeishoDescription;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getMachineType() {
        return machineType;
    }

    public void setMachineType(String machineType) {
        this.machineType = machineType;
    }

    public String getRdDesiredDate() {
        return rdDesiredDate;
    }

    public void setRdDesiredDate(String rdDesiredDate) {
        this.rdDesiredDate = rdDesiredDate;
    }

    public String getDataOfIssue() {
        return dataOfIssue;
    }

    public void setDataOfIssue(String dataOfIssue) {
        this.dataOfIssue = dataOfIssue;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public Integer getProductionVolumeUnits() {
        return productionVolumeUnits;
    }

    public void setProductionVolumeUnits(Integer productionVolumeUnits) {
        this.productionVolumeUnits = productionVolumeUnits;
    }

    public String getPayout1() {
        return payout1;
    }

    public void setPayout1(String payout1) {
        this.payout1 = payout1;
    }

    public String getPayout2() {
        return payout2;
    }

    public String getRequesterID() {
        return requesterID;
    }

    public void setRequesterID(String requesterID) {
        this.requesterID = requesterID;
    }

    public void setPayout2(String payout2) {
        this.payout2 = payout2;
    }

    public String getPayout3() {
        return payout3;
    }

    public void setPayout3(String payout3) {
        this.payout3 = payout3;
    }

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public String getPartsDestination() {
        return partsDestination;
    }

    public void setPartsDestination(String partsDestination) {
        this.partsDestination = partsDestination;
    }

    public String getManagementNo() {
        return managementNo;
    }

    public void setManagementNo(String managementNo) {
        this.managementNo = managementNo;
    }

    public String getNetworkNumber() {
        return networkNumber;
    }

    public void setNetworkNumber(String networkNumber) {
        this.networkNumber = networkNumber;
    }

    public Integer getMaterialCost() {
        return materialCost;
    }

    public void setMaterialCost(Integer materialCost) {
        this.materialCost = materialCost;
    }

    public String getDesignerName() {
        return designerName;
    }

    public void setDesignerName(String designerName) {
        this.designerName = designerName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getResponsiblePersonL4() {
        return responsiblePersonL4;
    }

    public void setResponsiblePersonL4(String responsiblePersonL4) {
        this.responsiblePersonL4 = responsiblePersonL4;
    }

    public String getPersonInchrge() {
        return personInchrge;
    }

    public void setPersonInchrge(String personInchrge) {
        this.personInchrge = personInchrge;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getEoNo() {
        return eoNo;
    }

    public void setEoNo(String eoNo) {
        this.eoNo = eoNo;
    }

    public List<Part> getParts() {
        return parts;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }

    public String getShikeishoState() {
        return shikeishoState;
    }

    public void setShikeishoState(String shikeishoState) {
        this.shikeishoState = shikeishoState;
    }

    public String getRdApprovalImageBase64Data() {
        return rdApprovalImageBase64Data;
    }

    public void setRdApprovalImageBase64Data(String rdApprovalImageBase64Data) {
        this.rdApprovalImageBase64Data = rdApprovalImageBase64Data;
    }

    public String getFundingApprovalLink() {
        return fundingApprovalLink;
    }

    public void setFundingApprovalLink(String fundingApprovalLink) {
        this.fundingApprovalLink = fundingApprovalLink;
    }

}
