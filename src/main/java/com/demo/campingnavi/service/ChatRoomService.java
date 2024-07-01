package com.demo.campingnavi.service;

import com.demo.campingnavi.domain.Camp;
import com.demo.campingnavi.domain.ChatRoom;
import com.demo.campingnavi.domain.Member;

import java.util.List;

public interface ChatRoomService {

    public List<ChatRoom> findAllRoom();
    public ChatRoom findRoomById(String roomId);
    public ChatRoom createChatRoom(ChatRoom chatRoom);

    // 이름에 특정 문자열이 포함된 채팅방 찾기
    public List<ChatRoom> findByCampNameContaining(String campName);

    // 현재 채팅방이 개설된 캠핑장 리스트 찾기
    public List<Camp> getCampListExistingChatRoom();


    public void addUser(String roomId, int mseq);
    public void delUser(String roomId, int mseq);
    public String getUserName(String roomId);
    public List<String> getUserList(String roomId);
    public List<ChatRoom> findMyChatRooms(String memberMseq);
    public void addBanUser(String roomId, int mseq);
    public List<String> getBanUserList(String roomId);
    public void deleteRoom(String roomId);
    public List<ChatRoom> findByCampNameContainingAndPurposeIn(String campName, String[] purposes);
}
