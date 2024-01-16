package com.ourapplication.server.ourapplication.Controller;

import com.ourapplication.server.ourapplication.Exception.NullValueException;
import com.ourapplication.server.ourapplication.Exception.UserAlreadyExistsException;
import com.ourapplication.server.ourapplication.Model.Users;
import com.ourapplication.server.ourapplication.Response.LoginResponse;
import com.ourapplication.server.ourapplication.Response.NotifyTokenResponse;
import com.ourapplication.server.ourapplication.Response.UserInforResponse;
import com.ourapplication.server.ourapplication.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/")
public class UserController {
    private final UserService userService;

    @GetMapping("/infor")
    public ResponseEntity<UserInforResponse> getUserInfor() {
        return ResponseEntity.ok().body(userService.getUser());
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody Users request) {
        System.out.println(request.getIdOfPartner());
        userService.updateUser(request);
        return ResponseEntity.ok("Done");
    }

    @GetMapping("/notifytoken")
    public ResponseEntity<NotifyTokenResponse> getNotifyToken() {
        return ResponseEntity.ok().body(userService.getNotifyToken());
    }

    @PostMapping("/update-token")
    public ResponseEntity<String> updateNotifyToken(@RequestParam String token) {
        userService.updateNotifyToken(token);
        return ResponseEntity.ok().body("Successfully");
    }

    @PostMapping("/updatestate")
    public ResponseEntity<String> updateState(@RequestParam  boolean State){
        userService.updateState(State);
        return ResponseEntity.ok().body("Successfully");
    }
}