package com.covid19army.HelpRequestService.services;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.covid19army.HelpRequestService.dtos.HelpRequestDto;
import com.covid19army.HelpRequestService.models.HelpRequest;
import com.covid19army.HelpRequestService.models.RequestNeed;
import com.covid19army.HelpRequestService.repositories.HelpRequestRepository;

public class HelpRequestService {
	
	@Autowired
	HelpRequestRepository _helpRequestRepository;

	@Autowired
	ModelMapper _mapper;
	
	public void createHelpRequest(HelpRequestDto helpRequestDto) {
		HelpRequest helpRequest = _mapper.map(helpRequestDto, HelpRequest.class);
		List<RequestNeed> requestNeeds = new ArrayList<>();
		for(int needType : helpRequestDto.getNeeds()){
			requestNeeds.add(new RequestNeed(needType,helpRequest));
		}
	}
}
