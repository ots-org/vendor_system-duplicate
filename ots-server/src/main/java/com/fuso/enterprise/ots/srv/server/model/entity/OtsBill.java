/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fuso.enterprise.ots.srv.server.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author shashikumar.ys
 */
@Entity
@Table(name = "ots_bill")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtsBill.findAll", query = "SELECT o FROM OtsBill o"),
    @NamedQuery(name = "OtsBill.findByOtsBillId", query = "SELECT o FROM OtsBill o WHERE o.otsBillId = :otsBillId"),
    @NamedQuery(name = "OtsBill.findByOtsBillNumber", query = "SELECT o FROM OtsBill o WHERE o.otsBillNumber = :otsBillNumber"),
    @NamedQuery(name = "OtsBill.findByOtsBillAmount", query = "SELECT o FROM OtsBill o WHERE o.otsBillAmount = :otsBillAmount"),
    @NamedQuery(name = "OtsBill.findByOtsBillAmountReceived", query = "SELECT o FROM OtsBill o WHERE o.otsBillAmountReceived = :otsBillAmountReceived"),
    @NamedQuery(name = "OtsBill.findByOtsBillGenerated", query = "SELECT o FROM OtsBill o WHERE o.otsBillGenerated = :otsBillGenerated"),
    @NamedQuery(name = "OtsBill.findByOtsBillStatus", query = "SELECT o FROM OtsBill o WHERE o.otsBillStatus = :otsBillStatus"),
    @NamedQuery(name = "OtsBill.findByOtsBillTimestamp", query = "SELECT o FROM OtsBill o WHERE o.otsBillTimestamp = :otsBillTimestamp"),
    @NamedQuery(name = "OtsBill.findByOtsBillCreated", query = "SELECT o FROM OtsBill o WHERE o.otsBillCreated = :otsBillCreated"),
    @NamedQuery(name = "OtsBill.findByOtsbillIGST", query = "SELECT o FROM OtsBill o WHERE o.otsbillIGST = :otsbillIGST"),
    @NamedQuery(name = "OtsBill.findByOtsbillSGST", query = "SELECT o FROM OtsBill o WHERE o.otsbillSGST = :otsbillSGST"),
    @NamedQuery(name = "OtsBill.findByOtsBillOutstandingAmt", query = "SELECT o FROM OtsBill o WHERE o.otsBillOutstandingAmt = :otsBillOutstandingAmt"),
    @NamedQuery(name = "OtsBill.findByOtsBillPdf", query = "SELECT o FROM OtsBill o WHERE o.otsBillPdf = :otsBillPdf")})
public class OtsBill implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ots_bill_id")
    private Integer otsBillId;
    @Size(max = 45)
    @Column(name = "ots_bill_number")
    private String otsBillNumber;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ots_bill_amount")
    private BigDecimal otsBillAmount;
    @Column(name = "ots_bill_amount_received")
    private BigDecimal otsBillAmountReceived;
    @Size(max = 45)
    @Column(name = "ots_bill_generated")
    private String otsBillGenerated;
    @Size(max = 45)
    @Column(name = "ots_bill_status")
    private String otsBillStatus;
    @Column(name = "ots_bill_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otsBillTimestamp;
    @Column(name = "ots_bill_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otsBillCreated;
    @Column(name = "ots_bill_IGST")
    private BigDecimal otsbillIGST;
    @Column(name = "ots_bill_SGST")
    private BigDecimal otsbillSGST;
    @Column(name = "ots_bill_outstanding_amt")
    private BigDecimal otsBillOutstandingAmt;
    @Size(max = 5000)
    @Column(name = "ots_bill_pdf")
    private String otsBillPdf;
    @JoinColumn(name = "ots_customer_id", referencedColumnName = "ots_users_id")
    @ManyToOne(optional = false)
    private OtsUsers otsCustomerId;
    @OneToMany(mappedBy = "otsBillId")
    private Collection<OtsOrder> otsOrderCollection;

    public OtsBill() {
    }

    public OtsBill(Integer otsBillId) {
        this.otsBillId = otsBillId;
    }

    public Integer getOtsBillId() {
        return otsBillId;
    }

    public void setOtsBillId(Integer otsBillId) {
        this.otsBillId = otsBillId;
    }

    public String getOtsBillNumber() {
        return otsBillNumber;
    }

    public void setOtsBillNumber(String otsBillNumber) {
        this.otsBillNumber = otsBillNumber;
    }

    public BigDecimal getOtsBillAmount() {
        return otsBillAmount;
    }

    public void setOtsBillAmount(BigDecimal otsBillAmount) {
        this.otsBillAmount = otsBillAmount;
    }

    public BigDecimal getOtsBillAmountReceived() {
        return otsBillAmountReceived;
    }

    public void setOtsBillAmountReceived(BigDecimal otsBillAmountReceived) {
        this.otsBillAmountReceived = otsBillAmountReceived;
    }

    public String getOtsBillGenerated() {
        return otsBillGenerated;
    }

    public void setOtsBillGenerated(String otsBillGenerated) {
        this.otsBillGenerated = otsBillGenerated;
    }

    public String getOtsBillStatus() {
        return otsBillStatus;
    }

    public void setOtsBillStatus(String otsBillStatus) {
        this.otsBillStatus = otsBillStatus;
    }

    public Date getOtsBillTimestamp() {
        return otsBillTimestamp;
    }

    public void setOtsBillTimestamp(Date otsBillTimestamp) {
        this.otsBillTimestamp = otsBillTimestamp;
    }

    public Date getOtsBillCreated() {
        return otsBillCreated;
    }

    public void setOtsBillCreated(Date otsBillCreated) {
        this.otsBillCreated = otsBillCreated;
    }

    public BigDecimal getOtsbillIGST() {
        return otsbillIGST;
    }

    public void setOtsbillIGST(BigDecimal otsbillIGST) {
        this.otsbillIGST = otsbillIGST;
    }

    public BigDecimal getOtsbillSGST() {
        return otsbillSGST;
    }

    public void setOtsbillSGST(BigDecimal otsbillSGST) {
        this.otsbillSGST = otsbillSGST;
    }

    public BigDecimal getOtsBillOutstandingAmt() {
        return otsBillOutstandingAmt;
    }

    public void setOtsBillOutstandingAmt(BigDecimal otsBillOutstandingAmt) {
        this.otsBillOutstandingAmt = otsBillOutstandingAmt;
    }

    public String getOtsBillPdf() {
        return otsBillPdf;
    }

    public void setOtsBillPdf(String otsBillPdf) {
        this.otsBillPdf = otsBillPdf;
    }

    public OtsUsers getOtsCustomerId() {
        return otsCustomerId;
    }

    public void setOtsCustomerId(OtsUsers otsCustomerId) {
        this.otsCustomerId = otsCustomerId;
    }

    @XmlTransient
    public Collection<OtsOrder> getOtsOrderCollection() {
        return otsOrderCollection;
    }

    public void setOtsOrderCollection(Collection<OtsOrder> otsOrderCollection) {
        this.otsOrderCollection = otsOrderCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (otsBillId != null ? otsBillId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsBill)) {
            return false;
        }
        OtsBill other = (OtsBill) object;
        if ((this.otsBillId == null && other.otsBillId != null) || (this.otsBillId != null && !this.otsBillId.equals(other.otsBillId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsBill[ otsBillId=" + otsBillId + " ]";
    }
    
}
