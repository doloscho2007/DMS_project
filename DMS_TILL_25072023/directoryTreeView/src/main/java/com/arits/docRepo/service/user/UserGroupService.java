package com.arits.docRepo.service.user;

import com.arits.docRepo.dto.LoginRegistration;
import com.arits.docRepo.dto.UserGroup;
import com.arits.docRepo.model.UserDetails;
import com.arits.docRepo.repo.LoginRegistrationRepository;
import com.arits.docRepo.repo.UserGroupRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserGroupService {
    private UserGroupRepository userGroupRepository;
    private LoginRegistrationRepository loginRegistrationRepository;

    public UserGroupService(UserGroupRepository userGroupRepository, LoginRegistrationRepository loginRegistrationRepository) {
        this.userGroupRepository = userGroupRepository;
        this.loginRegistrationRepository = loginRegistrationRepository;
    }

    public boolean createUserGroup(String userGroupName, String userGroupStatus, String userGroupDescription) {
        List<UserGroup> userGroups = userGroupRepository.findUserGroupByGroupNameEquals(userGroupName);
        if (userGroups.size() > 0) {
            return false;
        }
        UserGroup ug = new UserGroup();
        ug.setGroupName(userGroupName);
        ug.setGroupStatus(userGroupStatus);
        ug.setGroupDescription(userGroupDescription);
        if (userGroupRepository.save(ug) != null) {
            return true;
        }
        return false;
    }

    public List<UserGroup> getAllUserGroup() {
        Iterable<UserGroup> userGroups = userGroupRepository.findAll();
        List<UserGroup> userGroupList = new ArrayList<>();
        userGroups.forEach(userGroup -> {
            List<LoginRegistration> users = loginRegistrationRepository
                    .findLoginRegistrationByUserGroupEqualsIgnoreCaseOrderByUserNameAsc(userGroup.getGroupName());
            userGroup.setUserList(users);
            userGroupList.add(userGroup);
        });
        return userGroupList;
    }

    public String deleteUserGroup(String userGroupId) {
        Optional<UserGroup> userGroupOptional = userGroupRepository.findById(Long.parseLong(userGroupId));
        if (userGroupOptional.isPresent()) {
            UserGroup userGroup = userGroupOptional.get();
            userGroup.setGroupStatus("InActive");
            userGroupRepository.save(userGroup);
            return "User Group Status has been set to InActive";
        }
        return "An error occurred";
    }

    public String updateUserGroup(String userGroupId, String userGroupName, String userGroupStatus, String userGroupDescription) {
        Optional<UserGroup> userGroupOptional = userGroupRepository.findById(Long.parseLong(userGroupId));
        if (userGroupOptional.isPresent()) {
            UserGroup userGroup = userGroupOptional.get();
            userGroup.setGroupName(userGroupName);
            userGroup.setGroupStatus(userGroupStatus);
            userGroup.setGroupDescription(userGroupDescription);
            userGroupRepository.save(userGroup);
            if (userGroup.getGroupStatus().equalsIgnoreCase("InActive")) {
                List<LoginRegistration> users = loginRegistrationRepository
                        .findLoginRegistrationByUserGroupEqualsIgnoreCaseOrderByUserNameAsc(userGroup.getGroupName());
                users.forEach(user ->{
                    user.setEmpcurrentStatus("InActive");
                    loginRegistrationRepository.save(user);
                });
            }
            return "User Group Status has been updated";
        }
        return "An error occurred";
    }
}
