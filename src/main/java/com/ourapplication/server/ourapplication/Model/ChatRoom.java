package com.ourapplication.server.ourapplication.Model;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ChatRoom {
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id

    private String id;
    private String chatId;
    private Long senderId;
    private Long recipientId;
}
