package com.arits.docRepo.repo;

import com.arits.docRepo.dto.UserGroup;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserGroupRepository extends CrudRepository<UserGroup, Long> {
    List<UserGroup> findUserGroupByGroupNameEquals(String groupName);
}
