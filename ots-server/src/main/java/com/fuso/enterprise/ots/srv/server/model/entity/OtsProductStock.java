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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SABBABU
 */
@Entity
@Table(name = "ots_product_stock")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtsProductStock.findAll", query = "SELECT o FROM OtsProductStock o")
    , @NamedQuery(name = "OtsProductStock.findByOtsProdcutStockId", query = "SELECT o FROM OtsProductStock o WHERE o.otsProdcutStockId = :otsProdcutStockId")
    , @NamedQuery(name = "OtsProductStock.findByOtsProdcutStockActQty", query = "SELECT o FROM OtsProductStock o WHERE o.otsProdcutStockActQty = :otsProdcutStockActQty")
    , @NamedQuery(name = "OtsProductStock.findByOtsProdcutStockStatus", query = "SELECT o FROM OtsProductStock o WHERE o.otsProdcutStockStatus = :otsProdcutStockStatus")
    , @NamedQuery(name = "OtsProductStock.findByOtsProdcutStockTimestamp", query = "SELECT o FROM OtsProductStock o WHERE o.otsProdcutStockTimestamp = :otsProdcutStockTimestamp")
    , @NamedQuery(name = "OtsProductStock.findByOtsProdcutStockCreated", query = "SELECT o FROM OtsProductStock o WHERE o.otsProdcutStockCreated = :otsProdcutStockCreated")})
public class OtsProductStock implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ots_prodcut_stock_id")
    private Integer otsProdcutStockId;
    @Size(max = 45)
    @Column(name = "ots_prodcut_stock_act_qty")
    private String otsProdcutStockActQty;
    @Size(max = 45)
    @Column(name = "ots_prodcut_stock_status")
    private String otsProdcutStockStatus;
    @Column(name = "ots_prodcut_stock_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otsProdcutStockTimestamp;
    @Column(name = "ots_prodcut_stock_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otsProdcutStockCreated;
    @JoinColumn(name = "ots_product_id", referencedColumnName = "ots_product_id")
    @ManyToOne(optional = false)
    private OtsProduct otsProductId;
    @JoinColumn(name = "ots_users_id", referencedColumnName = "ots_users_id")
    @ManyToOne(optional = false)
    private OtsUsers otsUsersId;

    public OtsProductStock() {
    }

    public OtsProductStock(Integer otsProdcutStockId) {
        this.otsProdcutStockId = otsProdcutStockId;
    }

    public Integer getOtsProdcutStockId() {
        return otsProdcutStockId;
    }

    public void setOtsProdcutStockId(Integer otsProdcutStockId) {
        this.otsProdcutStockId = otsProdcutStockId;
    }

    public String getOtsProdcutStockActQty() {
        return otsProdcutStockActQty;
    }

    public void setOtsProdcutStockActQty(String otsProdcutStockActQty) {
        this.otsProdcutStockActQty = otsProdcutStockActQty;
    }

    public String getOtsProdcutStockStatus() {
        return otsProdcutStockStatus;
    }

    public void setOtsProdcutStockStatus(String otsProdcutStockStatus) {
        this.otsProdcutStockStatus = otsProdcutStockStatus;
    }

    public Date getOtsProdcutStockTimestamp() {
        return otsProdcutStockTimestamp;
    }

    public void setOtsProdcutStockTimestamp(Date otsProdcutStockTimestamp) {
        this.otsProdcutStockTimestamp = otsProdcutStockTimestamp;
    }

    public Date getOtsProdcutStockCreated() {
        return otsProdcutStockCreated;
    }

    public void setOtsProdcutStockCreated(Date otsProdcutStockCreated) {
        this.otsProdcutStockCreated = otsProdcutStockCreated;
    }

    public OtsProduct getOtsProductId() {
        return otsProductId;
    }

    public void setOtsProductId(OtsProduct otsProductId) {
        this.otsProductId = otsProductId;
    }

    public OtsUsers getOtsUsersId() {
        return otsUsersId;
    }

    public void setOtsUsersId(OtsUsers otsUsersId) {
        this.otsUsersId = otsUsersId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (otsProdcutStockId != null ? otsProdcutStockId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsProductStock)) {
            return false;
        }
        OtsProductStock other = (OtsProductStock) object;
        if ((this.otsProdcutStockId == null && other.otsProdcutStockId != null) || (this.otsProdcutStockId != null && !this.otsProdcutStockId.equals(other.otsProdcutStockId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsProductStock[ otsProdcutStockId=" + otsProdcutStockId + " ]";
    }
    
}
