package com.arits.docRepo.util;

import com.arits.docRepo.dto.EventDetails;
import com.arits.docRepo.repo.EventDetailsRepository;

import org.springframework.stereotype.Service;

@Service
public class EventDetailsUtil {

	private EventDetailsRepository eventDetailsRepository;
	
	public EventDetailsUtil(EventDetailsRepository eventDetailsRepository) {
		this.eventDetailsRepository = eventDetailsRepository;
	}
	
    public void loadEvents(String userName, String message) {
    	String loggedinName = "";
    	EventDetails eventDetails = new EventDetails();        
        if (userName == null || userName == "" ) {
        	loggedinName = "Guest";
        }else {
        	loggedinName = userName;
        }
        eventDetails.setEventDesc(message);
        eventDetails.setUserName(userName);    
        eventDetailsRepository.save(eventDetails);

    }
}
