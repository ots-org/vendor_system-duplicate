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
@Table(name = "ots_stock_dist_ob")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtsStockDistOb.findAll", query = "SELECT o FROM OtsStockDistOb o")
    , @NamedQuery(name = "OtsStockDistOb.findByOtsStockDistObId", query = "SELECT o FROM OtsStockDistOb o WHERE o.otsStockDistObId = :otsStockDistObId")
    , @NamedQuery(name = "OtsStockDistOb.findByOtsStockDistObStockdt", query = "SELECT o FROM OtsStockDistOb o WHERE o.otsStockDistObStockdt = :otsStockDistObStockdt")
    , @NamedQuery(name = "OtsStockDistOb.findByOtsStockDistOpeningBalance", query = "SELECT o FROM OtsStockDistOb o WHERE o.otsStockDistOpeningBalance = :otsStockDistOpeningBalance")})
public class OtsStockDistOb implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ots_stock_dist_ob_id")
    private Integer otsStockDistObId;
    @Column(name = "ots_stock_dist_ob_stockdt")
    @Temporal(TemporalType.DATE)
    private Date otsStockDistObStockdt;
    @Size(max = 45)
    @Column(name = "ots_stock_dist_opening_balance")
    private String otsStockDistOpeningBalance;
    @JoinColumn(name = "ots_product_id", referencedColumnName = "ots_product_id")
    @ManyToOne(optional = false)
    private OtsProduct otsProductId;
    @JoinColumn(name = "ots_users_id", referencedColumnName = "ots_users_id")
    @ManyToOne(optional = false)
    private OtsUsers otsUsersId;

    public OtsStockDistOb() {
    }

    public OtsStockDistOb(Integer otsStockDistObId) {
        this.otsStockDistObId = otsStockDistObId;
    }

    public Integer getOtsStockDistObId() {
        return otsStockDistObId;
    }

    public void setOtsStockDistObId(Integer otsStockDistObId) {
        this.otsStockDistObId = otsStockDistObId;
    }

    public Date getOtsStockDistObStockdt() {
        return otsStockDistObStockdt;
    }

    public void setOtsStockDistObStockdt(Date otsStockDistObStockdt) {
        this.otsStockDistObStockdt = otsStockDistObStockdt;
    }

    public String getOtsStockDistOpeningBalance() {
        return otsStockDistOpeningBalance;
    }

    public void setOtsStockDistOpeningBalance(String otsStockDistOpeningBalance) {
        this.otsStockDistOpeningBalance = otsStockDistOpeningBalance;
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
        hash += (otsStockDistObId != null ? otsStockDistObId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsStockDistOb)) {
            return false;
        }
        OtsStockDistOb other = (OtsStockDistOb) object;
        if ((this.otsStockDistObId == null && other.otsStockDistObId != null) || (this.otsStockDistObId != null && !this.otsStockDistObId.equals(other.otsStockDistObId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsStockDistOb[ otsStockDistObId=" + otsStockDistObId + " ]";
    }
    
}
