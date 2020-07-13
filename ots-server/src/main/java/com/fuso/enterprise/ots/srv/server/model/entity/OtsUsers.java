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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name = "ots_users")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtsUsers.findAll", query = "SELECT o FROM OtsUsers o")
    , @NamedQuery(name = "OtsUsers.findByOtsUsersId", query = "SELECT o FROM OtsUsers o WHERE o.otsUsersId = :otsUsersId")
    , @NamedQuery(name = "OtsUsers.findByOtsUsersFirstname", query = "SELECT o FROM OtsUsers o WHERE o.otsUsersFirstname = :otsUsersFirstname")
    , @NamedQuery(name = "OtsUsers.findByOtsUsersLastname", query = "SELECT o FROM OtsUsers o WHERE o.otsUsersLastname = :otsUsersLastname")
    , @NamedQuery(name = "OtsUsers.findByOtsUsersAddr1", query = "SELECT o FROM OtsUsers o WHERE o.otsUsersAddr1 = :otsUsersAddr1")
    , @NamedQuery(name = "OtsUsers.findByOtsUsersAddr2", query = "SELECT o FROM OtsUsers o WHERE o.otsUsersAddr2 = :otsUsersAddr2")
    , @NamedQuery(name = "OtsUsers.findByOtsUsersPincode", query = "SELECT o FROM OtsUsers o WHERE o.otsUsersPincode = :otsUsersPincode")
    , @NamedQuery(name = "OtsUsers.findByOtsUsersEmailid", query = "SELECT o FROM OtsUsers o WHERE o.otsUsersEmailid = :otsUsersEmailid")
    , @NamedQuery(name = "OtsUsers.findByOtsUsersStatus", query = "SELECT o FROM OtsUsers o WHERE o.otsUsersStatus = :otsUsersStatus")
    , @NamedQuery(name = "OtsUsers.findByOtsUsersTimestamp", query = "SELECT o FROM OtsUsers o WHERE o.otsUsersTimestamp = :otsUsersTimestamp")
    , @NamedQuery(name = "OtsUsers.findByOtsUsersCreated", query = "SELECT o FROM OtsUsers o WHERE o.otsUsersCreated = :otsUsersCreated")
    , @NamedQuery(name = "OtsUsers.findByOtsUsersPassword", query = "SELECT o FROM OtsUsers o WHERE o.otsUsersPassword = :otsUsersPassword")
    , @NamedQuery(name = "OtsUsers.findByOtsUsersContactNo", query = "SELECT o FROM OtsUsers o WHERE o.otsUsersContactNo = :otsUsersContactNo")
    , @NamedQuery(name = "OtsUsers.findByOtsDeviceToken", query = "SELECT o FROM OtsUsers o WHERE o.otsDeviceToken = :otsDeviceToken")
    , @NamedQuery(name = "OtsUsers.findByOtsUsersLong", query = "SELECT o FROM OtsUsers o WHERE o.otsUsersLong = :otsUsersLong")
    , @NamedQuery(name = "OtsUsers.findByOtsUsersLat", query = "SELECT o FROM OtsUsers o WHERE o.otsUsersLat = :otsUsersLat")
    , @NamedQuery(name = "OtsUsers.findByOtsUserPannumber", query = "SELECT o FROM OtsUsers o WHERE o.otsUserPannumber = :otsUserPannumber")
    , @NamedQuery(name = "OtsUsers.findByOtsUsersRotarynumber", query = "SELECT o FROM OtsUsers o WHERE o.otsUsersRotarynumber = :otsUsersRotarynumber")})
