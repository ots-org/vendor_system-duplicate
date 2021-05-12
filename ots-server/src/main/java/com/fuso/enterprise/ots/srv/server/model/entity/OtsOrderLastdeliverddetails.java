/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fuso.enterprise.ots.srv.server.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lenovo
 */
@Entity
@Table(name = "ots_order_lastdeliverddetails")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtsOrderLastdeliverddetails.findAll", query = "SELECT o FROM OtsOrderLastdeliverddetails o")
    , @NamedQuery(name = "OtsOrderLastdeliverddetails.findByOtsOrderLastdeliverddetailsId", query = "SELECT o FROM OtsOrderLastdeliverddetails o WHERE o.otsOrderLastdeliverddetailsId = :otsOrderLastdeliverddetailsId")
    , @NamedQuery(name = "OtsOrderLastdeliverddetails.findByOtsOrderLastdeliverdDate", query = "SELECT o FROM OtsOrderLastdeliverddetails o WHERE o.otsOrderLastdeliverdDate = :otsOrderLastdeliverdDate")})
public class OtsOrderLastdeliverddetails implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ots_order_lastdeliverddetails_id")
    private Integer otsOrderLastdeliverddetailsId;
    @Column(name = "ots_order_lastdeliverd_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otsOrderLastdeliverdDate;
    @JoinColumn(name = "ots_distributor_id", referencedColumnName = "ots_users_id")
    @ManyToOne(optional = false)
    private OtsUsers otsDistributorId;

    public OtsOrderLastdeliverddetails() {
    }

    public OtsOrderLastdeliverddetails(Integer otsOrderLastdeliverddetailsId) {
        this.otsOrderLastdeliverddetailsId = otsOrderLastdeliverddetailsId;
    }

    public Integer getOtsOrderLastdeliverddetailsId() {
        return otsOrderLastdeliverddetailsId;
    }

    public void setOtsOrderLastdeliverddetailsId(Integer otsOrderLastdeliverddetailsId) {
        this.otsOrderLastdeliverddetailsId = otsOrderLastdeliverddetailsId;
    }

    public Date getOtsOrderLastdeliverdDate() {
        return otsOrderLastdeliverdDate;
    }

    public void setOtsOrderLastdeliverdDate(Date otsOrderLastdeliverdDate) {
        this.otsOrderLastdeliverdDate = otsOrderLastdeliverdDate;
    }

    public OtsUsers getOtsDistributorId() {
        return otsDistributorId;
    }

    public void setOtsDistributorId(OtsUsers otsDistributorId) {
        this.otsDistributorId = otsDistributorId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (otsOrderLastdeliverddetailsId != null ? otsOrderLastdeliverddetailsId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsOrderLastdeliverddetails)) {
            return false;
        }
        OtsOrderLastdeliverddetails other = (OtsOrderLastdeliverddetails) object;
        if ((this.otsOrderLastdeliverddetailsId == null && other.otsOrderLastdeliverddetailsId != null) || (this.otsOrderLastdeliverddetailsId != null && !this.otsOrderLastdeliverddetailsId.equals(other.otsOrderLastdeliverddetailsId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsOrderLastdeliverddetails[ otsOrderLastdeliverddetailsId=" + otsOrderLastdeliverddetailsId + " ]";
    }
    
}
