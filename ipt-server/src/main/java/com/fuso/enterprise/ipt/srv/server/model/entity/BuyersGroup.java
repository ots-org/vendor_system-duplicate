/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fuso.enterprise.ipt.srv.server.model.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;


/**
 *
 * @author SABBABU
 */
@Entity
@Table(name = "buyers_group")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BuyersGroup.findAll", query = "SELECT b FROM BuyersGroup b")
    , @NamedQuery(name = "BuyersGroup.findById", query = "SELECT b FROM BuyersGroup b WHERE b.id = :id")
    , @NamedQuery(name = "BuyersGroup.findByGroupCode", query = "SELECT b FROM BuyersGroup b WHERE b.groupCode = :groupCode")
    , @NamedQuery(name = "BuyersGroup.findByGroupName", query = "SELECT b FROM BuyersGroup b WHERE b.groupName = :groupName")
    , @NamedQuery(name = "BuyersGroup.findByGroupDesc", query = "SELECT b FROM BuyersGroup b WHERE b.groupDesc = :groupDesc")
    , @NamedQuery(name = "BuyersGroup.findBySupplierAssistance", query = "SELECT b FROM BuyersGroup b WHERE b.supplierAssistance = :supplierAssistance")
    , @NamedQuery(name = "BuyersGroup.findByDeptCode", query = "SELECT b FROM BuyersGroup b WHERE b.deptCode = :deptCode")})
public class BuyersGroup implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Id")
    private int id;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "group_code")
    private String groupCode;
    @Size(max = 50)
    @Column(name = "group_name")
    private String groupName;
    @Size(max = 50)
    @Column(name = "group_desc")
    private String groupDesc;
    @Size(max = 50)
    @Column(name = "supplier_assistance")
    private String supplierAssistance;
    @Size(max = 10)
    @Column(name = "dept_code")
    private String deptCode;

    public BuyersGroup() {
    }

    public BuyersGroup(String groupCode) {
        this.groupCode = groupCode;
    }

    public BuyersGroup(String groupCode, int id) {
        this.groupCode = groupCode;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public String getSupplierAssistance() {
        return supplierAssistance;
    }

    public void setSupplierAssistance(String supplierAssistance) {
        this.supplierAssistance = supplierAssistance;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (groupCode != null ? groupCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BuyersGroup)) {
            return false;
        }
        BuyersGroup other = (BuyersGroup) object;
        if ((this.groupCode == null && other.groupCode != null) || (this.groupCode != null && !this.groupCode.equals(other.groupCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ipt.srv.server.model.entity.BuyersGroup[ groupCode=" + groupCode + " ]";
    }
    
}
