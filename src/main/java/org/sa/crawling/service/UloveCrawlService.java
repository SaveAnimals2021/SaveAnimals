package org.sa.crawling.service;

import lombok.extern.log4j.Log4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.sa.common.domain.AnimalInfo;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


@Log4j
// 서울유기동물 입양센터
public class UloveCrawlService extends CrawlService{

    private static String baseUrl = "https://ulovepetshelter.co.kr";
    private static String url = "https://ulovepetshelter.co.kr/adoption/?q=YToxOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjt9&page=";

    // page 수집에 실패했을 경우, 무한반복을 막기 위해
    private static int maxCount =3;

    public void doCrawl() throws Exception {
        for(int i = 1; i < maxCount; i++) {
            String pageurl = url + i;

            Document doc = Jsoup.connect(pageurl)
                    .userAgent(agent)
                    .get();

            // 마지막 페이지 체크
            if(i > getLastPage(".pagination li:nth-last-child(2) a", doc)){
                log.info("마지막 페이지입니다.");
                return;
            }

            //a Tag 수집
            List<String> hrefList = getATagList(".post_link_wrap._fade_link", doc);

            // view 페이지에 직접 이동해서 crawling
            crawlView(hrefList, baseUrl);
        }
    }

    // view 페이지에 직접 이동해서 crawling
    public void crawlView(List<String> aURL, String baseUrl) throws Exception {
        int size = aURL.size();
        for(int i = 0; i < size; ++i) {
            //=================== IMAGE ===================//
            // img 태그에서 src 추출하기
            //String imgUrl = imageEles.get(i).attr("src");

            String tempUrl = baseUrl + aURL.get(i);
            Document doc = Jsoup.connect(tempUrl)
                    .userAgent(agent)
                    .get();

            URL urlObj = new URL(tempUrl);

            // 보안을 위해 header에 userAgent를 추가한다.
            HttpURLConnection urlCon = (HttpURLConnection)urlObj.openConnection();
            urlCon.setRequestProperty("User-Agent", agent);

            // 동물 정보
            doc.select(".board_view").forEach(e -> {


                log.info("===============================================================");
                //============================= 동물 이미지 ==========================//

                String imageURL = e.select(".board_txt_area img").attr("src");
                String imageType = imageURL.substring(imageURL.lastIndexOf(".") + 1);

                List<String> imgList = new ArrayList<>();
                imgList.add(imageURL);

                //============================= 동물 정보 ==========================//

                Elements divs = e.select(".noBorder.tableHorizontal div");

                int divsize = divs.size();

//                for(int j = 1; j < divsize; ++j){
//                    log.info(j + " : " + divs.get(j).html());
//                }


                String species = divs.get(1).html();
                String age = divs.get(3).html();
                String sex = divs.get(5).html();
                String vaccinated = divs.get(7).html();
                String neutralized = divs.get(9).html();
                String special = divs.get(11).html();

                AnimalInfo info = AnimalInfo.builder()
                        .age(age).sex(sex).species(species).isNeutralized(neutralized).isVaccinated(vaccinated)
                        .special(special).imageType(imageType).imageUrlList(imgList).build();

                animalList.add(info);
                log.info(animalList.size());
            });

        }
    }
}
