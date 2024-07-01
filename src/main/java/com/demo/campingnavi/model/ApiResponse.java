package com.demo.campingnavi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ApiResponse {

    @JsonProperty("response")
    private Response response;

    @Getter
    @Setter
    public static class Response {
        @JsonProperty("header")
        private Header header;

        @JsonProperty("body")
        private Body body;
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
        private Items items;

        private int numOfRows;

        private int pageNo;

        private int totalCount;

    }

    @Getter
    @Setter
    public static class Items {
        @JsonProperty("item")
        private List<Item> itemList;
    }

    @Getter
    @Setter
    public static class Item {
        @JsonProperty("contentId")
        private String contentId;

        @JsonProperty("facltNm")
        private String facltNm;

        @JsonProperty("lineIntro")
        private String lineIntro;

        @JsonProperty("intro")
        private String intro;

        @JsonProperty("allar")
        private String allar;

        @JsonProperty("insrncAt")
        private String insrncAt;

        @JsonProperty("trsagntNo")
        private String trsagntNo;

        @JsonProperty("bizrno")
        private String bizrno;

        @JsonProperty("facltDivNm")
        private String facltDivNm;

        @JsonProperty("mangeDivNm")
        private String mangeDivNm;

        @JsonProperty("mgcDiv")
        private String mgcDiv;

        @JsonProperty("manageSttus")
        private String manageSttus;

        @JsonProperty("hvofBgnde")
        private String hvofBgnde;

        @JsonProperty("hvofEnddle")
        private String hvofEnddle;

        @JsonProperty("featureNm")
        private String featureNm;

        @JsonProperty("induty")
        private String induty;

        @JsonProperty("lctCl")
        private String lctCl;

        @JsonProperty("doNm")
        private String doNm;

        @JsonProperty("sigunguNm")
        private String sigunguNm;

        @JsonProperty("zipcode")
        private String zipcode;

        @JsonProperty("addr1")
        private String addr1;

        @JsonProperty("addr2")
        private String addr2;

        @JsonProperty("mapX")
        private String mapX;

        @JsonProperty("mapY")
        private String mapY;

        @JsonProperty("direction")
        private String direction;

        @JsonProperty("tel")
        private String tel;

        @JsonProperty("homepage")
        private String homepage;

        @JsonProperty("resveUrl")
        private String resveUrl;

        @JsonProperty("resveCl")
        private String resveCl;

        @JsonProperty("manageNmpr")
        private String manageNmpr;

        @JsonProperty("gnrlSiteCo")
        private String gnrlSiteCo;

        @JsonProperty("autoSiteCo")
        private String autoSiteCo;

        @JsonProperty("glampSiteCo")
        private String glampSiteCo;

        @JsonProperty("caravSiteCo")
        private String caravSiteCo;

        @JsonProperty("indvdlCaravSiteCo")
        private String indvdlCaravSiteCo;

        @JsonProperty("sitedStnc")
        private String sitedStnc;

        @JsonProperty("siteMg1Width")
        private String siteMg1Width;

        @JsonProperty("siteMg2Width")
        private String siteMg2Width;

        @JsonProperty("siteMg3Width")
        private String siteMg3Width;

        @JsonProperty("siteMg1Vrticl")
        private String siteMg1Vrticl;

        @JsonProperty("siteMg2Vrticl")
        private String siteMg2Vrticl;

        @JsonProperty("siteMg3Vrticl")
        private String siteMg3Vrticl;

        @JsonProperty("siteMg1Co")
        private String siteMg1Co;

        @JsonProperty("siteMg2Co")
        private String siteMg2Co;

        @JsonProperty("siteMg3Co")
        private String siteMg3Co;

        @JsonProperty("siteBottomCl1")
        private String siteBottomCl1;

        @JsonProperty("siteBottomCl2")
        private String siteBottomCl2;

        @JsonProperty("siteBottomCl3")
        private String siteBottomCl3;

        @JsonProperty("siteBottomCl4")
        private String siteBottomCl4;

        @JsonProperty("siteBottomCl5")
        private String siteBottomCl5;

        @JsonProperty("tooltip")
        private String tooltip;

        @JsonProperty("glampInnerFclty")
        private String glampInnerFclty;

        @JsonProperty("caravInnerFclty")
        private String caravInnerFclty;

        @JsonProperty("prmisnDe")
        private String prmisnDe;

        @JsonProperty("operPdCl")
        private String operPdCl;

        @JsonProperty("operDeCl")
        private String operDeCl;

        @JsonProperty("trlerAcmpnyAt")
        private String trlerAcmpnyAt;

        @JsonProperty("caravAcmpnyAt")
        private String caravAcmpnyAt;

        @JsonProperty("toiletCo")
        private String toiletCo;

        @JsonProperty("swrmCo")
        private String swrmCo;

        @JsonProperty("wtrplCo")
        private String wtrplCo;

        @JsonProperty("brazierCl")
        private String brazierCl;

        @JsonProperty("sbrsCl")
        private String sbrsCl;

        @JsonProperty("sbrsEtc")
        private String sbrsEtc;

        @JsonProperty("posblFcltyCl")
        private String posblFcltyCl;

        @JsonProperty("posblFcltyEtc")
        private String posblFcltyEtc;

        @JsonProperty("clturEventAt")
        private String clturEventAt;

        @JsonProperty("clturEvent")
        private String clturEvent;

        @JsonProperty("exprnProgrmAt")
        private String exprnProgrmAt;

        @JsonProperty("exprnProgrm")
        private String exprnProgrm;

        @JsonProperty("extshrCo")
        private String extshrCo;

        @JsonProperty("frprvtWrppCo")
        private String frprvtWrppCo;

        @JsonProperty("frprvtSandCo")
        private String frprvtSandCo;

        @JsonProperty("fireSensorCo")
        private String fireSensorCo;

        @JsonProperty("themaEnvrnCl")
        private String themaEnvrnCl;

        @JsonProperty("eqpmnLendCl")
        private String eqpmnLendCl;

        @JsonProperty("animalCmgCl")
        private String animalCmgCl;

        @JsonProperty("tourEraCl")
        private String tourEraCl;

        @JsonProperty("firstImageUrl")
        private String firstImageUrl;

        @JsonProperty("createdtime")
        private String createdtime;

        @JsonProperty("modifiedtime")
        private String modifiedtime;
    }
}
