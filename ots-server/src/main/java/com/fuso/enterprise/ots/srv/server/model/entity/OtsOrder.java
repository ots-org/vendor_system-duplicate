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
import javax.persistence.CascadeType;
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
 * @author lenovo
 */
@Entity
@Table(name = "ots_order")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtsOrder.findAll", query = "SELECT o FROM OtsOrder o")
    , @NamedQuery(name = "OtsOrder.findByOtsOrderId", query = "SELECT o FROM OtsOrder o WHERE o.otsOrderId = :otsOrderId")
    , @NamedQuery(name = "OtsOrder.findByOtsOrderNumber", query = "SELECT o FROM OtsOrder o WHERE o.otsOrderNumber = :otsOrderNumber")
    , @NamedQuery(name = "OtsOrder.findByOtsOrderCost", query = "SELECT o FROM OtsOrder o WHERE o.otsOrderCost = :otsOrderCost")
    , @NamedQuery(name = "OtsOrder.findByOtsOrderDt", query = "SELECT o FROM OtsOrder o WHERE o.otsOrderDt = :otsOrderDt")
    , @NamedQuery(name = "OtsOrder.findByOtsOrderDeliveryDt", query = "SELECT o FROM OtsOrder o WHERE o.otsOrderDeliveryDt = :otsOrderDeliveryDt")
    , @NamedQuery(name = "OtsOrder.findByOtsOrderDeliveredDt", query = "SELECT o FROM OtsOrder o WHERE o.otsOrderDeliveredDt = :otsOrderDeliveredDt")
    , @NamedQuery(name = "OtsOrder.findByOtsOrderStatus", query = "SELECT o FROM OtsOrder o WHERE o.otsOrderStatus = :otsOrderStatus")
    , @NamedQuery(name = "OtsOrder.findByOtsOrderTimestamp", query = "SELECT o FROM OtsOrder o WHERE o.otsOrderTimestamp = :otsOrderTimestamp")
    , @NamedQuery(name = "OtsOrder.findByOtsOrderCreated", query = "SELECT o FROM OtsOrder o WHERE o.otsOrderCreated = :otsOrderCreated")
    , @NamedQuery(name = "OtsOrder.findByOtsOrderAmountReceived", query = "SELECT o FROM OtsOrder o WHERE o.otsOrderAmountReceived = :otsOrderAmountReceived")
    , @NamedQuery(name = "OtsOrder.findByOtsOrderBalanceCan", query = "SELECT o FROM OtsOrder o WHERE o.otsOrderBalanceCan = :otsOrderBalanceCan")
    , @NamedQuery(name = "OtsOrder.findByOtsOrderOutstandingAmount", query = "SELECT o FROM OtsOrder o WHERE o.otsOrderOutstandingAmount = :otsOrderOutstandingAmount")
    , @NamedQuery(name = "OtsOrder.findByOtsOrderRemarks", query = "SELECT o FROM OtsOrder o WHERE o.otsOrderRemarks = :otsOrderRemarks")
    , @NamedQuery(name = "OtsOrder.findByOtsOrderAddress", query = "SELECT o FROM OtsOrder o WHERE o.otsOrderAddress = :otsOrderAddress")
    , @NamedQuery(name = "OtsOrder.findByOtsOrderLat", query = "SELECT o FROM OtsOrder o WHERE o.otsOrderLat = :otsOrderLat")
    , @NamedQuery(name = "OtsOrder.findByOtsOrderLong", query = "SELECT o FROM OtsOrder o WHERE o.otsOrderLong = :otsOrderLong")
    , @NamedQuery(name = "OtsOrder.findByOtsOrderPayementId", query = "SELECT o FROM OtsOrder o WHERE o.otsOrderPayementId = :otsOrderPayementId")
    , @NamedQuery(name = "OtsOrder.findByOtsOrderPaymentStatus", query = "SELECT o FROM OtsOrder o WHERE o.otsOrderPaymentStatus = :otsOrderPaymentStatus")
    , @NamedQuery(name = "OtsOrder.findByOtsOrderBasePrice", query = "SELECT o FROM OtsOrder o WHERE o.otsOrderBasePrice = :otsOrderBasePrice")})
