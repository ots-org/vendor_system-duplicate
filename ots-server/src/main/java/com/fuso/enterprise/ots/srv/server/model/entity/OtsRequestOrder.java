/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fuso.enterprise.ots.srv.server.model.entity;

import java.io.Serializable;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SERAJKU
 */
@Entity
@Table(name = "ots_request_order")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtsRequestOrder.findAll", query = "SELECT o FROM OtsRequestOrder o"),
    @NamedQuery(name = "OtsRequestOrder.findByOtsRequestOrderId", query = "SELECT o FROM OtsRequestOrder o WHERE o.otsRequestOrderId = :otsRequestOrderId"),
    @NamedQuery(name = "OtsRequestOrder.findByOtsRequestNumber", query = "SELECT o FROM OtsRequestOrder o WHERE o.otsRequestNumber = :otsRequestNumber"),
    @NamedQuery(name = "OtsRequestOrder.findByOtsRequestQty", query = "SELECT o FROM OtsRequestOrder o WHERE o.otsRequestQty = :otsRequestQty"),
    @NamedQuery(name = "OtsRequestOrder.findByOtsScheduleDt", query = "SELECT o FROM OtsRequestOrder o WHERE o.otsScheduleDt = :otsScheduleDt"),
    @NamedQuery(name = "OtsRequestOrder.findByOtsNxtScheduleDt", query = "SELECT o FROM OtsRequestOrder o WHERE o.otsNxtScheduleDt = :otsNxtScheduleDt"),
    @NamedQuery(name = "OtsRequestOrder.findByOtsRequestStatus", query = "SELECT o FROM OtsRequestOrder o WHERE o.otsRequestStatus = :otsRequestStatus"),
    @NamedQuery(name = "OtsRequestOrder.findByOtsRequestOrderCreated", query = "SELECT o FROM OtsRequestOrder o WHERE o.otsRequestOrderCreated = :otsRequestOrderCreated"),
    @NamedQuery(name = "OtsRequestOrder.findByOtsRequestOrderTimestamp", query = "SELECT o FROM OtsRequestOrder o WHERE o.otsRequestOrderTimestamp = :otsRequestOrderTimestamp")})
public class OtsRequestOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ots_request_order_id")
    private Integer otsRequestOrderId;
    @Column(name = "ots_request_number")
    private String otsRequestNumber;
    @Column(name = "ots_request_qty")
    private Integer otsRequestQty;
    @Column(name = "ots_schedule_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otsScheduleDt;
    @Column(name = "ots_nxt_schedule_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otsNxtScheduleDt;
    @Column(name = "ots_request_status")
    private String otsRequestStatus;
    @Column(name = "ots_request_order_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otsRequestOrderCreated;
    @Column(name = "ots_request_order_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otsRequestOrderTimestamp;
    @JoinColumn(name = "ots_product_id", referencedColumnName = "ots_product_id")
    @ManyToOne(optional = false)
    private OtsProduct otsProductId;
    @JoinColumn(name = "ots_scheduler_id", referencedColumnName = "ots_scheduler_id")
    @ManyToOne(optional = false)
    private OtsScheduler otsSchedulerId;
    @JoinColumn(name = "ots_customer_id", referencedColumnName = "ots_users_id")
    @ManyToOne(optional = false)
    private OtsUsers otsCustomerId;
    @JoinColumn(name = "ots_distributor_id", referencedColumnName = "ots_users_id")
    @ManyToOne(optional = false)
    private OtsUsers otsDistributorId;

    public OtsRequestOrder() {
    }

    public OtsRequestOrder(Integer otsRequestOrderId) {
        this.otsRequestOrderId = otsRequestOrderId;
    }

    public Integer getOtsRequestOrderId() {
        return otsRequestOrderId;
    }

    public void setOtsRequestOrderId(Integer otsRequestOrderId) {
        this.otsRequestOrderId = otsRequestOrderId;
    }

    public String getOtsRequestNumber() {
        return otsRequestNumber;
    }

    public void setOtsRequestNumber(String otsRequestNumber) {
        this.otsRequestNumber = otsRequestNumber;
    }

    public Integer getOtsRequestQty() {
        return otsRequestQty;
    }

    public void setOtsRequestQty(Integer otsRequestQty) {
        this.otsRequestQty = otsRequestQty;
    }

    public Date getOtsScheduleDt() {
        return otsScheduleDt;
    }

    public void setOtsScheduleDt(Date otsScheduleDt) {
        this.otsScheduleDt = otsScheduleDt;
    }

    public Date getOtsNxtScheduleDt() {
        return otsNxtScheduleDt;
    }

    public void setOtsNxtScheduleDt(Date otsNxtScheduleDt) {
        this.otsNxtScheduleDt = otsNxtScheduleDt;
    }

    public String getOtsRequestStatus() {
        return otsRequestStatus;
    }

    public void setOtsRequestStatus(String otsRequestStatus) {
        this.otsRequestStatus = otsRequestStatus;
    }

    public Date getOtsRequestOrderCreated() {
        return otsRequestOrderCreated;
    }

    public void setOtsRequestOrderCreated(Date otsRequestOrderCreated) {
        this.otsRequestOrderCreated = otsRequestOrderCreated;
    }

    public Date getOtsRequestOrderTimestamp() {
        return otsRequestOrderTimestamp;
    }

    public void setOtsRequestOrderTimestamp(Date otsRequestOrderTimestamp) {
        this.otsRequestOrderTimestamp = otsRequestOrderTimestamp;
    }

    public OtsProduct getOtsProductId() {
        return otsProductId;
    }

    public void setOtsProductId(OtsProduct otsProductId) {
        this.otsProductId = otsProductId;
    }

    public OtsScheduler getOtsSchedulerId() {
        return otsSchedulerId;
    }

    public void setOtsSchedulerId(OtsScheduler otsSchedulerId) {
        this.otsSchedulerId = otsSchedulerId;
    }

    public OtsUsers getOtsCustomerId() {
        return otsCustomerId;
    }

    public void setOtsCustomerId(OtsUsers otsCustomerId) {
        this.otsCustomerId = otsCustomerId;
    }

    public OtsUsers getOtsDistributorId() {
        return otsDistributorId;
    }

    public void setOtsDistributorId(OtsUsers otsDistributorId) {
        this.otsDistributorId = otsDistributorId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (otsRequestOrderId != null ? otsRequestOrderId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsRequestOrder)) {
            return false;
        }
        OtsRequestOrder other = (OtsRequestOrder) object;
        if ((this.otsRequestOrderId == null && other.otsRequestOrderId != null) || (this.otsRequestOrderId != null && !this.otsRequestOrderId.equals(other.otsRequestOrderId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsRequestOrder[ otsRequestOrderId=" + otsRequestOrderId + " ]";
    }
    
}
