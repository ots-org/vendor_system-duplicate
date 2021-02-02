/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fuso.enterprise.ots.srv.server.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author lenovo
 */
@Entity
@Table(name = "ots_product")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtsProduct.findAll", query = "SELECT o FROM OtsProduct o")
    , @NamedQuery(name = "OtsProduct.findByOtsProductId", query = "SELECT o FROM OtsProduct o WHERE o.otsProductId = :otsProductId")
    , @NamedQuery(name = "OtsProduct.findByOtsProductName", query = "SELECT o FROM OtsProduct o WHERE o.otsProductName = :otsProductName")
    , @NamedQuery(name = "OtsProduct.findByOtsProductDescription", query = "SELECT o FROM OtsProduct o WHERE o.otsProductDescription = :otsProductDescription")
    , @NamedQuery(name = "OtsProduct.findByOtsProductStatus", query = "SELECT o FROM OtsProduct o WHERE o.otsProductStatus = :otsProductStatus")
    , @NamedQuery(name = "OtsProduct.findByOtsProductTimestamp", query = "SELECT o FROM OtsProduct o WHERE o.otsProductTimestamp = :otsProductTimestamp")
    , @NamedQuery(name = "OtsProduct.findByOtsProductCreated", query = "SELECT o FROM OtsProduct o WHERE o.otsProductCreated = :otsProductCreated")
    , @NamedQuery(name = "OtsProduct.findByOtsProductPrice", query = "SELECT o FROM OtsProduct o WHERE o.otsProductPrice = :otsProductPrice")
    , @NamedQuery(name = "OtsProduct.findByOtsProductType", query = "SELECT o FROM OtsProduct o WHERE o.otsProductType = :otsProductType")
    , @NamedQuery(name = "OtsProduct.findByOtsProductThresholdDay", query = "SELECT o FROM OtsProduct o WHERE o.otsProductThresholdDay = :otsProductThresholdDay")
    , @NamedQuery(name = "OtsProduct.findByOtsProductGst", query = "SELECT o FROM OtsProduct o WHERE o.otsProductGst = :otsProductGst")
    , @NamedQuery(name = "OtsProduct.findByOtsProductProducerName", query = "SELECT o FROM OtsProduct o WHERE o.otsProductProducerName = :otsProductProducerName")
    , @NamedQuery(name = "OtsProduct.findByOtsProductAirtableId", query = "SELECT o FROM OtsProduct o WHERE o.otsProductAirtableId = :otsProductAirtableId")
    , @NamedQuery(name = "OtsProduct.findByOtsProductBasePrice", query = "SELECT o FROM OtsProduct o WHERE o.otsProductBasePrice = :otsProductBasePrice")})
