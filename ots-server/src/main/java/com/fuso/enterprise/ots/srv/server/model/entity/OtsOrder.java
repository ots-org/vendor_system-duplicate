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
 * @author shashikumar.ys
 */
@Entity
@Table(name = "ots_order")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtsOrder.findAll", query = "SELECT o FROM OtsOrder o"),
    @NamedQuery(name = "OtsOrder.findByOtsOrderId", query = "SELECT o FROM OtsOrder o WHERE o.otsOrderId = :otsOrderId"),
    @NamedQuery(name = "OtsOrder.findByOtsOrderNumber", query = "SELECT o FROM OtsOrder o WHERE o.otsOrderNumber = :otsOrderNumber"),
    @NamedQuery(name = "OtsOrder.findByOtsOrderCost", query = "SELECT o FROM OtsOrder o WHERE o.otsOrderCost = :otsOrderCost"),
    @NamedQuery(name = "OtsOrder.findByOtsOrderDt", query = "SELECT o FROM OtsOrder o WHERE o.otsOrderDt = :otsOrderDt"),
    @NamedQuery(name = "OtsOrder.findByOtsOrderDeliveryDt", query = "SELECT o FROM OtsOrder o WHERE o.otsOrderDeliveryDt = :otsOrderDeliveryDt"),
    @NamedQuery(name = "OtsOrder.findByOtsOrderDeliveredDt", query = "SELECT o FROM OtsOrder o WHERE o.otsOrderDeliveredDt = :otsOrderDeliveredDt"),
    @NamedQuery(name = "OtsOrder.findByOtsOrderStatus", query = "SELECT o FROM OtsOrder o WHERE o.otsOrderStatus = :otsOrderStatus"),
    @NamedQuery(name = "OtsOrder.findByOtsOrderAmountReceived", query = "SELECT o FROM OtsOrder o WHERE o.otsOrderAmountReceived = :otsOrderAmountReceived"),
    @NamedQuery(name = "OtsOrder.findByOtsOrderBalanceCan", query = "SELECT o FROM OtsOrder o WHERE o.otsOrderBalanceCan = :otsOrderBalanceCan"),
    @NamedQuery(name = "OtsOrder.findByOtsOrderOutstandingAmount", query = "SELECT o FROM OtsOrder o WHERE o.otsOrderOutstandingAmount = :otsOrderOutstandingAmount"),
    @NamedQuery(name = "OtsOrder.findByOtsOrderRemarks", query = "SELECT o FROM OtsOrder o WHERE o.otsOrderRemarks = :otsOrderRemarks")})
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
    @Temporal(TemporalType.DATE)
    private Date otsOrderDt;
    @Column(name = "ots_order_delivery_dt")
    @Temporal(TemporalType.DATE)
    private Date otsOrderDeliveryDt;
    @Column(name = "ots_order_delivered_dt")
    @Temporal(TemporalType.DATE)
    private Date otsOrderDeliveredDt;
    @Size(max = 45)
    @Column(name = "ots_order_status")
    private String otsOrderStatus;
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
    @JoinColumn(name = "ots_bill_id", referencedColumnName = "ots_bill_id")
    @ManyToOne
    private OtsBill otsBillId;
    @JoinColumn(name = "ots_distributor_id", referencedColumnName = "ots_users_id")
    @ManyToOne(optional = false)
    private OtsUsers otsDistributorId;
    @JoinColumn(name = "ots_customer_id", referencedColumnName = "ots_users_id")
    @ManyToOne(optional = false)
    private OtsUsers otsCustomerId;
    @JoinColumn(name = "ots_assigned_id", referencedColumnName = "ots_users_id")
    @ManyToOne
    private OtsUsers otsAssignedId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsOrderId")
    private Collection<OtsOrderProduct> otsOrderProductCollection;

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

    public OtsBill getOtsBillId() {
        return otsBillId;
    }

    public void setOtsBillId(OtsBill otsBillId) {
        this.otsBillId = otsBillId;
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

    @XmlTransient
    public Collection<OtsOrderProduct> getOtsOrderProductCollection() {
        return otsOrderProductCollection;
    }

    public void setOtsOrderProductCollection(Collection<OtsOrderProduct> otsOrderProductCollection) {
        this.otsOrderProductCollection = otsOrderProductCollection;
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
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsOrder[ otsOrderId=" + otsOrderId + " ]";
    }
    
}
