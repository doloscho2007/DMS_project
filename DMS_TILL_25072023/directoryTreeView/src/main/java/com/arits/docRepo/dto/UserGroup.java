package com.arits.docRepo.dto;

import com.arits.docRepo.model.UserDetails;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user_groups")
public class UserGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_GROUP_UG", nullable = false, unique = true)
    private Long groupId;

    @Column(name = "GROUP_NAME_UG", nullable = false)
    private String groupName;

    @Column(name = "GROUP_DESC_UG", nullable = false)
    private String groupDescription;

    @Column(name = "GROUP_STATUS_UG", nullable = false)
    private String groupStatus;

    @Transient
    private List<LoginRegistration> userList;

    public List<LoginRegistration> getUserList() {
        return userList;
    }

    public void setUserList(List<LoginRegistration> userList) {
        this.userList = userList;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public String getGroupStatus() {
        return groupStatus;
    }

    public void setGroupStatus(String groupStatus) {
        this.groupStatus = groupStatus;
    }
}
