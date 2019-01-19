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
public class OtsUsersPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "ots_users_id")
    private int otsUsersId;
    @Basic(optional = false)
    @Column(name = "ots_registration_id")
    private int otsRegistrationId;
    @Basic(optional = false)
    @Column(name = "ots_userrole_id")
    private int otsUserroleId;

    public OtsUsersPK() {
    }

    public OtsUsersPK(int otsUsersId, int otsRegistrationId, int otsUserroleId) {
        this.otsUsersId = otsUsersId;
        this.otsRegistrationId = otsRegistrationId;
        this.otsUserroleId = otsUserroleId;
    }

    public int getOtsUsersId() {
        return otsUsersId;
    }

    public void setOtsUsersId(int otsUsersId) {
        this.otsUsersId = otsUsersId;
    }

    public int getOtsRegistrationId() {
        return otsRegistrationId;
    }

    public void setOtsRegistrationId(int otsRegistrationId) {
        this.otsRegistrationId = otsRegistrationId;
    }

    public int getOtsUserroleId() {
        return otsUserroleId;
    }

    public void setOtsUserroleId(int otsUserroleId) {
        this.otsUserroleId = otsUserroleId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) otsUsersId;
        hash += (int) otsRegistrationId;
        hash += (int) otsUserroleId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsUsersPK)) {
            return false;
        }
        OtsUsersPK other = (OtsUsersPK) object;
        if (this.otsUsersId != other.otsUsersId) {
            return false;
        }
        if (this.otsRegistrationId != other.otsRegistrationId) {
            return false;
        }
        if (this.otsUserroleId != other.otsUserroleId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsUsersPK[ otsUsersId=" + otsUsersId + ", otsRegistrationId=" + otsRegistrationId + ", otsUserroleId=" + otsUserroleId + " ]";
    }
    
}
