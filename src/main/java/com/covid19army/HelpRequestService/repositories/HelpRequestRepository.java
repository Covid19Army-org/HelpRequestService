package com.covid19army.HelpRequestService.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.covid19army.HelpRequestService.models.HelpRequest;

public interface HelpRequestRepository extends PagingAndSortingRepository<HelpRequest,Long>{

	Page<HelpRequest> findByUserid(long userid, Pageable pageable);
	List<HelpRequest> findByRequestidIn(List<Long> requestIds);
	
	@Query("Select Hr from HelpRequest Hr JOIN Hr.requestvolunteers rv where rv.volunteerid = :volunteerid and rv.isactive is true")
	Page<HelpRequest> findVolunteerAcceptedRequest(@Param("volunteerid") long volunteerid, Pageable pageable);
}
