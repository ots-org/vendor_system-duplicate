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
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lenovo
 */
@Entity
@Table(name = "ots_airtable")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtsAirtable.findAll", query = "SELECT o FROM OtsAirtable o")
    , @NamedQuery(name = "OtsAirtable.findByOtsAirtableId", query = "SELECT o FROM OtsAirtable o WHERE o.otsAirtableId = :otsAirtableId")
    , @NamedQuery(name = "OtsAirtable.findByOtsairtableproductId", query = "SELECT o FROM OtsAirtable o WHERE o.otsairtableproductId = :otsairtableproductId")
    , @NamedQuery(name = "OtsAirtable.findByOtsairtableproductStock", query = "SELECT o FROM OtsAirtable o WHERE o.otsairtableproductStock = :otsairtableproductStock")
    , @NamedQuery(name = "OtsAirtable.findByOtsairtableproductImage", query = "SELECT o FROM OtsAirtable o WHERE o.otsairtableproductImage = :otsairtableproductImage")
    , @NamedQuery(name = "OtsAirtable.findByOtsAirtableProductgst", query = "SELECT o FROM OtsAirtable o WHERE o.otsAirtableProductgst = :otsAirtableProductgst")
    , @NamedQuery(name = "OtsAirtable.findByOtsAirtableProductname", query = "SELECT o FROM OtsAirtable o WHERE o.otsAirtableProductname = :otsAirtableProductname")
    , @NamedQuery(name = "OtsAirtable.findByOtsAirtableProductprice", query = "SELECT o FROM OtsAirtable o WHERE o.otsAirtableProductprice = :otsAirtableProductprice")
    , @NamedQuery(name = "OtsAirtable.findByOtsAirtableProducername", query = "SELECT o FROM OtsAirtable o WHERE o.otsAirtableProducername = :otsAirtableProducername")
    , @NamedQuery(name = "OtsAirtable.findByOtsAirtableProductcategoryId", query = "SELECT o FROM OtsAirtable o WHERE o.otsAirtableProductcategoryId = :otsAirtableProductcategoryId")
    , @NamedQuery(name = "OtsAirtable.findByOtsAirtableProductcategoryname", query = "SELECT o FROM OtsAirtable o WHERE o.otsAirtableProductcategoryname = :otsAirtableProductcategoryname")
    , @NamedQuery(name = "OtsAirtable.findByOtsairtableproductsubcategoryId", query = "SELECT o FROM OtsAirtable o WHERE o.otsairtableproductsubcategoryId = :otsairtableproductsubcategoryId")
    , @NamedQuery(name = "OtsAirtable.findByOtsAirtableProductsubcategoryName", query = "SELECT o FROM OtsAirtable o WHERE o.otsAirtableProductsubcategoryName = :otsAirtableProductsubcategoryName")
    , @NamedQuery(name = "OtsAirtable.findByOtsAirtableAddedDate", query = "SELECT o FROM OtsAirtable o WHERE o.otsAirtableAddedDate = :otsAirtableAddedDate")
    , @NamedQuery(name = "OtsAirtable.findByOtsAirtableTransactionId", query = "SELECT o FROM OtsAirtable o WHERE o.otsAirtableTransactionId = :otsAirtableTransactionId")
    , @NamedQuery(name = "OtsAirtable.findByOtsAirtableDescription", query = "SELECT o FROM OtsAirtable o WHERE o.otsAirtableDescription = :otsAirtableDescription")})
