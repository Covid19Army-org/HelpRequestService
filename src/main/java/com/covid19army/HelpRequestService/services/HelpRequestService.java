package com.covid19army.HelpRequestService.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.covid19army.HelpRequestService.clients.VolunteerServiceClient;
import com.covid19army.HelpRequestService.dtos.HelpRequestDto;
import com.covid19army.HelpRequestService.dtos.HelpRequestResponseDto;
import com.covid19army.HelpRequestService.dtos.PagedResponseDto;
import com.covid19army.HelpRequestService.dtos.RequestVolunteerDto;
import com.covid19army.HelpRequestService.dtos.VolunteerResponseDto;
import com.covid19army.HelpRequestService.models.HelpRequest;
import com.covid19army.HelpRequestService.models.NewRequestWaitingQueue;
import com.covid19army.HelpRequestService.models.RequestNeed;
import com.covid19army.HelpRequestService.models.RequestVolunteer;
import com.covid19army.HelpRequestService.repositories.HelpRequestRepository;
import com.covid19army.HelpRequestService.repositories.NewRequestWaitingQueueRepository;
import com.covid19army.HelpRequestService.repositories.RequestVolunteerRepository;
import com.covid19army.core.dtos.MobileVerificationQueueDto;
import com.covid19army.core.enums.HelpRequestStatusEnum;
import com.covid19army.core.enums.NeedsEnum;
import com.covid19army.core.exceptions.NotAuthorizedException;
import com.covid19army.core.exceptions.ResourceNotFoundException;
import com.covid19army.core.extensions.HttpServletRequestExtension;
import com.covid19army.core.mex.rabbitmq.RabbitMQSender;



@Service
public class HelpRequestService {
	
	Logger _logger = LoggerFactory.getLogger(HelpRequestService.class);
	
	@Autowired
	HelpRequestRepository _helpRequestRepository;
	
	@Autowired
	NewRequestWaitingQueueRepository _newRequestWaitingQueueRepository;

	@Autowired
	RequestVolunteerRepository _rvRepository;
	
	@Autowired
	RequestVolunteerService _rvService;
	
	@Autowired
	ModelMapper _mapper;
	
	@Autowired
	@Qualifier("otpExchangeSender")
	RabbitMQSender _otpExchangeSender;
	
	@Autowired
	@Qualifier("newRequestWaitingExchangeSender")
	RabbitMQSender _newRequestWaitingExchangeSender;
	
	@Autowired
	HttpServletRequestExtension _requestExtension;
	
	@Autowired
	VolunteerServiceClient _volunteerServiceClient;
	
	public long getHelpRequestOwner(long requestId) throws ResourceNotFoundException, NotAuthorizedException {
		long authUserId = Long.parseLong( _requestExtension.getAuthenticatedUser());
		var helprequest = _helpRequestRepository.findById(requestId);
		
		if(helprequest.isEmpty())
			throw new ResourceNotFoundException("Request not found");
		
		var helprequestmodel = helprequest.get();
		var owner = helprequestmodel.getUserid();
		if(owner != authUserId)
			throw new NotAuthorizedException();
		
		return owner;
		
	}
	
	public void updateHelpRequest(long requestid, HelpRequestDto helpRequestDto) 
			throws ResourceNotFoundException {
		var helprequest = _helpRequestRepository.findById(requestid);
		
		if(helprequest.isEmpty())
			throw new ResourceNotFoundException("Request not found");
		
		List<RequestNeed> requestNeeds = new ArrayList<>();
		HelpRequest helpRequest = _mapper.map(helpRequestDto, HelpRequest.class);
		for(NeedsEnum needType : helpRequestDto.getNeeds()){
			requestNeeds.add(new RequestNeed(needType,helpRequest));				
		}
		helpRequest.setRequestneeds(requestNeeds);
		_helpRequestRepository.save(helpRequest);
		
	}
	
