package com.covid19army.HelpRequestService.dtos;

public class RequestVolunteerDto implements Cloneable{
	long volunteerid;
	boolean isactive;
	boolean isreject;
	String rejectreason;
	String volunteerName;
	
	
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
	public boolean isIsreject() {
		return isreject;
	}
	public void setIsreject(boolean isreject) {
		this.isreject = isreject;
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