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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SABBABU
 */
@Entity
@Table(name = "ots_subscription_orderroledetails")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtsSubscriptionOrderroledetails.findAll", query = "SELECT o FROM OtsSubscriptionOrderroledetails o")
    , @NamedQuery(name = "OtsSubscriptionOrderroledetails.findByOtssubscriptionorderRoleDetailsid", query = "SELECT o FROM OtsSubscriptionOrderroledetails o WHERE o.otssubscriptionorderRoleDetailsid = :otssubscriptionorderRoleDetailsid")
    , @NamedQuery(name = "OtsSubscriptionOrderroledetails.findByOtssubscriptionorderaddOns", query = "SELECT o FROM OtsSubscriptionOrderroledetails o WHERE o.otssubscriptionorderaddOns = :otssubscriptionorderaddOns")})
public class OtsSubscriptionOrderroledetails implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ots_subscription_orderRoleDetails_id")
    private Integer otssubscriptionorderRoleDetailsid;
    @Size(max = 45)
    @Column(name = "ots_subscription_order_addOns")
    private String otssubscriptionorderaddOns;
    @JoinColumn(name = "ots_subscription_order_history_id", referencedColumnName = "ots_subscription_order_history_id")
    @ManyToOne(optional = false)
    private OtsSubscriptionOrderHistory otsSubscriptionOrderHistoryId;
    @JoinColumn(name = "ots_user_role_id", referencedColumnName = "ots_user_role_id")
    @ManyToOne(optional = false)
    private OtsUserRole otsUserRoleId;

    public OtsSubscriptionOrderroledetails() {
    }

    public OtsSubscriptionOrderroledetails(Integer otssubscriptionorderRoleDetailsid) {
        this.otssubscriptionorderRoleDetailsid = otssubscriptionorderRoleDetailsid;
    }

    public Integer getOtssubscriptionorderRoleDetailsid() {
        return otssubscriptionorderRoleDetailsid;
    }

    public void setOtssubscriptionorderRoleDetailsid(Integer otssubscriptionorderRoleDetailsid) {
        this.otssubscriptionorderRoleDetailsid = otssubscriptionorderRoleDetailsid;
    }

    public String getOtssubscriptionorderaddOns() {
        return otssubscriptionorderaddOns;
    }

    public void setOtssubscriptionorderaddOns(String otssubscriptionorderaddOns) {
        this.otssubscriptionorderaddOns = otssubscriptionorderaddOns;
    }

    public OtsSubscriptionOrderHistory getOtsSubscriptionOrderHistoryId() {
        return otsSubscriptionOrderHistoryId;
    }

    public void setOtsSubscriptionOrderHistoryId(OtsSubscriptionOrderHistory otsSubscriptionOrderHistoryId) {
        this.otsSubscriptionOrderHistoryId = otsSubscriptionOrderHistoryId;
    }

    public OtsUserRole getOtsUserRoleId() {
        return otsUserRoleId;
    }

    public void setOtsUserRoleId(OtsUserRole otsUserRoleId) {
        this.otsUserRoleId = otsUserRoleId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (otssubscriptionorderRoleDetailsid != null ? otssubscriptionorderRoleDetailsid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsSubscriptionOrderroledetails)) {
            return false;
        }
        OtsSubscriptionOrderroledetails other = (OtsSubscriptionOrderroledetails) object;
        if ((this.otssubscriptionorderRoleDetailsid == null && other.otssubscriptionorderRoleDetailsid != null) || (this.otssubscriptionorderRoleDetailsid != null && !this.otssubscriptionorderRoleDetailsid.equals(other.otssubscriptionorderRoleDetailsid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsSubscriptionOrderroledetails[ otssubscriptionorderRoleDetailsid=" + otssubscriptionorderRoleDetailsid + " ]";
    }
    
}
