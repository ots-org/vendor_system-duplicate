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
public class OtsRegistrationPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "ots_registration_id")
    private int otsRegistrationId;
    @Basic(optional = false)
    @Column(name = "ots_users_mapped_to")
    private int otsUsersMappedTo;
    @Basic(optional = false)
    @Column(name = "ots_user_role_id")
    private int otsUserRoleId;

    public OtsRegistrationPK() {
    }

    public OtsRegistrationPK(int otsRegistrationId, int otsUsersMappedTo, int otsUserRoleId) {
        this.otsRegistrationId = otsRegistrationId;
        this.otsUsersMappedTo = otsUsersMappedTo;
        this.otsUserRoleId = otsUserRoleId;
    }

    public int getOtsRegistrationId() {
        return otsRegistrationId;
    }

    public void setOtsRegistrationId(int otsRegistrationId) {
        this.otsRegistrationId = otsRegistrationId;
    }

    public int getOtsUsersMappedTo() {
        return otsUsersMappedTo;
    }

    public void setOtsUsersMappedTo(int otsUsersMappedTo) {
        this.otsUsersMappedTo = otsUsersMappedTo;
    }

    public int getOtsUserRoleId() {
        return otsUserRoleId;
    }

    public void setOtsUserRoleId(int otsUserRoleId) {
        this.otsUserRoleId = otsUserRoleId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) otsRegistrationId;
        hash += (int) otsUsersMappedTo;
        hash += (int) otsUserRoleId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsRegistrationPK)) {
            return false;
        }
        OtsRegistrationPK other = (OtsRegistrationPK) object;
        if (this.otsRegistrationId != other.otsRegistrationId) {
            return false;
        }
        if (this.otsUsersMappedTo != other.otsUsersMappedTo) {
            return false;
        }
        if (this.otsUserRoleId != other.otsUserRoleId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsRegistrationPK[ otsRegistrationId=" + otsRegistrationId + ", otsUsersMappedTo=" + otsUsersMappedTo + ", otsUserRoleId=" + otsUserRoleId + " ]";
    }
    
}
