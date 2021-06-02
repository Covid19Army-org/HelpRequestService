package com.covid19army.HelpRequestService.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="VolunteerService", url = "${app.client.tokenservice.url:}")
public interface VolunteerServiceClient {

}
