package com.ourapplication.server.ourapplication.Request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class findChatMessageRequest {
    private Date createAt;
}
