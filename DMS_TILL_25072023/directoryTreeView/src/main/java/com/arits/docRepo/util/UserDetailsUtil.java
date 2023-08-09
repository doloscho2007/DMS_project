package com.arits.docRepo.util;

import com.arits.docRepo.model.UserDetails;

import javax.servlet.http.HttpServletRequest;

public class UserDetailsUtil {

    public static void validateSession(HttpServletRequest request) throws Exception {
        UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
        if (userDetails == null || userDetails.getUserName() == null) {
            throw new Exception("Session Expired");
        }

    }
}
