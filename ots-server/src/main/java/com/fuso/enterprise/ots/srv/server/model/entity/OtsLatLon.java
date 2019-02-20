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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SERAJKU
 */
@Entity
@Table(name = "ots_lat_lon")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtsLatLon.findAll", query = "SELECT o FROM OtsLatLon o"),
    @NamedQuery(name = "OtsLatLon.findByOtsLatLonId", query = "SELECT o FROM OtsLatLon o WHERE o.otsLatLonId = :otsLatLonId"),
    @NamedQuery(name = "OtsLatLon.findByOtsLatitude", query = "SELECT o FROM OtsLatLon o WHERE o.otsLatitude = :otsLatitude"),
    @NamedQuery(name = "OtsLatLon.findByOtsLongitude", query = "SELECT o FROM OtsLatLon o WHERE o.otsLongitude = :otsLongitude"),
    @NamedQuery(name = "OtsLatLon.findByOtsLatLonCreated", query = "SELECT o FROM OtsLatLon o WHERE o.otsLatLonCreated = :otsLatLonCreated"),
    @NamedQuery(name = "OtsLatLon.findByOtsLatLonTimestamp", query = "SELECT o FROM OtsLatLon o WHERE o.otsLatLonTimestamp = :otsLatLonTimestamp")})
public class OtsLatLon implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ots_lat_lon_id")
    private Integer otsLatLonId;
    @Column(name = "ots_latitude")
    private String otsLatitude;
    @Column(name = "ots_longitude")
    private String otsLongitude;
    @Column(name = "ots_lat_lon_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otsLatLonCreated;
    @Column(name = "ots_lat_lon_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otsLatLonTimestamp;
    @JoinColumn(name = "ots_usersusers_id", referencedColumnName = "ots_users_id")
    @ManyToOne(optional = false)
    private OtsUsers otsUsersusersId;

    public OtsLatLon() {
    }

    public OtsLatLon(Integer otsLatLonId) {
        this.otsLatLonId = otsLatLonId;
    }

    public Integer getOtsLatLonId() {
        return otsLatLonId;
    }

    public void setOtsLatLonId(Integer otsLatLonId) {
        this.otsLatLonId = otsLatLonId;
    }

    public String getOtsLatitude() {
        return otsLatitude;
    }

    public void setOtsLatitude(String otsLatitude) {
        this.otsLatitude = otsLatitude;
    }

    public String getOtsLongitude() {
        return otsLongitude;
    }

    public void setOtsLongitude(String otsLongitude) {
        this.otsLongitude = otsLongitude;
    }

    public Date getOtsLatLonCreated() {
        return otsLatLonCreated;
    }

    public void setOtsLatLonCreated(Date otsLatLonCreated) {
        this.otsLatLonCreated = otsLatLonCreated;
    }

    public Date getOtsLatLonTimestamp() {
        return otsLatLonTimestamp;
    }

    public void setOtsLatLonTimestamp(Date otsLatLonTimestamp) {
        this.otsLatLonTimestamp = otsLatLonTimestamp;
    }

    public OtsUsers getOtsUsersusersId() {
        return otsUsersusersId;
    }

    public void setOtsUsersusersId(OtsUsers otsUsersusersId) {
        this.otsUsersusersId = otsUsersusersId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (otsLatLonId != null ? otsLatLonId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsLatLon)) {
            return false;
        }
        OtsLatLon other = (OtsLatLon) object;
        if ((this.otsLatLonId == null && other.otsLatLonId != null) || (this.otsLatLonId != null && !this.otsLatLonId.equals(other.otsLatLonId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsLatLon[ otsLatLonId=" + otsLatLonId + " ]";
    }
    
}
