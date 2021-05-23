package com.covid19army.HelpRequestService.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.covid19army.HelpRequestService.models.NewRequestWaitingQueue;

public interface NewRequestWaitingQueueRepository extends PagingAndSortingRepository<NewRequestWaitingQueue,Long> {

	Page<NewRequestWaitingQueue> findByVolunteerid(long volunteerid, Pageable pageable);
}
