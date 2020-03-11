/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fuso.enterprise.ots.srv.server.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "ots_order_product")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtsOrderProduct.findAll", query = "SELECT o FROM OtsOrderProduct o"),
    @NamedQuery(name = "OtsOrderProduct.findByOtsOrderProductId", query = "SELECT o FROM OtsOrderProduct o WHERE o.otsOrderProductId = :otsOrderProductId"),
    @NamedQuery(name = "OtsOrderProduct.findByOtsOrderedQty", query = "SELECT o FROM OtsOrderProduct o WHERE o.otsOrderedQty = :otsOrderedQty"),
    @NamedQuery(name = "OtsOrderProduct.findByOtsDeliveredQty", query = "SELECT o FROM OtsOrderProduct o WHERE o.otsDeliveredQty = :otsDeliveredQty"),
    @NamedQuery(name = "OtsOrderProduct.findByOtsOrderProductCost", query = "SELECT o FROM OtsOrderProduct o WHERE o.otsOrderProductCost = :otsOrderProductCost"),
    @NamedQuery(name = "OtsOrderProduct.findByOtsOrderProductStatus", query = "SELECT o FROM OtsOrderProduct o WHERE o.otsOrderProductStatus = :otsOrderProductStatus"),
    @NamedQuery(name = "OtsOrderProduct.findByOtsOrderProductTimestamp", query = "SELECT o FROM OtsOrderProduct o WHERE o.otsOrderProductTimestamp = :otsOrderProductTimestamp"),
    @NamedQuery(name = "OtsOrderProduct.findByOtsOrderProductCreated", query = "SELECT o FROM OtsOrderProduct o WHERE o.otsOrderProductCreated = :otsOrderProductCreated"),
    @NamedQuery(name = "OtsOrderProduct.findByOtsReceivedCans", query = "SELECT o FROM OtsOrderProduct o WHERE o.otsReceivedCans = :otsReceivedCans")})
public class OtsOrderProduct implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ots_order_product_id")
    private Integer otsOrderProductId;
    @Column(name = "ots_ordered_qty")
    private Integer otsOrderedQty;
    @Column(name = "ots_delivered_qty")
    private Integer otsDeliveredQty;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ots_order_product_cost")
    private BigDecimal otsOrderProductCost;
    @Column(name = "ots_order_product_status")
    private String otsOrderProductStatus;
    @Column(name = "ots_order_product_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otsOrderProductTimestamp;
    @Column(name = "ots_order_product_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otsOrderProductCreated;
    @Column(name = "ots_received_cans")
    private Integer otsReceivedCans;
    @JoinColumn(name = "ots_order_id", referencedColumnName = "ots_order_id")
    @ManyToOne(optional = false)
    private OtsOrder otsOrderId;
    @JoinColumn(name = "ots_product_id", referencedColumnName = "ots_product_id")
    @ManyToOne(optional = false)
    private OtsProduct otsProductId;

    public OtsOrderProduct() {
    }

    public OtsOrderProduct(Integer otsOrderProductId) {
        this.otsOrderProductId = otsOrderProductId;
    }

    public Integer getOtsOrderProductId() {
        return otsOrderProductId;
    }

    public void setOtsOrderProductId(Integer otsOrderProductId) {
        this.otsOrderProductId = otsOrderProductId;
    }

    public Integer getOtsOrderedQty() {
        return otsOrderedQty;
    }

    public void setOtsOrderedQty(Integer otsOrderedQty) {
        this.otsOrderedQty = otsOrderedQty;
    }

    public Integer getOtsDeliveredQty() {
        return otsDeliveredQty;
    }

    public void setOtsDeliveredQty(Integer otsDeliveredQty) {
        this.otsDeliveredQty = otsDeliveredQty;
    }

    public BigDecimal getOtsOrderProductCost() {
        return otsOrderProductCost;
    }

    public void setOtsOrderProductCost(BigDecimal otsOrderProductCost) {
        this.otsOrderProductCost = otsOrderProductCost;
    }

    public String getOtsOrderProductStatus() {
        return otsOrderProductStatus;
    }

    public void setOtsOrderProductStatus(String otsOrderProductStatus) {
        this.otsOrderProductStatus = otsOrderProductStatus;
    }

    public Date getOtsOrderProductTimestamp() {
        return otsOrderProductTimestamp;
    }

    public void setOtsOrderProductTimestamp(Date otsOrderProductTimestamp) {
        this.otsOrderProductTimestamp = otsOrderProductTimestamp;
    }

    public Date getOtsOrderProductCreated() {
        return otsOrderProductCreated;
    }

    public void setOtsOrderProductCreated(Date otsOrderProductCreated) {
        this.otsOrderProductCreated = otsOrderProductCreated;
    }

    public Integer getOtsReceivedCans() {
        return otsReceivedCans;
    }

    public void setOtsReceivedCans(Integer otsReceivedCans) {
        this.otsReceivedCans = otsReceivedCans;
    }

    public OtsOrder getOtsOrderId() {
        return otsOrderId;
    }

    public void setOtsOrderId(OtsOrder otsOrderId) {
        this.otsOrderId = otsOrderId;
    }

    public OtsProduct getOtsProductId() {
        return otsProductId;
    }

    public void setOtsProductId(OtsProduct otsProductId) {
        this.otsProductId = otsProductId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (otsOrderProductId != null ? otsOrderProductId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsOrderProduct)) {
            return false;
        }
        OtsOrderProduct other = (OtsOrderProduct) object;
        if ((this.otsOrderProductId == null && other.otsOrderProductId != null) || (this.otsOrderProductId != null && !this.otsOrderProductId.equals(other.otsOrderProductId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsOrderProduct[ otsOrderProductId=" + otsOrderProductId + " ]";
    }
    
}
