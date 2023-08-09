package com.arits.docRepo.dto;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "EVENT_DETAILS")
@Data
public class EventDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EVENT_ID", nullable = false, unique = true)
    private Long eventId;

   
    @Column(name = "EVENT_DESC", nullable = false)
    private String eventDesc;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EVENT_DATE", updatable = false)
    private Calendar eventDate;
    
    @Column(name = "USER_NAME", nullable = false)
    private String userName;

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public String getEventDesc() {
		return eventDesc;
	}

	public void setEventDesc(String eventDesc) {
		this.eventDesc = eventDesc;
	}

	public Calendar getEventDate() {
		return eventDate;
	}

	public void setEventDate(Calendar eventDate) {
		this.eventDate = eventDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	
    
}