	public HelpRequest createHelpRequest(HelpRequestDto helpRequestDto) {
		HelpRequest helpRequest = _mapper.map(helpRequestDto, HelpRequest.class);
		helpRequest.setUserid(Long.parseLong( _requestExtension.getAuthenticatedUser()));
		List<RequestNeed> requestNeeds = new ArrayList<>();
		for(NeedsEnum needType : helpRequestDto.getNeeds()){
			requestNeeds.add(new RequestNeed(needType,helpRequest));			
		}
		
		
		//List<RequestVolunteer> requestVolunteers = new ArrayList<RequestVolunteer>();
		//RequestVolunteer requestVolunteer = new RequestVolunteer();
		//requestVolunteer.setHelprequest(helpRequest);
		//requestVolunteers.add(requestVolunteer);
		
		helpRequest.setRequestneeds(requestNeeds);
		//helpRequest.setRequestvolunteers(requestVolunteers);
		
		_helpRequestRepository.save(helpRequest);
		
		MobileVerificationQueueDto otpdto = new MobileVerificationQueueDto();
		otpdto.setMobilenumber(helpRequest.getContactnumber());
		otpdto.setEntityid(helpRequest.getRequestid());
		otpdto.setEntitytype("HRQ");
		_otpExchangeSender.<MobileVerificationQueueDto>send(otpdto);
		
		var requestMessage = _mapper.map(helpRequest, HelpRequestResponseDto.class);
		
		_newRequestWaitingExchangeSender.<HelpRequestResponseDto>send(requestMessage);		
		
		return helpRequest;
	}
	
	public void updateMobileVerified(long requestId, boolean isVerified) throws ResourceNotFoundException {
		Optional<HelpRequest> helpRequest =  _helpRequestRepository.findById(requestId);
		if(helpRequest.isEmpty()) {
			throw new ResourceNotFoundException("Invalid Help Request Id");
			
		}
		
		var model = helpRequest.get();
		model.setIscontactverified(isVerified);
		_helpRequestRepository.save(model);
		
	}
	
	public HelpRequest updateRequestStatus(long requestId, HelpRequestStatusEnum status)
			throws ResourceNotFoundException, NotAuthorizedException {
		var authUserId = Long.parseLong(_requestExtension.getAuthenticatedUser());
		
		Optional<HelpRequest> helpRequest =  _helpRequestRepository.findById(requestId);
		if(helpRequest.isEmpty()) {
			throw new ResourceNotFoundException("Invalid Help Request Id.");			
		}
		var hrModel = helpRequest.get();
			
		Optional<RequestVolunteer> activeRequestVolunteerOpt = _rvRepository.findByIsactiveIsTrueAndRequestid(hrModel.getRequestid());
		if(activeRequestVolunteerOpt.isEmpty())
			throw new ResourceNotFoundException("Updating status of inactive request is not allowed");		
		
		List<Long> volunteersIdList = new ArrayList<>();
		volunteersIdList.add(activeRequestVolunteerOpt.get().getVolunteerid());
		List<VolunteerResponseDto> volunteerList = _volunteerServiceClient.searchByVolunteerId(volunteersIdList);
		
		VolunteerResponseDto volunteer = null;
		if(volunteerList.size() > 0) {
			volunteer = volunteerList.get(0);			
		}
		
		if(hrModel.getUserid() != authUserId && volunteer.getUserid() != authUserId)
			throw new NotAuthorizedException();
				
		hrModel.setStatus(status);			
		return _helpRequestRepository.save(hrModel);
		
	}
	
	public void reRequest(long requestid) throws ResourceNotFoundException, NotAuthorizedException {
		var helpRequest = this.updateRequestStatus(requestid, HelpRequestStatusEnum.NEW);
		_rvService.deactivateRequestVolunteer(requestid);
		
		var requestMessage = _mapper.map(helpRequest, HelpRequestResponseDto.class);
		
		_newRequestWaitingExchangeSender.<HelpRequestResponseDto>send(requestMessage);		
	}
	
