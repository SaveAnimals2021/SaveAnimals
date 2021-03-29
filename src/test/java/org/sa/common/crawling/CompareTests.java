package org.sa.common.crawling;

import lombok.extern.log4j.Log4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sa.animal.config.AnimalInfoConfig;
import org.sa.animal.dto.AnimalInfoDTO;
import org.sa.animal.mapper.AnimalInfoMapper;
import org.sa.animal.service.AnimalService;
import org.sa.common.config.CommonConfig;
import org.sa.common.crawling.service.IJoaCrawlService;
import org.sa.common.crawling.service.KaraCrawlService;
import org.sa.common.crawling.service.KarmaCrawlService;
import org.sa.common.crawling.service.SaacCrawlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@Log4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={CommonConfig.class, AnimalInfoConfig.class})
public class CompareTests {

    KaraCrawlService karaservice;
    SaacCrawlService saacCrawlService;
    KarmaCrawlService karmaCrawlService;

    IJoaCrawlService iJoaCrawlService;

    @Autowired
    AnimalInfoMapper mapper;

    @Autowired
    AnimalService service;

    @Before
    public void setup(){
        karaservice = new KaraCrawlService();
        saacCrawlService = new SaacCrawlService();
        karmaCrawlService = new KarmaCrawlService();

        iJoaCrawlService = new IJoaCrawlService();
    }

    @Test
    public void testCompare() throws Exception{
        // CRAWLING LIST
        List<AnimalInfoDTO> crawledList = new ArrayList<>();

        karaservice.doCrawl();
        saacCrawlService.doCrawl();
        karmaCrawlService.doCrawl();
        iJoaCrawlService.doCrawl();

        karaservice.getAnimalList().forEach(info-> crawledList.add(info));
        saacCrawlService.getAnimalList().forEach(info-> crawledList.add(info));
        karmaCrawlService.getAnimalList().forEach(info-> crawledList.add(info));
        iJoaCrawlService.getAnimalList().forEach(info-> crawledList.add(info));

        log.info("====================== CRAWL OVER ======================");

        // DB LIST
        List<AnimalInfoDTO> dbList =  service.getAllList();


        for(int i = 0; i < crawledList.size(); ++i){
            AnimalInfoDTO cdto = crawledList.get(i);
            AnimalInfoDTO temp = null;

            for(int j = 0; j < dbList.size(); ++j){
                AnimalInfoDTO ddto = dbList.get(j);

                if( cdto.getAnimalCode().equals(ddto.getAnimalCode()) ){
                    // 중복인 상황
                    crawledList.remove(i);
                    dbList.remove(j);
                    --j;
                    --i;
                    break;
                }
            }
            // 끝나서 왔는지... => 중복이 없는 상황
        // for i ends
        }

        int csize = crawledList.size();

        for(int i = 0; i < csize; ++i){
            // 2번상황 = insert한다.
            service.register(crawledList.get(i));
        }


        int dsize = dbList.size();

        for(int i = 0; i < dsize; ++i){
            // 1번상황 = modify해서 상태를 isAdopted = true; 로 바꾼다.
            dbList.get(i).setIsAdopted(true);
            service.setIsAdopted(dbList.get(i));
        }

        log.info("crawledList : " + crawledList);
        log.info("dbList : " + dbList);
    }


}
