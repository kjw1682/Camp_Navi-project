package com.demo.campingnavi.dto;

import com.demo.campingnavi.info.OAuth2UserInfo;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class FacebookUserDetails implements OAuth2UserInfo {

    private Map<String, Object> attributes;


    @Override
    public String getProvider() {
        return "facebook";
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getPicture() {
        return (String) attributes.get("image");
    }
}
