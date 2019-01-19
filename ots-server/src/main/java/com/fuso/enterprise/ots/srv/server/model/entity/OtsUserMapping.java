/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fuso.enterprise.ots.srv.server.model.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SERAJKU
 */
@MappedSuperclass
@Table(name = "ots_user_mapping")
@XmlRootElement
public class OtsUserMapping implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected OtsUserMappingPK otsUserMappingPK;
    @Column(name = "ots_user_mapped_to")
    private Integer otsUserMappedTo;
    @JoinColumn(name = "ots_users_id", referencedColumnName = "ots_users_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private OtsUsers otsUsers;

    public OtsUserMapping() {
    }

    public OtsUserMapping(OtsUserMappingPK otsUserMappingPK) {
        this.otsUserMappingPK = otsUserMappingPK;
    }

    public OtsUserMapping(int otsUserMappingId, int otsUsersId) {
        this.otsUserMappingPK = new OtsUserMappingPK(otsUserMappingId, otsUsersId);
    }

    public OtsUserMappingPK getOtsUserMappingPK() {
        return otsUserMappingPK;
    }

    public void setOtsUserMappingPK(OtsUserMappingPK otsUserMappingPK) {
        this.otsUserMappingPK = otsUserMappingPK;
    }

    public Integer getOtsUserMappedTo() {
        return otsUserMappedTo;
    }

    public void setOtsUserMappedTo(Integer otsUserMappedTo) {
        this.otsUserMappedTo = otsUserMappedTo;
    }

    public OtsUsers getOtsUsers() {
        return otsUsers;
    }

    public void setOtsUsers(OtsUsers otsUsers) {
        this.otsUsers = otsUsers;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (otsUserMappingPK != null ? otsUserMappingPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsUserMapping)) {
            return false;
        }
        OtsUserMapping other = (OtsUserMapping) object;
        if ((this.otsUserMappingPK == null && other.otsUserMappingPK != null) || (this.otsUserMappingPK != null && !this.otsUserMappingPK.equals(other.otsUserMappingPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsUserMapping[ otsUserMappingPK=" + otsUserMappingPK + " ]";
    }
    
}
