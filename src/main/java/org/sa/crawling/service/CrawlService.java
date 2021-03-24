package org.sa.crawling.service;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CrawlService {

    public void doCrawl() throws Exception {
        String url = "https://www.ekara.org/kams/adopt?species=&sex=&weight=&birth=&activity=&page=6";
        String agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.190 Safari/537.36";
        String baseUrl = "https://www.ekara.org";
        Document doc = Jsoup.connect(url)
                .userAgent(agent)
                .get();

        // css selector로 얻어올 수 있다.
        // class="wt_viewer" 아래에 있는 img 태그들을 긁어오자
        Elements imageEles = doc.select(".card-img-top");
        Elements textEles = doc.select(".card-body");

        // loop를 돌아서 img들의 src를 가져오자.
        // 내부적으로 arrayList이다.
        int size = imageEles.size();

        List srcList = new ArrayList();

        for(int i = 0; i < size; ++i) {
            //=================== IMAGE ===================//
            // img 태그에서 src 추출하기
            String imgUrl = imageEles.get(i).attr("src");
            URL urlObj = new URL(baseUrl + imgUrl);

            // 보안을 위해 header에 userAgent를 추가한다.
            HttpURLConnection urlCon = (HttpURLConnection)urlObj.openConnection();
            urlCon.setRequestProperty("User-Agent", agent);

            // 이미지 저장하기 위해 inputStream 생성
            InputStream in = urlCon.getInputStream();

            // 파일 이름 뽑기
            String fileName = imgUrl.substring(imgUrl.lastIndexOf("/") + 1);

            // 이미지를 파일로 저장하기 위해
            FileOutputStream fos = new FileOutputStream("C:\\zzz\\" + fileName + ".jpg");

            byte[] buffer = new byte[1024 * 8];

            while(true) {
                int count = in.read(buffer);

                if(-1 == count) { break;}

                fos.write(buffer, 0, count);
            }

            fos.close();

            //=================== TEXT ===================//
            String temp = textEles.get(i).getElementsByTag("p").html();
            // System.out.println(temp);
            String[] test1 = temp.split("/");

            String[] split = test1[0].split("<br>");

            System.out.println(test1[1]);
            System.out.println(test1[2]);
            System.out.println(split[0]);
            System.out.println(split[1]);
            System.out.println(split[2]);
            System.out.println("==========================");
        }

        // code : 403 에러가 뜬다. 보안으로 막힌 경우이다.
        // 보안 뚫기 = userAgent


    }

}
