package com.covid19army.HelpRequestService.dtos;

public class VolunteerResponseDto {
	long volunteerid;
	String name;
	long userid;
	
	public VolunteerResponseDto(long volunteerid, String name ) {
		this.volunteerid = volunteerid;
		this.name = name;
	}
	
	public long getVolunteerid() {
		return volunteerid;
	}
	public void setVolunteerid(long volunteerid) {
		this.volunteerid = volunteerid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}
	
	
}
