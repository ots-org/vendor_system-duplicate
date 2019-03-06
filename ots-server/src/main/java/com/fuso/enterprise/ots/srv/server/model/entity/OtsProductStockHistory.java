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
@Table(name = "ots_product_stock_history")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtsProductStockHistory.findAll", query = "SELECT o FROM OtsProductStockHistory o"),
    @NamedQuery(name = "OtsProductStockHistory.findByOtsProductStockHistoryId", query = "SELECT o FROM OtsProductStockHistory o WHERE o.otsProductStockHistoryId = :otsProductStockHistoryId"),
    @NamedQuery(name = "OtsProductStockHistory.findByOtsProductStockHistoryQty", query = "SELECT o FROM OtsProductStockHistory o WHERE o.otsProductStockHistoryQty = :otsProductStockHistoryQty"),
    @NamedQuery(name = "OtsProductStockHistory.findByOtsProductStockAddDate", query = "SELECT o FROM OtsProductStockHistory o WHERE o.otsProductStockAddDate = :otsProductStockAddDate"),
	@NamedQuery(name = "OtsProductStockHistory.findByOtsProductStockOrderId", query = "SELECT o FROM OtsProductStockHistory o WHERE o.otsProductStockOrderId = :otsProductStockOrderId")})
public class OtsProductStockHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ots_product_stock_history_id")
    private Integer otsProductStockHistoryId;
    @Column(name = "ots_product_stock_history_qty")
    private String otsProductStockHistoryQty;
    @Column(name = "ots_product_stock_history_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otsProductStockHistoryCreated;
    @Column(name = "ots_product_stock_history_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otsProductStockHistoryTimestamp;
    @Column(name = "ots_product_stock_add_date")
    @Temporal(TemporalType.DATE)
    private Date otsProductStockAddDate;
    @Column(name = "ots_product_stock_order_id")
    private String otsProductStockOrderId;
    @JoinColumn(name = "ots_product_id", referencedColumnName = "ots_product_id")
    @ManyToOne(optional = false)
    private OtsProduct otsProductId;
    @JoinColumn(name = "ots_users_id", referencedColumnName = "ots_users_id")
    @ManyToOne(optional = false)
    private OtsUsers otsUsersId;


    public OtsProductStockHistory() {
    }

    public OtsProductStockHistory(Integer otsProductStockHistoryId) {
        this.otsProductStockHistoryId = otsProductStockHistoryId;
    }

    public Integer getOtsProductStockHistoryId() {
        return otsProductStockHistoryId;
    }

    public void setOtsProductStockHistoryId(Integer otsProductStockHistoryId) {
        this.otsProductStockHistoryId = otsProductStockHistoryId;
    }

    public String getOtsProductStockHistoryQty() {
        return otsProductStockHistoryQty;
    }

    public void setOtsProductStockHistoryQty(String otsProductStockHistoryQty) {
        this.otsProductStockHistoryQty = otsProductStockHistoryQty;
    }

    public Date getOtsProductStockAddDate() {
        return otsProductStockAddDate;
    }

    public void setOtsProductStockAddDate(Date otsProductStockAddDate) {
        this.otsProductStockAddDate = otsProductStockAddDate;
    }

    public String getOtsProductStockOrderId() {
        return otsProductStockOrderId;
    }

    public void setOtsProductStockOrderId(String otsProductStockOrderId) {
        this.otsProductStockOrderId = otsProductStockOrderId;
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
        hash += (otsProductStockHistoryId != null ? otsProductStockHistoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsProductStockHistory)) {
            return false;
        }
        OtsProductStockHistory other = (OtsProductStockHistory) object;
        if ((this.otsProductStockHistoryId == null && other.otsProductStockHistoryId != null) || (this.otsProductStockHistoryId != null && !this.otsProductStockHistoryId.equals(other.otsProductStockHistoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsProductStockHistory[ otsProductStockHistoryId=" + otsProductStockHistoryId + " ]";
    }

}
