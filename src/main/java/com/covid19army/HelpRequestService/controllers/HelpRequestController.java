package com.covid19army.HelpRequestService.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.covid19army.HelpRequestService.dtos.HelpRequestDto;
import com.covid19army.HelpRequestService.dtos.HelpRequestResponseDto;
import com.covid19army.HelpRequestService.dtos.PagedResponseDto;
import com.covid19army.HelpRequestService.dtos.RequestVolunteerDto;
import com.covid19army.HelpRequestService.dtos.UpdateStatusDto;
import com.covid19army.HelpRequestService.models.HelpRequest;
import com.covid19army.HelpRequestService.services.HelpRequestService;
import com.covid19army.core.common.clients.OtpServiceClient;
import com.covid19army.core.dtos.OtpVerificationRequestDto;
import com.covid19army.core.exceptions.NotAuthorizedException;
import com.covid19army.core.exceptions.ResourceNotFoundException;
import com.covid19army.core.extensions.HttpServletRequestExtension;

@RestController
@RequestMapping("helprequest")
public class HelpRequestController {

	@Autowired
	HttpServletRequestExtension _requestExtension;
	
	@Autowired
	HelpRequestService _helpRequestService;
	
	@Autowired
	OtpServiceClient _otpServiceClient;
	
	@GetMapping("/health")
	public String health() {
		return "am running!";
	}
	
	@PostMapping
	public long createHelpRequest(@RequestBody HelpRequestDto helpRequestDto) {
		HelpRequest helpRequest = _helpRequestService.createHelpRequest(helpRequestDto);
		return helpRequest.getRequestid();
	}
	
	@PutMapping("/{helpRequestId}")
	public void updateHelpRequest(@RequestBody HelpRequestDto helpRequestDto, @PathVariable long helpRequestId) 
			throws ResourceNotFoundException {
		 _helpRequestService.updateHelpRequest(helpRequestId, helpRequestDto);
		//return helpRequest.getRequestid();
	}
	
	@GetMapping("/owner/{requestid}")
	public long getRequestOwner(@PathVariable long requestid) throws ResourceNotFoundException, NotAuthorizedException {
		return _helpRequestService.getHelpRequestOwner(requestid);
	}
	
	@GetMapping("/myRequests")
	public PagedResponseDto<HelpRequestResponseDto> getMyRequests(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size){
		Pageable pageable = PageRequest.of(page, size,Sort.by(Sort.Direction.DESC, "dateCreated"));
		return _helpRequestService.getHelpRequestsByUser(pageable);
	}
	
	@GetMapping("/volunteer/newRequests/{volunteerId}")
	public List<HelpRequestResponseDto> getNewVolunteerRequests(@PathVariable long volunteerId, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size){
		Pageable pageable = PageRequest.of(page, size,Sort.by(Sort.Direction.DESC, "dateCreated"));
		return _helpRequestService.getNewRequestsForVolunteer(volunteerId, pageable);
	}
	
	@GetMapping("/volunteer/acceptedRequests/{volunteerId}")
	public PagedResponseDto<HelpRequestResponseDto> getVolunteerAcceptedRequests(@PathVariable long volunteerId, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size){
		Pageable pageable = PageRequest.of(page, size,Sort.by(Sort.Direction.DESC, "dateCreated"));
		return _helpRequestService.getVolunteerAcceptedRequests(volunteerId, pageable);
	}
	
	@PostMapping("/validateotp")
	public boolean validateOtp(@RequestBody OtpVerificationRequestDto otpRequestDto) 
			throws ResourceNotFoundException{
		
		var result = _otpServiceClient.validateOtp(otpRequestDto);
		if(result) {
			_helpRequestService.updateMobileVerified(otpRequestDto.getEntityid(), true);
		}
		
		throw new ResourceNotFoundException("Invaid Otp.");
		
	}
	
	@PutMapping("/{requestid}/updatestatus")
	public ResponseEntity<String> updateRequestStatus(@PathVariable long requestid,  @RequestBody UpdateStatusDto updateStatusDto) 
			throws ResourceNotFoundException, NotAuthorizedException{
		_helpRequestService.updateRequestStatus(requestid, updateStatusDto.getStatus());
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@GetMapping("/rerequest/{requestid}")
	public ResponseEntity<String> updateRequestStatus(@PathVariable long requestid) 
			throws ResourceNotFoundException, NotAuthorizedException{
		_helpRequestService.reRequest(requestid);
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@GetMapping("/isvalid/{requestid}")
	public boolean isValidHelpRequest(@PathVariable long requestid) {
		var helpRequestOpt = _helpRequestService.getRequest(requestid);
		return helpRequestOpt.isPresent();
	}
}
