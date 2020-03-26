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
@Table(name = "ots_registration")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtsRegistration.findAll", query = "SELECT o FROM OtsRegistration o")
    , @NamedQuery(name = "OtsRegistration.findByOtsRegistrationId", query = "SELECT o FROM OtsRegistration o WHERE o.otsRegistrationId = :otsRegistrationId")
    , @NamedQuery(name = "OtsRegistration.findByOtsRegistrationFirstname", query = "SELECT o FROM OtsRegistration o WHERE o.otsRegistrationFirstname = :otsRegistrationFirstname")
    , @NamedQuery(name = "OtsRegistration.findByOtsRegistrationLastname", query = "SELECT o FROM OtsRegistration o WHERE o.otsRegistrationLastname = :otsRegistrationLastname")
    , @NamedQuery(name = "OtsRegistration.findByOtsRegistrationAddr1", query = "SELECT o FROM OtsRegistration o WHERE o.otsRegistrationAddr1 = :otsRegistrationAddr1")
    , @NamedQuery(name = "OtsRegistration.findByOtsRegistrationAddr2", query = "SELECT o FROM OtsRegistration o WHERE o.otsRegistrationAddr2 = :otsRegistrationAddr2")
    , @NamedQuery(name = "OtsRegistration.findByOtsRegistrationPincode", query = "SELECT o FROM OtsRegistration o WHERE o.otsRegistrationPincode = :otsRegistrationPincode")
    , @NamedQuery(name = "OtsRegistration.findByOtsRegistrationEmailid", query = "SELECT o FROM OtsRegistration o WHERE o.otsRegistrationEmailid = :otsRegistrationEmailid")
    , @NamedQuery(name = "OtsRegistration.findByOtsRegistrationProfilePic", query = "SELECT o FROM OtsRegistration o WHERE o.otsRegistrationProfilePic = :otsRegistrationProfilePic")
    , @NamedQuery(name = "OtsRegistration.findByOtsRegistrationStatus", query = "SELECT o FROM OtsRegistration o WHERE o.otsRegistrationStatus = :otsRegistrationStatus")
    , @NamedQuery(name = "OtsRegistration.findByOtsRegistrationTimestamp", query = "SELECT o FROM OtsRegistration o WHERE o.otsRegistrationTimestamp = :otsRegistrationTimestamp")
    , @NamedQuery(name = "OtsRegistration.findByOtsRegistrationCreated", query = "SELECT o FROM OtsRegistration o WHERE o.otsRegistrationCreated = :otsRegistrationCreated")
    , @NamedQuery(name = "OtsRegistration.findByOtsRegistrationPassword", query = "SELECT o FROM OtsRegistration o WHERE o.otsRegistrationPassword = :otsRegistrationPassword")
    , @NamedQuery(name = "OtsRegistration.findByOtsRegistrationContactNo", query = "SELECT o FROM OtsRegistration o WHERE o.otsRegistrationContactNo = :otsRegistrationContactNo")
    , @NamedQuery(name = "OtsRegistration.findByOtsDeviceToken", query = "SELECT o FROM OtsRegistration o WHERE o.otsDeviceToken = :otsDeviceToken")})
