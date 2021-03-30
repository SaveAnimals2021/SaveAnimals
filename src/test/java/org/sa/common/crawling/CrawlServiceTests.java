package org.sa.common.crawling;

import lombok.extern.log4j.Log4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sa.animal.config.AnimalInfoConfig;
import org.sa.animal.dto.MissingAnimalDTO;
import org.sa.animal.service.AnimalService;
import org.sa.common.config.CommonConfig;
import org.sa.common.crawling.service.*;
import org.sa.common.dao.AnimalInfoDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Log4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CommonConfig.class, AnimalInfoConfig.class})
public class CrawlServiceTests {

    KaraCrawlService karaservice;
    SaacCrawlService saacCrawlService;
    KarmaCrawlService karmaCrawlService;
    IJoaCrawlService iJoaCrawlService;

    AngelCrawlService angelCrawlService;
    APMSCrawlService apmsCrawlService;

    AnimalInfoDAO dao;

    @Autowired
    AnimalService service;

    @Before
    public void setup() {
        dao = new AnimalInfoDAO();
        angelCrawlService = new AngelCrawlService();
        apmsCrawlService = new APMSCrawlService();

    }

    @Test
    public void testAPMS() {
        try {
            apmsCrawlService.doCrawl();
            log.info("CRAWL OVER");
            apmsCrawlService.getAnimalList().forEach(info ->log.info(info));
            log.info("size : " + apmsCrawlService.getAnimalList().size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAngel() {
        try {
            angelCrawlService.doCrawl();
            log.info("CRAWL OVER");
            angelCrawlService.getAnimalList().forEach(info ->log.info(info));
            log.info("size : " + angelCrawlService.getAnimalList().size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAll() {
        try {
            angelCrawlService.doCrawl();
            // apmsCrawlService.doCrawl();

//            apmsCrawlService.getAnimalList().forEach(info -> {
//                log.info("Missing : " + info.getMissingDate());
//                log.info("Reg : " + info.getRegDate());
//                log.info("Rescue : " + info.getRescueDate());
//            });
//            log.info("===================================");

            angelCrawlService.getAnimalList().forEach(info -> {
                log.info("Missing : " + info.getMissingDate());
                log.info("Reg : " + info.getRegDate());
                log.info("Rescue : " + info.getRescueDate());
            });
            log.info("===================================");

            log.info("size : " + angelCrawlService.getAnimalList().size() + apmsCrawlService.getAnimalList().size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInsert(){
        MissingAnimalDTO animalDTO = new MissingAnimalDTO();

        animalDTO.setAnimalCode("vo.getAnimalCode()");
        animalDTO.setType("vo.getType()");
        animalDTO.setServiceName("vo.getServiceName()");
        animalDTO.setName("vo.getName()");
        animalDTO.setSpecies("vo.getSpecies()");
        animalDTO.setSex("vo.getSex()");
        animalDTO.setAge("vo.getAge()");
        animalDTO.setSpecial("vo.getSpecial()");
        animalDTO.setColor("vo.getColor()");


        animalDTO.setMissingLocation("vo.getMissingLocation()");

        animalDTO.setOriginURL("vo.getOriginURL()");

        animalDTO.setSituation("vo.getSituation()");
        animalDTO.setRescueStatus(1);
        animalDTO.setGuardianName("vo.getGuardianName()");
        animalDTO.setPhoneNumber("vo.getPhoneNumber()");

        animalDTO.setRescueLocation("vo.getRescueLocation()");

        service.register(animalDTO);

    }

    @Test
    public void testInsertAll() {
        try {
            // angelCrawlService.doCrawl();
            apmsCrawlService.doCrawl();

            // angelCrawlService.getAnimalList().forEach(info -> service.register(info));
            apmsCrawlService.getAnimalList().forEach(info -> service.register(info));
            log.info("========================= INSERT DONE ===============================");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//

//
//    @Test
//    public void testKara() {
//        try {
//            karaservice.doCrawl();
//            karaservice.getAnimalList().forEach(info -> log.info(info));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testSaac() {
//        try {
//            saacCrawlService.doCrawl();
//            log.info("size : " + saacCrawlService.getAnimalList().size());
//            saacCrawlService.getAnimalList().forEach(info -> log.info(info));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testKarma() {
//        try {
//            karmaCrawlService.doCrawl();
//            karmaCrawlService.getAnimalList().forEach(info -> log.info(info));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testJoa() {
//        try {
//            iJoaCrawlService.doCrawl();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
