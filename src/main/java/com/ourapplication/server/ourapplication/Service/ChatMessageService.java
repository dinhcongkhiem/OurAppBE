package com.ourapplication.server.ourapplication.Service;


import com.ourapplication.server.ourapplication.Model.ChatMessage;
import com.ourapplication.server.ourapplication.Model.ChatRoom;
import com.ourapplication.server.ourapplication.Model.Users;
import com.ourapplication.server.ourapplication.Repository.ChatMessageRepository;
import com.ourapplication.server.ourapplication.Repository.UsersRepository;
import com.ourapplication.server.ourapplication.Request.SendNotificationRequest;
import com.ourapplication.server.ourapplication.Request.findChatMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;
    private final UsersRepository usersRepository;
    @Autowired
    private RestTemplate restTemplate;

    public ChatMessage save(ChatMessage chatMessage) {
        var chatId = chatRoomService
                .getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true)
                .orElseThrow();
        chatMessage.setChatId(chatId);
        System.out.println(chatMessage);
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    public List<ChatMessage> findChatMessages(Long senderId, Long recipientId, findChatMessageRequest timestamp) {
        var chatId = chatRoomService.getChatRoomId(senderId, recipientId, false);
        if(chatId.isEmpty()){
            return null;
        }
        List<ChatMessage> listChatmesage =  chatMessageRepository.findByChatIdAndLessThanCreateAt(chatId.get(), timestamp.getCreateAt(),
                PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "createAt")));
        Collections.reverse(listChatmesage);
        return listChatmesage;
//        return chatId.map(chatMessageRepository::findByChatId).orElse(new ArrayList<>());
    }

    public void SendNotification(ChatMessage request) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Users user = usersRepository.findById(request.getRecipientId()).get();



        SendNotificationRequest sendNotificationRequest = new SendNotificationRequest();
        sendNotificationRequest.setTo(user.getPushNotifyToken());
        sendNotificationRequest.setSound("default");
        sendNotificationRequest.setTitle("Message");
        sendNotificationRequest.setBody(request.getContent());

        HttpEntity<SendNotificationRequest> entity = new HttpEntity<>(sendNotificationRequest, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "https://exp.host/--/api/v2/push/send",
                HttpMethod.POST,
                entity,
                String.class
        );

    }
}
