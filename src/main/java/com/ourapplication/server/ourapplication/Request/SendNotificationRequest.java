package com.ourapplication.server.ourapplication.Request;

import lombok.Data;

@Data
public class SendNotificationRequest {
    private String to;
    private String sound;
    private String title;
    private String body;

}
