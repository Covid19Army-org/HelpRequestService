package com.covid19army.HelpRequestService.dtos;

import java.util.List;

import com.covid19army.core.enums.NeedsEnum;

public class VolunteerSearchDto {

	private String state;
	private String district;
	private String pincode;
	
	private List<NeedsEnum> needs;
	
	
	
	public List<NeedsEnum> getNeeds() {
		return needs;
	}
	public void setNeeds(List<NeedsEnum> needs) {
		this.needs = needs;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	
	
}
