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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author SABBABU
 */
@Entity
@Table(name = "ots_subscription_order")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtsSubscriptionOrder.findAll", query = "SELECT o FROM OtsSubscriptionOrder o")
    , @NamedQuery(name = "OtsSubscriptionOrder.findByOtsSubscriptionOrderId", query = "SELECT o FROM OtsSubscriptionOrder o WHERE o.otsSubscriptionOrderId = :otsSubscriptionOrderId")
    , @NamedQuery(name = "OtsSubscriptionOrder.findByOtsSubscriptionName", query = "SELECT o FROM OtsSubscriptionOrder o WHERE o.otsSubscriptionName = :otsSubscriptionName")
    , @NamedQuery(name = "OtsSubscriptionOrder.findByOtsUsersExpirationDate", query = "SELECT o FROM OtsSubscriptionOrder o WHERE o.otsUsersExpirationDate = :otsUsersExpirationDate")
    , @NamedQuery(name = "OtsSubscriptionOrder.findByOtsSubscriptionOrderStatus", query = "SELECT o FROM OtsSubscriptionOrder o WHERE o.otsSubscriptionOrderStatus = :otsSubscriptionOrderStatus")
    , @NamedQuery(name = "OtsSubscriptionOrder.findByOtsSubscriptionType", query = "SELECT o FROM OtsSubscriptionOrder o WHERE o.otsSubscriptionType = :otsSubscriptionType")
    , @NamedQuery(name = "OtsSubscriptionOrder.findByOtsSubscriptionOrdercost", query = "SELECT o FROM OtsSubscriptionOrder o WHERE o.otsSubscriptionOrdercost = :otsSubscriptionOrdercost")})
public class OtsSubscriptionOrder implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ots_subscription_order_id")
    private Integer otsSubscriptionOrderId;
    @Size(max = 45)
    @Column(name = "ots_subscription_name")
    private String otsSubscriptionName;
    @Column(name = "ots_users_expiration_date")
    @Temporal(TemporalType.DATE)
    private Date otsUsersExpirationDate;
    @Size(max = 45)
    @Column(name = "ots_subscription_order_status")
    private String otsSubscriptionOrderStatus;
    @Size(max = 45)
    @Column(name = "ots_subscription_type")
    private String otsSubscriptionType;
    @Size(max = 45)
    @Column(name = "ots_subscription_ordercost")
    private String otsSubscriptionOrdercost;
    @JoinColumn(name = "ots_users_id", referencedColumnName = "ots_users_id")
    @ManyToOne(optional = false)
    private OtsUsers otsUsersId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsSubscriptionOrderId")
    private Collection<OtsSubscriptionRoleMapping> otsSubscriptionRoleMappingCollection;

    public OtsSubscriptionOrder() {
    }

    public OtsSubscriptionOrder(Integer otsSubscriptionOrderId) {
        this.otsSubscriptionOrderId = otsSubscriptionOrderId;
    }

    public Integer getOtsSubscriptionOrderId() {
        return otsSubscriptionOrderId;
    }

    public void setOtsSubscriptionOrderId(Integer otsSubscriptionOrderId) {
        this.otsSubscriptionOrderId = otsSubscriptionOrderId;
    }

    public String getOtsSubscriptionName() {
        return otsSubscriptionName;
    }

    public void setOtsSubscriptionName(String otsSubscriptionName) {
        this.otsSubscriptionName = otsSubscriptionName;
    }

    public Date getOtsUsersExpirationDate() {
        return otsUsersExpirationDate;
    }

    public void setOtsUsersExpirationDate(Date otsUsersExpirationDate) {
        this.otsUsersExpirationDate = otsUsersExpirationDate;
    }

    public String getOtsSubscriptionOrderStatus() {
        return otsSubscriptionOrderStatus;
    }

    public void setOtsSubscriptionOrderStatus(String otsSubscriptionOrderStatus) {
        this.otsSubscriptionOrderStatus = otsSubscriptionOrderStatus;
    }

    public String getOtsSubscriptionType() {
        return otsSubscriptionType;
    }

    public void setOtsSubscriptionType(String otsSubscriptionType) {
        this.otsSubscriptionType = otsSubscriptionType;
    }

    public String getOtsSubscriptionOrdercost() {
        return otsSubscriptionOrdercost;
    }

    public void setOtsSubscriptionOrdercost(String otsSubscriptionOrdercost) {
        this.otsSubscriptionOrdercost = otsSubscriptionOrdercost;
    }

    public OtsUsers getOtsUsersId() {
        return otsUsersId;
    }

    public void setOtsUsersId(OtsUsers otsUsersId) {
        this.otsUsersId = otsUsersId;
    }

    @XmlTransient
    public Collection<OtsSubscriptionRoleMapping> getOtsSubscriptionRoleMappingCollection() {
        return otsSubscriptionRoleMappingCollection;
    }

    public void setOtsSubscriptionRoleMappingCollection(Collection<OtsSubscriptionRoleMapping> otsSubscriptionRoleMappingCollection) {
        this.otsSubscriptionRoleMappingCollection = otsSubscriptionRoleMappingCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (otsSubscriptionOrderId != null ? otsSubscriptionOrderId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsSubscriptionOrder)) {
            return false;
        }
        OtsSubscriptionOrder other = (OtsSubscriptionOrder) object;
        if ((this.otsSubscriptionOrderId == null && other.otsSubscriptionOrderId != null) || (this.otsSubscriptionOrderId != null && !this.otsSubscriptionOrderId.equals(other.otsSubscriptionOrderId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsSubscriptionOrder[ otsSubscriptionOrderId=" + otsSubscriptionOrderId + " ]";
    }
    
}
