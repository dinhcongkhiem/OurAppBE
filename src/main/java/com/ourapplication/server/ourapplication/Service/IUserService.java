package com.ourapplication.server.ourapplication.Service;

import com.ourapplication.server.ourapplication.Model.Users;
import com.ourapplication.server.ourapplication.Response.LoginResponse;
import com.ourapplication.server.ourapplication.Response.NotifyTokenResponse;
import com.ourapplication.server.ourapplication.Response.UserInforResponse;

public interface IUserService {
    void registerUser(Users request);

    String generateActiveKey();

    void verifyAccount(String activeKey);

    LoginResponse login(Users request);


    UserInforResponse getUser();

    void updateUser(Users users);

    NotifyTokenResponse getNotifyToken();

    void updateNotifyToken(String token);
}
