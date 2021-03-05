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
@Table(name = "ots_request_product")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtsRequestProduct.findAll", query = "SELECT o FROM OtsRequestProduct o")
    , @NamedQuery(name = "OtsRequestProduct.findByOtsRequestProductId", query = "SELECT o FROM OtsRequestProduct o WHERE o.otsRequestProductId = :otsRequestProductId")
    , @NamedQuery(name = "OtsRequestProduct.findByOtsRequestProductStock", query = "SELECT o FROM OtsRequestProduct o WHERE o.otsRequestProductStock = :otsRequestProductStock")
    , @NamedQuery(name = "OtsRequestProduct.findByOtsRequestProductAddedStock", query = "SELECT o FROM OtsRequestProduct o WHERE o.otsRequestProductAddedStock = :otsRequestProductAddedStock")
    , @NamedQuery(name = "OtsRequestProduct.findByOtsRequestProductStatus", query = "SELECT o FROM OtsRequestProduct o WHERE o.otsRequestProductStatus = :otsRequestProductStatus")
    , @NamedQuery(name = "OtsRequestProduct.findByOtsRequestProductTimestamp", query = "SELECT o FROM OtsRequestProduct o WHERE o.otsRequestProductTimestamp = :otsRequestProductTimestamp")})
public class OtsRequestProduct implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ots_request_product_id")
    private Integer otsRequestProductId;
    @Size(max = 45)
    @Column(name = "ots_request_product_stock")
    private String otsRequestProductStock;
    @Size(max = 45)
    @Column(name = "ots_request_product_added_stock")
    private String otsRequestProductAddedStock;
    @Size(max = 45)
    @Column(name = "ots_request_product_status")
    private String otsRequestProductStatus;
    @Column(name = "ots_request_product_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otsRequestProductTimestamp;
    @JoinColumn(name = "ots_product_id", referencedColumnName = "ots_product_id")
    @ManyToOne(optional = false)
    private OtsProduct otsProductId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsRequestProductId")
    private Collection<OtsDonationRequestMapping> otsDonationRequestMappingCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsRequestProductId")
    private Collection<OtsOrderrequestMapping> otsOrderrequestMappingCollection;

    public OtsRequestProduct() {
    }

    public OtsRequestProduct(Integer otsRequestProductId) {
        this.otsRequestProductId = otsRequestProductId;
    }

    public Integer getOtsRequestProductId() {
        return otsRequestProductId;
    }

    public void setOtsRequestProductId(Integer otsRequestProductId) {
        this.otsRequestProductId = otsRequestProductId;
    }

    public String getOtsRequestProductStock() {
        return otsRequestProductStock;
    }

    public void setOtsRequestProductStock(String otsRequestProductStock) {
        this.otsRequestProductStock = otsRequestProductStock;
    }

    public String getOtsRequestProductAddedStock() {
        return otsRequestProductAddedStock;
    }

    public void setOtsRequestProductAddedStock(String otsRequestProductAddedStock) {
        this.otsRequestProductAddedStock = otsRequestProductAddedStock;
    }

    public String getOtsRequestProductStatus() {
        return otsRequestProductStatus;
    }

    public void setOtsRequestProductStatus(String otsRequestProductStatus) {
        this.otsRequestProductStatus = otsRequestProductStatus;
    }

    public Date getOtsRequestProductTimestamp() {
        return otsRequestProductTimestamp;
    }

    public void setOtsRequestProductTimestamp(Date otsRequestProductTimestamp) {
        this.otsRequestProductTimestamp = otsRequestProductTimestamp;
    }

    public OtsProduct getOtsProductId() {
        return otsProductId;
    }

    public void setOtsProductId(OtsProduct otsProductId) {
        this.otsProductId = otsProductId;
    }

    @XmlTransient
    public Collection<OtsDonationRequestMapping> getOtsDonationRequestMappingCollection() {
        return otsDonationRequestMappingCollection;
    }

    public void setOtsDonationRequestMappingCollection(Collection<OtsDonationRequestMapping> otsDonationRequestMappingCollection) {
        this.otsDonationRequestMappingCollection = otsDonationRequestMappingCollection;
    }

    @XmlTransient
    public Collection<OtsOrderrequestMapping> getOtsOrderrequestMappingCollection() {
        return otsOrderrequestMappingCollection;
    }

    public void setOtsOrderrequestMappingCollection(Collection<OtsOrderrequestMapping> otsOrderrequestMappingCollection) {
        this.otsOrderrequestMappingCollection = otsOrderrequestMappingCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (otsRequestProductId != null ? otsRequestProductId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsRequestProduct)) {
            return false;
        }
        OtsRequestProduct other = (OtsRequestProduct) object;
        if ((this.otsRequestProductId == null && other.otsRequestProductId != null) || (this.otsRequestProductId != null && !this.otsRequestProductId.equals(other.otsRequestProductId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsRequestProduct[ otsRequestProductId=" + otsRequestProductId + " ]";
    }
    
}
