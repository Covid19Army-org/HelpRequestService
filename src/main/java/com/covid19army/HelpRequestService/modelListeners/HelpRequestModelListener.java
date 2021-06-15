package com.covid19army.HelpRequestService.modelListeners;

import java.util.Date;

import javax.persistence.PrePersist;

import com.covid19army.HelpRequestService.models.HelpRequest;
import com.covid19army.core.enums.HelpRequestStatusEnum;

public class HelpRequestModelListener {

	@PrePersist
	public void onCreating(HelpRequest helpRequest) {
		Date currentDate = new Date();
		helpRequest.setIsdeleted(false);
		helpRequest.setDateCreated(currentDate);
		helpRequest.setIscontactverified(false);
		helpRequest.setStatus(HelpRequestStatusEnum.NEW);
		helpRequest.setCountrycode(91);
	}
}
