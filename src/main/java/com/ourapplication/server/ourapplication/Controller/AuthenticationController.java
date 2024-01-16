package com.ourapplication.server.ourapplication.Controller;

import com.ourapplication.server.ourapplication.Exception.NullValueException;
import com.ourapplication.server.ourapplication.Exception.UserAlreadyExistsException;
import com.ourapplication.server.ourapplication.Model.Users;
import com.ourapplication.server.ourapplication.Response.LoginResponse;
import com.ourapplication.server.ourapplication.Response.UserInforResponse;
import com.ourapplication.server.ourapplication.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/")
public class AuthenticationController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Users request) {
        try {
            userService.registerUser(request);
            return ResponseEntity.ok("Please check your email to active account");
        }catch(NullValueException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Value is not valid to register, please try again!");

        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with email " + request.getEmail() + " already exists");
        }

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody Users request){
        return ResponseEntity.ok().body(userService.login(request));
    }




}
