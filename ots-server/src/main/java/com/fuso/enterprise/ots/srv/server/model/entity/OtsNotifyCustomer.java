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
@Table(name = "ots_notify_customer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtsNotifyCustomer.findAll", query = "SELECT o FROM OtsNotifyCustomer o")
    , @NamedQuery(name = "OtsNotifyCustomer.findByOtsNotifyCustomerId", query = "SELECT o FROM OtsNotifyCustomer o WHERE o.otsNotifyCustomerId = :otsNotifyCustomerId")})
public class OtsNotifyCustomer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ots_notify_customer_id")
    private Integer otsNotifyCustomerId;
    @JoinColumn(name = "ots_product_id", referencedColumnName = "ots_product_id")
    @ManyToOne(optional = false)
    private OtsProduct otsProductId;
    @JoinColumn(name = "ots_users_id", referencedColumnName = "ots_users_id")
    @ManyToOne(optional = false)
    private OtsUsers otsUsersId;

    public OtsNotifyCustomer() {
    }

    public OtsNotifyCustomer(Integer otsNotifyCustomerId) {
        this.otsNotifyCustomerId = otsNotifyCustomerId;
    }

    public Integer getOtsNotifyCustomerId() {
        return otsNotifyCustomerId;
    }

    public void setOtsNotifyCustomerId(Integer otsNotifyCustomerId) {
        this.otsNotifyCustomerId = otsNotifyCustomerId;
    }

    public OtsProduct getOtsProductId() {
        return otsProductId;
    }

    public void setOtsProductId(OtsProduct otsProductId) {
        this.otsProductId = otsProductId;
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
        hash += (otsNotifyCustomerId != null ? otsNotifyCustomerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsNotifyCustomer)) {
            return false;
        }
        OtsNotifyCustomer other = (OtsNotifyCustomer) object;
        if ((this.otsNotifyCustomerId == null && other.otsNotifyCustomerId != null) || (this.otsNotifyCustomerId != null && !this.otsNotifyCustomerId.equals(other.otsNotifyCustomerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsNotifyCustomer[ otsNotifyCustomerId=" + otsNotifyCustomerId + " ]";
    }
    
}
