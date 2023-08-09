package com.arits.docRepo.controller.events;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arits.docRepo.dto.EventDetails;
import com.arits.docRepo.service.event.EventService;

import java.text.ParseException;
import java.util.List;

@RestController
public class EventDetailsController {
    private EventService eventService;

    public EventDetailsController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/searchAllEvents")
    public List<EventDetails> searchAllEvents(@RequestParam("username") String username,@RequestParam("fromDate") String fromDate,@RequestParam("toDate") String toDate,@RequestHeader(value="Authorization", required=false) String bearerToken ) {
        try {
			return eventService.getEvents(username,fromDate,toDate);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
    }
    
    @GetMapping("/getAllEvents")
    public List<EventDetails> getAllEvents(@RequestHeader(value="Authorization", required=false ) String bearerToken) {
        return eventService.getAllEvents();
    }
}
