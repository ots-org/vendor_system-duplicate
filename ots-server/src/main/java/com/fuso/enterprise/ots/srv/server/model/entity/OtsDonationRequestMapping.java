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
 * @author lenovo
 */
@Entity
@Table(name = "ots_donation_request_mapping")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtsDonationRequestMapping.findAll", query = "SELECT o FROM OtsDonationRequestMapping o")
    , @NamedQuery(name = "OtsDonationRequestMapping.findByOtsDonationRequestMappingId", query = "SELECT o FROM OtsDonationRequestMapping o WHERE o.otsDonationRequestMappingId = :otsDonationRequestMappingId")
    , @NamedQuery(name = "OtsDonationRequestMapping.findByOtsDonationDate", query = "SELECT o FROM OtsDonationRequestMapping o WHERE o.otsDonationDate = :otsDonationDate")})
public class OtsDonationRequestMapping implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ots_donation_request_mapping_id")
    private Integer otsDonationRequestMappingId;
    @Column(name = "ots_donation_date")
    @Temporal(TemporalType.DATE)
    private Date otsDonationDate;
    @JoinColumn(name = "ots_donation_id", referencedColumnName = "ots_donation_id")
    @ManyToOne(optional = false)
    private OtsDonation otsDonationId;
    @JoinColumn(name = "ots_request_product_id", referencedColumnName = "ots_request_product_id")
    @ManyToOne(optional = false)
    private OtsRequestProduct otsRequestProductId;

    public OtsDonationRequestMapping() {
    }

    public OtsDonationRequestMapping(Integer otsDonationRequestMappingId) {
        this.otsDonationRequestMappingId = otsDonationRequestMappingId;
    }

    public Integer getOtsDonationRequestMappingId() {
        return otsDonationRequestMappingId;
    }

    public void setOtsDonationRequestMappingId(Integer otsDonationRequestMappingId) {
        this.otsDonationRequestMappingId = otsDonationRequestMappingId;
    }

    public Date getOtsDonationDate() {
        return otsDonationDate;
    }

    public void setOtsDonationDate(Date otsDonationDate) {
        this.otsDonationDate = otsDonationDate;
    }

    public OtsDonation getOtsDonationId() {
        return otsDonationId;
    }

    public void setOtsDonationId(OtsDonation otsDonationId) {
        this.otsDonationId = otsDonationId;
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
        hash += (otsDonationRequestMappingId != null ? otsDonationRequestMappingId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsDonationRequestMapping)) {
            return false;
        }
        OtsDonationRequestMapping other = (OtsDonationRequestMapping) object;
        if ((this.otsDonationRequestMappingId == null && other.otsDonationRequestMappingId != null) || (this.otsDonationRequestMappingId != null && !this.otsDonationRequestMappingId.equals(other.otsDonationRequestMappingId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsDonationRequestMapping[ otsDonationRequestMappingId=" + otsDonationRequestMappingId + " ]";
    }
    
}
