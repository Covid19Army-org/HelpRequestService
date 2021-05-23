package com.covid19army.HelpRequestService.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;


/**
 * The persistent class for the newrequestwaitingqueue database table.
 * 
 */
@Entity
@Table(name = "newrequestwaitingqueue")
@NamedQuery(name="NewRequestWaitingQueue.findAll", query="SELECT n FROM NewRequestWaitingQueue n")
public class NewRequestWaitingQueue implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String itemid;
	
	private long requestid;
	
	private long volunteerid;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_created")
	private Date dateCreated;

	public NewRequestWaitingQueue() {
	}

	public Date getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	public String getItemid() {
		return this.itemid;
	}

	public void setItemid(String itemid) {
		this.itemid = itemid;
	}

	public long getRequestid() {
		return this.requestid;
	}

	public void setRequestid(long requestid) {
		this.requestid = requestid;
	}

	public long getVolunteerid() {
		return this.volunteerid;
	}

	public void setVolunteerid(long volunteerid) {
		this.volunteerid = volunteerid;
	}

}