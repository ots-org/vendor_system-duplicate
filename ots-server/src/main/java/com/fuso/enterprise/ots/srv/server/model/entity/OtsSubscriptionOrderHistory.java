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
@Table(name = "ots_subscription_order_history")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtsSubscriptionOrderHistory.findAll", query = "SELECT o FROM OtsSubscriptionOrderHistory o")
    , @NamedQuery(name = "OtsSubscriptionOrderHistory.findByOtsSubscriptionOrderHistoryId", query = "SELECT o FROM OtsSubscriptionOrderHistory o WHERE o.otsSubscriptionOrderHistoryId = :otsSubscriptionOrderHistoryId")
    , @NamedQuery(name = "OtsSubscriptionOrderHistory.findByOtsSubscriptionTransactionId", query = "SELECT o FROM OtsSubscriptionOrderHistory o WHERE o.otsSubscriptionTransactionId = :otsSubscriptionTransactionId")
    , @NamedQuery(name = "OtsSubscriptionOrderHistory.findByOtsSubscriptionHistoryMode", query = "SELECT o FROM OtsSubscriptionOrderHistory o WHERE o.otsSubscriptionHistoryMode = :otsSubscriptionHistoryMode")
    , @NamedQuery(name = "OtsSubscriptionOrderHistory.findByOtsSubscriptionHistoryStatus", query = "SELECT o FROM OtsSubscriptionOrderHistory o WHERE o.otsSubscriptionHistoryStatus = :otsSubscriptionHistoryStatus")
    , @NamedQuery(name = "OtsSubscriptionOrderHistory.findByOtsSubscriptionHistoryTimestamp", query = "SELECT o FROM OtsSubscriptionOrderHistory o WHERE o.otsSubscriptionHistoryTimestamp = :otsSubscriptionHistoryTimestamp")
    , @NamedQuery(name = "OtsSubscriptionOrderHistory.findByOtsSubscriptionOrderCost", query = "SELECT o FROM OtsSubscriptionOrderHistory o WHERE o.otsSubscriptionOrderCost = :otsSubscriptionOrderCost")
    , @NamedQuery(name = "OtsSubscriptionOrderHistory.findByOtsSubscriptionName", query = "SELECT o FROM OtsSubscriptionOrderHistory o WHERE o.otsSubscriptionName = :otsSubscriptionName")})
public class OtsSubscriptionOrderHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ots_subscription_order_history_id")
    private Integer otsSubscriptionOrderHistoryId;
    @Size(max = 45)
    @Column(name = "ots_subscription__transaction_id")
    private String otsSubscriptionTransactionId;
    @Size(max = 45)
    @Column(name = "ots_subscription_history_mode")
    private String otsSubscriptionHistoryMode;
    @Size(max = 45)
    @Column(name = "ots_subscription_history_status")
    private String otsSubscriptionHistoryStatus;
    @Column(name = "ots_subscription_history_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otsSubscriptionHistoryTimestamp;
    @Size(max = 45)
    @Column(name = "ots_subscription_order_cost")
    private String otsSubscriptionOrderCost;
    @Size(max = 45)
    @Column(name = "ots_subscription_name")
    private String otsSubscriptionName;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsSubscriptionOrderHistoryId")
    private Collection<OtsSubscriptionOrderroledetails> otsSubscriptionOrderroledetailsCollection;
    @JoinColumn(name = "ots_users_id", referencedColumnName = "ots_users_id")
    @ManyToOne(optional = false)
    private OtsUsers otsUsersId;

    public OtsSubscriptionOrderHistory() {
    }

    public OtsSubscriptionOrderHistory(Integer otsSubscriptionOrderHistoryId) {
        this.otsSubscriptionOrderHistoryId = otsSubscriptionOrderHistoryId;
    }

    public Integer getOtsSubscriptionOrderHistoryId() {
        return otsSubscriptionOrderHistoryId;
    }

    public void setOtsSubscriptionOrderHistoryId(Integer otsSubscriptionOrderHistoryId) {
        this.otsSubscriptionOrderHistoryId = otsSubscriptionOrderHistoryId;
    }

    public String getOtsSubscriptionTransactionId() {
        return otsSubscriptionTransactionId;
    }

    public void setOtsSubscriptionTransactionId(String otsSubscriptionTransactionId) {
        this.otsSubscriptionTransactionId = otsSubscriptionTransactionId;
    }

    public String getOtsSubscriptionHistoryMode() {
        return otsSubscriptionHistoryMode;
    }

    public void setOtsSubscriptionHistoryMode(String otsSubscriptionHistoryMode) {
        this.otsSubscriptionHistoryMode = otsSubscriptionHistoryMode;
    }

    public String getOtsSubscriptionHistoryStatus() {
        return otsSubscriptionHistoryStatus;
    }

    public void setOtsSubscriptionHistoryStatus(String otsSubscriptionHistoryStatus) {
        this.otsSubscriptionHistoryStatus = otsSubscriptionHistoryStatus;
    }

    public Date getOtsSubscriptionHistoryTimestamp() {
        return otsSubscriptionHistoryTimestamp;
    }

    public void setOtsSubscriptionHistoryTimestamp(Date otsSubscriptionHistoryTimestamp) {
        this.otsSubscriptionHistoryTimestamp = otsSubscriptionHistoryTimestamp;
    }

    public String getOtsSubscriptionOrderCost() {
        return otsSubscriptionOrderCost;
    }

    public void setOtsSubscriptionOrderCost(String otsSubscriptionOrderCost) {
        this.otsSubscriptionOrderCost = otsSubscriptionOrderCost;
    }

    public String getOtsSubscriptionName() {
        return otsSubscriptionName;
    }

    public void setOtsSubscriptionName(String otsSubscriptionName) {
        this.otsSubscriptionName = otsSubscriptionName;
    }

    @XmlTransient
    public Collection<OtsSubscriptionOrderroledetails> getOtsSubscriptionOrderroledetailsCollection() {
        return otsSubscriptionOrderroledetailsCollection;
    }

    public void setOtsSubscriptionOrderroledetailsCollection(Collection<OtsSubscriptionOrderroledetails> otsSubscriptionOrderroledetailsCollection) {
        this.otsSubscriptionOrderroledetailsCollection = otsSubscriptionOrderroledetailsCollection;
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
        hash += (otsSubscriptionOrderHistoryId != null ? otsSubscriptionOrderHistoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsSubscriptionOrderHistory)) {
            return false;
        }
        OtsSubscriptionOrderHistory other = (OtsSubscriptionOrderHistory) object;
        if ((this.otsSubscriptionOrderHistoryId == null && other.otsSubscriptionOrderHistoryId != null) || (this.otsSubscriptionOrderHistoryId != null && !this.otsSubscriptionOrderHistoryId.equals(other.otsSubscriptionOrderHistoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsSubscriptionOrderHistory[ otsSubscriptionOrderHistoryId=" + otsSubscriptionOrderHistoryId + " ]";
    }
    
}
