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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SERAJKU
 */
@Entity
@Table(name = "ots_user_mapping")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtsUserMapping.findAll", query = "SELECT o FROM OtsUserMapping o"),
    @NamedQuery(name = "OtsUserMapping.findByOtsUserMappingId", query = "SELECT o FROM OtsUserMapping o WHERE o.otsUserMappingId = :otsUserMappingId"),
    @NamedQuery(name = "OtsUserMapping.findByOtsMappedTo", query = "SELECT o FROM OtsUserMapping o WHERE o.otsMappedTo = :otsMappedTo")})
public class OtsUserMapping implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ots_user_mapping_id")
    private Integer otsUserMappingId;
    @Column(name = "ots_mapped_to")
    private Integer otsMappedTo;
    @JoinColumn(name = "ots_users_id", referencedColumnName = "ots_users_id")
    @OneToOne(optional = false)
    private OtsUsers otsUsersId;

    public OtsUserMapping() {
    }

    public OtsUserMapping(Integer otsUserMappingId) {
        this.otsUserMappingId = otsUserMappingId;
    }

    public Integer getOtsUserMappingId() {
        return otsUserMappingId;
    }

    public void setOtsUserMappingId(Integer otsUserMappingId) {
        this.otsUserMappingId = otsUserMappingId;
    }

    public Integer getOtsMappedTo() {
        return otsMappedTo;
    }

    public void setOtsMappedTo(Integer otsMappedTo) {
        this.otsMappedTo = otsMappedTo;
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
        hash += (otsUserMappingId != null ? otsUserMappingId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsUserMapping)) {
            return false;
        }
        OtsUserMapping other = (OtsUserMapping) object;
        if ((this.otsUserMappingId == null && other.otsUserMappingId != null) || (this.otsUserMappingId != null && !this.otsUserMappingId.equals(other.otsUserMappingId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsUserMapping[ otsUserMappingId=" + otsUserMappingId + " ]";
    }
    
}
