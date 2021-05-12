/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fuso.enterprise.ots.srv.server.model.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lenovo
 */
@Entity
@Table(name = "ots_seller_product_mapping")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtsSellerProductMapping.findAll", query = "SELECT o FROM OtsSellerProductMapping o")
    , @NamedQuery(name = "OtsSellerProductMapping.findByOtsSellerProductMappingId", query = "SELECT o FROM OtsSellerProductMapping o WHERE o.otsSellerProductMappingId = :otsSellerProductMappingId")})
public class OtsSellerProductMapping implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ots_seller_product_mapping_id")
    private Integer otsSellerProductMappingId;
    @JoinColumn(name = "ots_product_id", referencedColumnName = "ots_product_id")
    @ManyToOne(optional = false)
    private OtsProduct otsProductId;
    @JoinColumn(name = "ots_seller_id", referencedColumnName = "ots_seller_id")
    @ManyToOne(optional = false)
    private OtsSeller otsSellerId;

    public OtsSellerProductMapping() {
    }

    public OtsSellerProductMapping(Integer otsSellerProductMappingId) {
        this.otsSellerProductMappingId = otsSellerProductMappingId;
    }

    public Integer getOtsSellerProductMappingId() {
        return otsSellerProductMappingId;
    }

    public void setOtsSellerProductMappingId(Integer otsSellerProductMappingId) {
        this.otsSellerProductMappingId = otsSellerProductMappingId;
    }

    public OtsProduct getOtsProductId() {
        return otsProductId;
    }

    public void setOtsProductId(OtsProduct otsProductId) {
        this.otsProductId = otsProductId;
    }

    public OtsSeller getOtsSellerId() {
        return otsSellerId;
    }

    public void setOtsSellerId(OtsSeller otsSellerId) {
        this.otsSellerId = otsSellerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (otsSellerProductMappingId != null ? otsSellerProductMappingId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsSellerProductMapping)) {
            return false;
        }
        OtsSellerProductMapping other = (OtsSellerProductMapping) object;
        if ((this.otsSellerProductMappingId == null && other.otsSellerProductMappingId != null) || (this.otsSellerProductMappingId != null && !this.otsSellerProductMappingId.equals(other.otsSellerProductMappingId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsSellerProductMapping[ otsSellerProductMappingId=" + otsSellerProductMappingId + " ]";
    }
    
}
