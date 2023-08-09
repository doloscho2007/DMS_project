package com.arits.docRepo.service.event;

import com.arits.docRepo.dto.EventDetails;
import com.arits.docRepo.repo.EventDetailsRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    @Autowired
    private Environment env;

    @Autowired
    private EventDetailsRepository eventDetailsRepository;
    
  //Convert Date to Calendar
  	private Calendar dateToCalendar(Date date) {

  		Calendar calendar = Calendar.getInstance();
  		calendar.setTime(date);
  		return calendar;

  	}

   
	/**
	 * getEvents
	 * 
	 * @param username
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @return List<EventDetails>
	 * @throws ParseException 
	 */
	public List<EventDetails> getEvents(String username, String fromDate, String toDate) throws ParseException {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar fromD = dateToCalendar(sdf.parse(fromDate));
		Calendar toD = dateToCalendar(sdf.parse(toDate));
		
		//return eventDetailsRepository.findEventDetailsByUserNameContains(username);
		return eventDetailsRepository.findEventDetailsByUserNameContainsAndEventDateBetween(username, fromD, toD);
		
	}

	public List<EventDetails> getAllEvents() {
		Iterable<EventDetails> iterable = eventDetailsRepository.findAll();
		
	        List<EventDetails> eventList = StreamSupport
	                           .stream(iterable.spliterator(), false)
	                           .collect(Collectors.toList());
	        
	        List<EventDetails> eventList1 = eventDetailsRepository.findAllByOrderByEventIdDesc();
		
		return eventList1;
	}
}
