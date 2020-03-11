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
@Table(name = "ots_subscription_history")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtsSubscriptionHistory.findAll", query = "SELECT o FROM OtsSubscriptionHistory o"),
    @NamedQuery(name = "OtsSubscriptionHistory.findByOtsSubscriptionHistoryId", query = "SELECT o FROM OtsSubscriptionHistory o WHERE o.otsSubscriptionHistoryId = :otsSubscriptionHistoryId"),
    @NamedQuery(name = "OtsSubscriptionHistory.findByOtsSubscriptionHistoryUsercount", query = "SELECT o FROM OtsSubscriptionHistory o WHERE o.otsSubscriptionHistoryUsercount = :otsSubscriptionHistoryUsercount"),
    @NamedQuery(name = "OtsSubscriptionHistory.findByOtsSubscriptionHistoryStatus", query = "SELECT o FROM OtsSubscriptionHistory o WHERE o.otsSubscriptionHistoryStatus = :otsSubscriptionHistoryStatus"),
    @NamedQuery(name = "OtsSubscriptionHistory.findByOtssubscriptionhistorytransactionId", query = "SELECT o FROM OtsSubscriptionHistory o WHERE o.otssubscriptionhistorytransactionId = :otssubscriptionhistorytransactionId")})
public class OtsSubscriptionHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ots_subscription_history_id")
    private Integer otsSubscriptionHistoryId;
    @Column(name = "ots_subscription_history_usercount")
    private String otsSubscriptionHistoryUsercount;
    @Column(name = "ots_subscription_history_status")
    private String otsSubscriptionHistoryStatus;
    @Column(name = "ots_subscription_history_transactionId")
    private String otssubscriptionhistorytransactionId;
    @JoinColumn(name = "ots_user_role", referencedColumnName = "ots_user_role_id")
    @ManyToOne(optional = false)
    private OtsUserRole otsUserRole;
    @JoinColumn(name = "ots_subscription_users_id", referencedColumnName = "ots_users_id")
    @ManyToOne(optional = false)
    private OtsUsers otsSubscriptionUsersId;

    public OtsSubscriptionHistory() {
    }

    public OtsSubscriptionHistory(Integer otsSubscriptionHistoryId) {
        this.otsSubscriptionHistoryId = otsSubscriptionHistoryId;
    }

    public Integer getOtsSubscriptionHistoryId() {
        return otsSubscriptionHistoryId;
    }

    public void setOtsSubscriptionHistoryId(Integer otsSubscriptionHistoryId) {
        this.otsSubscriptionHistoryId = otsSubscriptionHistoryId;
    }

    public String getOtsSubscriptionHistoryUsercount() {
        return otsSubscriptionHistoryUsercount;
    }

    public void setOtsSubscriptionHistoryUsercount(String otsSubscriptionHistoryUsercount) {
        this.otsSubscriptionHistoryUsercount = otsSubscriptionHistoryUsercount;
    }

    public String getOtsSubscriptionHistoryStatus() {
        return otsSubscriptionHistoryStatus;
    }

    public void setOtsSubscriptionHistoryStatus(String otsSubscriptionHistoryStatus) {
        this.otsSubscriptionHistoryStatus = otsSubscriptionHistoryStatus;
    }

    public String getOtssubscriptionhistorytransactionId() {
        return otssubscriptionhistorytransactionId;
    }

    public void setOtssubscriptionhistorytransactionId(String otssubscriptionhistorytransactionId) {
        this.otssubscriptionhistorytransactionId = otssubscriptionhistorytransactionId;
    }

    public OtsUserRole getOtsUserRole() {
        return otsUserRole;
    }

    public void setOtsUserRole(OtsUserRole otsUserRole) {
        this.otsUserRole = otsUserRole;
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
        hash += (otsSubscriptionHistoryId != null ? otsSubscriptionHistoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsSubscriptionHistory)) {
            return false;
        }
        OtsSubscriptionHistory other = (OtsSubscriptionHistory) object;
        if ((this.otsSubscriptionHistoryId == null && other.otsSubscriptionHistoryId != null) || (this.otsSubscriptionHistoryId != null && !this.otsSubscriptionHistoryId.equals(other.otsSubscriptionHistoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsSubscriptionHistory[ otsSubscriptionHistoryId=" + otsSubscriptionHistoryId + " ]";
    }
    
}
