package com.demo.campingnavi.controller;

import com.demo.campingnavi.model.CampItem;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CampingEssentialsController {

    private List<CampItem> products;

    public CampingEssentialsController() {
        products = new ArrayList<>();

        products.add(new CampItem("텐트", "<br><br>" +
                "모든 캠핑 여행의 초석." +
                "<br><br><br><br>" +
                "텐트의 종류로는 돔텐트, 터널텐트, 백패킹텐트, 사계정텐트등 크기, 모양, 재질 무게에 따른 다양한 종류가 있습니다." +
                "<br><br><br><br>" +
                "이와 같은 캠핑용 텐트를 선택할 때 고려해야 할 몇가지 요소가 있습니다." +
                "<br><br><br><br>" +
                "첫째, 텐트의 크기는 그 안에서 잠을 잘 사람들의 수에 적합해야 합니다. 이동하고 장비를 보관할 수 있는 충분한 공간을 확보하는 것도 중요하지만," +
                "휴대하기가 번거로울 정로도 크지는 않아야 합니다." +
                "<br><br><br><br>" +
                "둘째, 텐트는 내구성이 좋고 방수가 될 뿐만 아니라 설치와 철거가 용이해야 합니다." +
                "캠핑장에 설치할 때 시간과 노력을 절약할 수 있는 텐트를 구입하는 것을 고려해 보세요." +
                "<br><br><br><br>", "/assets/images/campItem/free-icon-camping-2486940.png", "https://www.coupang.com/np/search?component=&q=%EC%BA%A0%ED%95%91+%ED%85%90%ED%8A%B8&channel=user"));

        products.add(new CampItem("침낭", "<br><br><br><br>" +
                "따뜻하고 편안한 침낭." +
                "<br><br><br><br>" +
                "숙면은 성공적인 캠핑을 위해 필수적이고, 양질의 침낭은 따듯하고 편안하게 지내기 위해 필수적입니다." +
                "<br><br><br><br>" +
                "침남은 모양과 크기(1인용/커플,더블 등), 단열재 등이 다양하게 나오기 때문에 자신의 체형과 캠핑을 하게 될 조건에 맞는 침낭을 선택하는 것이 중요합니다." +
                "<br><br><br><br>" +
                "합성 소재를 사용한 침낭은 내구성과 다재다능성 면에서 훌륭한 옵션이며, 천연 다운 소재를 사용한 침낭은 가볍고 압축성이 뛰어나지만 가격이 더 비쌀 수 있습니다" +
                "<br><br><br><br>" +
                "캠핑을 하게 될 환경에 적합한 온도 등급의 3계절 4계절 침낭을 선택하는 것도 중요합니다." +
                "<br><br><br><br>", "/assets/images/campItem/free-icon-sleeping-bag-2323416.png", "https://www.coupang.com/np/search?component=&q=%EC%BA%A0%ED%95%91+%EC%B9%A8%EB%82%AD&channel=user"));

        products.add(new CampItem("캠핑 의자", "<br><br><br><br>" +
                "휴식을 위한 편안한 의자." +
                "<br><br><br><br>" +
                "캠핑 의자는 캠핑장에서 긴 하루를 보낸 후 휴식을 취하고 긴장을 풀 수 있는 좋은 방법입니다." +
                "<br><br><br><br>" +
                "편안하고 내구성이 좋으며 휴대하기 쉬운 의자를 찾으세요." +
                "<br><br><br><br>" +
                "추가적인 편의를 위해 컵 홀더와 포켓이 내장된 의자를 구입하는 것을 고려해 보세요." +
                "<br><br><br><br>", "/assets/images/campItem/free-icon-camping-chair-1981027.png", "https://www.coupang.com/np/search?component=&q=%EC%BA%A0%ED%95%91+%EC%9D%98%EC%9E%90&channel=user"));

        products.add(new CampItem("캠핑 테이블", "<br><br><br><br>" +
                "식사를 위한 간편한 테이블." +
                "<br><br><br><br>" +
                "캠핑 테이블은 요리, 식사 그리고 다른 활동을 위한 안정적인 표면을 제공하면서 캠핑하는 동안 필요한 편리한 장비입니다." +
                "<br><br><br><br>" +
                "가볍고 튼튼하며 설치하기 쉬운 캠핑 테이블을 찾으세요. 다리가 조절 가능한 테이블은 다용도로 사용할 수 있고, 보관 공간이 내장된 테이블은 편리하게 사용할 수 있습니다." +
                "<br><br><br><br>" +
                "컴팩트하고 쉬게 접을 수 있는 테이블은 캠핑 장비 공간이 제한되 사람들에게도 이상적입니다." +
                "<br><br><br><br>" +
                "식사 준비, 보드게임, 커피한잔 등을 즐기기 위해 사용하는 캠핑 테이블은 필수적인 아이템입니다.", "/assets/images/campItem/free-icon-camping-table-10756188.png", "https://www.coupang.com/np/search?component=&q=%EC%BA%A0%ED%95%91+%ED%85%8C%EC%9D%B4%EB%B8%94&channel=user"));

        products.add(new CampItem("랜턴", "<br><br><br><br>" +
                "야간 조명을 위한 랜턴." +
                "<br><br><br><br>" +
                "캠핑용 랜턴은 여러분의 캠핑장에서 시야와 분위기를 더해줍니다." +
                "<br><br><br><br>" +
                "밝고 가벽고 걸기 쉬운 조면을 찾으세요." +
                "<br><br><br><br>" +
                "배터리를 절약하기 위해 충전식 조명을," +
                "<br><br><br><br>" +
                "감성, 다목적성을 추가하기 위해 다중조명 모드를 갖춘 캠핑렌턴을 찾아 구입해 보세요." +
                "<br><br><br><br>", "/assets/images/campItem/free-icon-lightbulb-7682800.png", "https://www.coupang.com/np/search?component=&q=%EC%BA%A0%ED%95%91+%EB%A0%8C%ED%84%B4&channel=user"));

        products.add(new CampItem("아이스 쿨러", "<br><br><br><br>" +
                "음식과 음료를 신선하게 유지." +
                "<br><br><br><br>" +
                "음식과 음료를 차갑게 유지하는 것은 캠핑하는 동안 안전과 즐거움 모두를 위해 필수적입니다." +
                "<br><br><br><br>" +
                "좋은 아이스 박스는 여러분의 모든 물품을 담을 수 있을 만큼 충분히 크고," +
                "<br><br><br><br>" +
                "단연성이 강해야 하며, 캠핑의 혹독함을 견딜 수 있을 만큼 충분히 내구성이 있어야 합니다." +
                "<br><br><br><br>" +
                "운반하기 쉬운 튼튼한 손잡이와 바퀴가 있는 아이스 쿨러와 청소와 유지보수가 쉬운 쿨러를 찾압 보세요." +
                "<br><br><br><br>", "/assets/images/campItem/free-icon-ice-box-8700263.png", "https://www.coupang.com/np/search?component=&q=%EC%BA%A0%ED%95%91+%EC%95%84%EC%9D%B4%EC%8A%A4%EB%B0%95%EC%8A%A4&channel=user"));

        products.add(new CampItem("캠핑 화로대", "<br><br><br><br>" +
                "불멍과 음식조리를 위한 화로대" +
                "<br><br><br><br>" +
                "불을 필우는 것은 캠핑 경험의 중요한 부분이고," +
                "<br><br><br><br>" +
                "불멍을 하거나 캠핑음식을 조리하는 감성적인 캠핑용품의 역할을 합니다." +
                "<br><br><br><br>" +
                "다양한 소토브가 존재하지만, 우선은 휴대성이 용이한 접이식 캠핑 화로대를 고려해보세요." +
                "<br><br><br><br>", "/assets/images/campItem/free-icon-bonfire-442472.png", "https://www.coupang.com/np/search?component=&q=%EC%BA%A0%ED%95%91+%ED%99%94%EB%A1%9C%EB%8C%80&channel=user"));
    }

    @GetMapping("/camp/essentials")
    public ModelAndView showCampingEssentials(Model model) {
        model.addAttribute("products", products);
        return new ModelAndView("/campingEssentials/campingEssentials");
    }
}
