package com.demo.campingnavi;

import com.demo.campingnavi.domain.Camp;
import com.demo.campingnavi.repository.jpa.ChatRoomRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ChatRoomRepositoryTest {
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Disabled
    @Test
    public void getCampList() {
        List<Camp> campList = chatRoomRepository.getCampListExistingChatRoom();
        for (Camp camp : campList) {
            System.out.println(camp.getName());
        }
    }
}