public class OtsProduct implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ots_product_id")
    private Integer otsProductId;
    @Size(max = 100)
    @Column(name = "ots_product_name")
    private String otsProductName;
    @Size(max = 1000)
    @Column(name = "ots_product_description")
    private String otsProductDescription;
    @Size(max = 45)
    @Column(name = "ots_product_status")
    private String otsProductStatus;
    @Column(name = "ots_product_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otsProductTimestamp;
    @Column(name = "ots_product_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otsProductCreated;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ots_product_price")
    private BigDecimal otsProductPrice;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "ots_product_image")
    private String otsProductImage;
    @Size(max = 45)
    @Column(name = "ots_product_type")
    private String otsProductType;
    @Size(max = 45)
    @Column(name = "ots_product_threshold_day")
    private String otsProductThresholdDay;
    @Size(max = 45)
    @Column(name = "ots_product_gst")
    private String otsProductGst;
    @Size(max = 45)
    @Column(name = "ots_product_producer_name")
    private String otsProductProducerName;
    @Size(max = 45)
    @Column(name = "ots_product_airtable_id")
    private String otsProductAirtableId;
    @Size(max = 45)
    @Column(name = "ots_product_base_price")
    private String otsProductBasePrice;
    
    /*Multiple Product Image*/ 
    /*shreekant.rathod 29-dec-2020*/
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
    

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsProductId")
    private Collection<OtsProductStockHistory> otsProductStockHistoryCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsProductId")
    private Collection<OtsStockDistOb> otsStockDistObCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsProductId")
    private Collection<OtsProductStock> otsProductStockCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsProductId")
    private Collection<OtsRequestProduct> otsRequestProductCollection;
    @JoinColumn(name = "ots_product_level_id", referencedColumnName = "ots_product_level_id")
    @ManyToOne(optional = false)
    private OtsProductLevel otsProductLevelId;
    @JoinColumn(name = "ots_distributor_id", referencedColumnName = "ots_users_id")
    @ManyToOne
    private OtsUsers otsDistributorId;
    @OneToMany(mappedBy = "otsProductId")
    private Collection<OtsRegistration> otsRegistrationCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsProductCategoryId")
    private Collection<OtsProductCategoryMapping> otsProductCategoryMappingCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsProductId")
    private Collection<OtsProductCategoryMapping> otsProductCategoryMappingCollection1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsProductId")
    private Collection<OtsOrderProduct> otsOrderProductCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsProductId")
    private Collection<OtsScheduler> otsSchedulerCollection;
    @OneToMany(mappedBy = "otsProductId")
    private Collection<OtsCustomerProduct> otsCustomerProductCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otsProductId")
    private Collection<OtsRequestOrder> otsRequestOrderCollection;

    public OtsProduct() {
    }

    public OtsProduct(Integer otsProductId) {
        this.otsProductId = otsProductId;
    }

    public Integer getOtsProductId() {
        return otsProductId;
    }

    public void setOtsProductId(Integer otsProductId) {
        this.otsProductId = otsProductId;
    }

    public String getOtsProductName() {
        return otsProductName;
    }

    public void setOtsProductName(String otsProductName) {
        this.otsProductName = otsProductName;
    }

    public String getOtsProductDescription() {
        return otsProductDescription;
    }

    public void setOtsProductDescription(String otsProductDescription) {
        this.otsProductDescription = otsProductDescription;
    }

    public String getOtsProductStatus() {
        return otsProductStatus;
    }

    public void setOtsProductStatus(String otsProductStatus) {
        this.otsProductStatus = otsProductStatus;
    }

    public Date getOtsProductTimestamp() {
        return otsProductTimestamp;
    }

    public void setOtsProductTimestamp(Date otsProductTimestamp) {
        this.otsProductTimestamp = otsProductTimestamp;
    }

    public Date getOtsProductCreated() {
        return otsProductCreated;
    }

    public void setOtsProductCreated(Date otsProductCreated) {
        this.otsProductCreated = otsProductCreated;
    }

    public BigDecimal getOtsProductPrice() {
        return otsProductPrice;
    }

    public void setOtsProductPrice(BigDecimal otsProductPrice) {
        this.otsProductPrice = otsProductPrice;
    }

    public String getOtsProductImage() {
        return otsProductImage;
    }

    public void setOtsProductImage(String otsProductImage) {
        this.otsProductImage = otsProductImage;
    }

    public String getOtsProductType() {
        return otsProductType;
    }

    public void setOtsProductType(String otsProductType) {
        this.otsProductType = otsProductType;
    }

    public String getOtsProductThresholdDay() {
        return otsProductThresholdDay;
    }

    public void setOtsProductThresholdDay(String otsProductThresholdDay) {
        this.otsProductThresholdDay = otsProductThresholdDay;
    }

    public String getOtsProductGst() {
        return otsProductGst;
    }

    public void setOtsProductGst(String otsProductGst) {
        this.otsProductGst = otsProductGst;
    }

    public String getOtsProductProducerName() {
        return otsProductProducerName;
    }

    public void setOtsProductProducerName(String otsProductProducerName) {
        this.otsProductProducerName = otsProductProducerName;
    }

    public String getOtsProductAirtableId() {
        return otsProductAirtableId;
    }

    public void setOtsProductAirtableId(String otsProductAirtableId) {
        this.otsProductAirtableId = otsProductAirtableId;
    }

    public String getOtsProductBasePrice() {
        return otsProductBasePrice;
    }

    public void setOtsProductBasePrice(String otsProductBasePrice) {
        this.otsProductBasePrice = otsProductBasePrice;
    }
    
    /*shreekant.rathod 29-dec-2020*/
    

    @XmlTransient
    public Collection<OtsProductStockHistory> getOtsProductStockHistoryCollection() {
        return otsProductStockHistoryCollection;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
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

    @XmlTransient
    public Collection<OtsRequestProduct> getOtsRequestProductCollection() {
        return otsRequestProductCollection;
    }

    public void setOtsRequestProductCollection(Collection<OtsRequestProduct> otsRequestProductCollection) {
        this.otsRequestProductCollection = otsRequestProductCollection;
    }

    public OtsProductLevel getOtsProductLevelId() {
        return otsProductLevelId;
    }

    public void setOtsProductLevelId(OtsProductLevel otsProductLevelId) {
        this.otsProductLevelId = otsProductLevelId;
    }

    public OtsUsers getOtsDistributorId() {
        return otsDistributorId;
    }

    public void setOtsDistributorId(OtsUsers otsDistributorId) {
        this.otsDistributorId = otsDistributorId;
    }

    @XmlTransient
    public Collection<OtsRegistration> getOtsRegistrationCollection() {
        return otsRegistrationCollection;
    }

    public void setOtsRegistrationCollection(Collection<OtsRegistration> otsRegistrationCollection) {
        this.otsRegistrationCollection = otsRegistrationCollection;
    }

    @XmlTransient
    public Collection<OtsProductCategoryMapping> getOtsProductCategoryMappingCollection() {
        return otsProductCategoryMappingCollection;
    }

    public void setOtsProductCategoryMappingCollection(Collection<OtsProductCategoryMapping> otsProductCategoryMappingCollection) {
        this.otsProductCategoryMappingCollection = otsProductCategoryMappingCollection;
    }

    @XmlTransient
    public Collection<OtsProductCategoryMapping> getOtsProductCategoryMappingCollection1() {
        return otsProductCategoryMappingCollection1;
    }

    public void setOtsProductCategoryMappingCollection1(Collection<OtsProductCategoryMapping> otsProductCategoryMappingCollection1) {
        this.otsProductCategoryMappingCollection1 = otsProductCategoryMappingCollection1;
    }

    @XmlTransient
    public Collection<OtsOrderProduct> getOtsOrderProductCollection() {
        return otsOrderProductCollection;
    }

    public void setOtsOrderProductCollection(Collection<OtsOrderProduct> otsOrderProductCollection) {
        this.otsOrderProductCollection = otsOrderProductCollection;
    }

    @XmlTransient
    public Collection<OtsScheduler> getOtsSchedulerCollection() {
        return otsSchedulerCollection;
    }

    public void setOtsSchedulerCollection(Collection<OtsScheduler> otsSchedulerCollection) {
        this.otsSchedulerCollection = otsSchedulerCollection;
    }

    @XmlTransient
    public Collection<OtsCustomerProduct> getOtsCustomerProductCollection() {
        return otsCustomerProductCollection;
    }

    public void setOtsCustomerProductCollection(Collection<OtsCustomerProduct> otsCustomerProductCollection) {
        this.otsCustomerProductCollection = otsCustomerProductCollection;
    }

    @XmlTransient
    public Collection<OtsRequestOrder> getOtsRequestOrderCollection() {
        return otsRequestOrderCollection;
    }

    public void setOtsRequestOrderCollection(Collection<OtsRequestOrder> otsRequestOrderCollection) {
        this.otsRequestOrderCollection = otsRequestOrderCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (otsProductId != null ? otsProductId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtsProduct)) {
            return false;
        }
        OtsProduct other = (OtsProduct) object;
        if ((this.otsProductId == null && other.otsProductId != null) || (this.otsProductId != null && !this.otsProductId.equals(other.otsProductId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fuso.enterprise.ots.srv.server.model.entity.OtsProduct[ otsProductId=" + otsProductId + " ]";
    }
    
}
