package org.sa.crawling.service;

import lombok.extern.log4j.Log4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.sa.common.domain.AnimalInfo;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;



@Log4j
// 서울유기동물 입양센터
public class SaacCrawlService extends CrawlService{
    private static String baseUrl = "http://saac.kr";
    private static String url = "http://saac.kr/?act=board&bbs_code=sub2_1&page=";
    private static int minYear = 2020;


    public void doCrawl() throws Exception {

        int maxCount = 100;
        int prevPage = 0;

        for(int i = 1; i < maxCount; i++) {
            String pageurl = url + i;

            Document doc = Jsoup.connect(pageurl)
                    .userAgent(agent)
                    .get();

            // 현재 페이지 확인
            int page = Integer.parseInt(doc.select(".board_pagetab").select("span a").html()
                    .replace("[ ", "").replace(" ]",""));

            log.info("current page : " + page);

            if(prevPage == page){
                log.info("DOC이 비어있습니다.");
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
            // log.info("===============================================================");


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

            // 입양 완료 체크
            String title = doc.select(".board-title h3").html();
            // log.info("title : " + title);

            if(title.contains("입양") || title.contains("완료")
                    || title.contains("품으로") || title.contains("치와와 와와") || title.contains("상주") ||  title.contains("동영상")){
               // log.info("입양 완료되었습니다......................");
                continue;
            }


            String date = doc.select(".board-info span:nth-last-child(3)").html();
            // 날짜 추가
            int year = Integer.parseInt(date.substring(0, 4));

            if(minYear >= year){
                return;
            }

            AnimalInfo info = new AnimalInfo();
            info.setBoardDate(date);


            doc.select(".board-content").forEach(e -> {

                Elements divs = e.select("div");
                int divsize = divs.size();


                for(int j = 2; j < divsize; ++j){
                    String temp = divs.get(j).html();
                    
                    log.info(j + " : " + temp);

                    if(temp.contains("이름")){
                        int index = temp.lastIndexOf("이름:");
                        if(-1 == index){
                            continue;
                        }
                        info.setName(temp.substring(index).replace(" ", "")  );
                    } else if(temp.contains("성별")){
                        int index = temp.lastIndexOf("성별:");
                        if(-1 == index){
                            continue;
                        }
                        info.setSex(temp.substring(index).replace(" ", ""));
                    } else if(temp.contains("나이")){
                        int index = temp.lastIndexOf("나이:");
                        if(-1 == index){
                            continue;
                        }
                        info.setAge(temp.substring(index).replace(" ", ""));
                    } else if(temp.contains("특징")){
                        int index = temp.lastIndexOf("특징:");
                        if(-1 == index){
                            continue;
                        }
                        info.setSpecial(temp.substring(index).replace(" ", ""));
                    }
                }

                animalList.add(info);
            });

        }
    }
}
