
package com.fuso.enterprise.ots.srv.server.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Embeddable
public class TmpShikeishoPartEmbedded implements Serializable {

    private static final long serialVersionUID = 6240921362647414653L;

    @Fetch(FetchMode.SELECT)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shikeisho_no")
    private TmpShikeisho tmpShikeisho;

    @Column(name = "part_no")
    private String partNo;

    public TmpShikeisho getTmpShikeisho() {
        return tmpShikeisho;
    }

    public void setTmpShikeisho(TmpShikeisho tmpShikeisho) {
        this.tmpShikeisho = tmpShikeisho;
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
        result = prime * result + ((tmpShikeisho == null) ? 0 : tmpShikeisho.getShikeishoNo().hashCode());
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
        TmpShikeishoPartEmbedded other = (TmpShikeishoPartEmbedded) obj;
        if (partNo == null) {
            if (other.partNo != null)
                return false;
        } else if (!partNo.equals(other.partNo))
            return false;
        if (tmpShikeisho == null) {
            if (other.tmpShikeisho != null)
                return false;
        } else if (!tmpShikeisho.getShikeishoNo().equals(other.tmpShikeisho.getShikeishoNo()))
            return false;
        return true;
    }

}
