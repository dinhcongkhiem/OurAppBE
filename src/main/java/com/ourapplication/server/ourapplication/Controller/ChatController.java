package com.ourapplication.server.ourapplication.Controller;


import com.ourapplication.server.ourapplication.Model.ChatMessage;
import com.ourapplication.server.ourapplication.Model.Users;
import com.ourapplication.server.ourapplication.Repository.UsersRepository;
import com.ourapplication.server.ourapplication.Request.findChatMessageRequest;
import com.ourapplication.server.ourapplication.Response.ChatNotification;
import com.ourapplication.server.ourapplication.Service.ChatMessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
@AllArgsConstructor
public class ChatController {
    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    private final UsersRepository usersRepository;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
//        headerAccessor.getSessionAttributes().put("username", chatMessage.getSenderId());
        return chatMessage;
    }

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) throws Exception {
        ChatMessage savedMsg = chatMessageService.save(chatMessage);
        Users user = usersRepository.findById(chatMessage.getRecipientId()).get();
        if (!user.isOnline()) {
            chatMessageService.SendNotification(chatMessage);
        }
        messagingTemplate.convertAndSendToUser(
                String.valueOf(chatMessage.getRecipientId()), "/queue/messages",
                new ChatNotification(
                        savedMsg.getId(),
                        savedMsg.getSenderId(),
                        savedMsg.getRecipientId(),
                        savedMsg.getContent()
                )
        );
    }

    @PutMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable Long senderId,
                                                              @PathVariable Long recipientId,
                                                              @RequestBody findChatMessageRequest timestamp) {
        return ResponseEntity
                .ok(chatMessageService.findChatMessages(senderId, recipientId, timestamp));
    }


}
