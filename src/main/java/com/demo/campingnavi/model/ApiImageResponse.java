package com.demo.campingnavi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ApiImageResponse {
    @JsonProperty("response")
    private ApiImageResponse.Response response;

    @Getter
    @Setter
    public static class Response {
        @JsonProperty("header")
        private ApiImageResponse.Header header;

        @JsonProperty("body")
        private ApiImageResponse.Body body;
    }

    @Getter
    @Setter
    public static class Header {
        @JsonProperty("resultCode")
        private String resultCode;

        @JsonProperty("resultMsg")
        private String resultMsg;
    }

    @Getter
    @Setter
    public static class Body {
        @JsonProperty("items")
        private ApiImageResponse.Items items;

        private int numOfRows;

        private int pageNo;

        private int totalCount;

    }
    @Getter
    @Setter
    public static class Items {
        @JsonProperty("item")
        private List<ApiImageResponse.Item> itemList;
    }

    @Getter
    @Setter
    public static class Item {

        @JsonProperty("contendId")
        private String contendId;

        @JsonProperty("serialnum")
        private String serialnum;

        @JsonProperty("imageUrl")
        private String imageUrl;

        @JsonProperty("createdtime")
        private String createdtime;

        @JsonProperty("modifiedtime")
        private String modifiedtime;

    }
}
