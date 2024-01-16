package com.ourapplication.server.ourapplication.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotifyTokenResponse {
    private String pushNotifyToken;
}
