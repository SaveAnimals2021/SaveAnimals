package org.sa.common.crawling.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.sa.animal.dto.MissingAnimalDTO;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class CrawlService {
    protected final static String agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.190 Safari/537.36";


    abstract void doCrawl() throws Exception;

    protected List<MissingAnimalDTO> animalList;

    public CrawlService(){
        animalList = new ArrayList<>();
    }

    public List<MissingAnimalDTO> getAnimalList() {
        return animalList;
    }



    protected int getLastPage(String className, Document doc){
        String lastPage = doc.select(className).html();

        // 못찾았다면 -1 반환
        if(lastPage.isEmpty()){
            return -1;
        }

        return Integer.parseInt(lastPage);
    }

    protected List<String> getATagList(String className, Document doc){
        List<String> hrefList = new ArrayList<>();

        doc.select(className).forEach(a -> {
            hrefList.add(a.attr("href"));
        });

        return hrefList;
    }

    protected void saveImages(String imageURL) throws Exception{
        URL urlObj = new URL(imageURL);

        // 보안을 위해 header에 userAgent를 추가한다.
        HttpURLConnection urlCon = (HttpURLConnection)urlObj.openConnection();
        urlCon.setRequestProperty("User-Agent", agent);

        // 이미지 저장하기 위해 inputStream 생성
        InputStream in = urlCon.getInputStream();

        // 파일 이름 뽑기
        String fileName = imageURL.substring(imageURL.lastIndexOf("/") + 1);

        // 이미지를 파일로 저장하기 위해
        FileOutputStream fos = new FileOutputStream("C:\\zzz\\" + fileName + ".jpg");

        byte[] buffer = new byte[1024 * 8];

        while(true) {
            int count = in.read(buffer);

            if(-1 == count) { break; }

            fos.write(buffer, 0, count);
        }

        fos.close();
    }

    protected Document getDocument(String url) throws Exception{

        Document doc = Jsoup.connect(url)
                .userAgent(agent)
                .get();

        URL urlObj = new URL(url);

        // 보안을 위해 header에 userAgent를 추가한다.
        HttpURLConnection urlCon = (HttpURLConnection)urlObj.openConnection();
        urlCon.setRequestProperty("User-Agent", agent);

        return doc;
    }

    protected static void setAnimalCode(MissingAnimalDTO info){
        String code = info.getName() + info.getSex() + info.getSpecies() + info.getMissingDate();
        info.setAnimalCode(code);
    }


}
