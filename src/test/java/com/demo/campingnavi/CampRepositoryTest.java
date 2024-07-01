package com.demo.campingnavi;

import com.demo.campingnavi.domain.Camp;
import com.demo.campingnavi.repository.jpa.CampRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;


@SpringBootTest
public class CampRepositoryTest {
    @Autowired
    private CampRepository campRepository;

    @Disabled
    @Test
    public void getCampList() {
        String useyn = "y";
        String name = "";
        String locationB = "";
        String locationS = "";
        String campType1 = "일반야영장";
        String campType2 = "자동차야영장";
        String campType3 = "글램핑";
        String campType4 = "카라반";
        String sortBy = "name";
        String sortDirection = "ASC";
        List<Camp> campList = campRepository.getCampList(useyn, name, locationB, locationS,
                campType1, campType2, campType3, campType4);
        for (Camp camp : campList) {
            System.out.println(camp.getName());
        }
        System.out.println(campList.size());
    }
}
