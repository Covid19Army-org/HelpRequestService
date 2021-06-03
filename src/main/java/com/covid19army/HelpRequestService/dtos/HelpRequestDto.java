package com.covid19army.HelpRequestService.dtos;

import java.io.Serializable;
import java.util.List;

import com.covid19army.core.enums.NeedsEnum;

public class HelpRequestDto implements  Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7788578419319792321L;

	private long requestid;

	private int age;

	private String comments;

	private String contactnumber;
	
	private String district;	

	private boolean iscovidpositive;

	private String pincode;
	
	private String preExistingConditions;

	private String recipientname;

	private long srfid;

	private String state;

	private int status;
	
	List<NeedsEnum> needs;
	
	public List<NeedsEnum> getNeeds(){
		return needs;
	}
	
	public void setNeeds(List<NeedsEnum> needs) {
		this.needs = needs;
	}
	
	public long getRequestid() {
		return requestid;
	}

	public void setRequestid(long requestid) {
		this.requestid = requestid;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getContactnumber() {
		return contactnumber;
	}

	public void setContactnumber(String contactnumber) {
		this.contactnumber = contactnumber;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public boolean getIscovidpositive() {
		return iscovidpositive;
	}

	public void setIscovidpositive(boolean iscovidpositive) {
		this.iscovidpositive = iscovidpositive;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getPreExistingConditions() {
		return preExistingConditions;
	}

	public void setPreExistingConditions(String preExistingConditions) {
		this.preExistingConditions = preExistingConditions;
	}

	public String getRecipientname() {
		return recipientname;
	}

	public void setRecipientname(String recipientname) {
		this.recipientname = recipientname;
	}

	public long getSrfid() {
		return srfid;
	}

	public void setSrfid(long srfid) {
		this.srfid = srfid;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
