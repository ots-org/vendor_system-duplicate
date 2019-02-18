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
@Table(name = "ots_bill")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtsBill.findAll", query = "SELECT o FROM OtsBill o"),
    @NamedQuery(name = "OtsBill.findByOtsBillId", query = "SELECT o FROM OtsBill o WHERE o.otsBillId = :otsBillId"),
    @NamedQuery(name = "OtsBill.findByOtsBillNumber", query = "SELECT o FROM OtsBill o WHERE o.otsBillNumber = :otsBillNumber"),
    @NamedQuery(name = "OtsBill.findByOtsBillAmount", query = "SELECT o FROM OtsBill o WHERE o.otsBillAmount = :otsBillAmount"),
    @NamedQuery(name = "OtsBill.findByOtsBillAmountReceived", query = "SELECT o FROM OtsBill o WHERE o.otsBillAmountReceived = :otsBillAmountReceived"),
    @NamedQuery(name = "OtsBill.findByOtsBillGenerated", query = "SELECT o FROM OtsBill o WHERE o.otsBillGenerated = :otsBillGenerated"),
    @NamedQuery(name = "OtsBill.findByOtsBillStatus", query = "SELECT o FROM OtsBill o WHERE o.otsBillStatus = :otsBillStatus")})
public class OtsBill implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ots_bill_id")
    private Integer otsBillId;
    @Column(name = "ots_bill_number")
    private String otsBillNumber;
    @Column(name = "ots_bill_amount")
    private Long otsBillAmount;
    @Column(name = "ots_bill_amount_received")
    private Long otsBillAmountReceived;
    @Column(name = "ots_bill_generated")
    private String otsBillGenerated;
    @Column(name = "ots_bill_status")
    private String otsBillStatus;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsBillId")
    private Collection<OtsOrder> otsOrderCollection;

    public OtsBill() {
    }

    public OtsBill(Integer otsBillId) {
        this.otsBillId = otsBillId;
    }

    public Integer getOtsBillId() {
        return otsBillId;
    }

    public void setOtsBillId(Integer otsBillId) {
        this.otsBillId = otsBillId;
    }

    public String getOtsBillNumber() {
        return otsBillNumber;
    }

    public void setOtsBillNumber(String otsBillNumber) {
        this.otsBillNumber = otsBillNumber;
    }

    public Long getOtsBillAmount() {
        return otsBillAmount;
    }

    public void setOtsBillAmount(Long otsBillAmount) {
        this.otsBillAmount = otsBillAmount;
    }

    public Long getOtsBillAmountReceived() {
        return otsBillAmountReceived;
    }

    public void setOtsBillAmountReceived(Long otsBillAmountReceived) {
        this.otsBillAmountReceived = otsBillAmountReceived;
    }

    public String getOtsBillGenerated() {
        return otsBillGenerated;
    }

    public void setOtsBillGenerated(String otsBillGenerated) {
        this.otsBillGenerated = otsBillGenerated;
    }

    public String getOtsBillStatus() {
        return otsBillStatus;
    }

    public void setOtsBillStatus(String otsBillStatus) {
        this.otsBillStatus = otsBillStatus;
    }

    @XmlTransient
    public Collection<OtsOrder> getOtsOrderCollection() {
        return otsOrderCollection;
    }

    public void setOtsOrderCollection(Collection<OtsOrder> otsOrderCollection) {
        this.otsOrderCollection = otsOrderCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (otsBillId != null ? otsBillId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsBill)) {
            return false;
        }
        OtsBill other = (OtsBill) object;
        if ((this.otsBillId == null && other.otsBillId != null) || (this.otsBillId != null && !this.otsBillId.equals(other.otsBillId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsBill[ otsBillId=" + otsBillId + " ]";
    }
    
}
