package com.covid19army.HelpRequestService.dtos;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.netflix.discovery.provider.ISerializer;

public class HelpRequestResponseDto extends HelpRequestDto{
	List<RequestNeedDto> requestneeds;
	List<RequestVolunteerDto> requestvolunteers;
	Date dateCreated;
	boolean iscontactverified;	
	
	long userid;	
	
	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public boolean isIscontactverified() {
		return iscontactverified;
	}
	public void setIscontactverified(boolean iscontactverified) {
		this.iscontactverified = iscontactverified;
	}
	public List<RequestNeedDto> getRequestneeds() {
		return requestneeds;
	}
	public void setRequestneeds(List<RequestNeedDto> requestneeds) {
		this.requestneeds = requestneeds;
		this.setNeeds(requestneeds.stream()
				.map(RequestNeedDto::getNeedid)
				.collect(Collectors.toList()));
	}
	public List<RequestVolunteerDto> getRequestvolunteers() {
		return requestvolunteers;
	}
	public void setRequestvolunteers(List<RequestVolunteerDto> requestvolunteers) {
		this.requestvolunteers = requestvolunteers;
	}		
}