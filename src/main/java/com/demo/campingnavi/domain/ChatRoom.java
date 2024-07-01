package com.demo.campingnavi.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ChatRoom {

    @ManyToOne
    @JoinColumn(name="cseq")
    private Camp camp;
    @Id
    private String roomId;
    private String name;
    private String startDate;
    private String endDate;
    private Integer maxMem;
    private String campName;
    private Long reviewCount;
    @Column(columnDefinition = "INT default 0")
    private Long userCount;
    private String owner;
    @ElementCollection
    private List<String> userList = new ArrayList<>();
    @ElementCollection
    private List<String> purpose;
    @ElementCollection
    private List<String> banList = new ArrayList<>();

    public void upUserCount(){
        this.userCount++;
        System.out.println("userCount 실행 완료");
    }
    public void downUserCount(){
        this.userCount--;
    }
    public void addUser(String userMseq){
        this.userList.add(userMseq);
        System.out.println("addUser 실행완료");
    }
    public void removeUser(String username){
        this.userList.remove(username);
    }
    public void addBanUser(String userMseq){
        this.banList.add(userMseq);
    }
    public static com.demo.campingnavi.domain.ChatRoom create(String name) {
        com.demo.campingnavi.domain.ChatRoom chatRoom = new com.demo.campingnavi.domain.ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.name = name;
        return chatRoom;
    }

}