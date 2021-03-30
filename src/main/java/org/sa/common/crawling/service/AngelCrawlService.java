package org.sa.common.crawling.service;

import lombok.extern.log4j.Log4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.sa.animal.dto.MissingAnimalDTO;
import org.sa.common.util.DateFormatter;
import org.sa.common.util.SimpleDateFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Log4j
public class AngelCrawlService extends CrawlService {


    private static String baseUrl = "http://www.angel.or.kr/";
    private static String url = "http://www.angel.or.kr/index.php?";
    private static String serviceName = "동물보호센터";

    @Override
    public void doCrawl() throws Exception {

        log.info(serviceName + "가 시작됩니다...");

        String type = "개";
        String typeURL = "code=dog";
        crawlType(type, typeURL);
        type = "고양이";
        typeURL = "code=cat";
        crawlType(type, typeURL);
        type = "기타";
        typeURL = "code=other";
        crawlType(type, typeURL);
    }

    public void crawlType(String type, String typeURL) throws Exception{
        int count = 0;

        for (int i = 1; ; i++) {
            String pageurl = url + typeURL + "&page=" + i;

            // DOM
            Document doc = Jsoup.connect(pageurl)
                    .userAgent(agent)
                    .get();

            log.info("현재 페이지 : " + i);



            List<String> hrefList = getATagList(".reportlist .reportlist_detail .title a", doc);

            count = crawlView(hrefList, baseUrl, type, count);

            // 마지막 페이지 체크
            if(10 < count){
                log.info(serviceName + "가 끝났습니다...");
                return;
            }
        }
    }

    public int crawlView(List<String> aURL, String baseUrl, String type, int count) throws Exception {

        int size = aURL.size();

        main : for (int i = 0; i < size; ++i) {
            Document doc = getDocument(baseUrl + aURL.get(i));

            // ========== 날짜
            String date = doc.select("#bodycontent .num").get(0).html();
            date = date.substring(5, 27).replace("년 ", "/");
            date = date.replace("월 ", "/");
            date = date.replace("일, ", " ");
            date = date.replace("시 ", ":");
            date = date.replace("분", "");


            Date newDate = DateFormatter.fromStringToDate(date);
            Boolean isInThree = DateFormatter.checkInThreeMonths(newDate);

            if(false == isInThree){
                log.info("3달이 지난 게시판입니다...");
                return ++count;
            }

            // ========== 이름
            Elements content1 = doc.select(".tblWrap tr td");

            String species = content1.select("a").get(0).html();
            String sex = content1.get(3).html();
            String age = content1.get(5).html();
            String name = content1.get(7).html();
            String missingDate = (content1.get(9).html().replace("-", "/"));
            String rescueDate = "";
            String guardianName = content1.get(11).select("strong").html();
            Elements tempEle = content1.select(".ty1");
            String statusStr = tempEle.get(4).html();

            Elements eles = doc.select(".section_content_box");

            // 구조장소, 목격장소, 실종장소
            String missingLocation = "";
            String rescueLocation = "";


            if(missingDate.length() < 10){
                missingDate= SimpleDateFormatter.makeDateFormat(missingDate);
            }

            int status = 0;

            if(statusStr.contains("목격")){
                status = 1;
            } else if(statusStr.contains("구조")){
                status = 2;
                rescueLocation = eles.get(1).html();
            } else{
                missingLocation = eles.get(1).html();
                rescueDate = missingDate;
                missingDate = "";
            }

            // 로그인이 필요합니다.
            // String phoneNumber = content1.get(13).html();
            

            String temp = eles.get(0).html();
            String situation =  temp.substring(temp.lastIndexOf("g>") + 2);
            String special = eles.get(2).html().replace("<br>", " ");


            // 이미지
            Elements images = doc.select("#thumbs img");
            List<String> imageList = new ArrayList<>();

            for(int j = 0; j < images.size(); ++j){
                String image = images.get(j).attr("src").replace("/upThumb/", "/upImg/");
                imageList.add(image);
            }

            // 등록일 설정

            String regDate = date.substring(0,10);

            MissingAnimalDTO info = MissingAnimalDTO.builder().age(age).type(type).species(species)
                    .sex(sex).missingDate(missingDate).missingLocation(missingLocation).guardianName(guardianName)
                    .name(name).imageType("jpg").serviceName(serviceName).originURL(baseUrl).special(special)
                    .imageUrlList(imageList).situation(situation).rescueStatus(status).regDate(regDate).rescueLocation(rescueLocation)
                    .rescueDate(rescueDate)
                    .build();

            setAnimalCode(info);
            String code = info.getAnimalCode();

            for(int j = 0; j < animalList.size(); ++j){
                // 중복이라면 continue
                String tempCode = animalList.get(j).getAnimalCode();
                if(tempCode.equals(code) || tempCode.contains(code)){
                    continue main;
                }
            }

            animalList.add(info);
        }

        return 0;
    }
}
