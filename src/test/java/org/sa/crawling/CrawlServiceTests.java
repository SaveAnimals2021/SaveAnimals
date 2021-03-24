package org.sa.crawling;

import lombok.extern.log4j.Log4j;
import org.junit.Before;
import org.junit.Test;
import org.sa.crawling.service.SaacCrawlService;
import org.sa.crawling.service.KaraCrawlService;
import org.sa.crawling.service.TestCrawlClass;
import org.sa.crawling.service.UloveCrawlService;


@Log4j
public class CrawlServiceTests {

    KaraCrawlService karaservice;
    SaacCrawlService saacCrawlService;
    UloveCrawlService uloveCrawlService;
    TestCrawlClass tcc;

    @Before
    public void setup(){
        karaservice = new KaraCrawlService();
        saacCrawlService = new SaacCrawlService();
        uloveCrawlService = new UloveCrawlService();
        tcc = new TestCrawlClass();
    }

    @Test
    public void testKara(){
        try {
            karaservice.doCrawl();
            karaservice.getAnimalList().forEach(info-> log.info(info));
        }  catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testSaac(){
        try {
            saacCrawlService.doCrawl();
            log.info("size : " +saacCrawlService.getAnimalList().size());
            saacCrawlService.getAnimalList().forEach(info-> log.info(info));
        }  catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testUlove(){
        try {
            uloveCrawlService.doCrawl();


            uloveCrawlService.getAnimalList().forEach(info-> log.info(info));

        }  catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testSeoul(){
        try {
            tcc.doCrawl();
        }  catch (Exception e){
            e.printStackTrace();
        }
    }


}
