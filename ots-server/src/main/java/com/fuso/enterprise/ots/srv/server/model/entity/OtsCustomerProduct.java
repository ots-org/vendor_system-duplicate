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
@Table(name = "ots_customer_product")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtsCustomerProduct.findAll", query = "SELECT o FROM OtsCustomerProduct o"),
    @NamedQuery(name = "OtsCustomerProduct.findByOtsCustomerProductId", query = "SELECT o FROM OtsCustomerProduct o WHERE o.otsCustomerProductId = :otsCustomerProductId"),
    @NamedQuery(name = "OtsCustomerProduct.findByOtsCustomerProductPrice", query = "SELECT o FROM OtsCustomerProduct o WHERE o.otsCustomerProductPrice = :otsCustomerProductPrice")})
public class OtsCustomerProduct implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ots_customer_product_id")
    private Integer otsCustomerProductId;
    @Column(name = "ots_customer_product_price")
    private String otsCustomerProductPrice;
    @JoinColumn(name = "ots_product_id", referencedColumnName = "ots_product_id")
    @ManyToOne(optional = false)
    private OtsProduct otsProductId;
    @JoinColumn(name = "ots_users_id", referencedColumnName = "ots_users_id")
    @ManyToOne(optional = false)
    private OtsUsers otsUsersId;

    public OtsCustomerProduct() {
    }

    public OtsCustomerProduct(Integer otsCustomerProductId) {
        this.otsCustomerProductId = otsCustomerProductId;
    }

    public Integer getOtsCustomerProductId() {
        return otsCustomerProductId;
    }

    public void setOtsCustomerProductId(Integer otsCustomerProductId) {
        this.otsCustomerProductId = otsCustomerProductId;
    }

    public String getOtsCustomerProductPrice() {
        return otsCustomerProductPrice;
    }

    public void setOtsCustomerProductPrice(String otsCustomerProductPrice) {
        this.otsCustomerProductPrice = otsCustomerProductPrice;
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
        hash += (otsCustomerProductId != null ? otsCustomerProductId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsCustomerProduct)) {
            return false;
        }
        OtsCustomerProduct other = (OtsCustomerProduct) object;
        if ((this.otsCustomerProductId == null && other.otsCustomerProductId != null) || (this.otsCustomerProductId != null && !this.otsCustomerProductId.equals(other.otsCustomerProductId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsCustomerProduct[ otsCustomerProductId=" + otsCustomerProductId + " ]";
    }
    
}
