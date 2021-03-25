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
public class IJoaCrawlService extends CrawlService{

    private static String baseUrl = "https://www.ijoa.co.kr";
    private static String url = "https://www.ijoa.co.kr/42?page=";
    private static String serviceName = "아이조아요양보호소";

    public void doCrawl() throws Exception {

        int type = 0;
        log.info(serviceName + "가 시작됩니다...");

        for(int i = 1; ; i++) {
            String listurl = url+ i;

            //url 검색조건 설정
            String[] keyword = {"강아지", "고양이"};
            listurl += "&keyword=" + keyword[type];

            //baseUrl
            Document doc = Jsoup.connect(listurl)
                    .userAgent(agent)
                    .get();

            //마지막 페이지 수집
            String lastPageItem = doc.select(".pagination li:nth-last-child(2) a").html();

            //a Tag 수집
            List<String> urlList = getATagList(".holder.blocked a", doc);


            //각 페이지의 게시물 정보 수집
            crawlView(urlList, baseUrl, type, "20210101");

            //마지막 페이지와 i가 같으면 종료
            if(type == 1 && lastPageItem.equals("" + i + "")) {
                log.info(serviceName+" 마지막 페이지입니다. -> " + i);
                return;

            } else if(lastPageItem.equals(""+i+"")){

                type = 1;
                i = 0;

            }//end if

        }//end for

    }

    public void crawlView(List<String> aURL, String baseUrl, int type, String conditionDate) throws Exception {

        String animalType = "";

        if(type == 0){
            animalType = "개";
        }else {
            animalType = "고양이";
        };

        int size = aURL.size();


        for(int i = 0; i < size; ++i) {

            AnimalInfoDTO animalInfoDTO = new AnimalInfoDTO();
            List<String> textList = new ArrayList<>();
            List<String> imgUrl = new ArrayList<>();

            //=================== IMAGE ===================//
            String tempUrl = baseUrl + aURL.get(i);

            Document doc = getDocument(tempUrl);

            //Head - meta Tag에 있는 등록 시간 가져오기
            String date = doc.select("meta[property='article:published_time']").attr("content");

            if(date.startsWith("2021")) {

                date = date.substring(0, 10);
                //log.info(date);
                // "-" -> "/" 로 변경
                date = date.replace("-", "/"); //가공된 등록날짜
                //log.info("Date: " + date);
                textList.add(date);

                // img 태그에서 src 추출하기
                // class="margin-top-xxl" 아래에 있는 img 태그들을 긁어오자
                Elements imageEles = doc.select(".margin-top-xxl img");
                Elements textEles = doc.select(".tableHover div"); //text div

                //log.info("textEles : " + textEles);

                //항목 값 list에 추가
                textEles.forEach(a -> {
                    if(a.select("span").isEmpty()) {
                        textList.add(a.html());
                    }

                });//end foreach

                //AnimalInfo에 값 추가
                animalInfoDTO.setServiceName(serviceName);

                animalInfoDTO.setType(animalType);
                animalInfoDTO.setDate(date);
                animalInfoDTO.setSpecies(textList.get(1));
                animalInfoDTO.setSex(textList.get(2));
                animalInfoDTO.setName(textList.get(3));
                animalInfoDTO.setIsVaccinated(textList.get(4));
                animalInfoDTO.setAge(textList.get(5));
                animalInfoDTO.setIsNeutralized(textList.get(6));

                String src = "";
                String imgType = "";
                String imgName = "";

                //이미지 url 추가
                for (Element imageEle:imageEles) {

                    src = imageEle.attr("src");
                    int idxType = src.lastIndexOf(".");
                    imgName = src.substring(0, idxType);

                    imgType = src.substring(idxType + 1);

                    //AnimalInfo에 값 추가
                    animalInfoDTO.setImageType(imgType);

                    imgUrl.add(src);
                }//end foreach

                //AnimalInfo에 값 추가
                animalInfoDTO.setImageUrlList(imgUrl);

                setAnimalCode(animalInfoDTO);
                animalList.add(animalInfoDTO);

            }//end if

        }//end for

        //log.info("textList: " + textList);
        //log.info("imgUrl: " + imgUrl);
        //log.info("animalInfoList: " + animalInfoList);


    }

}
