
package com.org.enterprise.ots.srv.server.model.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "TmpShikeishoParts")
public class TmpShikeishoPart implements Serializable {

    private static final long serialVersionUID = 8576436602690776920L;

    @EmbeddedId
    private TmpShikeishoPartEmbedded tmpShikeishoPartEmbedded;

    @Column(name = "prototype_code")
    private String prototypeCode;

    @Column(name = "district")
    private String district;

    @Column(name = "generation")
    private String generation;

    @Column(name = "part_name")
    private String partName;

    @Column(name = "requested_quantity")
    private Integer requestedQuantity;

    @Column(name = "internal_delivery_time")
    @Convert(converter = LocalDateAttributeConverter.class)
    private LocalDate internalDeliveryTime;

    @Column(name = "implementation_instructions")
    private String implementationInstructions;

    @Column(name = "upg")
    private String upg;

    @Column(name = "interior_make")
    private String interiorMake;

    @Column(name = "part_remarks")
    private String partRemarks;

    public TmpShikeishoPartEmbedded getTmpShikeishoPartEmbedded() {
        return tmpShikeishoPartEmbedded;
    }

    public void setTmpShikeishoPartEmbedded(TmpShikeishoPartEmbedded tmpShikeishoPartEmbedded) {
        this.tmpShikeishoPartEmbedded = tmpShikeishoPartEmbedded;
    }

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

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public Integer getRequestedQuantity() {
        return requestedQuantity;
    }

    public void setRequestedQuantity(Integer requestedQuantity) {
        this.requestedQuantity = requestedQuantity;
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

    public String getUpg() {
        return upg;
    }

    public void setUpg(String upg) {
        this.upg = upg;
    }

    public String getInteriorMake() {
        return interiorMake;
    }

    public void setInteriorMake(String interiorMake) {
        this.interiorMake = interiorMake;
    }

    public String getPartRemarks() {
        return partRemarks;
    }

    public void setPartRemarks(String partRemarks) {
        this.partRemarks = partRemarks;
    }

}
