package org.sa.crawling.service;


import lombok.extern.log4j.Log4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Log4j
public class TestCrawlClass {


    String agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.190 Safari/537.36";


    public void doCrawl() throws Exception {

        for(int i = 1; ; i++) {

            String listUrl = "http://www.kcanimal.or.kr/board_gallery01/board_list.asp?search_type=A&page=";
            listUrl += i;

            String baseUrl = "http://www.kcanimal.or.kr";


            Document doc = Jsoup.connect(listUrl)
                    .userAgent(agent)
                    .get();

            //마지막 페이지 조건
            Elements lastPageCondition = doc.select(".pg_wrap a:nth-last-child(4)");
            String last =  lastPageCondition.html();
            log.info("currentPage : " + i);
            log.info("lastPageCondition : " + last);

            String lastPageItem = "";

            //각각의 Pagination의 마지막 페이지들
            if(lastPageCondition != null) {

                lastPageItem = doc.select(".pg_wrap a:nth-last-child(4)").html();

            }else {

                lastPageItem = doc.select(".pg_wrap a:nth-last-child(3)").prev("pg_page").html();

            }

            log.info("last: " + lastPageItem);

            //마지막 페이지면 종료
            if(i > Integer.parseInt(last)) {
                log.info("페이지가 존재하지 않습니다.");
                return;

            }
            //log.info(lastPageItem);

            //a Tag 수집
            Elements aTag = doc.select(".list_B_left a");

            List<String> viewUrlList = new ArrayList<>();
            aTag.forEach(a -> {

                viewUrlList.add(a.attr("href"));

            });

            // log.info(viewUrlList);

            //crawlView(urlList, baseUrl);
            log.info("=======================================");
        }

    }

    public void crawlView(List<String> aURL, String baseUrl) throws Exception {

        int size = aURL.size();

        log.info(baseUrl);

        List<String> infoList = new ArrayList<>();

        for(int i = 0; i < size; ++i) {
            //=================== IMAGE ===================//
            // img 태그에서 src 추출하기
            //String imgUrl = imageEles.attr("src");

            String tempUrl = baseUrl + aURL.get(i);
            Document doc = Jsoup.connect(tempUrl)
                    .userAgent(agent)
                    .get();

            URL urlObj = new URL(tempUrl);

            // 보안을 위해 header에 userAgent를 추가한다.
            HttpURLConnection urlCon = (HttpURLConnection)urlObj.openConnection();
            urlCon.setRequestProperty("User-Agent", agent);

            doc.select(".list-unstyled.mb-0 h3").forEach(e -> infoList.add(e.html()));
        }

        log.info(infoList);
    }

}
