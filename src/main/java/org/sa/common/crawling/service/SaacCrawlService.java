package org.sa.common.crawling.service;

import lombok.extern.log4j.Log4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.sa.animal.dto.AnimalInfoDTO;

import java.util.ArrayList;
import java.util.List;



@Log4j
// 서울유기동물 입양센터
public class SaacCrawlService extends CrawlService{
    private static String baseUrl = "http://saac.kr";
    private static String url = "http://saac.kr/?act=board&bbs_code=sub2_1&page=";
    private static String serviceName = "서울유기동물 입양센터";
    private static int minYear = 2020;


    public void doCrawl() throws Exception {
        log.info(serviceName + "가 시작됩니다...");

        int prevPage = 0;

        for(int i = 1; ; i++) {
            String pageurl = url + i;

            Document doc = Jsoup.connect(pageurl)
                    .userAgent(agent)
                    .get();

            // 현재 페이지 확인
            int page = Integer.parseInt(doc.select(".board_pagetab").select("span a").html()
                    .replace("[ ", "").replace(" ]",""));


            if(prevPage == page){
                log.info(serviceName+ " 마지막 페이지 입니다.");
                return;
            }

            List<String> hrefList = getATagList(".webzine-title a", doc);;

            crawlView(hrefList, baseUrl);
            prevPage = page;
        }
    }

    public void crawlView(List<String> aURL, String baseUrl) throws Exception {
        int size = aURL.size();

        for(int i = 0; i < size; ++i) {
            Document doc = getDocument( baseUrl + aURL.get(i));

            // 입양 완료 체크
            String title = doc.select(".board-title h3").html();
            // log.info("title : " + title);

            if(title.contains("입양") || title.contains("완료")
                    || title.contains("품으로") || title.contains("치와와 와와") || title.contains("상주") ||  title.contains("동영상")){
               // log.info("입양 완료되었습니다......................");
                continue;
            }

            // ========== 날짜 추가
            String date = doc.select(".board-info span:nth-last-child(3)").html();

            int year = Integer.parseInt(date.substring(0, 4));

            if(minYear >= year){
                return;
            }

            AnimalInfoDTO info = new AnimalInfoDTO();

            date = date.substring(0, date.lastIndexOf(" ")).replace(".", "/");
            info.setDate(date);


            doc.select(".board-content").forEach(e -> {

                // ========== 이미지
                Elements images = e.select("img");
                List<String> imageList = new ArrayList<>();
                int imageSize = images.size();
                String type = "";

                for(int j = 0; j < imageSize; ++j){
                    String imageString  = images.get(j).attr("src");
                    type = imageString.substring(imageString.lastIndexOf("."));

                    if(imageString.contains("facebook") || imageString.contains("twitter") || imageString.contains(".gif") ){
                        continue;
                    }

                    imageList.add(imageString);
                }

                info.setImageType("jpg");

                info.setImageUrlList(imageList);
                info.setServiceName(serviceName);

                // ========== 기타 정보
                Elements divs = e.select("div");
                int divsize = divs.size();


                for(int j = 2; j < divsize; ++j){
                    String temp = divs.get(j).html();

                    if(temp.contains("이름")){
                        temp = setInfo(temp);
                        if(temp.isEmpty()){ continue;}

                        info.setName(temp);

                    } else if(temp.contains("성별")){
                        temp = setInfo(temp);
                        if(temp.isEmpty()){ continue;}

                        info.setSex(temp);

                    } else if(temp.contains("나이")){
                        temp = setInfo(temp);
                        if(temp.isEmpty()){ continue;}

                        info.setAge(temp.replace("살", "살 "));

                    } else if(temp.contains("특징")){
                        int index = temp.lastIndexOf(":") + 1;

                        if(0 > index){
                            continue;
                        }

                        info.setSpecial(temp);

                    }
                }

               //  setAnimalCode(info);

                // animalList.add(info);
            });

        }
    }

    private String setInfo(String str){
        int index = str.lastIndexOf(":") + 1;

        if(0 > index){
            return "";
        }
        str = str.substring(index).replace(" ", "");

        return str;
    }
}
