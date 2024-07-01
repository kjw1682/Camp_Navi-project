package com.demo.campingnavi.controller;

import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.dto.ChatMessage;
import com.demo.campingnavi.repository.jpa.MemberRepository;
import com.demo.campingnavi.repository.mongo.MongoChatMessageRepository;
import com.demo.campingnavi.service.ChatRoomService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class ChatController {
    private final SimpMessageSendingOperations messagingTemplate;
    private final MongoChatMessageRepository mongoChatMessageRepository;
    private final ChatRoomService chatRoomService;
    private final MemberRepository memberRepository;

    @Transactional
    @MessageMapping("/chat/message")
    public void message(ChatMessage message) {
        // 메시지 타입이 ENTER인 경우 알림 메시지를 설정하는 부분
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
            List<String> userList = chatRoomService.getUserList(message.getRoomId());
            System.out.println(message.getChatRoomUserList());
            String newMseq = String.valueOf(message.getMseq());
            boolean isAlreadyJoined = userList.contains(newMseq);
            if (!isAlreadyJoined) {
                chatRoomService.addUser(message.getRoomId(), message.getMseq());
            }
            // 새로 입장한 유저에게 기존 메시지를 전송
            List<ChatMessage> messagesInRoom = mongoChatMessageRepository.findByRoomId(message.getRoomId());
            messagesInRoom.forEach(msg -> messagingTemplate.convertAndSend("/user/" + message.getMseq() + "/queue/messages", msg));

            // 입장 메시지를 설정하고 저장
            message.setCreatedAt(LocalDateTime.now());
            mongoChatMessageRepository.save(message);
            // 채팅방에 입장 메시지를 전송
            messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);

        } else if(ChatMessage.MessageType.TALK.equals(message.getType())) {
            // 다른 타입의 메시지 처리
            message.setCreatedAt(LocalDateTime.now());
            mongoChatMessageRepository.save(message);
            messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
        } else if(ChatMessage.MessageType.LEAVE.equals(message.getType())) {
            chatRoomService.delUser(message.getRoomId(), message.getMseq());
            List<String> userList = chatRoomService.getUserList(message.getRoomId());
            // 사용자 리스트가 비어있으면 방 삭제
            if (userList.isEmpty()) {
                chatRoomService.deleteRoom(message.getRoomId());
                mongoChatMessageRepository.deleteByRoomId(message.getRoomId());
            }

        }
    }

}
