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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author SERAJKU
 */
@Entity
@Table(name = "ots_user_role")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtsUserRole.findAll", query = "SELECT o FROM OtsUserRole o"),
    @NamedQuery(name = "OtsUserRole.findByOtsUserRoleId", query = "SELECT o FROM OtsUserRole o WHERE o.otsUserRoleId = :otsUserRoleId"),
    @NamedQuery(name = "OtsUserRole.findByOtsUserRoleCode", query = "SELECT o FROM OtsUserRole o WHERE o.otsUserRoleCode = :otsUserRoleCode"),
    @NamedQuery(name = "OtsUserRole.findByOtsUserRoleName", query = "SELECT o FROM OtsUserRole o WHERE o.otsUserRoleName = :otsUserRoleName"),
    @NamedQuery(name = "OtsUserRole.findByOtsUserRoleStatus", query = "SELECT o FROM OtsUserRole o WHERE o.otsUserRoleStatus = :otsUserRoleStatus")})
public class OtsUserRole implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ots_user_role_id")
    private Integer otsUserRoleId;
    @Column(name = "ots_user_role_code")
    private String otsUserRoleCode;
    @Column(name = "ots_user_role_name")
    private String otsUserRoleName;
    @Column(name = "ots_user_role_status")
    private String otsUserRoleStatus;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsSubscriptionUserroleId")
    private Collection<OtsSubscription> otsSubscriptionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsUserRoleId")
    private Collection<OtsUsers> otsUsersCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsUserRoleId")
    private Collection<OtsRegistration> otsRegistrationCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsUserRole")
    private Collection<OtsSubscriptionHistory> otsSubscriptionHistoryCollection;

    public OtsUserRole() {
    }

    public OtsUserRole(Integer otsUserRoleId) {
        this.otsUserRoleId = otsUserRoleId;
    }

    public Integer getOtsUserRoleId() {
        return otsUserRoleId;
    }

    public void setOtsUserRoleId(Integer otsUserRoleId) {
        this.otsUserRoleId = otsUserRoleId;
    }

    public String getOtsUserRoleCode() {
        return otsUserRoleCode;
    }

    public void setOtsUserRoleCode(String otsUserRoleCode) {
        this.otsUserRoleCode = otsUserRoleCode;
    }

    public String getOtsUserRoleName() {
        return otsUserRoleName;
    }

    public void setOtsUserRoleName(String otsUserRoleName) {
        this.otsUserRoleName = otsUserRoleName;
    }

    public String getOtsUserRoleStatus() {
        return otsUserRoleStatus;
    }

    public void setOtsUserRoleStatus(String otsUserRoleStatus) {
        this.otsUserRoleStatus = otsUserRoleStatus;
    }
    @XmlTransient
    public Collection<OtsSubscription> getOtsSubscriptionCollection() {
        return otsSubscriptionCollection;
    }

    public void setOtsSubscriptionCollection(Collection<OtsSubscription> otsSubscriptionCollection) {
        this.otsSubscriptionCollection = otsSubscriptionCollection;
    }

    @XmlTransient
    public Collection<OtsUsers> getOtsUsersCollection() {
        return otsUsersCollection;
    }

    public void setOtsUsersCollection(Collection<OtsUsers> otsUsersCollection) {
        this.otsUsersCollection = otsUsersCollection;
    }

    @XmlTransient
    public Collection<OtsRegistration> getOtsRegistrationCollection() {
        return otsRegistrationCollection;
    }

    public void setOtsRegistrationCollection(Collection<OtsRegistration> otsRegistrationCollection) {
        this.otsRegistrationCollection = otsRegistrationCollection;
    }

    @XmlTransient
    public Collection<OtsSubscriptionHistory> getOtsSubscriptionHistoryCollection() {
        return otsSubscriptionHistoryCollection;
    }

    public void setOtsSubscriptionHistoryCollection(Collection<OtsSubscriptionHistory> otsSubscriptionHistoryCollection) {
        this.otsSubscriptionHistoryCollection = otsSubscriptionHistoryCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (otsUserRoleId != null ? otsUserRoleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsUserRole)) {
            return false;
        }
        OtsUserRole other = (OtsUserRole) object;
        if ((this.otsUserRoleId == null && other.otsUserRoleId != null) || (this.otsUserRoleId != null && !this.otsUserRoleId.equals(other.otsUserRoleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsUserRole[ otsUserRoleId=" + otsUserRoleId + " ]";
    }
    
}
