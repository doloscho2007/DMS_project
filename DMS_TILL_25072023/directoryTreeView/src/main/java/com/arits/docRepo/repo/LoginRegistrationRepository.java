package com.arits.docRepo.repo;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.arits.docRepo.dto.LoginRegistration;

public interface LoginRegistrationRepository extends CrudRepository<LoginRegistration, Integer>{	
	
	List<LoginRegistration> findByPassCodeEquals(String passCode);
	
	List<LoginRegistration> findByLoginId(Long loginId);

	List<LoginRegistration> findLoginRegistrationByUserGroupEqualsIgnoreCaseOrderByUserNameAsc(String userGroup);
}
