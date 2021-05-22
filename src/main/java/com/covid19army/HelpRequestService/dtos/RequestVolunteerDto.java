package com.covid19army.HelpRequestService.dtos;

public class RequestVolunteerDto implements Cloneable{
	Long volunteerid = 1L;
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
	public Long getVolunteerid() {
		return volunteerid;
	}
	public void setVolunteerid(Long volunteerid) {
		this.volunteerid = 1L;
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