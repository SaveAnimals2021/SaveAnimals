package org.sa.common.crawling.service;

import lombok.extern.log4j.Log4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.sa.animal.dto.MissingAnimalDTO;
import org.sa.common.util.SimpleDateFormatter;

import java.util.ArrayList;
import java.util.List;

@Log4j
public class APMSCrawlService extends CrawlService {


    private static String baseUrl = "http://www.animal.go.kr/";
    private static String url = "https://www.animal.go.kr/front/awtis/loss/lossList.do?menuNo=1000000057&page=";
    private static String serviceName = "동물보호관리시스템";

    @Override
    public void doCrawl() throws Exception {

        log.info(serviceName + "가 시작됩니다...");

        int count = 0;

        for (int i = 1; ; i++) {
            String pageurl = url + i;

            // DOM
            Document doc = Jsoup.connect(pageurl)
                    .userAgent(agent)
                    .get();

            log.info("현재 페이지 : " + i);

            List<String> hrefList = new ArrayList<>();
                    // getATagList(".boardList .photo a", doc);
            Elements eles = doc.select(".boardList .photo a");

            for(int j = 0; j < eles.size(); ++j){
                String temp = eles.get(j).attr("onclick");

                if(temp.contains("showImgWin")){
                    continue;
                }

                temp = temp.substring(temp.indexOf("'") + 1, temp.lastIndexOf("'"));
                hrefList.add(temp);
            }

            crawlView(hrefList, pageurl);

            if(0 == hrefList.size()){
                ++count;
            }

            // 마지막 페이지 체크
            if(10 < count){
                log.info(serviceName + "가 끝났습니다...");
                return;
            }
        }
    }

    public void crawlView(List<String> aURL, String url) throws Exception {
        // https://www.animal.go.kr/front/awtis/loss/lossDtl.do?menuNo=1000000057&page=2&seqNo=56128
        int size = aURL.size();
        String newURL = url.replace("lossList","lossDtl");
        main:for (int i = 1; i < size; ++i) {
            Document doc = getDocument(newURL + "&seqNo=" + aURL.get(i));

            Elements eles = doc.select("tbody td");

            String name = eles.get(0).html();
            String guardianName = eles.get(1).html();
            String species = eles.get(2).html();
            String phoneNumber = eles.select("a").html();
            String missingDate = eles.get(5).html().replace("-", "/") + " 00:00";
            String sex = eles.get(6).html();
            String age = eles.get(7).html();
            String missingLocation = eles.get(8).html();
            String color = eles.get(9).html();
            String situation = eles.get(11).html();
            String special = eles.get(10).html(); // 경위
            special.replace("&amp;", " ");

            // 이미지
            String href = doc.select(".photo a").attr("href");
            String fileType = "";
            String regDate = "";
            if(!href.isEmpty()){
                href = href.substring(href.indexOf("'") + 1, href.lastIndexOf("'"));
                // log.info("href : " + href);
                fileType = href.substring(href.lastIndexOf("."));
                regDate =  href.substring(href.lastIndexOf("/") + 1).substring(0, 8);
            }

            log.info("regDate : " + regDate);
            regDate = SimpleDateFormatter.makeDateFormat(regDate);

            log.info("regDate : " + regDate);
            log.info("missingDate : " + missingDate);

            List<String> imageList = new ArrayList<>();
            imageList.add(href);

            MissingAnimalDTO info = MissingAnimalDTO.builder().age(age).type("").species(species)
                    .sex(sex).missingDate(missingDate).missingLocation(missingLocation).guardianName(guardianName)
                    .name(name).imageType(fileType).serviceName(serviceName).originURL(baseUrl).special(special)
                    .imageUrlList(imageList).regDate(regDate)
                    .build();

            setAnimalCode(info);

            String code = info.getAnimalCode();

            for (int j = 0; j < animalList.size(); ++j) {
                // 중복이라면 continue
                String tempCode = animalList.get(j).getAnimalCode();
                if (tempCode.equals(code) || tempCode.contains(code)) {
                    continue main;
                }
            }

            animalList.add(info);
        }




        return;
    }
}