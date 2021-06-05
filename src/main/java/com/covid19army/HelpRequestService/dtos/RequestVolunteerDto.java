package com.covid19army.HelpRequestService.dtos;

import java.io.Serializable;

public class RequestVolunteerDto implements Cloneable, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4918770331781743158L;
	long requestid;
	long volunteerid;
	boolean isactive;
	boolean isrejected;
	String rejectreason;
	String volunteerName;
	
	
	public long getRequestid() {
		return requestid;
	}
	public void setRequestid(long requestid) {
		this.requestid = requestid;
	}
	public String getVolunteerName() {
		return volunteerName;
	}
	public void setVolunteerName(String volunteerName) {
		this.volunteerName = volunteerName;
	}
	public long getVolunteerid() {
		return volunteerid;
	}
	public void setVolunteerid(long volunteerid) {
		this.volunteerid = volunteerid;
	}
	public boolean isIsactive() {
		return isactive;
	}
	public void setIsactive(boolean isactive) {
		this.isactive = isactive;
	}
	public boolean isIsrejected() {
		return isrejected;
	}
	public void setIsrejected(boolean isrejected) {
		this.isrejected = isrejected;
	}
	public String getRejectreason() {
		return rejectreason;
	}
	public void setRejectreason(String rejectreason) {
		this.rejectreason = rejectreason;
	}	
	public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
}