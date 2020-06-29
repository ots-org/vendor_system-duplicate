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
@Table(name = "ots_donation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtsDonation.findAll", query = "SELECT o FROM OtsDonation o")
    , @NamedQuery(name = "OtsDonation.findByOtsDonationId", query = "SELECT o FROM OtsDonation o WHERE o.otsDonationId = :otsDonationId")
    , @NamedQuery(name = "OtsDonation.findByOtsDonationAmount", query = "SELECT o FROM OtsDonation o WHERE o.otsDonationAmount = :otsDonationAmount")
    , @NamedQuery(name = "OtsDonation.findByOtsDonationPaymentid", query = "SELECT o FROM OtsDonation o WHERE o.otsDonationPaymentid = :otsDonationPaymentid")
    , @NamedQuery(name = "OtsDonation.findByOtsDonationDonatedqty", query = "SELECT o FROM OtsDonation o WHERE o.otsDonationDonatedqty = :otsDonationDonatedqty")
    , @NamedQuery(name = "OtsDonation.findByOtsDonationPaymentMethod", query = "SELECT o FROM OtsDonation o WHERE o.otsDonationPaymentMethod = :otsDonationPaymentMethod")
    , @NamedQuery(name = "OtsDonation.findByOtsDonationDate", query = "SELECT o FROM OtsDonation o WHERE o.otsDonationDate = :otsDonationDate")
    , @NamedQuery(name = "OtsDonation.findByOtsDonationStatus", query = "SELECT o FROM OtsDonation o WHERE o.otsDonationStatus = :otsDonationStatus")
    , @NamedQuery(name = "OtsDonation.findByOtsDonationDescription", query = "SELECT o FROM OtsDonation o WHERE o.otsDonationDescription = :otsDonationDescription")
    , @NamedQuery(name = "OtsDonation.findByOtsDonationPanNumber", query = "SELECT o FROM OtsDonation o WHERE o.otsDonationPanNumber = :otsDonationPanNumber")
    , @NamedQuery(name = "OtsDonation.findByOtsDonationOtherNumber", query = "SELECT o FROM OtsDonation o WHERE o.otsDonationOtherNumber = :otsDonationOtherNumber")})
public class OtsDonation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ots_donation_id")
    private Integer otsDonationId;
    @Size(max = 45)
    @Column(name = "ots_donation_amount")
    private String otsDonationAmount;
    @Size(max = 45)
    @Column(name = "ots_donation_paymentid")
    private String otsDonationPaymentid;
    @Size(max = 45)
    @Column(name = "ots_donation_donatedqty")
    private String otsDonationDonatedqty;
    @Size(max = 45)
    @Column(name = "ots_donation_payment_method")
    private String otsDonationPaymentMethod;
    @Column(name = "ots_donation_date")
    @Temporal(TemporalType.DATE)
    private Date otsDonationDate;
    @Size(max = 45)
    @Column(name = "ots_donation_status")
    private String otsDonationStatus;
    @Size(max = 45)
    @Column(name = "ots_donation_description")
    private String otsDonationDescription;
    @Size(max = 45)
    @Column(name = "ots_donation_pan_number")
    private String otsDonationPanNumber;
    @Size(max = 45)
    @Column(name = "ots_donation_other_number")
    private String otsDonationOtherNumber;
    @JoinColumn(name = "ots_donors_id", referencedColumnName = "ots_users_id")
    @ManyToOne(optional = false)
    private OtsUsers otsDonorsId;
    @JoinColumn(name = "ots_assgine_id", referencedColumnName = "ots_users_id")
    @ManyToOne
    private OtsUsers otsAssgineId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsDonationId")
    private Collection<OtsDonationRequestMapping> otsDonationRequestMappingCollection;
    @OneToMany(mappedBy = "otsDonationId")
    private Collection<OtsOrder> otsOrderCollection;

    public OtsDonation() {
    }

    public OtsDonation(Integer otsDonationId) {
        this.otsDonationId = otsDonationId;
    }

    public Integer getOtsDonationId() {
        return otsDonationId;
    }

    public void setOtsDonationId(Integer otsDonationId) {
        this.otsDonationId = otsDonationId;
    }

    public String getOtsDonationAmount() {
        return otsDonationAmount;
    }

    public void setOtsDonationAmount(String otsDonationAmount) {
        this.otsDonationAmount = otsDonationAmount;
    }

    public String getOtsDonationPaymentid() {
        return otsDonationPaymentid;
    }

    public void setOtsDonationPaymentid(String otsDonationPaymentid) {
        this.otsDonationPaymentid = otsDonationPaymentid;
    }

    public String getOtsDonationDonatedqty() {
        return otsDonationDonatedqty;
    }

    public void setOtsDonationDonatedqty(String otsDonationDonatedqty) {
        this.otsDonationDonatedqty = otsDonationDonatedqty;
    }

    public String getOtsDonationPaymentMethod() {
        return otsDonationPaymentMethod;
    }

    public void setOtsDonationPaymentMethod(String otsDonationPaymentMethod) {
        this.otsDonationPaymentMethod = otsDonationPaymentMethod;
    }

    public Date getOtsDonationDate() {
        return otsDonationDate;
    }

    public void setOtsDonationDate(Date otsDonationDate) {
        this.otsDonationDate = otsDonationDate;
    }

    public String getOtsDonationStatus() {
        return otsDonationStatus;
    }

    public void setOtsDonationStatus(String otsDonationStatus) {
        this.otsDonationStatus = otsDonationStatus;
    }

    public String getOtsDonationDescription() {
        return otsDonationDescription;
    }

    public void setOtsDonationDescription(String otsDonationDescription) {
        this.otsDonationDescription = otsDonationDescription;
    }

    public String getOtsDonationPanNumber() {
        return otsDonationPanNumber;
    }

    public void setOtsDonationPanNumber(String otsDonationPanNumber) {
        this.otsDonationPanNumber = otsDonationPanNumber;
    }

    public String getOtsDonationOtherNumber() {
        return otsDonationOtherNumber;
    }

    public void setOtsDonationOtherNumber(String otsDonationOtherNumber) {
        this.otsDonationOtherNumber = otsDonationOtherNumber;
    }

    public OtsUsers getOtsDonorsId() {
        return otsDonorsId;
    }

    public void setOtsDonorsId(OtsUsers otsDonorsId) {
        this.otsDonorsId = otsDonorsId;
    }

    public OtsUsers getOtsAssgineId() {
        return otsAssgineId;
    }

    public void setOtsAssgineId(OtsUsers otsAssgineId) {
        this.otsAssgineId = otsAssgineId;
    }

    @XmlTransient
    public Collection<OtsDonationRequestMapping> getOtsDonationRequestMappingCollection() {
        return otsDonationRequestMappingCollection;
    }

    public void setOtsDonationRequestMappingCollection(Collection<OtsDonationRequestMapping> otsDonationRequestMappingCollection) {
        this.otsDonationRequestMappingCollection = otsDonationRequestMappingCollection;
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
        hash += (otsDonationId != null ? otsDonationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsDonation)) {
            return false;
        }
        OtsDonation other = (OtsDonation) object;
        if ((this.otsDonationId == null && other.otsDonationId != null) || (this.otsDonationId != null && !this.otsDonationId.equals(other.otsDonationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsDonation[ otsDonationId=" + otsDonationId + " ]";
    }
    
}
