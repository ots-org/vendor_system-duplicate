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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SERAJKU
 */
@Entity
@Table(name = "ots_subscription")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtsSubscription.findAll", query = "SELECT o FROM OtsSubscription o"),
    @NamedQuery(name = "OtsSubscription.findByOtsrSubscriptionId", query = "SELECT o FROM OtsSubscription o WHERE o.otsrSubscriptionId = :otsrSubscriptionId"),
    @NamedQuery(name = "OtsSubscription.findByOtsSubscriptionStatus", query = "SELECT o FROM OtsSubscription o WHERE o.otsSubscriptionStatus = :otsSubscriptionStatus"),
    @NamedQuery(name = "OtsSubscription.findByOtsSubscriptionTotalusers", query = "SELECT o FROM OtsSubscription o WHERE o.otsSubscriptionTotalusers = :otsSubscriptionTotalusers")})
public class OtsSubscription implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "otsr_subscription_id")
    private Integer otsrSubscriptionId;
    @Column(name = "ots_subscription_status")
    private String otsSubscriptionStatus;
    @Column(name = "ots_subscription_totalusers")
    private String otsSubscriptionTotalusers;
    @JoinColumn(name = "ots_subscription_userrole_id", referencedColumnName = "ots_user_role_id")
    @ManyToOne(optional = false)
    private OtsUserRole otsSubscriptionUserroleId;
    @JoinColumn(name = "ots_subscription_users_id", referencedColumnName = "ots_users_id")
    @ManyToOne(optional = false)
    private OtsUsers otsSubscriptionUsersId;

    public OtsSubscription() {
    }

    public OtsSubscription(Integer otsrSubscriptionId) {
        this.otsrSubscriptionId = otsrSubscriptionId;
    }

    public Integer getOtsrSubscriptionId() {
        return otsrSubscriptionId;
    }

    public void setOtsrSubscriptionId(Integer otsrSubscriptionId) {
        this.otsrSubscriptionId = otsrSubscriptionId;
    }

    public String getOtsSubscriptionStatus() {
        return otsSubscriptionStatus;
    }

    public void setOtsSubscriptionStatus(String otsSubscriptionStatus) {
        this.otsSubscriptionStatus = otsSubscriptionStatus;
    }

    public String getOtsSubscriptionTotalusers() {
        return otsSubscriptionTotalusers;
    }

    public void setOtsSubscriptionTotalusers(String otsSubscriptionTotalusers) {
        this.otsSubscriptionTotalusers = otsSubscriptionTotalusers;
    }

    public OtsUserRole getOtsSubscriptionUserroleId() {
        return otsSubscriptionUserroleId;
    }

    public void setOtsSubscriptionUserroleId(OtsUserRole otsSubscriptionUserroleId) {
        this.otsSubscriptionUserroleId = otsSubscriptionUserroleId;
    }

    public OtsUsers getOtsSubscriptionUsersId() {
        return otsSubscriptionUsersId;
    }

    public void setOtsSubscriptionUsersId(OtsUsers otsSubscriptionUsersId) {
        this.otsSubscriptionUsersId = otsSubscriptionUsersId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (otsrSubscriptionId != null ? otsrSubscriptionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsSubscription)) {
            return false;
        }
        OtsSubscription other = (OtsSubscription) object;
        if ((this.otsrSubscriptionId == null && other.otsrSubscriptionId != null) || (this.otsrSubscriptionId != null && !this.otsrSubscriptionId.equals(other.otsrSubscriptionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsSubscription[ otsrSubscriptionId=" + otsrSubscriptionId + " ]";
    }
    
}
