
package com.fuso.enterprise.ots.srv.server.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

@Entity
@Table(name = "users")
public class Users implements Serializable {

    private static final long serialVersionUID = 7360672237739183568L;
    
    @Id
    @Column(name = "UserId")
    private String userid;

    @Column(name = "FirstName")
    private String firstname;

    @Column(name = "LastName")
    private String lastname;
    

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

   

}
