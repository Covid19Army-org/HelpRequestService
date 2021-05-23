package com.covid19army.HelpRequestService.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.covid19army.HelpRequestService.models.RequestVolunteer;

public interface RequestVolunteerRepository extends CrudRepository<RequestVolunteer, Long> {

	Page<RequestVolunteer> findByVolunteerid(long volunteerid, Pageable pageable);
}
