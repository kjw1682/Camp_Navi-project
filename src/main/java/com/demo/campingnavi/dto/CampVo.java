package com.demo.campingnavi.dto;

import com.demo.campingnavi.domain.Camp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CampVo {
    private Camp camp = null;
    private float score = 0f;
    private String scoreView = "";
    private String predict = "";
    private String homepage = "";
    private String addressFull = "";
    private String addressShort = "";
    private Map<String, String> sortMap = new HashMap<>();

    public CampVo(Camp camp) {
        this.camp = camp;
        this.addressFull = camp.getAddr1() + " " + camp.getAddr2();
        this.addressShort = camp.getLocationB() + " " + camp.getLocationS();
        this.sortMap.put("name", camp.getName());
        this.sortMap.put("addr", this.addressShort);
    }

    public CampVo(Camp camp, float score, String predict) {
        this(camp);
        this.score = score;
        this.scoreView = String.valueOf(score).substring(0, 3);
        this.predict = predict;
    }
}
