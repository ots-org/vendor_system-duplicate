
package com.fuso.enterprise.ots.srv.server.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Embeddable
public class ShikeishoPartDetailEmbedded implements Serializable {

    private static final long serialVersionUID = 6240921362647414653L;

    @Fetch(FetchMode.SELECT)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({ @JoinColumn(name = "shikeisho_no", referencedColumnName = "shikeisho_no"), @JoinColumn(name = "eo_no", referencedColumnName = "eo_no") })
    private ShikeishoDetail shikeishoDetail;

    @Column(name = "part_no")
    private String partNo;

    public ShikeishoDetail getShikeishoDetail() {
        return shikeishoDetail;
    }

    public void setShikeishoDetail(ShikeishoDetail shikeishoDetail) {
        this.shikeishoDetail = shikeishoDetail;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((partNo == null) ? 0 : partNo.hashCode());
        result = prime * result + ((shikeishoDetail.getShikeishoDetailEmbedded() == null) ? 0 : shikeishoDetail.getShikeishoDetailEmbedded().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ShikeishoPartDetailEmbedded other = (ShikeishoPartDetailEmbedded) obj;
        if (partNo == null) {
            if (other.partNo != null)
                return false;
        } else if (!partNo.equals(other.partNo))
            return false;
        if (shikeishoDetail == null) {
            if (other.shikeishoDetail != null)
                return false;
        } else if (!shikeishoDetail.getShikeishoDetailEmbedded().equals(other.shikeishoDetail.getShikeishoDetailEmbedded()))
            return false;
        return true;
    }

}
