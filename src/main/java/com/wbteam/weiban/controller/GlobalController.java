package com.wbteam.weiban.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@RestController
public class GlobalController {

    @ModelAttribute
    public void getModel(Model model, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        Object user = request.getAttribute(role);
        model.addAttribute("role",role);
        model.addAttribute(role,user);
    }

}
