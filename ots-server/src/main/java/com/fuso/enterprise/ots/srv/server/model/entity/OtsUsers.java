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
@Table(name = "ots_users")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtsUsers.findAll", query = "SELECT o FROM OtsUsers o"),
    @NamedQuery(name = "OtsUsers.findByOtsUsersId", query = "SELECT o FROM OtsUsers o WHERE o.otsUsersId = :otsUsersId"),
    @NamedQuery(name = "OtsUsers.findByOtsUsersFirstname", query = "SELECT o FROM OtsUsers o WHERE o.otsUsersFirstname = :otsUsersFirstname"),
    @NamedQuery(name = "OtsUsers.findByOtsUsersLastname", query = "SELECT o FROM OtsUsers o WHERE o.otsUsersLastname = :otsUsersLastname"),
    @NamedQuery(name = "OtsUsers.findByOtsUsersAddr1", query = "SELECT o FROM OtsUsers o WHERE o.otsUsersAddr1 = :otsUsersAddr1"),
    @NamedQuery(name = "OtsUsers.findByOtsUsersAddr2", query = "SELECT o FROM OtsUsers o WHERE o.otsUsersAddr2 = :otsUsersAddr2"),
    @NamedQuery(name = "OtsUsers.findByOtsUsersPincode", query = "SELECT o FROM OtsUsers o WHERE o.otsUsersPincode = :otsUsersPincode"),
    @NamedQuery(name = "OtsUsers.findByOtsUsersEmailid", query = "SELECT o FROM OtsUsers o WHERE o.otsUsersEmailid = :otsUsersEmailid"),
    @NamedQuery(name = "OtsUsers.findByOtsUsersProfilePic", query = "SELECT o FROM OtsUsers o WHERE o.otsUsersProfilePic = :otsUsersProfilePic"),
    @NamedQuery(name = "OtsUsers.findByOtsUsersStatus", query = "SELECT o FROM OtsUsers o WHERE o.otsUsersStatus = :otsUsersStatus"),
    @NamedQuery(name = "OtsUsers.findByOtsUsersTimestamp", query = "SELECT o FROM OtsUsers o WHERE o.otsUsersTimestamp = :otsUsersTimestamp"),
    @NamedQuery(name = "OtsUsers.findByOtsUsersCreated", query = "SELECT o FROM OtsUsers o WHERE o.otsUsersCreated = :otsUsersCreated")})
public class OtsUsers implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ots_users_id")
    private Integer otsUsersId;
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
    @Column(name = "ots_users_emailid")
    private String otsUsersEmailid;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsUsersId")
    private Collection<OtsUserMapping> otsUserMappingCollection;
    @JoinColumn(name = "ots_registration_id", referencedColumnName = "ots_registration_id")
    @ManyToOne(optional = false)
    private OtsRegistration otsRegistrationId;
    @JoinColumn(name = "ots_user_role_id", referencedColumnName = "ots_user_role_id")
    @ManyToOne(optional = false)
    private OtsUserRole otsUserRoleId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsUsersMappedTo")
    private Collection<OtsRegistration> otsRegistrationCollection;

    public OtsUsers() {
    }

    public OtsUsers(Integer otsUsersId) {
        this.otsUsersId = otsUsersId;
    }

    public Integer getOtsUsersId() {
        return otsUsersId;
    }

    public void setOtsUsersId(Integer otsUsersId) {
        this.otsUsersId = otsUsersId;
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

    public String getOtsUsersEmailid() {
        return otsUsersEmailid;
    }

    public void setOtsUsersEmailid(String otsUsersEmailid) {
        this.otsUsersEmailid = otsUsersEmailid;
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

    public OtsRegistration getOtsRegistrationId() {
        return otsRegistrationId;
    }

    public void setOtsRegistrationId(OtsRegistration otsRegistrationId) {
        this.otsRegistrationId = otsRegistrationId;
    }

    public OtsUserRole getOtsUserRoleId() {
        return otsUserRoleId;
    }

    public void setOtsUserRoleId(OtsUserRole otsUserRoleId) {
        this.otsUserRoleId = otsUserRoleId;
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
        hash += (otsUsersId != null ? otsUsersId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsUsers)) {
            return false;
        }
        OtsUsers other = (OtsUsers) object;
        if ((this.otsUsersId == null && other.otsUsersId != null) || (this.otsUsersId != null && !this.otsUsersId.equals(other.otsUsersId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers[ otsUsersId=" + otsUsersId + " ]";
    }
    
}
