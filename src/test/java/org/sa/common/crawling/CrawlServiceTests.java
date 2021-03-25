package org.sa.common.crawling;

import lombok.extern.log4j.Log4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sa.animal.config.AnimalInfoConfig;
import org.sa.animal.service.AnimalService;
import org.sa.common.config.CommonConfig;
import org.sa.common.crawling.service.IJoaCrawlService;
import org.sa.common.crawling.service.KaraCrawlService;
import org.sa.common.crawling.service.KarmaCrawlService;
import org.sa.common.crawling.service.SaacCrawlService;
import org.sa.common.dao.AnimalInfoDAO;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.beans.factory.annotation.Autowired;

@Log4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={CommonConfig.class, AnimalInfoConfig.class})
public class CrawlServiceTests {

    KaraCrawlService karaservice;
    SaacCrawlService saacCrawlService;
    KarmaCrawlService karmaCrawlService;

    IJoaCrawlService iJoaCrawlService;

    AnimalInfoDAO dao;

    @Autowired
    AnimalService service;

    @Before
    public void setup(){
        dao = new AnimalInfoDAO();
        karaservice = new KaraCrawlService();
        saacCrawlService = new SaacCrawlService();
        karmaCrawlService = new KarmaCrawlService();

        iJoaCrawlService = new IJoaCrawlService();
    }

    @Test
    public void testInsertAll(){
        try {
            karaservice.doCrawl();
            saacCrawlService.doCrawl();
            karmaCrawlService.doCrawl();
            iJoaCrawlService.doCrawl();

            karaservice.getAnimalList().forEach(info-> dao.addAnimal(info));
            saacCrawlService.getAnimalList().forEach(info-> dao.addAnimal(info));
            karmaCrawlService.getAnimalList().forEach(info-> dao.addAnimal(info));
            iJoaCrawlService.getAnimalList().forEach(info-> dao.addAnimal(info));

            dao.getList().forEach(info-> service.register(info));
        }  catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testAll(){
        try {
            karaservice.doCrawl();
            saacCrawlService.doCrawl();
            karmaCrawlService.doCrawl();
            iJoaCrawlService.doCrawl();

            karaservice.getAnimalList().forEach(info-> dao.addAnimal(info));
            saacCrawlService.getAnimalList().forEach(info-> dao.addAnimal(info));
            karmaCrawlService.getAnimalList().forEach(info-> dao.addAnimal(info));
            iJoaCrawlService.getAnimalList().forEach(info-> dao.addAnimal(info));

            dao.getList().forEach(info-> log.info(info));
        }  catch (Exception e){
            e.printStackTrace();
        }
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
    public void testKarma(){
        try {
            karmaCrawlService.doCrawl();
            karmaCrawlService.getAnimalList().forEach(info-> log.info(info));
        }  catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testJoa(){
        try {
            iJoaCrawlService.doCrawl();
        }  catch (Exception e){
            e.printStackTrace();
        }
    }

}
