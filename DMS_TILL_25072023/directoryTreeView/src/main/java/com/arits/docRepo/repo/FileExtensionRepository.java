package com.arits.docRepo.repo;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

import com.arits.docRepo.dto.FileExtestion;



public interface FileExtensionRepository extends CrudRepository<FileExtestion, Integer>{
	List<FileExtestion> findByFileExtEquals(String fileExt);
}
