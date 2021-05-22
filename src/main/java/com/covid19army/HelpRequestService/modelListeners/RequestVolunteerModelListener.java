package com.covid19army.HelpRequestService.modelListeners;

import java.util.Date;

import javax.persistence.PrePersist;

import com.covid19army.HelpRequestService.models.RequestVolunteer;

public class RequestVolunteerModelListener {

	@PrePersist
	public void onCreating(RequestVolunteer requestVolunteer) {
		Date currentDate = new Date();
		requestVolunteer.setDateCreated(currentDate);
		requestVolunteer.setDateMessageLastSeenByVolunteer(currentDate);
		requestVolunteer.setIsactive(true);
		requestVolunteer.setIsrejected(false);	
		requestVolunteer.setVolunteerid(null);
	}
}
