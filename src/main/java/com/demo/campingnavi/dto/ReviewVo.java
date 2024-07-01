package com.demo.campingnavi.dto;

import com.demo.campingnavi.domain.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReviewVo {
    private Review review;
    private int rcdCount;
    private String createdAt;
    private String modifiedAt = null;

    public ReviewVo(Review review, int rcdCount) {
        this.review = review;
        this.rcdCount = rcdCount;
        this.createdAt = String.format(review.getCreatedAt().toString(), "yyyy-MM-dd HH:mm:ss").substring(0, 19);
        if (review.getModifiedAt() != null) {
            this.modifiedAt = String.format(review.getModifiedAt().toString(), "yyyy-MM-dd HH:mm:ss").substring(0, 19);
        }
    }

}
