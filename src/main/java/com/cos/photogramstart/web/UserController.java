package com.cos.photogramstart.web;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/user/{id}")
    public String profile(@PathVariable int id) {
        return "user/profile";
    }

    @GetMapping("/user/{id}/update")
    public String update(@PathVariable int id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        logger.info(principalDetails.getUser().toString());

        return "user/update";
    }
}
