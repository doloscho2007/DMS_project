package com.arits.docRepo.service.user;

import com.arits.docRepo.dto.LoginRegistration;
import com.arits.docRepo.model.UserDetails;
import com.arits.docRepo.repo.LoginRegistrationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsService {

    private LoginRegistrationRepository loginRegistrationRepository;

    public UserDetailsService(LoginRegistrationRepository loginRegistrationRepository) {
        this.loginRegistrationRepository = loginRegistrationRepository;
    }

    public List<UserDetails> extractUsers() {
        Iterable<LoginRegistration> loginRegistrationList = loginRegistrationRepository.findAll();

        List<UserDetails> userDetailsList = new ArrayList<UserDetails>();
        loginRegistrationList.forEach(loginRegistration -> {
            UserDetails userDetails = new UserDetails();
            userDetails.setLoginId(loginRegistration.getLoginId());
            userDetails.setPassCode(loginRegistration.getPassCode());
            userDetails.setSystemIp(loginRegistration.getSystemIp());
            userDetails.setUserGroup(loginRegistration.getUserGroup());
            userDetails.setUserName(loginRegistration.getUserName());
            userDetails.setEmpcurrentStatus(loginRegistration.getEmpcurrentStatus());
            userDetailsList.add(userDetails);
        });

        return userDetailsList;
    }

    public String deleteUser(String userId) {

        List<LoginRegistration> loginRegistrationList = loginRegistrationRepository
                .findByLoginId(Long.parseLong(userId));
        if (loginRegistrationList.size() > 0) {
            LoginRegistration loginRegistration = loginRegistrationList.get(0);
            loginRegistration.setEmpcurrentStatus("InActive");
            loginRegistrationRepository.save(loginRegistration);
            System.out.println("Status has been updated for the user id :" + userId);
            return "User status has been set to InActive";
        }
        return "An Error occurred";
    }

    /**
     * validateLogin
     *
     * @param pinCode
     * @return void
     */
    public UserDetails validateLogin(String pinCode) {
        List<LoginRegistration> loginRegistrationList = loginRegistrationRepository.findByPassCodeEquals(pinCode);
        UserDetails userDetails = null;
        if (loginRegistrationList.size() > 0) {
            LoginRegistration loginRegistration = loginRegistrationList.get(0);
            if (!loginRegistration.getEmpcurrentStatus().equals("InActive")) {
                userDetails = new UserDetails();
                userDetails.setLoginId(loginRegistration.getLoginId());
                userDetails.setPassCode(loginRegistration.getPassCode());
                userDetails.setSystemIp(loginRegistration.getSystemIp());
                userDetails.setUserGroup(loginRegistration.getUserGroup());
                userDetails.setUserName(loginRegistration.getUserName());
                userDetails.setEmpcurrentStatus(loginRegistration.getEmpcurrentStatus());
                System.out.println("Username : " + loginRegistration.getUserName());
                return userDetails;
            }
        }
        return userDetails;
    }

    public String addUserDetails(String userName, String status, String userGroup, String pin) {
        LoginRegistration loginRegistration = new LoginRegistration();
        loginRegistration.setEmpcurrentStatus(status);
        loginRegistration.setUserName(userName);
        loginRegistration.setUserGroup(userGroup);
        loginRegistration.setPassCode(pin);
        loginRegistration.setConfPassCode(pin);
        loginRegistrationRepository.save(loginRegistration);

        return "Added Successfully";
    }

    public String updateUserDetails(String loginId, String userName, String status, String userGroup, String pin) {

        List<LoginRegistration> loginRegistrationList = loginRegistrationRepository
                .findByLoginId(Long.parseLong(loginId));

        if (loginRegistrationList.size() > 0) {
            LoginRegistration loginRegistration = loginRegistrationList.get(0);
            loginRegistration.setEmpcurrentStatus(status);
            loginRegistration.setUserName(userName);
            loginRegistration.setUserGroup(userGroup);
            loginRegistration.setPassCode(pin);
            loginRegistration.setConfPassCode(pin);
            loginRegistrationRepository.save(loginRegistration);
        }
        // TODO Auto-generated method stub
        return "Updated Successfully";
    }
}
