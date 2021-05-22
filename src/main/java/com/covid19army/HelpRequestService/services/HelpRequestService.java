package com.covid19army.HelpRequestService.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.covid19army.HelpRequestService.dtos.HelpRequestDto;
import com.covid19army.HelpRequestService.dtos.HelpRequestResponseDto;
import com.covid19army.HelpRequestService.dtos.PagedResponseDto;
import com.covid19army.HelpRequestService.dtos.RequestVolunteerDto;
import com.covid19army.HelpRequestService.dtos.VolunteerResponseDto;
import com.covid19army.HelpRequestService.models.HelpRequest;
import com.covid19army.HelpRequestService.models.RequestNeed;
import com.covid19army.HelpRequestService.models.RequestVolunteer;
import com.covid19army.HelpRequestService.repositories.HelpRequestRepository;
import com.covid19army.core.extensions.HttpServletRequestExtension;



@Service
public class HelpRequestService {
	
	Logger _logger = LoggerFactory.getLogger(HelpRequestService.class);
	
	@Autowired
	HelpRequestRepository _helpRequestRepository;

	@Autowired
	ModelMapper _mapper;
	
	@Autowired
	HttpServletRequestExtension _requestExtension;
	
	public HelpRequest createHelpRequest(HelpRequestDto helpRequestDto) {
		HelpRequest helpRequest = _mapper.map(helpRequestDto, HelpRequest.class);
		helpRequest.setUserid(Long.parseLong( _requestExtension.getAuthenticatedUser()));
		List<RequestNeed> requestNeeds = new ArrayList<>();
		for(int needType : helpRequestDto.getNeeds()){
			requestNeeds.add(new RequestNeed(needType,helpRequest));			
		}
		
		
		List<RequestVolunteer> requestVolunteers = new ArrayList<RequestVolunteer>();
		RequestVolunteer requestVolunteer = new RequestVolunteer();
		requestVolunteer.setHelprequest(helpRequest);
		requestVolunteers.add(requestVolunteer);
		
		helpRequest.setRequestneeds(requestNeeds);
		helpRequest.setRequestvolunteers(requestVolunteers);
		
		_helpRequestRepository.save(helpRequest);
		return helpRequest;
	}
	
	public PagedResponseDto<HelpRequestResponseDto> getHelpRequestsByUser(Pageable pageable){
		long userid = Long.parseLong( _requestExtension.getAuthenticatedUser());
		Page<HelpRequest> page = _helpRequestRepository.findByUserid(userid, pageable);
		
		List<HelpRequestResponseDto> dto = page.getContent().stream()
		.map(hr -> _mapper.map(hr, HelpRequestResponseDto.class))
		.collect(Collectors.toList());
		
		List<Long> volunteersIdList = dto.stream()
				.flatMap(d -> d.getRequestvolunteers()
						.stream()
						.map(RequestVolunteerDto::getVolunteerid)).distinct()
				.collect(Collectors.toList());
		
		_logger.info("volunteer value "+ volunteersIdList.size());
		
		//TODO: make client call to get volunteer data
		
		List<VolunteerResponseDto> volunteerResponseResult = 
				new ArrayList<VolunteerResponseDto>();
		
		volunteerResponseResult.add(new VolunteerResponseDto(1,"Ravi"));
			
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
	
}
