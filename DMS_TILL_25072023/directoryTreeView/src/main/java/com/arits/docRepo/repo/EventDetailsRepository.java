package com.arits.docRepo.repo;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import com.arits.docRepo.dto.EventDetails;
import java.util.List;
import java.util.Calendar;

public interface EventDetailsRepository extends CrudRepository<EventDetails, Long> {
	
	List<EventDetails> findAllByOrderByEventIdDesc();
	
	List<EventDetails> findEventDetailsByUserNameContainsAndEventDateBetween(String userName,Calendar fromDate, Calendar toDate);

	List<EventDetails> findEventDetailsByUserNameContains(String username);

	

	
    
}
