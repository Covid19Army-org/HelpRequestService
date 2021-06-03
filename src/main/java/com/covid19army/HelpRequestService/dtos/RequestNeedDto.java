package com.covid19army.HelpRequestService.dtos;

import java.io.Serializable;

import com.covid19army.core.enums.NeedsEnum;

public class RequestNeedDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3197356661330614418L;
	private NeedsEnum needid;

	public RequestNeedDto() {
	}

	public NeedsEnum getNeedid() {
		return needid;
	}

	public void setNeedid(NeedsEnum needid) {
		this.needid = needid;
	}
}