public class OtsOrder implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ots_order_id")
    private Integer otsOrderId;
    @Size(max = 45)
    @Column(name = "ots_order_number")
    private String otsOrderNumber;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ots_order_cost")
    private BigDecimal otsOrderCost;
    @Column(name = "ots_order_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otsOrderDt;
    @Column(name = "ots_order_delivery_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otsOrderDeliveryDt;
    @Column(name = "ots_order_delivered_dt")
    @Temporal(TemporalType.DATE)
    private Date otsOrderDeliveredDt;
    @Size(max = 45)
    @Column(name = "ots_order_status")
    private String otsOrderStatus;
    @Column(name = "ots_order_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otsOrderTimestamp;
    @Column(name = "ots_order_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otsOrderCreated;
    @Column(name = "ots_order_amount_received")
    private BigDecimal otsOrderAmountReceived;
    @Size(max = 45)
    @Column(name = "ots_order_balance_can")
    private String otsOrderBalanceCan;
    @Size(max = 45)
    @Column(name = "ots_order_outstanding_amount")
    private String otsOrderOutstandingAmount;
    @Size(max = 45)
    @Column(name = "ots_order_remarks")
    private String otsOrderRemarks;
    @Size(max = 20000)
    @Column(name = "ots_order_address")
    private String otsOrderAddress;
    @Size(max = 45)
    @Column(name = "ots_order_lat")
    private String otsOrderLat;
    @Size(max = 45)
    @Column(name = "ots_order_long")
    private String otsOrderLong;
    @Size(max = 45)
    @Column(name = "ots_order_payement_id")
    private String otsOrderPayementId;
    @Size(max = 45)
    @Column(name = "ots_order_payment_status")
    private String otsOrderPaymentStatus;
    @Size(max = 45)
    @Column(name = "ots_order_base_price")
    private String otsOrderBasePrice;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsOrderId")
    private Collection<OtsOrderProduct> otsOrderProductCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsOrderId")
    private Collection<OtsOrderrequestMapping> otsOrderrequestMappingCollection;
    @JoinColumn(name = "ots_bill_id", referencedColumnName = "ots_bill_id")
    @ManyToOne
    private OtsBill otsBillId;
    @JoinColumn(name = "ots_donation_id", referencedColumnName = "ots_donation_id")
    @ManyToOne
    private OtsDonation otsDonationId;
    @JoinColumn(name = "ots_distributor_id", referencedColumnName = "ots_users_id")
    @ManyToOne(optional = false)
    private OtsUsers otsDistributorId;
    @JoinColumn(name = "ots_customer_id", referencedColumnName = "ots_users_id")
    @ManyToOne(optional = false)
    private OtsUsers otsCustomerId;
    @JoinColumn(name = "ots_assigned_id", referencedColumnName = "ots_users_id")
    @ManyToOne
    private OtsUsers otsAssignedId;
    @JoinColumn(name = "ots_order_created_by", referencedColumnName = "ots_users_id")
    @ManyToOne
    private OtsUsers otsOrderCreatedBy;

    public OtsOrder() {
    }

    public OtsOrder(Integer otsOrderId) {
        this.otsOrderId = otsOrderId;
    }

    public Integer getOtsOrderId() {
        return otsOrderId;
    }

    public void setOtsOrderId(Integer otsOrderId) {
        this.otsOrderId = otsOrderId;
    }

    public String getOtsOrderNumber() {
        return otsOrderNumber;
    }

    public void setOtsOrderNumber(String otsOrderNumber) {
        this.otsOrderNumber = otsOrderNumber;
    }

    public BigDecimal getOtsOrderCost() {
        return otsOrderCost;
    }

    public void setOtsOrderCost(BigDecimal otsOrderCost) {
        this.otsOrderCost = otsOrderCost;
    }

    public Date getOtsOrderDt() {
        return otsOrderDt;
    }

    public void setOtsOrderDt(Date otsOrderDt) {
        this.otsOrderDt = otsOrderDt;
    }

    public Date getOtsOrderDeliveryDt() {
        return otsOrderDeliveryDt;
    }

    public void setOtsOrderDeliveryDt(Date otsOrderDeliveryDt) {
        this.otsOrderDeliveryDt = otsOrderDeliveryDt;
    }

    public Date getOtsOrderDeliveredDt() {
        return otsOrderDeliveredDt;
    }

    public void setOtsOrderDeliveredDt(Date otsOrderDeliveredDt) {
        this.otsOrderDeliveredDt = otsOrderDeliveredDt;
    }

    public String getOtsOrderStatus() {
        return otsOrderStatus;
    }

    public void setOtsOrderStatus(String otsOrderStatus) {
        this.otsOrderStatus = otsOrderStatus;
    }

    public Date getOtsOrderTimestamp() {
        return otsOrderTimestamp;
    }

    public void setOtsOrderTimestamp(Date otsOrderTimestamp) {
        this.otsOrderTimestamp = otsOrderTimestamp;
    }

    public Date getOtsOrderCreated() {
        return otsOrderCreated;
    }

    public void setOtsOrderCreated(Date otsOrderCreated) {
        this.otsOrderCreated = otsOrderCreated;
    }

    public BigDecimal getOtsOrderAmountReceived() {
        return otsOrderAmountReceived;
    }

    public void setOtsOrderAmountReceived(BigDecimal otsOrderAmountReceived) {
        this.otsOrderAmountReceived = otsOrderAmountReceived;
    }

    public String getOtsOrderBalanceCan() {
        return otsOrderBalanceCan;
    }

    public void setOtsOrderBalanceCan(String otsOrderBalanceCan) {
        this.otsOrderBalanceCan = otsOrderBalanceCan;
    }

    public String getOtsOrderOutstandingAmount() {
        return otsOrderOutstandingAmount;
    }

    public void setOtsOrderOutstandingAmount(String otsOrderOutstandingAmount) {
        this.otsOrderOutstandingAmount = otsOrderOutstandingAmount;
    }

    public String getOtsOrderRemarks() {
        return otsOrderRemarks;
    }

    public void setOtsOrderRemarks(String otsOrderRemarks) {
        this.otsOrderRemarks = otsOrderRemarks;
    }

    public String getOtsOrderAddress() {
        return otsOrderAddress;
    }

    public void setOtsOrderAddress(String otsOrderAddress) {
        this.otsOrderAddress = otsOrderAddress;
    }

    public String getOtsOrderLat() {
        return otsOrderLat;
    }

    public void setOtsOrderLat(String otsOrderLat) {
        this.otsOrderLat = otsOrderLat;
    }

    public String getOtsOrderLong() {
        return otsOrderLong;
    }

    public void setOtsOrderLong(String otsOrderLong) {
        this.otsOrderLong = otsOrderLong;
    }

    public String getOtsOrderPayementId() {
        return otsOrderPayementId;
    }

    public void setOtsOrderPayementId(String otsOrderPayementId) {
        this.otsOrderPayementId = otsOrderPayementId;
    }

    public String getOtsOrderPaymentStatus() {
        return otsOrderPaymentStatus;
    }

    public void setOtsOrderPaymentStatus(String otsOrderPaymentStatus) {
        this.otsOrderPaymentStatus = otsOrderPaymentStatus;
    }

    public String getOtsOrderBasePrice() {
        return otsOrderBasePrice;
    }

    public void setOtsOrderBasePrice(String otsOrderBasePrice) {
        this.otsOrderBasePrice = otsOrderBasePrice;
    }

    @XmlTransient
    public Collection<OtsOrderProduct> getOtsOrderProductCollection() {
        return otsOrderProductCollection;
    }

    public void setOtsOrderProductCollection(Collection<OtsOrderProduct> otsOrderProductCollection) {
        this.otsOrderProductCollection = otsOrderProductCollection;
    }

    @XmlTransient
    public Collection<OtsOrderrequestMapping> getOtsOrderrequestMappingCollection() {
        return otsOrderrequestMappingCollection;
    }

    public void setOtsOrderrequestMappingCollection(Collection<OtsOrderrequestMapping> otsOrderrequestMappingCollection) {
        this.otsOrderrequestMappingCollection = otsOrderrequestMappingCollection;
    }

    public OtsBill getOtsBillId() {
        return otsBillId;
    }

    public void setOtsBillId(OtsBill otsBillId) {
        this.otsBillId = otsBillId;
    }

    public OtsDonation getOtsDonationId() {
        return otsDonationId;
    }

    public void setOtsDonationId(OtsDonation otsDonationId) {
        this.otsDonationId = otsDonationId;
    }

    public OtsUsers getOtsDistributorId() {
        return otsDistributorId;
    }

    public void setOtsDistributorId(OtsUsers otsDistributorId) {
        this.otsDistributorId = otsDistributorId;
    }

    public OtsUsers getOtsCustomerId() {
        return otsCustomerId;
    }

    public void setOtsCustomerId(OtsUsers otsCustomerId) {
        this.otsCustomerId = otsCustomerId;
    }

    public OtsUsers getOtsAssignedId() {
        return otsAssignedId;
    }

    public void setOtsAssignedId(OtsUsers otsAssignedId) {
        this.otsAssignedId = otsAssignedId;
    }

    public OtsUsers getOtsOrderCreatedBy() {
        return otsOrderCreatedBy;
    }

    public void setOtsOrderCreatedBy(OtsUsers otsOrderCreatedBy) {
        this.otsOrderCreatedBy = otsOrderCreatedBy;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (otsOrderId != null ? otsOrderId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsOrder)) {
            return false;
        }
        OtsOrder other = (OtsOrder) object;
        if ((this.otsOrderId == null && other.otsOrderId != null) || (this.otsOrderId != null && !this.otsOrderId.equals(other.otsOrderId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.supreme.enterprise.ots.srv.server.model.entity.OtsOrder[ otsOrderId=" + otsOrderId + " ]";
    }
    
}
