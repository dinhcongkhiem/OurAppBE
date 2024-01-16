package com.ourapplication.server.ourapplication.Service;

import com.ourapplication.server.ourapplication.Model.ChatMessage;
import com.ourapplication.server.ourapplication.Model.ChatRoom;
import com.ourapplication.server.ourapplication.Model.Users;
import com.ourapplication.server.ourapplication.Repository.ChatRoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    public Optional<String> getChatRoomId(
            Long sender,Long recipient,boolean createNewRoomIfNotExists
    ){
        return chatRoomRepository.findBySenderIdAndRecipientId(sender,recipient)
                .map(ChatRoom::getChatId)
                .or(() -> {
                    if(createNewRoomIfNotExists){
                        var chatID = createChatId(sender,recipient);
                        return Optional.of(chatID);
                    }
                    return Optional.empty();
                });
    }
    private String createChatId(Long senderId, Long recipientId) {
        var chatId = String.format("%s_%s", senderId, recipientId);

        ChatRoom senderRecipient = ChatRoom
                .builder()
                .chatId(chatId)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();

        ChatRoom recipientSender = ChatRoom
                .builder()
                .chatId(chatId)
                .senderId(recipientId)
                .recipientId(senderId)
                .build();

        chatRoomRepository.save(senderRecipient);
        chatRoomRepository.save(recipientSender);

        return chatId;
    }


}
