package com.covid19army.HelpRequestService.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.covid19army.HelpRequestService.dtos.RequestVolunteerDto;
import com.covid19army.HelpRequestService.models.RequestVolunteer;
import com.covid19army.HelpRequestService.repositories.HelpRequestRepository;
import com.covid19army.HelpRequestService.repositories.RequestVolunteerRepository;
import com.covid19army.core.dtos.RequestVolutneerStatusDto;
import com.covid19army.core.exceptions.ResourceNotFoundException;
import com.covid19army.core.mex.rabbitmq.RabbitMQSender;

@Service
public class RequestVolunteerService {

	@Autowired
	RequestVolunteerRepository _requestVolunteerRepository;
	
	@Autowired
	HelpRequestRepository _helpRequestRepository;
	
	@Autowired
	@Qualifier("newRequestAcceptRejectExchangeSender")
	RabbitMQSender _newRequestAcceptRejectSender;
	
	@Autowired
	ModelMapper _mapper;
	
	public long createRequestVolunteer(RequestVolunteerDto dto) throws ResourceNotFoundException {
		
		var helpRequest = _helpRequestRepository.findById(dto.getRequestid());
		
		if(helpRequest.isEmpty())
			throw new ResourceNotFoundException("Request not found.");
		
		RequestVolunteer rvModel = _mapper.map(dto, RequestVolunteer.class);
		rvModel.setHelprequest(helpRequest.get());
		_requestVolunteerRepository.save(rvModel);
		
		RequestVolutneerStatusDto statusDto = new RequestVolutneerStatusDto();
		statusDto.setRequestid(rvModel.getHelprequest().getRequestid());
		statusDto.setVolunteerid(rvModel.getVolunteerid());
		if(dto.isIsactive()) {
			statusDto.setIsaccepted(true);	
		}
		
		_newRequestAcceptRejectSender.<RequestVolutneerStatusDto>send(statusDto);
		
		return rvModel.getRequestvolunteerid();		
	}
	
}
