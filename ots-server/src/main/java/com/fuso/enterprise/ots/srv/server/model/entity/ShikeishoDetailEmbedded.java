
package com.fuso.enterprise.ots.srv.server.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ShikeishoDetailEmbedded implements Serializable {

    private static final long serialVersionUID = 6240921362647414653L;

    @Column(name = "shikeisho_no")
    private String shikeishoNo;

    @Column(name = "eo_no")
    private String eoNo;

    public String getShikeishoNo() {
        return shikeishoNo;
    }

    public void setShikeishoNo(String shikeishoNo) {
        this.shikeishoNo = shikeishoNo;
    }

    public String getEoNo() {
        return eoNo;
    }

    public void setEoNo(String eoNo) {
        this.eoNo = eoNo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((eoNo == null) ? 0 : eoNo.hashCode());
        result = prime * result + ((shikeishoNo == null) ? 0 : shikeishoNo.hashCode());
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
        ShikeishoDetailEmbedded other = (ShikeishoDetailEmbedded) obj;
        if (eoNo == null) {
            if (other.eoNo != null)
                return false;
        } else if (!eoNo.equals(other.eoNo))
            return false;
        if (shikeishoNo == null) {
            if (other.shikeishoNo != null)
                return false;
        } else if (!shikeishoNo.equals(other.shikeishoNo))
            return false;
        return true;
    }

}
