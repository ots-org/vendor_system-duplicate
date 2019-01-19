/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fuso.enterprise.ots.srv.server.model.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author SERAJKU
 */
@Embeddable
public class OtsUserMappingPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "ots_user_mapping_id")
    private int otsUserMappingId;
    @Basic(optional = false)
    @Column(name = "ots_users_id")
    private int otsUsersId;

    public OtsUserMappingPK() {
    }

    public OtsUserMappingPK(int otsUserMappingId, int otsUsersId) {
        this.otsUserMappingId = otsUserMappingId;
        this.otsUsersId = otsUsersId;
    }

    public int getOtsUserMappingId() {
        return otsUserMappingId;
    }

    public void setOtsUserMappingId(int otsUserMappingId) {
        this.otsUserMappingId = otsUserMappingId;
    }

    public int getOtsUsersId() {
        return otsUsersId;
    }

    public void setOtsUsersId(int otsUsersId) {
        this.otsUsersId = otsUsersId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) otsUserMappingId;
        hash += (int) otsUsersId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsUserMappingPK)) {
            return false;
        }
        OtsUserMappingPK other = (OtsUserMappingPK) object;
        if (this.otsUserMappingId != other.otsUserMappingId) {
            return false;
        }
        if (this.otsUsersId != other.otsUsersId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsUserMappingPK[ otsUserMappingId=" + otsUserMappingId + ", otsUsersId=" + otsUsersId + " ]";
    }
    
}
