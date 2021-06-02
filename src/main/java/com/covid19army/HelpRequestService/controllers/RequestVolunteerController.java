package com.covid19army.HelpRequestService.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.covid19army.HelpRequestService.dtos.RequestVolunteerDto;
import com.covid19army.HelpRequestService.services.RequestVolunteerService;


@RestController
@RequestMapping("requestvolunteer")
public class RequestVolunteerController {

	@Autowired
	RequestVolunteerService _requestVolunteerService;

	
	@PostMapping("/accept")
	public long acceptRequest(@RequestBody RequestVolunteerDto acceptDto) {
		acceptDto.setIsactive(true);
		acceptDto.setIsreject(false);
		return _requestVolunteerService.createRequestVolunteer(acceptDto);
	}
	
	@PostMapping("/reject")
	public long rejectRequest(@RequestBody RequestVolunteerDto rejectDto) {
		rejectDto.setIsactive(false);
		rejectDto.setIsreject(true);
		return _requestVolunteerService.createRequestVolunteer(rejectDto);
	}
}
