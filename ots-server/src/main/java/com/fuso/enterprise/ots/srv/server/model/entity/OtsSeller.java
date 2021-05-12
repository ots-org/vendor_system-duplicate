/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fuso.enterprise.ots.srv.server.model.entity;

import java.io.Serializable;
import java.util.Collection;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author lenovo
 */
@Entity
@Table(name = "ots_seller")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtsSeller.findAll", query = "SELECT o FROM OtsSeller o")
    , @NamedQuery(name = "OtsSeller.findByOtsSellerId", query = "SELECT o FROM OtsSeller o WHERE o.otsSellerId = :otsSellerId")
    , @NamedQuery(name = "OtsSeller.findByOtsSellerName", query = "SELECT o FROM OtsSeller o WHERE o.otsSellerName = :otsSellerName")
    , @NamedQuery(name = "OtsSeller.findByOtsSellerAddress", query = "SELECT o FROM OtsSeller o WHERE o.otsSellerAddress = :otsSellerAddress")
    , @NamedQuery(name = "OtsSeller.findByOtsSellerContactNumber", query = "SELECT o FROM OtsSeller o WHERE o.otsSellerContactNumber = :otsSellerContactNumber")
    , @NamedQuery(name = "OtsSeller.findByOtsSellerPincode", query = "SELECT o FROM OtsSeller o WHERE o.otsSellerPincode = :otsSellerPincode")
    , @NamedQuery(name = "OtsSeller.findByOtsSellerLat", query = "SELECT o FROM OtsSeller o WHERE o.otsSellerLat = :otsSellerLat")
    , @NamedQuery(name = "OtsSeller.findByOtsSellerLong", query = "SELECT o FROM OtsSeller o WHERE o.otsSellerLong = :otsSellerLong")})
public class OtsSeller implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ots_seller_id")
    private Integer otsSellerId;
    @Size(max = 45)
    @Column(name = "ots_seller_name")
    private String otsSellerName;
    @Size(max = 500)
    @Column(name = "ots_seller_address")
    private String otsSellerAddress;
    @Size(max = 45)
    @Column(name = "ots_seller_contact_number")
    private String otsSellerContactNumber;
    @Size(max = 45)
    @Column(name = "ots_seller_pincode")
    private String otsSellerPincode;
    @Size(max = 45)
    @Column(name = "ots_seller_lat")
    private String otsSellerLat;
    @Size(max = 45)
    @Column(name = "ots_seller_long")
    private String otsSellerLong;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsSellerId")
    private Collection<OtsSellerProductMapping> otsSellerProductMappingCollection;

    public OtsSeller() {
    }

    public OtsSeller(Integer otsSellerId) {
        this.otsSellerId = otsSellerId;
    }

    public Integer getOtsSellerId() {
        return otsSellerId;
    }

    public void setOtsSellerId(Integer otsSellerId) {
        this.otsSellerId = otsSellerId;
    }

    public String getOtsSellerName() {
        return otsSellerName;
    }

    public void setOtsSellerName(String otsSellerName) {
        this.otsSellerName = otsSellerName;
    }

    public String getOtsSellerAddress() {
        return otsSellerAddress;
    }

    public void setOtsSellerAddress(String otsSellerAddress) {
        this.otsSellerAddress = otsSellerAddress;
    }

    public String getOtsSellerContactNumber() {
        return otsSellerContactNumber;
    }

    public void setOtsSellerContactNumber(String otsSellerContactNumber) {
        this.otsSellerContactNumber = otsSellerContactNumber;
    }

    public String getOtsSellerPincode() {
        return otsSellerPincode;
    }

    public void setOtsSellerPincode(String otsSellerPincode) {
        this.otsSellerPincode = otsSellerPincode;
    }

    public String getOtsSellerLat() {
        return otsSellerLat;
    }

    public void setOtsSellerLat(String otsSellerLat) {
        this.otsSellerLat = otsSellerLat;
    }

    public String getOtsSellerLong() {
        return otsSellerLong;
    }

    public void setOtsSellerLong(String otsSellerLong) {
        this.otsSellerLong = otsSellerLong;
    }

    @XmlTransient
    public Collection<OtsSellerProductMapping> getOtsSellerProductMappingCollection() {
        return otsSellerProductMappingCollection;
    }

    public void setOtsSellerProductMappingCollection(Collection<OtsSellerProductMapping> otsSellerProductMappingCollection) {
        this.otsSellerProductMappingCollection = otsSellerProductMappingCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (otsSellerId != null ? otsSellerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsSeller)) {
            return false;
        }
        OtsSeller other = (OtsSeller) object;
        if ((this.otsSellerId == null && other.otsSellerId != null) || (this.otsSellerId != null && !this.otsSellerId.equals(other.otsSellerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsSeller[ otsSellerId=" + otsSellerId + " ]";
    }
    
}