public class OtsAirtable implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ots_airtable_id")
    private Integer otsAirtableId;
    @Size(max = 45)
    @Column(name = "ots_airtable_productId")
    private String otsairtableproductId;
    @Column(name = "ots_airtable_productStock")
    private Integer otsairtableproductStock;
    @Size(max = 150)
    @Column(name = "ots_airtable_productImage")
    private String otsairtableproductImage;
    @Size(max = 45)
    @Column(name = "ots_airtable_productgst")
    private String otsAirtableProductgst;
    @Size(max = 100)
    @Column(name = "ots_airtable_productname")
    private String otsAirtableProductname;
    @Size(max = 45)
    @Column(name = "ots_airtable_productprice")
    private String otsAirtableProductprice;
    @Size(max = 45)
    @Column(name = "ots_airtable_producername")
    private String otsAirtableProducername;
    @Size(max = 45)
    @Column(name = "ots_airtable_productcategory_id")
    private String otsAirtableProductcategoryId;
    @Size(max = 45)
    @Column(name = "ots_airtable_productcategoryname")
    private String otsAirtableProductcategoryname;
    @Size(max = 45)
    @Column(name = "ots_airtable_productsubcategory_Id")
    private String otsairtableproductsubcategoryId;
    @Size(max = 45)
    @Column(name = "ots_airtable_productsubcategory_name")
    private String otsAirtableProductsubcategoryName;
    @Column(name = "ots_airtable_added_date")
    @Temporal(TemporalType.DATE)
    private Date otsAirtableAddedDate;
    @Size(max = 45)
    @Column(name = "ots_airtable_transaction_id")
    private String otsAirtableTransactionId;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "ots_multi_product_image1")
    private String otsMultiProductImage1;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "ots_multi_product_image2")
    private String otsMultiProductImage2;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "ots_multi_product_image3")
    private String otsMultiProductImage3;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "ots_multi_product_image4")
    private String otsMultiProductImage4;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "ots_multi_product_image5")
    private String otsMultiProductImage5;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "ots_multi_product_image6")
    private String otsMultiProductImage6;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "ots_multi_product_image7")
    private String otsMultiProductImage7;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "ots_multi_product_image8")
    private String otsMultiProductImage8;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "ots_multi_product_image9")
    private String otsMultiProductImage9;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "ots_multi_product_image10")
    private String otsMultiProductImage10;
    @Size(max = 1000)
    @Column(name = "ots_airtable_description")
    private String otsAirtableDescription;

    public OtsAirtable() {
    }

    public OtsAirtable(Integer otsAirtableId) {
        this.otsAirtableId = otsAirtableId;
    }

    public Integer getOtsAirtableId() {
        return otsAirtableId;
    }

    public void setOtsAirtableId(Integer otsAirtableId) {
        this.otsAirtableId = otsAirtableId;
    }

    public String getOtsairtableproductId() {
        return otsairtableproductId;
    }

    public void setOtsairtableproductId(String otsairtableproductId) {
        this.otsairtableproductId = otsairtableproductId;
    }

    public Integer getOtsairtableproductStock() {
        return otsairtableproductStock;
    }

    public void setOtsairtableproductStock(Integer otsairtableproductStock) {
        this.otsairtableproductStock = otsairtableproductStock;
    }

    public String getOtsairtableproductImage() {
        return otsairtableproductImage;
    }

    public void setOtsairtableproductImage(String otsairtableproductImage) {
        this.otsairtableproductImage = otsairtableproductImage;
    }

    public String getOtsAirtableProductgst() {
        return otsAirtableProductgst;
    }

    public void setOtsAirtableProductgst(String otsAirtableProductgst) {
        this.otsAirtableProductgst = otsAirtableProductgst;
    }

    public String getOtsAirtableProductname() {
        return otsAirtableProductname;
    }

    public void setOtsAirtableProductname(String otsAirtableProductname) {
        this.otsAirtableProductname = otsAirtableProductname;
    }

    public String getOtsAirtableProductprice() {
        return otsAirtableProductprice;
    }

    public void setOtsAirtableProductprice(String otsAirtableProductprice) {
        this.otsAirtableProductprice = otsAirtableProductprice;
    }

    public String getOtsAirtableProducername() {
        return otsAirtableProducername;
    }

    public void setOtsAirtableProducername(String otsAirtableProducername) {
        this.otsAirtableProducername = otsAirtableProducername;
    }

    public String getOtsAirtableProductcategoryId() {
        return otsAirtableProductcategoryId;
    }

    public void setOtsAirtableProductcategoryId(String otsAirtableProductcategoryId) {
        this.otsAirtableProductcategoryId = otsAirtableProductcategoryId;
    }

    public String getOtsAirtableProductcategoryname() {
        return otsAirtableProductcategoryname;
    }

    public void setOtsAirtableProductcategoryname(String otsAirtableProductcategoryname) {
        this.otsAirtableProductcategoryname = otsAirtableProductcategoryname;
    }

    public String getOtsairtableproductsubcategoryId() {
        return otsairtableproductsubcategoryId;
    }

    public void setOtsairtableproductsubcategoryId(String otsairtableproductsubcategoryId) {
        this.otsairtableproductsubcategoryId = otsairtableproductsubcategoryId;
    }

    public String getOtsAirtableProductsubcategoryName() {
        return otsAirtableProductsubcategoryName;
    }

    public void setOtsAirtableProductsubcategoryName(String otsAirtableProductsubcategoryName) {
        this.otsAirtableProductsubcategoryName = otsAirtableProductsubcategoryName;
    }

    public Date getOtsAirtableAddedDate() {
        return otsAirtableAddedDate;
    }

    public void setOtsAirtableAddedDate(Date otsAirtableAddedDate) {
        this.otsAirtableAddedDate = otsAirtableAddedDate;
    }

    public String getOtsAirtableTransactionId() {
        return otsAirtableTransactionId;
    }

    public void setOtsAirtableTransactionId(String otsAirtableTransactionId) {
        this.otsAirtableTransactionId = otsAirtableTransactionId;
    }

    public String getOtsMultiProductImage1() {
        return otsMultiProductImage1;
    }

    public void setOtsMultiProductImage1(String otsMultiProductImage1) {
        this.otsMultiProductImage1 = otsMultiProductImage1;
    }

    public String getOtsMultiProductImage2() {
        return otsMultiProductImage2;
    }

    public void setOtsMultiProductImage2(String otsMultiProductImage2) {
        this.otsMultiProductImage2 = otsMultiProductImage2;
    }

    public String getOtsMultiProductImage3() {
        return otsMultiProductImage3;
    }

    public void setOtsMultiProductImage3(String otsMultiProductImage3) {
        this.otsMultiProductImage3 = otsMultiProductImage3;
    }

    public String getOtsMultiProductImage4() {
        return otsMultiProductImage4;
    }

    public void setOtsMultiProductImage4(String otsMultiProductImage4) {
        this.otsMultiProductImage4 = otsMultiProductImage4;
    }

    public String getOtsMultiProductImage5() {
        return otsMultiProductImage5;
    }

    public void setOtsMultiProductImage5(String otsMultiProductImage5) {
        this.otsMultiProductImage5 = otsMultiProductImage5;
    }

    public String getOtsMultiProductImage6() {
        return otsMultiProductImage6;
    }

    public void setOtsMultiProductImage6(String otsMultiProductImage6) {
        this.otsMultiProductImage6 = otsMultiProductImage6;
    }

    public String getOtsMultiProductImage7() {
        return otsMultiProductImage7;
    }

    public void setOtsMultiProductImage7(String otsMultiProductImage7) {
        this.otsMultiProductImage7 = otsMultiProductImage7;
    }

    public String getOtsMultiProductImage8() {
        return otsMultiProductImage8;
    }

    public void setOtsMultiProductImage8(String otsMultiProductImage8) {
        this.otsMultiProductImage8 = otsMultiProductImage8;
    }

    public String getOtsMultiProductImage9() {
        return otsMultiProductImage9;
    }

    public void setOtsMultiProductImage9(String otsMultiProductImage9) {
        this.otsMultiProductImage9 = otsMultiProductImage9;
    }

    public String getOtsMultiProductImage10() {
        return otsMultiProductImage10;
    }

    public void setOtsMultiProductImage10(String otsMultiProductImage10) {
        this.otsMultiProductImage10 = otsMultiProductImage10;
    }

    public String getOtsAirtableDescription() {
        return otsAirtableDescription;
    }

    public void setOtsAirtableDescription(String otsAirtableDescription) {
        this.otsAirtableDescription = otsAirtableDescription;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (otsAirtableId != null ? otsAirtableId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsAirtable)) {
            return false;
        }
        OtsAirtable other = (OtsAirtable) object;
        if ((this.otsAirtableId == null && other.otsAirtableId != null) || (this.otsAirtableId != null && !this.otsAirtableId.equals(other.otsAirtableId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsAirtable[ otsAirtableId=" + otsAirtableId + " ]";
    }
    
}