public class OtsRegistration implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ots_registration_id")
    private Integer otsRegistrationId;
    @Size(max = 45)
    @Column(name = "ots_registration_firstname")
    private String otsRegistrationFirstname;
    @Size(max = 45)
    @Column(name = "ots_registration_lastname")
    private String otsRegistrationLastname;
    @Size(max = 45)
    @Column(name = "ots_registration_addr1")
    private String otsRegistrationAddr1;
    @Size(max = 45)
    @Column(name = "ots_registration_addr2")
    private String otsRegistrationAddr2;
    @Size(max = 45)
    @Column(name = "ots_registration_pincode")
    private String otsRegistrationPincode;
    @Size(max = 45)
    @Column(name = "ots_registration_emailid")
    private String otsRegistrationEmailid;
    @Size(max = 45)
    @Column(name = "ots_registration_profile_pic")
    private String otsRegistrationProfilePic;
    @Size(max = 45)
    @Column(name = "ots_registration_status")
    private String otsRegistrationStatus;
    @Column(name = "ots_registration_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otsRegistrationTimestamp;
    @Column(name = "ots_registration_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otsRegistrationCreated;
    @Size(max = 45)
    @Column(name = "ots_registration_password")
    private String otsRegistrationPassword;
    @Size(max = 45)
    @Column(name = "ots_registration_contact_no")
    private String otsRegistrationContactNo;
    @Size(max = 255)
    @Column(name = "ots_device_token")
    private String otsDeviceToken;
    @OneToMany(mappedBy = "otsRegistrationId")
    private Collection<OtsUsers> otsUsersCollection;
    @JoinColumn(name = "ots_product_id", referencedColumnName = "ots_product_id")
    @ManyToOne
    private OtsProduct otsProductId;
    @JoinColumn(name = "ots_user_role_id", referencedColumnName = "ots_user_role_id")
    @ManyToOne(optional = false)
    private OtsUserRole otsUserRoleId;
    @JoinColumn(name = "ots_users_mapped_to", referencedColumnName = "ots_users_id")
    @ManyToOne
    private OtsUsers otsUsersMappedTo;

    public OtsRegistration() {
    }

    public OtsRegistration(Integer otsRegistrationId) {
        this.otsRegistrationId = otsRegistrationId;
    }

    public Integer getOtsRegistrationId() {
        return otsRegistrationId;
    }

    public void setOtsRegistrationId(Integer otsRegistrationId) {
        this.otsRegistrationId = otsRegistrationId;
    }

    public String getOtsRegistrationFirstname() {
        return otsRegistrationFirstname;
    }

    public void setOtsRegistrationFirstname(String otsRegistrationFirstname) {
        this.otsRegistrationFirstname = otsRegistrationFirstname;
    }

    public String getOtsRegistrationLastname() {
        return otsRegistrationLastname;
    }

    public void setOtsRegistrationLastname(String otsRegistrationLastname) {
        this.otsRegistrationLastname = otsRegistrationLastname;
    }

    public String getOtsRegistrationAddr1() {
        return otsRegistrationAddr1;
    }

    public void setOtsRegistrationAddr1(String otsRegistrationAddr1) {
        this.otsRegistrationAddr1 = otsRegistrationAddr1;
    }

    public String getOtsRegistrationAddr2() {
        return otsRegistrationAddr2;
    }

    public void setOtsRegistrationAddr2(String otsRegistrationAddr2) {
        this.otsRegistrationAddr2 = otsRegistrationAddr2;
    }

    public String getOtsRegistrationPincode() {
        return otsRegistrationPincode;
    }

    public void setOtsRegistrationPincode(String otsRegistrationPincode) {
        this.otsRegistrationPincode = otsRegistrationPincode;
    }

    public String getOtsRegistrationEmailid() {
        return otsRegistrationEmailid;
    }

    public void setOtsRegistrationEmailid(String otsRegistrationEmailid) {
        this.otsRegistrationEmailid = otsRegistrationEmailid;
    }

    public String getOtsRegistrationProfilePic() {
        return otsRegistrationProfilePic;
    }

    public void setOtsRegistrationProfilePic(String otsRegistrationProfilePic) {
        this.otsRegistrationProfilePic = otsRegistrationProfilePic;
    }

    public String getOtsRegistrationStatus() {
        return otsRegistrationStatus;
    }

    public void setOtsRegistrationStatus(String otsRegistrationStatus) {
        this.otsRegistrationStatus = otsRegistrationStatus;
    }

    public Date getOtsRegistrationTimestamp() {
        return otsRegistrationTimestamp;
    }

    public void setOtsRegistrationTimestamp(Date otsRegistrationTimestamp) {
        this.otsRegistrationTimestamp = otsRegistrationTimestamp;
    }

    public Date getOtsRegistrationCreated() {
        return otsRegistrationCreated;
    }

    public void setOtsRegistrationCreated(Date otsRegistrationCreated) {
        this.otsRegistrationCreated = otsRegistrationCreated;
    }

    public String getOtsRegistrationPassword() {
        return otsRegistrationPassword;
    }

    public void setOtsRegistrationPassword(String otsRegistrationPassword) {
        this.otsRegistrationPassword = otsRegistrationPassword;
    }

    public String getOtsRegistrationContactNo() {
        return otsRegistrationContactNo;
    }

    public void setOtsRegistrationContactNo(String otsRegistrationContactNo) {
        this.otsRegistrationContactNo = otsRegistrationContactNo;
    }

    public String getOtsDeviceToken() {
        return otsDeviceToken;
    }

    public void setOtsDeviceToken(String otsDeviceToken) {
        this.otsDeviceToken = otsDeviceToken;
    }

    @XmlTransient
    public Collection<OtsUsers> getOtsUsersCollection() {
        return otsUsersCollection;
    }

    public void setOtsUsersCollection(Collection<OtsUsers> otsUsersCollection) {
        this.otsUsersCollection = otsUsersCollection;
    }

    public OtsProduct getOtsProductId() {
        return otsProductId;
    }

    public void setOtsProductId(OtsProduct otsProductId) {
        this.otsProductId = otsProductId;
    }

    public OtsUserRole getOtsUserRoleId() {
        return otsUserRoleId;
    }

    public void setOtsUserRoleId(OtsUserRole otsUserRoleId) {
        this.otsUserRoleId = otsUserRoleId;
    }

    public OtsUsers getOtsUsersMappedTo() {
        return otsUsersMappedTo;
    }

    public void setOtsUsersMappedTo(OtsUsers otsUsersMappedTo) {
        this.otsUsersMappedTo = otsUsersMappedTo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (otsRegistrationId != null ? otsRegistrationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsRegistration)) {
            return false;
        }
        OtsRegistration other = (OtsRegistration) object;
        if ((this.otsRegistrationId == null && other.otsRegistrationId != null) || (this.otsRegistrationId != null && !this.otsRegistrationId.equals(other.otsRegistrationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsRegistration[ otsRegistrationId=" + otsRegistrationId + " ]";
    }
    
}
