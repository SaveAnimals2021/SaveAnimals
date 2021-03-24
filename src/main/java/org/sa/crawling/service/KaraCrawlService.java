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
public class KaraCrawlService extends CrawlService{

    private static String baseUrl = "https://www.ekara.org";
    private static String url = "https://www.ekara.org/kams/adopt?species=&sex=&weight=&birth=&activity=&page=";


    public void doCrawl() throws Exception {

        for(int i = 1; ; i++) {
            String pageurl = url + i;

            log.info("i: " + i);

            // DOM
            Document doc = Jsoup.connect(pageurl)
                    .userAgent(agent)
                    .get();

            // 마지막 페이지 체크
            if(i > getLastPage(".pagination li:nth-last-child(2) a", doc)){
                log.info("마지막 페이지입니다.");
                return;
            }

            List<String> hrefList = getATagList(".font-weight-bold a", doc);

            crawlView(hrefList, baseUrl);
        }
    }

    public void crawlView(List<String> aURL, String baseUrl) throws Exception {


        // css selector로 얻어올 수 있다.
        // class="wt_viewer" 아래에 있는 img 태그들을 긁어오자
        //Elements imageEles = doc.select(".card-img-top");
        //Elements textEles = doc.select(".card-body");

        // loop를 돌아서 img들의 src를 가져오자.
        // 내부적으로 arrayList이다.
        int size = aURL.size();

        log.info(baseUrl);

        List<String> infoList = new ArrayList<>();

        for (int i = 0; i < size; ++i) {
            String tempUrl = baseUrl + aURL.get(i);

            Document doc = Jsoup.connect(tempUrl)
                    .userAgent(agent)
                    .get();

            URL urlObj = new URL(tempUrl);

            // 고유번호
            String siteNumber = doc.select(".pull-right span").html();
            String date = siteNumber.substring(1);


            // 보안을 위해 header에 userAgent를 추가한다.
            HttpURLConnection urlCon = (HttpURLConnection)urlObj.openConnection();
            urlCon.setRequestProperty("User-Agent", agent);

            Elements eles = doc.select(".list-unstyled.mb-0 h3");
            log.info("size : " + eles.size());

            String[] types = eles.get(0).html().split(" / ");
            String type = types[0];
            String species = types[1];

            String[] neus = eles.get(1).html().split(" / ");
            String sex = neus[0];
            String isNeu = neus[1];

            String age = eles.get(2).html();
            String weight =  eles.get(3).html();
            String color = eles.get(4).html();

            AnimalInfo info = AnimalInfo.builder().age(age).type(type).species(species)
                    .sex(sex).isNeutralized(isNeu).weight(weight).color(color).boardDate(date).siteNumber(siteNumber).build();

            animalList.add(info);

        }




        // code : 403 에러가 뜬다. 보안으로 막힌 경우이다.
        // 보안 뚫기 = userAgent

    }

}
