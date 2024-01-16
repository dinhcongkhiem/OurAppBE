package com.ourapplication.server.ourapplication.Controller;

import com.ourapplication.server.ourapplication.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class VerifyController {
    private final UserService userService;
    @GetMapping("verifyemail")
    public String verifyAccount(@RequestParam String activeKey, Model model){

        userService.verifyAccount(activeKey);
        model.addAttribute("");
        return "greeting";
    }
}