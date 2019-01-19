/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fuso.enterprise.ots.srv.server.model.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
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
@MappedSuperclass
@Table(name = "ots_registration")
@XmlRootElement
public class OtsRegistration implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected OtsRegistrationPK otsRegistrationPK;
    @Column(name = "ots_registration_firstname")
    private String otsRegistrationFirstname;
    @Column(name = "ots_registration_lastnam")
    private String otsRegistrationLastnam;
    @Column(name = "ots_registration_addr1")
    private String otsRegistrationAddr1;
    @Column(name = "ots_registration_addr2")
    private String otsRegistrationAddr2;
    @Column(name = "ots_registration_pincode")
    private String otsRegistrationPincode;
    @Column(name = "ots_registration_email_id")
    private String otsRegistrationEmailId;
    @Column(name = "ots_registration_profile_pic")
    private String otsRegistrationProfilePic;
    @Column(name = "ots_registration_status")
    private String otsRegistrationStatus;
    @Column(name = "ots_registration_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otsRegistrationTimestamp;
    @Column(name = "ots_registration_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otsRegistrationCreated;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsRegistration")
    private Collection<OtsUsers> otsUsersCollection;
    @JoinColumn(name = "ots_user_role_id", referencedColumnName = "ots_user_role_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private OtsUserRole otsUserRole;
    @JoinColumn(name = "ots_users_mapped_to", referencedColumnName = "ots_users_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private OtsUsers otsUsers;

    public OtsRegistration() {
    }

    public OtsRegistration(OtsRegistrationPK otsRegistrationPK) {
        this.otsRegistrationPK = otsRegistrationPK;
    }

    public OtsRegistration(int otsRegistrationId, int otsUsersMappedTo, int otsUserRoleId) {
        this.otsRegistrationPK = new OtsRegistrationPK(otsRegistrationId, otsUsersMappedTo, otsUserRoleId);
    }

    public OtsRegistrationPK getOtsRegistrationPK() {
        return otsRegistrationPK;
    }

    public void setOtsRegistrationPK(OtsRegistrationPK otsRegistrationPK) {
        this.otsRegistrationPK = otsRegistrationPK;
    }

    public String getOtsRegistrationFirstname() {
        return otsRegistrationFirstname;
    }

    public void setOtsRegistrationFirstname(String otsRegistrationFirstname) {
        this.otsRegistrationFirstname = otsRegistrationFirstname;
    }

    public String getOtsRegistrationLastnam() {
        return otsRegistrationLastnam;
    }

    public void setOtsRegistrationLastnam(String otsRegistrationLastnam) {
        this.otsRegistrationLastnam = otsRegistrationLastnam;
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

    public String getOtsRegistrationEmailId() {
        return otsRegistrationEmailId;
    }

    public void setOtsRegistrationEmailId(String otsRegistrationEmailId) {
        this.otsRegistrationEmailId = otsRegistrationEmailId;
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

    @XmlTransient
    public Collection<OtsUsers> getOtsUsersCollection() {
        return otsUsersCollection;
    }

    public void setOtsUsersCollection(Collection<OtsUsers> otsUsersCollection) {
        this.otsUsersCollection = otsUsersCollection;
    }

    public OtsUserRole getOtsUserRole() {
        return otsUserRole;
    }

    public void setOtsUserRole(OtsUserRole otsUserRole) {
        this.otsUserRole = otsUserRole;
    }

    public OtsUsers getOtsUsers() {
        return otsUsers;
    }

    public void setOtsUsers(OtsUsers otsUsers) {
        this.otsUsers = otsUsers;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (otsRegistrationPK != null ? otsRegistrationPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsRegistration)) {
            return false;
        }
        OtsRegistration other = (OtsRegistration) object;
        if ((this.otsRegistrationPK == null && other.otsRegistrationPK != null) || (this.otsRegistrationPK != null && !this.otsRegistrationPK.equals(other.otsRegistrationPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsRegistration[ otsRegistrationPK=" + otsRegistrationPK + " ]";
    }
    
}
