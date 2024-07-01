package com.demo.campingnavi.repository.mongo;

import com.demo.campingnavi.dto.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface MongoChatMessageRepository extends MongoRepository<ChatMessage, String> {

    List<ChatMessage> findByRoomId(String roomId);
    void deleteByRoomId(String roomId);
}
