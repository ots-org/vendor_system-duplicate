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
@Table(name = "ots_product_user_mapping")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtsProductUserMapping.findAll", query = "SELECT o FROM OtsProductUserMapping o")
    , @NamedQuery(name = "OtsProductUserMapping.findByOtsProductUserMappingId", query = "SELECT o FROM OtsProductUserMapping o WHERE o.otsProductUserMappingId = :otsProductUserMappingId")})
public class OtsProductUserMapping implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ots_product_user_mapping_id")
    private Integer otsProductUserMappingId;
    @JoinColumn(name = "ots_product_id", referencedColumnName = "ots_product_id")
    @ManyToOne(optional = false)
    private OtsProduct otsProductId;
    @JoinColumn(name = "ots_users_id", referencedColumnName = "ots_users_id")
    @ManyToOne(optional = false)
    private OtsUsers otsUsersId;

    public OtsProductUserMapping() {
    }

    public OtsProductUserMapping(Integer otsProductUserMappingId) {
        this.otsProductUserMappingId = otsProductUserMappingId;
    }

    public Integer getOtsProductUserMappingId() {
        return otsProductUserMappingId;
    }

    public void setOtsProductUserMappingId(Integer otsProductUserMappingId) {
        this.otsProductUserMappingId = otsProductUserMappingId;
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
        hash += (otsProductUserMappingId != null ? otsProductUserMappingId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsProductUserMapping)) {
            return false;
        }
        OtsProductUserMapping other = (OtsProductUserMapping) object;
        if ((this.otsProductUserMappingId == null && other.otsProductUserMappingId != null) || (this.otsProductUserMappingId != null && !this.otsProductUserMappingId.equals(other.otsProductUserMappingId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsProductUserMapping[ otsProductUserMappingId=" + otsProductUserMappingId + " ]";
    }
    
}
