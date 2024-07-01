package com.demo.campingnavi.dto;

import com.demo.campingnavi.domain.ChatRoom;
import com.demo.campingnavi.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ChatRoomVo {
    private ChatRoom chatRoom;
    private float score;
    private String scoreView;
    private Long reviewCount;
    private List<String> chatRoomUserList;
    private List<Member> chatRoomMemberList;

    public ChatRoomVo(ChatRoom chatRoom, float score, long reviewCount, List<String> chatRoomUserList, List<Member> chatRoomMemberList) {
        this.chatRoom = chatRoom;
        this.score = score;
        this.scoreView = String.format("%.1f", score);
        this.reviewCount = reviewCount;
        this.chatRoomUserList = chatRoomUserList;
        this.chatRoomMemberList = chatRoomMemberList;

    }

}
