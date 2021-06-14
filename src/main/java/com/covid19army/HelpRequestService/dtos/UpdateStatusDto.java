package com.covid19army.HelpRequestService.dtos;

public class UpdateStatusDto {

	int status;
	
	long requestid;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getRequestid() {
		return requestid;
	}

	public void setRequestid(long requestid) {
		this.requestid = requestid;
	}
	
	
}
