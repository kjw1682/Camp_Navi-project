package com.demo.campingnavi.dto;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "chat")
public class ChatMessage {

    //메세지 타입 : 입장, 채팅, 퇴장, 챗봇
    public enum MessageType {
        ENTER, TALK, LEAVE, CHATBOT

    }

    private MessageType type; // 메세지 타입
    @Id
    private String roomId; // 방번호
    private String sender; // 메세지 발신인
    private String message; // 메세지
    private int mseq;
    private List<String> chatRoomUserList;
    private LocalDateTime createdAt; // 발송시간

}
