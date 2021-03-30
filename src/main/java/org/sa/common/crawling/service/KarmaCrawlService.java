package org.sa.common.crawling.service;

import lombok.extern.log4j.Log4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.sa.animal.dto.AnimalInfoDTO;

import java.util.ArrayList;
import java.util.List;


@Log4j
// 서울유기동물 입양센터
public class KarmaCrawlService extends CrawlService{

    private static String baseUrl = "http://www.karma.or.kr";
    private static String url = "http://www.karma.or.kr/human_boardA/animal_board.php?act=list&bid=animal&page=";
    private static String serviceName = "한국동물구조관리협회";

    // page 수집에 실패했을 경우, 무한반복을 막기 위해
    private static int maxCount =3;

    public void doCrawl() throws Exception {
        log.info(serviceName + "가 시작됩니다...");

        for(int i = 1; ; i++) {
            String pageurl = url + i;

            Document doc = Jsoup.connect(pageurl)
                    .userAgent(agent)
                    .get();

            // ========== CRAWL VIEW
            Elements eles = doc.select(".rescue");
            eles.select(".list").forEach(b->{
                setInfo(b);
            });

            // ========== 페이지 넘어가기
            int lastPage = getLastPage("#pagingNav a:nth-last-child(2)", doc);


            if(i % 10 == 0){
                Elements ele = doc.select("#pagingNav a:nth-last-child(1)");

                if(ele.isEmpty()){
                    log.info(serviceName+" 마지막 페이지입니다.");
                    return;
                }
            } else{
                // 마지막 페이지 체크
                if(i > lastPage && i > getLastPage("#pagingNav a:nth-last-child(1)", doc)){
                    log.info(serviceName+" 마지막 페이지입니다.");
                    return;
                }
            }
        }
    }

    private void setInfo(Element ele){
        // div class="list" 가 들어온다.
        Elements ul = ele.select("ul li");

        // ========== 이미지
        String imageString = baseUrl + ele.select("a").attr("href");

        // ========== 날짜
        String date = ul.get(0).select("i").html();
        date = date.substring(0, date.indexOf("&")).replace("-", "/");


        // ========== 종
        String types = removeStrong(ul.get(3).html());
        String[] typeArr = types.split("/");
        String type = typeArr[0];
        String species = typeArr[1];


        // ========== 성별
        String sex = removeStrong(ul.get(4).html());


        // ========== 나이
        String age = removeStrong(ul.get(5).html());
        if(age.contains("년")){
            age = age.replace("년", "살");
        }



        // ========== 색
        String color = removeStrong(ul.get(6).html());


        // ========== 중성화
        String isNeu = removeStrong(ul.get(7).html());


        // ========== 특징(성격)
        String special = "성격은 " + removeStrong(ul.get(8).html());
        if(special.endsWith("적")){
            special += "인 편. ";
        }else{
            special += "한 편. ";
        }

        // ========== 체중
        String weight = removeStrong(ul.get(9).html());


        // ========== 특징(건강)
        special += "건강상태는 " + removeStrong(ul.get(10).html()) + ". ";

        // ========== 기타 특징
        String extra = ul.get(12).html();
        extra = extra.substring(extra.indexOf(">") + 1).replace("&nbsp;", " ").replace("</span>", "");
        special += "그 외에" + extra;


        // ========== 이미지

        List<String> imgList = new ArrayList<>();
        imgList.add(baseUrl + ele.select("img").attr("src"));
        String imageType = "jpg";

        // ========== 리스트에 추가하기

        AnimalInfoDTO info = AnimalInfoDTO.builder()
                .age(age).sex(sex).species(species).isNeutralized(isNeu).color(color).date(date)
                .special(special).imageType(imageType).imageUrlList(imgList).weight(weight).type(type)
                .serviceName(serviceName)
                .build();

        // setAnimalCode(info);

        // animalList.add(info);

    }

    private String removeStrong(String str){
        str = str.substring(str.lastIndexOf(">") + 1).replace(" ", "");
        return str;
    }
}
