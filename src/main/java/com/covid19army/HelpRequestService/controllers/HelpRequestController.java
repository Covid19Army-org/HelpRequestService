package com.covid19army.HelpRequestService.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.covid19army.core.extensions.HttpServletRequestExtension;

@RestController
@RequestMapping("helprequest")
public class HelpRequestController {

	@Autowired
	HttpServletRequestExtension _requestExtension;
	
	@GetMapping("/health")
	public String health() {
		return String.format("am running %s", _requestExtension.getAuthenticatedUser());
	}
}
