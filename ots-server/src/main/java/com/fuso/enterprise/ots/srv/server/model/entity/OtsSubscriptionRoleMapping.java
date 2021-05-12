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
 * @author lenovo
 */
@Entity
@Table(name = "ots_subscription_role_mapping")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtsSubscriptionRoleMapping.findAll", query = "SELECT o FROM OtsSubscriptionRoleMapping o")
    , @NamedQuery(name = "OtsSubscriptionRoleMapping.findByOtsSubscriptionRoleMappingId", query = "SELECT o FROM OtsSubscriptionRoleMapping o WHERE o.otsSubscriptionRoleMappingId = :otsSubscriptionRoleMappingId")
    , @NamedQuery(name = "OtsSubscriptionRoleMapping.findByOtsSubscriptionUsercount", query = "SELECT o FROM OtsSubscriptionRoleMapping o WHERE o.otsSubscriptionUsercount = :otsSubscriptionUsercount")})
public class OtsSubscriptionRoleMapping implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ots_subscription_role_mapping_id")
    private Integer otsSubscriptionRoleMappingId;
    @Column(name = "ots_subscription_usercount")
    private Integer otsSubscriptionUsercount;
    @JoinColumn(name = "ots_subscription_order_id", referencedColumnName = "ots_subscription_order_id")
    @ManyToOne(optional = false)
    private OtsSubscriptionOrder otsSubscriptionOrderId;
    @JoinColumn(name = "ots_user_role_id", referencedColumnName = "ots_user_role_id")
    @ManyToOne(optional = false)
    private OtsUserRole otsUserRoleId;

    public OtsSubscriptionRoleMapping() {
    }

    public OtsSubscriptionRoleMapping(Integer otsSubscriptionRoleMappingId) {
        this.otsSubscriptionRoleMappingId = otsSubscriptionRoleMappingId;
    }

    public Integer getOtsSubscriptionRoleMappingId() {
        return otsSubscriptionRoleMappingId;
    }

    public void setOtsSubscriptionRoleMappingId(Integer otsSubscriptionRoleMappingId) {
        this.otsSubscriptionRoleMappingId = otsSubscriptionRoleMappingId;
    }

    public Integer getOtsSubscriptionUsercount() {
        return otsSubscriptionUsercount;
    }

    public void setOtsSubscriptionUsercount(Integer otsSubscriptionUsercount) {
        this.otsSubscriptionUsercount = otsSubscriptionUsercount;
    }

    public OtsSubscriptionOrder getOtsSubscriptionOrderId() {
        return otsSubscriptionOrderId;
    }

    public void setOtsSubscriptionOrderId(OtsSubscriptionOrder otsSubscriptionOrderId) {
        this.otsSubscriptionOrderId = otsSubscriptionOrderId;
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
        hash += (otsSubscriptionRoleMappingId != null ? otsSubscriptionRoleMappingId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsSubscriptionRoleMapping)) {
            return false;
        }
        OtsSubscriptionRoleMapping other = (OtsSubscriptionRoleMapping) object;
        if ((this.otsSubscriptionRoleMappingId == null && other.otsSubscriptionRoleMappingId != null) || (this.otsSubscriptionRoleMappingId != null && !this.otsSubscriptionRoleMappingId.equals(other.otsSubscriptionRoleMappingId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsSubscriptionRoleMapping[ otsSubscriptionRoleMappingId=" + otsSubscriptionRoleMappingId + " ]";
    }
    
}
