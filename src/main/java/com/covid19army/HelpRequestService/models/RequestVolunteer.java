package com.covid19army.HelpRequestService.models;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import com.covid19army.HelpRequestService.modelListeners.RequestVolunteerModelListener;

import java.util.Date;


/**
 * The persistent class for the requestvolunteer database table.
 * 
 */
@EntityListeners(RequestVolunteerModelListener.class)
@Entity
@NamedQuery(name="RequestVolunteer.findAll", query="SELECT r FROM RequestVolunteer r")
@Table(name = "requestvolunteer")
public class RequestVolunteer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long requestvolunteerid;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_created")
	private Date dateCreated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_message_last_seen_by_volunteer")
	private Date dateMessageLastSeenByVolunteer;

	private boolean isactive;

	private boolean isrejected;

	private String rejectreason;
	
	private Long volunteerid;

	//bi-directional many-to-one association to HelpRequest	
	@ManyToOne(fetch=FetchType.LAZY, optional = false)	
	@JoinColumn(name="requestid")
	private HelpRequest helprequest;

	
	public RequestVolunteer() {
	}

	public long getRequestvolunteerid() {
		return this.requestvolunteerid;
	}

	public void setRequestvolunteerid(long requestvolunteerid) {
		this.requestvolunteerid = requestvolunteerid;
	}

	public Date getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateMessageLastSeenByVolunteer() {
		return this.dateMessageLastSeenByVolunteer;
	}

	public void setDateMessageLastSeenByVolunteer(Date dateMessageLastSeenByVolunteer) {
		this.dateMessageLastSeenByVolunteer = dateMessageLastSeenByVolunteer;
	}

	public boolean getIsactive() {
		return this.isactive;
	}

	public void setIsactive(boolean isactive) {
		this.isactive = isactive;
	}

	public boolean getIsrejected() {
		return this.isrejected;
	}

	public void setIsrejected(boolean isrejected) {
		this.isrejected = isrejected;
	}

	public String getRejectreason() {
		return this.rejectreason;
	}

	public void setRejectreason(String rejectreason) {
		this.rejectreason = rejectreason;
	}

	public HelpRequest getHelprequest() {
		return this.helprequest;
	}

	public void setHelprequest(HelpRequest helprequest) {
		this.helprequest = helprequest;
	}

	public Long getVolunteerid() {
		return volunteerid;
	}

	public void setVolunteerid(Long volunteerid) {
		this.volunteerid = volunteerid;
	}	
	
}