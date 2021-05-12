/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fuso.enterprise.ots.srv.server.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lenovo
 */
@Entity
@Table(name = "ots_customer_outstanding")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtsCustomerOutstanding.findAll", query = "SELECT o FROM OtsCustomerOutstanding o")
    , @NamedQuery(name = "OtsCustomerOutstanding.findByOtsCustomerOutstandingId", query = "SELECT o FROM OtsCustomerOutstanding o WHERE o.otsCustomerOutstandingId = :otsCustomerOutstandingId")
    , @NamedQuery(name = "OtsCustomerOutstanding.findByOtsCustomerOutstandingAmt", query = "SELECT o FROM OtsCustomerOutstanding o WHERE o.otsCustomerOutstandingAmt = :otsCustomerOutstandingAmt")
    , @NamedQuery(name = "OtsCustomerOutstanding.findByOtsCustomerOutstandingTimestamp", query = "SELECT o FROM OtsCustomerOutstanding o WHERE o.otsCustomerOutstandingTimestamp = :otsCustomerOutstandingTimestamp")
    , @NamedQuery(name = "OtsCustomerOutstanding.findByOtsCustomerOutstandingCreated", query = "SELECT o FROM OtsCustomerOutstanding o WHERE o.otsCustomerOutstandingCreated = :otsCustomerOutstandingCreated")})
public class OtsCustomerOutstanding implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ots_customer_outstanding_id")
    private Integer otsCustomerOutstandingId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ots_customer_outstanding_amt")
    private BigDecimal otsCustomerOutstandingAmt;
    @Column(name = "ots_customer_outstanding_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otsCustomerOutstandingTimestamp;
    @Column(name = "ots_customer_outstanding_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otsCustomerOutstandingCreated;
    @JoinColumn(name = "ots_customer_id", referencedColumnName = "ots_users_id")
    @OneToOne(optional = false)
    private OtsUsers otsCustomerId;

    public OtsCustomerOutstanding() {
    }

    public OtsCustomerOutstanding(Integer otsCustomerOutstandingId) {
        this.otsCustomerOutstandingId = otsCustomerOutstandingId;
    }

    public Integer getOtsCustomerOutstandingId() {
        return otsCustomerOutstandingId;
    }

    public void setOtsCustomerOutstandingId(Integer otsCustomerOutstandingId) {
        this.otsCustomerOutstandingId = otsCustomerOutstandingId;
    }

    public BigDecimal getOtsCustomerOutstandingAmt() {
        return otsCustomerOutstandingAmt;
    }

    public void setOtsCustomerOutstandingAmt(BigDecimal otsCustomerOutstandingAmt) {
        this.otsCustomerOutstandingAmt = otsCustomerOutstandingAmt;
    }

    public Date getOtsCustomerOutstandingTimestamp() {
        return otsCustomerOutstandingTimestamp;
    }

    public void setOtsCustomerOutstandingTimestamp(Date otsCustomerOutstandingTimestamp) {
        this.otsCustomerOutstandingTimestamp = otsCustomerOutstandingTimestamp;
    }

    public Date getOtsCustomerOutstandingCreated() {
        return otsCustomerOutstandingCreated;
    }

    public void setOtsCustomerOutstandingCreated(Date otsCustomerOutstandingCreated) {
        this.otsCustomerOutstandingCreated = otsCustomerOutstandingCreated;
    }

    public OtsUsers getOtsCustomerId() {
        return otsCustomerId;
    }

    public void setOtsCustomerId(OtsUsers otsCustomerId) {
        this.otsCustomerId = otsCustomerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (otsCustomerOutstandingId != null ? otsCustomerOutstandingId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsCustomerOutstanding)) {
            return false;
        }
        OtsCustomerOutstanding other = (OtsCustomerOutstanding) object;
        if ((this.otsCustomerOutstandingId == null && other.otsCustomerOutstandingId != null) || (this.otsCustomerOutstandingId != null && !this.otsCustomerOutstandingId.equals(other.otsCustomerOutstandingId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsCustomerOutstanding[ otsCustomerOutstandingId=" + otsCustomerOutstandingId + " ]";
    }
    
}