public class OtsUsers implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ots_users_id")
    private Integer otsUsersId;
    @Size(max = 45)
    @Column(name = "ots_users_firstname")
    private String otsUsersFirstname;
    @Size(max = 45)
    @Column(name = "ots_users_lastname")
    private String otsUsersLastname;
    @Size(max = 1000)
    @Column(name = "ots_users_addr1")
    private String otsUsersAddr1;
    @Size(max = 1000)
    @Column(name = "ots_users_addr2")
    private String otsUsersAddr2;
    @Size(max = 45)
    @Column(name = "ots_users_pincode")
    private String otsUsersPincode;
    @Size(max = 45)
    @Column(name = "ots_users_emailid")
    private String otsUsersEmailid;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "ots_users_profile_pic")
    private String otsUsersProfilePic;
    @Size(max = 45)
    @Column(name = "ots_users_status")
    private String otsUsersStatus;
    @Column(name = "ots_users_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otsUsersTimestamp;
    @Column(name = "ots_users_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otsUsersCreated;
    @Size(max = 1000)
    @Column(name = "ots_users_password")
    private String otsUsersPassword;
    @Size(max = 45)
    @Column(name = "ots_users_contact_no")
    private String otsUsersContactNo;
    @Size(max = 255)
    @Column(name = "ots_device_token")
    private String otsDeviceToken;
    @Size(max = 45)
    @Column(name = "ots_users_long")
    private String otsUsersLong;
    @Size(max = 45)
    @Column(name = "ots_users_lat")
    private String otsUsersLat;
    @Size(max = 45)
    @Column(name = "ots_user_pannumber")
    private String otsUserPannumber;
    @Size(max = 45)
    @Column(name = "ots_users_rotarynumber")
    private String otsUsersRotarynumber;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "otsUsersId")
    private OtsUserMapping otsUserMapping;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsUsersId")
    private Collection<OtsProductStockHistory> otsProductStockHistoryCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsUsersId")
    private Collection<OtsStockDistOb> otsStockDistObCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsUsersId")
    private Collection<OtsProductStock> otsProductStockCollection;
    @JoinColumn(name = "ots_registration_id", referencedColumnName = "ots_registration_id")
    @ManyToOne
    private OtsRegistration otsRegistrationId;
    @JoinColumn(name = "ots_user_role_id", referencedColumnName = "ots_user_role_id")
    @ManyToOne(optional = false)
    private OtsUserRole otsUserRoleId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsDonorsId")
    private Collection<OtsDonation> otsDonationCollection;
    @OneToMany(mappedBy = "otsAssgineId")
    private Collection<OtsDonation> otsDonationCollection1;
    @OneToMany(mappedBy = "otsUsersMappedTo")
    private Collection<OtsRegistration> otsRegistrationCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsDistributorId")
    private Collection<OtsScheduler> otsSchedulerCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsCustomerId")
    private Collection<OtsScheduler> otsSchedulerCollection1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsCustomerId")
    private Collection<OtsBill> otsBillCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsUsersId")
    private Collection<OtsCustomerProduct> otsCustomerProductCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsUsersusersId")
    private Collection<OtsLatLon> otsLatLonCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsUsersId")
    private Collection<OtsSubscriptionOrderHistory> otsSubscriptionOrderHistoryCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsDistributorId")
    private Collection<OtsOrder> otsOrderCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsCustomerId")
    private Collection<OtsOrder> otsOrderCollection1;
    @OneToMany(mappedBy = "otsAssignedId")
    private Collection<OtsOrder> otsOrderCollection2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsCustomerId")
    private Collection<OtsRequestOrder> otsRequestOrderCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsDistributorId")
    private Collection<OtsRequestOrder> otsRequestOrderCollection1;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "otsCustomerId")
    private OtsCustomerOutstanding otsCustomerOutstanding;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsUsersId")
    private Collection<OtsSubscriptionOrder> otsSubscriptionOrderCollection;

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

    public String getOtsUsersPassword() {
        return otsUsersPassword;
    }

    public void setOtsUsersPassword(String otsUsersPassword) {
        this.otsUsersPassword = otsUsersPassword;
    }

    public String getOtsUsersContactNo() {
        return otsUsersContactNo;
    }

    public void setOtsUsersContactNo(String otsUsersContactNo) {
        this.otsUsersContactNo = otsUsersContactNo;
    }

    public String getOtsDeviceToken() {
        return otsDeviceToken;
    }

    public void setOtsDeviceToken(String otsDeviceToken) {
        this.otsDeviceToken = otsDeviceToken;
    }

    public String getOtsUsersLong() {
        return otsUsersLong;
    }

    public void setOtsUsersLong(String otsUsersLong) {
        this.otsUsersLong = otsUsersLong;
    }

    public String getOtsUsersLat() {
        return otsUsersLat;
    }

    public void setOtsUsersLat(String otsUsersLat) {
        this.otsUsersLat = otsUsersLat;
    }

    public String getOtsUserPannumber() {
        return otsUserPannumber;
    }

    public void setOtsUserPannumber(String otsUserPannumber) {
        this.otsUserPannumber = otsUserPannumber;
    }

    public String getOtsUsersRotarynumber() {
        return otsUsersRotarynumber;
    }

    public void setOtsUsersRotarynumber(String otsUsersRotarynumber) {
        this.otsUsersRotarynumber = otsUsersRotarynumber;
    }

    public OtsUserMapping getOtsUserMapping() {
        return otsUserMapping;
    }

    public void setOtsUserMapping(OtsUserMapping otsUserMapping) {
        this.otsUserMapping = otsUserMapping;
    }

    @XmlTransient
    public Collection<OtsProductStockHistory> getOtsProductStockHistoryCollection() {
        return otsProductStockHistoryCollection;
    }

    public void setOtsProductStockHistoryCollection(Collection<OtsProductStockHistory> otsProductStockHistoryCollection) {
        this.otsProductStockHistoryCollection = otsProductStockHistoryCollection;
    }

    @XmlTransient
    public Collection<OtsStockDistOb> getOtsStockDistObCollection() {
        return otsStockDistObCollection;
    }

    public void setOtsStockDistObCollection(Collection<OtsStockDistOb> otsStockDistObCollection) {
        this.otsStockDistObCollection = otsStockDistObCollection;
    }

    @XmlTransient
    public Collection<OtsProductStock> getOtsProductStockCollection() {
        return otsProductStockCollection;
    }

    public void setOtsProductStockCollection(Collection<OtsProductStock> otsProductStockCollection) {
        this.otsProductStockCollection = otsProductStockCollection;
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
    public Collection<OtsDonation> getOtsDonationCollection() {
        return otsDonationCollection;
    }

    public void setOtsDonationCollection(Collection<OtsDonation> otsDonationCollection) {
        this.otsDonationCollection = otsDonationCollection;
    }

    @XmlTransient
    public Collection<OtsDonation> getOtsDonationCollection1() {
        return otsDonationCollection1;
    }

    public void setOtsDonationCollection1(Collection<OtsDonation> otsDonationCollection1) {
        this.otsDonationCollection1 = otsDonationCollection1;
    }

    @XmlTransient
    public Collection<OtsRegistration> getOtsRegistrationCollection() {
        return otsRegistrationCollection;
    }

    public void setOtsRegistrationCollection(Collection<OtsRegistration> otsRegistrationCollection) {
        this.otsRegistrationCollection = otsRegistrationCollection;
    }

    @XmlTransient
    public Collection<OtsScheduler> getOtsSchedulerCollection() {
        return otsSchedulerCollection;
    }

    public void setOtsSchedulerCollection(Collection<OtsScheduler> otsSchedulerCollection) {
        this.otsSchedulerCollection = otsSchedulerCollection;
    }

    @XmlTransient
    public Collection<OtsScheduler> getOtsSchedulerCollection1() {
        return otsSchedulerCollection1;
    }

    public void setOtsSchedulerCollection1(Collection<OtsScheduler> otsSchedulerCollection1) {
        this.otsSchedulerCollection1 = otsSchedulerCollection1;
    }

    @XmlTransient
    public Collection<OtsBill> getOtsBillCollection() {
        return otsBillCollection;
    }

    public void setOtsBillCollection(Collection<OtsBill> otsBillCollection) {
        this.otsBillCollection = otsBillCollection;
    }

    @XmlTransient
    public Collection<OtsCustomerProduct> getOtsCustomerProductCollection() {
        return otsCustomerProductCollection;
    }

    public void setOtsCustomerProductCollection(Collection<OtsCustomerProduct> otsCustomerProductCollection) {
        this.otsCustomerProductCollection = otsCustomerProductCollection;
    }

    @XmlTransient
    public Collection<OtsLatLon> getOtsLatLonCollection() {
        return otsLatLonCollection;
    }

    public void setOtsLatLonCollection(Collection<OtsLatLon> otsLatLonCollection) {
        this.otsLatLonCollection = otsLatLonCollection;
    }

    @XmlTransient
    public Collection<OtsSubscriptionOrderHistory> getOtsSubscriptionOrderHistoryCollection() {
        return otsSubscriptionOrderHistoryCollection;
    }

    public void setOtsSubscriptionOrderHistoryCollection(Collection<OtsSubscriptionOrderHistory> otsSubscriptionOrderHistoryCollection) {
        this.otsSubscriptionOrderHistoryCollection = otsSubscriptionOrderHistoryCollection;
    }

    @XmlTransient
    public Collection<OtsOrder> getOtsOrderCollection() {
        return otsOrderCollection;
    }

    public void setOtsOrderCollection(Collection<OtsOrder> otsOrderCollection) {
        this.otsOrderCollection = otsOrderCollection;
    }

    @XmlTransient
    public Collection<OtsOrder> getOtsOrderCollection1() {
        return otsOrderCollection1;
    }

    public void setOtsOrderCollection1(Collection<OtsOrder> otsOrderCollection1) {
        this.otsOrderCollection1 = otsOrderCollection1;
    }

    @XmlTransient
    public Collection<OtsOrder> getOtsOrderCollection2() {
        return otsOrderCollection2;
    }

    public void setOtsOrderCollection2(Collection<OtsOrder> otsOrderCollection2) {
        this.otsOrderCollection2 = otsOrderCollection2;
    }

    @XmlTransient
    public Collection<OtsRequestOrder> getOtsRequestOrderCollection() {
        return otsRequestOrderCollection;
    }

    public void setOtsRequestOrderCollection(Collection<OtsRequestOrder> otsRequestOrderCollection) {
        this.otsRequestOrderCollection = otsRequestOrderCollection;
    }

    @XmlTransient
    public Collection<OtsRequestOrder> getOtsRequestOrderCollection1() {
        return otsRequestOrderCollection1;
    }

    public void setOtsRequestOrderCollection1(Collection<OtsRequestOrder> otsRequestOrderCollection1) {
        this.otsRequestOrderCollection1 = otsRequestOrderCollection1;
    }

    public OtsCustomerOutstanding getOtsCustomerOutstanding() {
        return otsCustomerOutstanding;
    }

    public void setOtsCustomerOutstanding(OtsCustomerOutstanding otsCustomerOutstanding) {
        this.otsCustomerOutstanding = otsCustomerOutstanding;
    }

    @XmlTransient
    public Collection<OtsSubscriptionOrder> getOtsSubscriptionOrderCollection() {
        return otsSubscriptionOrderCollection;
    }

    public void setOtsSubscriptionOrderCollection(Collection<OtsSubscriptionOrder> otsSubscriptionOrderCollection) {
        this.otsSubscriptionOrderCollection = otsSubscriptionOrderCollection;
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
