package com.covid19army.HelpRequestService.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.covid19army.HelpRequestService.models.HelpRequest;

public interface HelpRequestRepository extends PagingAndSortingRepository<HelpRequest,Long>{

	Page<HelpRequest> findByUserid(long userid, Pageable pageable);
}
