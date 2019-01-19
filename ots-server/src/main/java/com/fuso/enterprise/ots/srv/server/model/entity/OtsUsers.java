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
@Table(name = "ots_users")
@XmlRootElement
public class OtsUsers implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected OtsUsersPK otsUsersPK;
    @Column(name = "ots_users_firstname")
    private String otsUsersFirstname;
    @Column(name = "ots_users_lastname")
    private String otsUsersLastname;
    @Column(name = "ots_users_addr1")
    private String otsUsersAddr1;
    @Column(name = "ots_users_addr2")
    private String otsUsersAddr2;
    @Column(name = "ots_users_pincode")
    private String otsUsersPincode;
    @Column(name = "ots_users_email_id")
    private String otsUsersEmailId;
    @Column(name = "ots_users_profile_pic")
    private String otsUsersProfilePic;
    @Column(name = "ots_users_status")
    private String otsUsersStatus;
    @Column(name = "ots_users_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otsUsersTimestamp;
    @Column(name = "ots_users_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otsUsersCreated;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsUsers")
    private Collection<OtsUserMapping> otsUserMappingCollection;
    @JoinColumn(name = "ots_registration_id", referencedColumnName = "ots_registration_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private OtsRegistration otsRegistration;
    @JoinColumn(name = "ots_userrole_id", referencedColumnName = "ots_user_role_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private OtsUserRole otsUserRole;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsUsers")
    private Collection<OtsRegistration> otsRegistrationCollection;

    public OtsUsers() {
    }

    public OtsUsers(OtsUsersPK otsUsersPK) {
        this.otsUsersPK = otsUsersPK;
    }

    public OtsUsers(int otsUsersId, int otsRegistrationId, int otsUserroleId) {
        this.otsUsersPK = new OtsUsersPK(otsUsersId, otsRegistrationId, otsUserroleId);
    }

    public OtsUsersPK getOtsUsersPK() {
        return otsUsersPK;
    }

    public void setOtsUsersPK(OtsUsersPK otsUsersPK) {
        this.otsUsersPK = otsUsersPK;
    }

    public String getOtsUsersFirstname() {
        return otsUsersFirstname;
    }

    public void setOtsUsersFirstname(String otsUsersFirstname) {
        this.otsUsersFirstname = otsUsersFirstname;
    }

    public String getOtsUsersLastname() {
        return otsUsersLastname;
    }

    public void setOtsUsersLastname(String otsUsersLastname) {
        this.otsUsersLastname = otsUsersLastname;
    }

    public String getOtsUsersAddr1() {
        return otsUsersAddr1;
    }

    public void setOtsUsersAddr1(String otsUsersAddr1) {
        this.otsUsersAddr1 = otsUsersAddr1;
    }

    public String getOtsUsersAddr2() {
        return otsUsersAddr2;
    }

    public void setOtsUsersAddr2(String otsUsersAddr2) {
        this.otsUsersAddr2 = otsUsersAddr2;
    }

    public String getOtsUsersPincode() {
        return otsUsersPincode;
    }

    public void setOtsUsersPincode(String otsUsersPincode) {
        this.otsUsersPincode = otsUsersPincode;
    }

    public String getOtsUsersEmailId() {
        return otsUsersEmailId;
    }

    public void setOtsUsersEmailId(String otsUsersEmailId) {
        this.otsUsersEmailId = otsUsersEmailId;
    }

    public String getOtsUsersProfilePic() {
        return otsUsersProfilePic;
    }

    public void setOtsUsersProfilePic(String otsUsersProfilePic) {
        this.otsUsersProfilePic = otsUsersProfilePic;
    }

    public String getOtsUsersStatus() {
        return otsUsersStatus;
    }

    public void setOtsUsersStatus(String otsUsersStatus) {
        this.otsUsersStatus = otsUsersStatus;
    }

    public Date getOtsUsersTimestamp() {
        return otsUsersTimestamp;
    }

    public void setOtsUsersTimestamp(Date otsUsersTimestamp) {
        this.otsUsersTimestamp = otsUsersTimestamp;
    }

    public Date getOtsUsersCreated() {
        return otsUsersCreated;
    }

    public void setOtsUsersCreated(Date otsUsersCreated) {
        this.otsUsersCreated = otsUsersCreated;
    }

    @XmlTransient
    public Collection<OtsUserMapping> getOtsUserMappingCollection() {
        return otsUserMappingCollection;
    }

    public void setOtsUserMappingCollection(Collection<OtsUserMapping> otsUserMappingCollection) {
        this.otsUserMappingCollection = otsUserMappingCollection;
    }

    public OtsRegistration getOtsRegistration() {
        return otsRegistration;
    }

    public void setOtsRegistration(OtsRegistration otsRegistration) {
        this.otsRegistration = otsRegistration;
    }

    public OtsUserRole getOtsUserRole() {
        return otsUserRole;
    }

    public void setOtsUserRole(OtsUserRole otsUserRole) {
        this.otsUserRole = otsUserRole;
    }

    @XmlTransient
    public Collection<OtsRegistration> getOtsRegistrationCollection() {
        return otsRegistrationCollection;
    }

    public void setOtsRegistrationCollection(Collection<OtsRegistration> otsRegistrationCollection) {
        this.otsRegistrationCollection = otsRegistrationCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (otsUsersPK != null ? otsUsersPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsUsers)) {
            return false;
        }
        OtsUsers other = (OtsUsers) object;
        if ((this.otsUsersPK == null && other.otsUsersPK != null) || (this.otsUsersPK != null && !this.otsUsersPK.equals(other.otsUsersPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers[ otsUsersPK=" + otsUsersPK + " ]";
    }
    
}
