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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SABBABU
 */
@Entity
@Table(name = "ots_orderrequest_mapping")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtsOrderrequestMapping.findAll", query = "SELECT o FROM OtsOrderrequestMapping o")
    , @NamedQuery(name = "OtsOrderrequestMapping.findByOtsOrderrequestMappingId", query = "SELECT o FROM OtsOrderrequestMapping o WHERE o.otsOrderrequestMappingId = :otsOrderrequestMappingId")})
public class OtsOrderrequestMapping implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ots_orderrequest_mapping_id")
    private Integer otsOrderrequestMappingId;
    @JoinColumn(name = "ots_order_id", referencedColumnName = "ots_order_id")
    @ManyToOne(optional = false)
    private OtsOrder otsOrderId;
    @JoinColumn(name = "ots_request_product_id", referencedColumnName = "ots_request_product_id")
    @ManyToOne(optional = false)
    private OtsRequestProduct otsRequestProductId;

    public OtsOrderrequestMapping() {
    }

    public OtsOrderrequestMapping(Integer otsOrderrequestMappingId) {
        this.otsOrderrequestMappingId = otsOrderrequestMappingId;
    }

    public Integer getOtsOrderrequestMappingId() {
        return otsOrderrequestMappingId;
    }

    public void setOtsOrderrequestMappingId(Integer otsOrderrequestMappingId) {
        this.otsOrderrequestMappingId = otsOrderrequestMappingId;
    }

    public OtsOrder getOtsOrderId() {
        return otsOrderId;
    }

    public void setOtsOrderId(OtsOrder otsOrderId) {
        this.otsOrderId = otsOrderId;
    }

    public OtsRequestProduct getOtsRequestProductId() {
        return otsRequestProductId;
    }

    public void setOtsRequestProductId(OtsRequestProduct otsRequestProductId) {
        this.otsRequestProductId = otsRequestProductId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (otsOrderrequestMappingId != null ? otsOrderrequestMappingId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsOrderrequestMapping)) {
            return false;
        }
        OtsOrderrequestMapping other = (OtsOrderrequestMapping) object;
        if ((this.otsOrderrequestMappingId == null && other.otsOrderrequestMappingId != null) || (this.otsOrderrequestMappingId != null && !this.otsOrderrequestMappingId.equals(other.otsOrderrequestMappingId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsOrderrequestMapping[ otsOrderrequestMappingId=" + otsOrderrequestMappingId + " ]";
    }
    
}
