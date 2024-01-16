package com.ourapplication.server.ourapplication.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInforResponse {
    private Long id;
    private String name;
    private String nickname;

    private Long idPartner;

    private String nameOfPartner;


}
