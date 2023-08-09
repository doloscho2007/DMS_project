package com.arits.docRepo.dto;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "checkout")
@Data
public class CheckOutFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHECKOUT_ID", nullable = false, unique = true)
    private Long checkOutId;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "FILE_ID", referencedColumnName = "FILE_ID_FD")
    private FileDetails fileDetails;

    @Column(name = "USERNAME", nullable = false)
    private String userName;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CHECKOUT_DATE", updatable = false)
    private Calendar checkoutDate;

	public Long getCheckOutId() {
		return checkOutId;
	}

	public void setCheckOutId(Long checkOutId) {
		this.checkOutId = checkOutId;
	}

	public FileDetails getFileDetails() {
		return fileDetails;
	}

	public void setFileDetails(FileDetails fileDetails) {
		this.fileDetails = fileDetails;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Calendar getCheckoutDate() {
		return checkoutDate;
	}

	public void setCheckoutDate(Calendar checkoutDate) {
		this.checkoutDate = checkoutDate;
	}
    
    
}
