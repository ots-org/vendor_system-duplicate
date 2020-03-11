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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author SERAJKU
 */
@Entity
@Table(name = "ots_scheduler")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtsScheduler.findAll", query = "SELECT o FROM OtsScheduler o"),
    @NamedQuery(name = "OtsScheduler.findByOtsSchedulerId", query = "SELECT o FROM OtsScheduler o WHERE o.otsSchedulerId = :otsSchedulerId"),
    @NamedQuery(name = "OtsScheduler.findByOtsOrderQty", query = "SELECT o FROM OtsScheduler o WHERE o.otsOrderQty = :otsOrderQty"),
    @NamedQuery(name = "OtsScheduler.findByOtsSchedulerType", query = "SELECT o FROM OtsScheduler o WHERE o.otsSchedulerType = :otsSchedulerType"),
    @NamedQuery(name = "OtsScheduler.findByOtsSchedulerWkdy", query = "SELECT o FROM OtsScheduler o WHERE o.otsSchedulerWkdy = :otsSchedulerWkdy"),
    @NamedQuery(name = "OtsScheduler.findByOtsSchedulerStDt", query = "SELECT o FROM OtsScheduler o WHERE o.otsSchedulerStDt = :otsSchedulerStDt"),
    @NamedQuery(name = "OtsScheduler.findByOtsSchedulerEtDt", query = "SELECT o FROM OtsScheduler o WHERE o.otsSchedulerEtDt = :otsSchedulerEtDt")})
public class OtsScheduler implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ots_scheduler_id")
    private Integer otsSchedulerId;
    @Column(name = "ots_order_qty")
    private Integer otsOrderQty;
    @Column(name = "ots_scheduler_type")
    private String otsSchedulerType;
    @Column(name = "ots_scheduler_wkdy")
    private String otsSchedulerWkdy;
    @Column(name = "ots_scheduler_st_dt")
    @Temporal(TemporalType.DATE)
    private Date otsSchedulerStDt;
    @Column(name = "ots_scheduler_et_dt")
    @Temporal(TemporalType.DATE)
    private Date otsSchedulerEtDt;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsSchedulerId")
    private Collection<OtsRequestOrder> otsRequestOrderCollection;
    @JoinColumn(name = "ots_product_id", referencedColumnName = "ots_product_id")
    @ManyToOne(optional = false)
    private OtsProduct otsProductId;
    @JoinColumn(name = "ots_distributor_id", referencedColumnName = "ots_users_id")
    @ManyToOne(optional = false)
    private OtsUsers otsDistributorId;
    @JoinColumn(name = "ots_customer_id", referencedColumnName = "ots_users_id")
    @ManyToOne(optional = false)
    private OtsUsers otsCustomerId;

    public OtsScheduler() {
    }

    public OtsScheduler(Integer otsSchedulerId) {
        this.otsSchedulerId = otsSchedulerId;
    }

    public Integer getOtsSchedulerId() {
        return otsSchedulerId;
    }

    public void setOtsSchedulerId(Integer otsSchedulerId) {
        this.otsSchedulerId = otsSchedulerId;
    }

    public Integer getOtsOrderQty() {
        return otsOrderQty;
    }

    public void setOtsOrderQty(Integer otsOrderQty) {
        this.otsOrderQty = otsOrderQty;
    }

    public String getOtsSchedulerType() {
        return otsSchedulerType;
    }

    public void setOtsSchedulerType(String otsSchedulerType) {
        this.otsSchedulerType = otsSchedulerType;
    }

    public String getOtsSchedulerWkdy() {
        return otsSchedulerWkdy;
    }

    public void setOtsSchedulerWkdy(String otsSchedulerWkdy) {
        this.otsSchedulerWkdy = otsSchedulerWkdy;
    }

    public Date getOtsSchedulerStDt() {
        return otsSchedulerStDt;
    }

    public void setOtsSchedulerStDt(Date otsSchedulerStDt) {
        this.otsSchedulerStDt = otsSchedulerStDt;
    }

    public Date getOtsSchedulerEtDt() {
        return otsSchedulerEtDt;
    }

    public void setOtsSchedulerEtDt(Date otsSchedulerEtDt) {
        this.otsSchedulerEtDt = otsSchedulerEtDt;
    }

    @XmlTransient
    public Collection<OtsRequestOrder> getOtsRequestOrderCollection() {
        return otsRequestOrderCollection;
    }

    public void setOtsRequestOrderCollection(Collection<OtsRequestOrder> otsRequestOrderCollection) {
        this.otsRequestOrderCollection = otsRequestOrderCollection;
    }

    public OtsProduct getOtsProductId() {
        return otsProductId;
    }

    public void setOtsProductId(OtsProduct otsProductId) {
        this.otsProductId = otsProductId;
    }

    public OtsUsers getOtsDistributorId() {
        return otsDistributorId;
    }

    public void setOtsDistributorId(OtsUsers otsDistributorId) {
        this.otsDistributorId = otsDistributorId;
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
        hash += (otsSchedulerId != null ? otsSchedulerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsScheduler)) {
            return false;
        }
        OtsScheduler other = (OtsScheduler) object;
        if ((this.otsSchedulerId == null && other.otsSchedulerId != null) || (this.otsSchedulerId != null && !this.otsSchedulerId.equals(other.otsSchedulerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsScheduler[ otsSchedulerId=" + otsSchedulerId + " ]";
    }
    
}
