/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fuso.enterprise.ots.srv.server.model.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author SERAJKU
 */
@Entity
@Table(name = "ots_product")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtsProduct.findAll", query = "SELECT o FROM OtsProduct o"),
    @NamedQuery(name = "OtsProduct.findByOtsProductId", query = "SELECT o FROM OtsProduct o WHERE o.otsProductId = :otsProductId"),
    @NamedQuery(name = "OtsProduct.findByOtsProductName", query = "SELECT o FROM OtsProduct o WHERE o.otsProductName = :otsProductName"),
    @NamedQuery(name = "OtsProduct.findByOtsProductDescription", query = "SELECT o FROM OtsProduct o WHERE o.otsProductDescription = :otsProductDescription"),
    @NamedQuery(name = "OtsProduct.findByOtsProductStatus", query = "SELECT o FROM OtsProduct o WHERE o.otsProductStatus = :otsProductStatus"),
    @NamedQuery(name = "OtsProduct.findByOtsProductPrice", query = "SELECT o FROM OtsProduct o WHERE o.otsProductPrice = :otsProductPrice")})
public class OtsProduct implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ots_product_id")
    private Integer otsProductId;
    @Column(name = "ots_product_name")
    private String otsProductName;
    @Column(name = "ots_product_description")
    private String otsProductDescription;
    @Column(name = "ots_product_status")
    private String otsProductStatus;
    @Column(name = "ots_product_price")
    private Long otsProductPrice;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsProductId")
    private Collection<OtsCustomerProduct> otsCustomerProductCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsProductId")
    private Collection<OtsProductStockHistory> otsProductStockHistoryCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsProductId")
    private Collection<OtsRegistration> otsRegistrationCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsProductId")
    private Collection<OtsProductStock> otsProductStockCollection;

    public OtsProduct() {
    }

    public OtsProduct(Integer otsProductId) {
        this.otsProductId = otsProductId;
    }

    public Integer getOtsProductId() {
        return otsProductId;
    }

    public void setOtsProductId(Integer otsProductId) {
        this.otsProductId = otsProductId;
    }

    public String getOtsProductName() {
        return otsProductName;
    }

    public void setOtsProductName(String otsProductName) {
        this.otsProductName = otsProductName;
    }

    public String getOtsProductDescription() {
        return otsProductDescription;
    }

    public void setOtsProductDescription(String otsProductDescription) {
        this.otsProductDescription = otsProductDescription;
    }

    public String getOtsProductStatus() {
        return otsProductStatus;
    }

    public void setOtsProductStatus(String otsProductStatus) {
        this.otsProductStatus = otsProductStatus;
    }

    public Long getOtsProductPrice() {
        return otsProductPrice;
    }

    public void setOtsProductPrice(Long otsProductPrice) {
        this.otsProductPrice = otsProductPrice;
    }

    @XmlTransient
    public Collection<OtsCustomerProduct> getOtsCustomerProductCollection() {
        return otsCustomerProductCollection;
    }

    public void setOtsCustomerProductCollection(Collection<OtsCustomerProduct> otsCustomerProductCollection) {
        this.otsCustomerProductCollection = otsCustomerProductCollection;
    }

    @XmlTransient
    public Collection<OtsProductStockHistory> getOtsProductStockHistoryCollection() {
        return otsProductStockHistoryCollection;
    }

    public void setOtsProductStockHistoryCollection(Collection<OtsProductStockHistory> otsProductStockHistoryCollection) {
        this.otsProductStockHistoryCollection = otsProductStockHistoryCollection;
    }

    @XmlTransient
    public Collection<OtsRegistration> getOtsRegistrationCollection() {
        return otsRegistrationCollection;
    }

    public void setOtsRegistrationCollection(Collection<OtsRegistration> otsRegistrationCollection) {
        this.otsRegistrationCollection = otsRegistrationCollection;
    }

    @XmlTransient
    public Collection<OtsProductStock> getOtsProductStockCollection() {
        return otsProductStockCollection;
    }

    public void setOtsProductStockCollection(Collection<OtsProductStock> otsProductStockCollection) {
        this.otsProductStockCollection = otsProductStockCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (otsProductId != null ? otsProductId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsProduct)) {
            return false;
        }
        OtsProduct other = (OtsProduct) object;
        if ((this.otsProductId == null && other.otsProductId != null) || (this.otsProductId != null && !this.otsProductId.equals(other.otsProductId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsProduct[ otsProductId=" + otsProductId + " ]";
    }
    
}
