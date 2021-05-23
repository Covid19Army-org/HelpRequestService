package com.covid19army.HelpRequestService.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.covid19army.HelpRequestService.dtos.RequestVolunteerDto;
import com.covid19army.HelpRequestService.models.RequestVolunteer;
import com.covid19army.HelpRequestService.repositories.RequestVolunteerRepository;

@Service
public class RequestVolunteerService {

	@Autowired
	RequestVolunteerRepository _requestVolunteerRepository;
	
	@Autowired
	ModelMapper _mapper;
	
	public long createRequestVolunteer(RequestVolunteerDto dto) {
		RequestVolunteer rvModel = _mapper.map(dto, RequestVolunteer.class);
		_requestVolunteerRepository.save(rvModel);
		return rvModel.getRequestvolunteerid();		
	}
	
}
