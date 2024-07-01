package com.demo.campingnavi.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CampItem {

    private String name;
    private String description;
    private String imageUrl;
    private String shoppingLink;

    public CampItem(String name, String description, String imageUrl, String shoppingLink) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.shoppingLink = shoppingLink;
    }
}
