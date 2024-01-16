package com.ourapplication.server.ourapplication.Repository;

import com.ourapplication.server.ourapplication.Model.ChatRoom;
import com.ourapplication.server.ourapplication.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,String> {
    Optional<ChatRoom> findBySenderIdAndRecipientId(Long senderId, Long recipientId);

}
