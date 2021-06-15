package com.covid19army.HelpRequestService.services;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.covid19army.HelpRequestService.clients.VolunteerServiceClient;
import com.covid19army.HelpRequestService.dtos.RequestVolunteerDto;
import com.covid19army.HelpRequestService.dtos.VolunteerResponseDto;
import com.covid19army.HelpRequestService.models.RequestVolunteer;
import com.covid19army.HelpRequestService.repositories.HelpRequestRepository;
import com.covid19army.HelpRequestService.repositories.RequestVolunteerRepository;
import com.covid19army.core.dtos.RequestVolutneerStatusDto;
import com.covid19army.core.enums.HelpRequestStatusEnum;
import com.covid19army.core.exceptions.NotAuthorizedException;
import com.covid19army.core.exceptions.ResourceNotFoundException;
import com.covid19army.core.extensions.HttpServletRequestExtension;
import com.covid19army.core.mex.rabbitmq.RabbitMQSender;

@Service
public class RequestVolunteerService {

	@Autowired
	RequestVolunteerRepository _requestVolunteerRepository;
	
	@Autowired
	HelpRequestRepository _helpRequestRepository;
	
	@Autowired
	HttpServletRequestExtension _requestExtension;
	
	@Autowired
	VolunteerServiceClient _volunteerServiceClient;
	
	
	@Autowired
	@Qualifier("newRequestAcceptRejectExchangeSender")
	RabbitMQSender _newRequestAcceptRejectSender;
	
	@Autowired
	ModelMapper _mapper;
	
	
	public long createRequestVolunteerAcceptedObject(RequestVolunteerDto dto) 
			throws ResourceNotFoundException, NotAuthorizedException {
		var authUserId = Long.parseLong(_requestExtension.getAuthenticatedUser());
		var helpRequest = _helpRequestRepository.findById(dto.getRequestid());
		
		if(helpRequest.isEmpty())
			throw new ResourceNotFoundException("Request not found.");
		
		var hrModel = helpRequest.get();
		
		var isRequestactive = _requestVolunteerRepository.existsByIsactiveIsTrueAndRequestid(hrModel.getRequestid());
		if(isRequestactive)
			throw new ResourceNotFoundException("Request is active. you cannot accept.");
		
		List<Long> volunteersIdList = new ArrayList<>();
		volunteersIdList.add(dto.getVolunteerid());
		List<VolunteerResponseDto> volunteerList = _volunteerServiceClient.searchByVolunteerId(volunteersIdList);
		
		VolunteerResponseDto volunteer = null;
		if(volunteerList.size() > 0) {
			volunteer = volunteerList.get(0);			
		}
		
		if(hrModel.getUserid() == authUserId && volunteer.getUserid() != authUserId)
			throw new NotAuthorizedException();
		
		RequestVolunteer rvModel = _mapper.map(dto, RequestVolunteer.class);
		rvModel.setHelprequest(hrModel);
		rvModel.setIsactive(true);
		rvModel.setIsrejected(false);
		_requestVolunteerRepository.save(rvModel);
		
		
		hrModel.setStatus(HelpRequestStatusEnum.ACCEPTED);		
		_helpRequestRepository.save(hrModel);
		
		this.publishMessage(rvModel, true);
		return rvModel.getRequestvolunteerid();		
	}
	
	public long createRequestVolunteerRejectedObject(RequestVolunteerDto dto) 
			throws ResourceNotFoundException, NotAuthorizedException {
		var authUserId = Long.parseLong(_requestExtension.getAuthenticatedUser());
		var helpRequest = _helpRequestRepository.findById(dto.getRequestid());
		
		if(helpRequest.isEmpty())
			throw new ResourceNotFoundException("Request not found.");
		
		var hrModel = helpRequest.get();
		List<Long> volunteersIdList = new ArrayList<>();
		volunteersIdList.add(dto.getVolunteerid());
		List<VolunteerResponseDto> volunteerList = _volunteerServiceClient.searchByVolunteerId(volunteersIdList);
		
		VolunteerResponseDto volunteer = null;
		if(volunteerList.size() > 0) {
			volunteer = volunteerList.get(0);			
		}
		
		if(hrModel.getUserid() == authUserId && volunteer.getUserid() != authUserId)
			throw new NotAuthorizedException();
		
		RequestVolunteer rvModel = _mapper.map(dto, RequestVolunteer.class);
		rvModel.setHelprequest(hrModel);
		rvModel.setIsactive(false);
		rvModel.setIsrejected(true);
		_requestVolunteerRepository.save(rvModel);
		
		this.publishMessage(rvModel, false);
		return rvModel.getRequestvolunteerid();		
	}
	
	public void deactivateRequestVolunteer(long requestid) 
			throws ResourceNotFoundException, NotAuthorizedException {
		var authUserId = Long.parseLong(_requestExtension.getAuthenticatedUser());
		var helpRequest = _helpRequestRepository.findById(requestid);
		
		if(helpRequest.isEmpty())
			throw new ResourceNotFoundException("Request not found.");
		
		var hrModel = helpRequest.get();		

		if(hrModel.getUserid() != authUserId)
			throw new NotAuthorizedException();		
		
		var activeRequestOpt = _requestVolunteerRepository.findByIsactiveIsTrueAndRequestid(hrModel.getRequestid());
		if(activeRequestOpt.isPresent()) {
			var rvModel = activeRequestOpt.get();	

			rvModel.setIsactive(false);
			_requestVolunteerRepository.save(rvModel);
		}
			
	}
	
	private void publishMessage(RequestVolunteer rvModel, boolean status) {
		RequestVolutneerStatusDto statusDto = new RequestVolutneerStatusDto();
		statusDto.setRequestid(rvModel.getHelprequest().getRequestid());
		statusDto.setVolunteerid(rvModel.getVolunteerid());
		
			statusDto.setIsaccepted(status);
		
		_newRequestAcceptRejectSender.<RequestVolutneerStatusDto>send(statusDto);
		
	}
	
}
