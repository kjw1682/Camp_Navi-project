package com.demo.campingnavi.service;

import com.demo.campingnavi.domain.Camp;
import com.demo.campingnavi.domain.ChatRoom;
import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.repository.jpa.ChatRoomRepository;
import com.demo.campingnavi.repository.jpa.MemberRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
@Transactional
@Service
public class ChatRoomServiceImpl implements ChatRoomService{

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    private Map<String, ChatRoom> chatRoomMap;
    @Autowired
    private MemberRepository memberRepository;

    @PostConstruct
    private void init() {
        chatRoomMap = new LinkedHashMap<>();
    }

    @Override
    public List<ChatRoom> findAllRoom() {
        List chatRooms = chatRoomRepository.findAll();
        Collections.reverse(chatRooms);
        return chatRooms;
    }

    @Override
    public ChatRoom findRoomById(String roomId) {

        return chatRoomRepository.findById(roomId).orElse(null);
    }

    @Override
    public ChatRoom createChatRoom(ChatRoom chatRoom) {
        chatRoomRepository.save(chatRoom);
        return chatRoom;
    }

    // 이름에 특정 문자열이 포함된 채팅방 찾기
    @Override
    public List<ChatRoom> findByCampNameContaining(String campName) {
        return chatRoomRepository.findByCampNameContaining(campName);
    }

    @Override
    public List<Camp> getCampListExistingChatRoom() {
        return chatRoomRepository.getCampListExistingChatRoom();
    }


    @Override
    public void addUser(String roomId, int mseq) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).get();
        Member user = memberRepository.findById(mseq).get();

        chatRoom.upUserCount();
        chatRoom.addUser(String.valueOf(user.getMseq()));
        chatRoomRepository.save(chatRoom);

    }
    @Override
    public void delUser(String roomId, int mseq) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).get();
        Member user = memberRepository.findById(mseq).get();

        chatRoom.downUserCount();
        chatRoom.removeUser(String.valueOf(user.getMseq()));
        chatRoomRepository.save(chatRoom);
    }

    @Override
    public String getUserName(String roomId) {
        ChatRoom room = chatRoomRepository.findById(roomId).get();

        return room.getUserList().toString();
    }

    @Override
    public List<String> getUserList(String roomId) {

        ChatRoom chatRoom = chatRoomRepository.findById(roomId).get();
        List<String> users = chatRoom.getUserList();

        return users;
    }
    @Override
    public List<ChatRoom> findMyChatRooms(String memberMseq) {
        // 사용자의 mseq를 기준으로 참여 중인 채팅방 리스트 조회
        return chatRoomRepository.findByUserListContains(memberMseq);
    }

    @Override
    public void addBanUser(String roomId, int mseq) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).get();
        Member user = memberRepository.findById(mseq).get();
        chatRoom.addBanUser(String.valueOf(user.getMseq()));
        chatRoomRepository.save(chatRoom);
    }

    @Override
    public List<String> getBanUserList(String roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).get();
        List<String> banUsers = chatRoom.getBanList();

        return banUsers;
    }

    @Override
    public void deleteRoom(String roomId) {
        chatRoomRepository.deleteById(roomId);
    }

    @Override
    public List<ChatRoom> findByCampNameContainingAndPurposeIn(String campName, String[] purposes) {
        return chatRoomRepository.findByCampNameContainingAndPurposeIn(campName, purposes);
    }


}