	public PagedResponseDto<HelpRequestResponseDto> getHelpRequestsByUser(Pageable pageable){
		long userid = Long.parseLong( _requestExtension.getAuthenticatedUser());
		Page<HelpRequest> page = _helpRequestRepository.findByUserid(userid, pageable);
		
		List<HelpRequestResponseDto> dto = this.convertToHelpRequestResponseDto(page.getContent());
		
		List<Long> volunteersIdList = dto.stream()
				.flatMap(d -> d.getRequestvolunteers()
						.stream()
						.map(RequestVolunteerDto::getVolunteerid)).distinct()
				.collect(Collectors.toList());
		
		_logger.info("volunteer value "+ volunteersIdList.size());
		
		//TODO: make client call to get volunteer data
		
		List<VolunteerResponseDto> volunteerResponseResult = _volunteerServiceClient.searchByVolunteerId(volunteersIdList);
		
		//volunteerResponseResult.add(new VolunteerResponseDto(1,"Ravi"));
			
		dto = dto.stream()
				.map(hr -> this.updateHelpRequestResponseDto(hr, volunteerResponseResult ))
				.collect(Collectors.toList());
		
		PagedResponseDto<HelpRequestResponseDto> responseData = new PagedResponseDto<>();
		responseData.setCurrentPage(page.getNumber());
		responseData.setTotalItems(page.getTotalElements());
		responseData.setTotalPages(page.getTotalPages());
		responseData.setData(dto);
		
		return responseData;
		
		
	}
	
	// get new requests for volunteer
	public List<HelpRequestResponseDto> getNewRequestsForVolunteer(long volunteerid, Pageable pageable){
		//TODO: validate volunteer
		Page<NewRequestWaitingQueue> requestWaitingQueuePagedResult = _newRequestWaitingQueueRepository.findByVolunteerid(volunteerid, pageable);
		List<Long> requestIds = requestWaitingQueuePagedResult.getContent()
		.stream().map(NewRequestWaitingQueue::getRequestid)
		.collect(Collectors.toList());
		List<HelpRequest> volunteerRequests = _helpRequestRepository.findByRequestidIn(requestIds);
		List<HelpRequestResponseDto> dto = this.convertToHelpRequestResponseDto(volunteerRequests);
				
		return dto;
	}
	
	// get accepted requests for volunteer
	public PagedResponseDto<HelpRequestResponseDto> getVolunteerAcceptedRequests(long volunteerid, Pageable pageable){
		//TODO:  validate volunteer
		Page<HelpRequest> helpRequestPage = _helpRequestRepository.findVolunteerAcceptedRequest(volunteerid, pageable);
		List<HelpRequestResponseDto> dto = convertToHelpRequestResponseDto(helpRequestPage.getContent());
		PagedResponseDto<HelpRequestResponseDto> responseData = new PagedResponseDto<>();
		responseData.setCurrentPage(helpRequestPage.getNumber());
		responseData.setTotalItems(helpRequestPage.getTotalElements());
		responseData.setTotalPages(helpRequestPage.getTotalPages());
		responseData.setData(dto);
		
		return responseData; 		
	}
	
	// get rejected requests for volunteer ?
	
	private HelpRequestResponseDto updateHelpRequestResponseDto(HelpRequestResponseDto dto, List<VolunteerResponseDto> volunteerResponseResult) {
		/*
		for(RequestVolunteerDto volunteerDto : dto.getRequestvolunteers()) {			
			VolunteerResponseDto volunteerData = volunteerResponseResult.stream()
										.filter(x->x.getVolunteerid() == volunteerDto.getVolunteerid())
										.findFirst().get();
			volunteerDto.setVolunteerName(volunteerData.getName());			
		}
		*/
		List<RequestVolunteerDto> updatedVolunteersList = dto.getRequestvolunteers().stream()
		.map(rv -> setVolunteerName(rv, volunteerResponseResult) )
		.collect(Collectors.toList());
		
		dto.setRequestvolunteers(updatedVolunteersList);
		
		//dto.setRequestvolunteers(null);
		return dto;
	}
	
	private RequestVolunteerDto setVolunteerName(RequestVolunteerDto volunteerDto, List<VolunteerResponseDto> volunteerResponseResult) {
		VolunteerResponseDto volunteerData = volunteerResponseResult.stream()
				.filter(x->x.getVolunteerid() == volunteerDto.getVolunteerid())
				.findFirst().get();
		volunteerDto.setVolunteerName(volunteerData.getName());	
		return volunteerDto;
	}
	
	
	private List<HelpRequestResponseDto> convertToHelpRequestResponseDto(List<HelpRequest> requestModelData){
		List<HelpRequestResponseDto> dto = requestModelData.stream()
		.map(hr -> _mapper.map(hr, HelpRequestResponseDto.class))
		.collect(Collectors.toList());
		return dto;
	}
}
