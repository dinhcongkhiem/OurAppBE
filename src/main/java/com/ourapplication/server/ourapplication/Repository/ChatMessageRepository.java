package com.ourapplication.server.ourapplication.Repository;

import com.ourapplication.server.ourapplication.Model.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,String> {

    @Query("select u from ChatMessage u where u.chatId = ?1 and u.createAt < ?2 ")
    List<ChatMessage> findByChatIdAndLessThanCreateAt(String chatId, Date createAt, Pageable pageable);

}
