package com.demo.campingnavi.info;

public interface OAuth2UserInfo {
    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();
    String getPicture();
}
