package com.covid19army.HelpRequestService.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.covid19army.HelpRequestService.dtos.VolunteerResponseDto;


@FeignClient(name="VolunteerService", url = "${app.client.volunteerservice.url:}")
public interface VolunteerServiceClient {

	@PostMapping("/volunteer/searchbyids")
	public List<VolunteerResponseDto> searchByVolunteerId(@RequestBody List<Long> volunteerIds );
}
