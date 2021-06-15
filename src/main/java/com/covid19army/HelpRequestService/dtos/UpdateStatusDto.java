package com.covid19army.HelpRequestService.dtos;

import com.covid19army.core.enums.HelpRequestStatusEnum;

public class UpdateStatusDto {

	HelpRequestStatusEnum status;
	
	long requestid;

	public HelpRequestStatusEnum getStatus() {
		return status;
	}

	public void setStatus(HelpRequestStatusEnum status) {
		this.status = status;
	}

	public long getRequestid() {
		return requestid;
	}

	public void setRequestid(long requestid) {
		this.requestid = requestid;
	}
	
	
}
