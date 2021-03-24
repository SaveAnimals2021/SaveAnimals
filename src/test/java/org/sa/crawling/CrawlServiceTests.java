package org.sa.crawling;

import org.junit.Before;
import org.junit.Test;
import org.sa.crawling.service.CrawlService;

public class CrawlServiceTests {

    CrawlService service;

    @Before
    public void setup(){
        service = new CrawlService();
    }

    @Test
    public void testCrawl(){
        try {
            service.doCrawl();
        }  catch (Exception e){
            e.printStackTrace();
        }
    }

}
