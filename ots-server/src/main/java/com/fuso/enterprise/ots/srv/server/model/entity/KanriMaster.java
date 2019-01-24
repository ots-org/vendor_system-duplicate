package com.fuso.enterprise.ots.srv.server.model.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table (name = "kanri_master")
public class KanriMaster  implements Serializable{
	
	private static final long      serialVersionUID = 7360652237739183568L;
	
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "id") 
    private String  id;
	
	@Column(name = "kanri_no")
	private String kanriNo;
	

	@Column (name = "description")
	private String description;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKanriNo() {
		return kanriNo;
	}

	public void setKanriNo(String kanriNo) {
		this.kanriNo = kanriNo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	
	
	

}
