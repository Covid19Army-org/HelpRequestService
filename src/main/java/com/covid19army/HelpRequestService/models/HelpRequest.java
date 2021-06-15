package com.covid19army.HelpRequestService.models;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import com.covid19army.HelpRequestService.modelListeners.HelpRequestModelListener;
import com.covid19army.core.enums.HelpRequestStatusEnum;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the helprequests database table.
 * 
 */
@EntityListeners(HelpRequestModelListener.class)
@Entity
@Table(name="helprequests")
@NamedQuery(name="HelpRequest.findAll", query="SELECT h FROM HelpRequest h")
public class HelpRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long requestid;

	private int age;

	private String comments;

	private String contactnumber;

	private int countrycode;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_created")
	private Date dateCreated;

	private String district;

	private boolean iscontactverified;

	private boolean iscovidpositive;

	private boolean isdeleted;

	private String pincode;

	@Column(name="pre_existing_conditions")
	private String preExistingConditions;

	private String recipientname;

	private long srfid;

	private String state;

	@Enumerated(EnumType.ORDINAL)
	private HelpRequestStatusEnum status;
	
	public long userid;


	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	//bi-directional many-to-one association to RequestNeed
	@OneToMany(mappedBy="helprequest", cascade = CascadeType.ALL, fetch= FetchType.LAZY)	
	private List<RequestNeed> requestneeds;

	//bi-directional many-to-one association to RequestVolunteer
	@OneToMany(mappedBy="helprequest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)	
	private List<RequestVolunteer> requestvolunteers;

	public HelpRequest() {
	}

	public long getRequestid() {
		return this.requestid;
	}

	public void setRequestid(long requestid) {
		this.requestid = requestid;
	}

	public int getAge() {
		return this.age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getContactnumber() {
		return this.contactnumber;
	}

	public void setContactnumber(String contactnumber) {
		this.contactnumber = contactnumber;
	}

	public int getCountrycode() {
		return this.countrycode;
	}

	public void setCountrycode(int countrycode) {
		this.countrycode = countrycode;
	}

	public Date getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getDistrict() {
		return this.district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public boolean getIscontactverified() {
		return this.iscontactverified;
	}

	public void setIscontactverified(boolean iscontactverified) {
		this.iscontactverified = iscontactverified;
	}

	public boolean getIscovidpositive() {
		return this.iscovidpositive;
	}

	public void setIscovidpositive(boolean iscovidpositive) {
		this.iscovidpositive = iscovidpositive;
	}

	public boolean getIsdeleted() {
		return this.isdeleted;
	}

	public void setIsdeleted(boolean isdeleted) {
		this.isdeleted = isdeleted;
	}

	public String getPincode() {
		return this.pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getPreExistingConditions() {
		return this.preExistingConditions;
	}

	public void setPreExistingConditions(String preExistingConditions) {
		this.preExistingConditions = preExistingConditions;
	}

	public String getRecipientname() {
		return this.recipientname;
	}

	public void setRecipientname(String recipientname) {
		this.recipientname = recipientname;
	}

	public long getSrfid() {
		return this.srfid;
	}

	public void setSrfid(long srfid) {
		this.srfid = srfid;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public HelpRequestStatusEnum getStatus() {
		return this.status;
	}

	public void setStatus(HelpRequestStatusEnum status) {
		this.status = status;
	}

	public List<RequestNeed> getRequestneeds() {
		return this.requestneeds;
	}

	public void setRequestneeds(List<RequestNeed> requestneeds) {
		this.requestneeds = requestneeds;
	}

	public RequestNeed addRequestneed(RequestNeed requestneed) {
		getRequestneeds().add(requestneed);
		requestneed.setHelprequest(this);

		return requestneed;
	}

	public RequestNeed removeRequestneed(RequestNeed requestneed) {
		getRequestneeds().remove(requestneed);
		requestneed.setHelprequest(null);

		return requestneed;
	}

	public List<RequestVolunteer> getRequestvolunteers() {
		return this.requestvolunteers;
	}

	public void setRequestvolunteers(List<RequestVolunteer> requestvolunteers) {
		this.requestvolunteers = requestvolunteers;
	}

	public RequestVolunteer addRequestvolunteer(RequestVolunteer requestvolunteer) {
		getRequestvolunteers().add(requestvolunteer);
		requestvolunteer.setHelprequest(this);

		return requestvolunteer;
	}

	public RequestVolunteer removeRequestvolunteer(RequestVolunteer requestvolunteer) {
		getRequestvolunteers().remove(requestvolunteer);
		requestvolunteer.setHelprequest(null);

		return requestvolunteer;
	}

}