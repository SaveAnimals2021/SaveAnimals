package org.sa.batch.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.sa.batch.step.AnimalCrawlTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Log4j
@Configuration
@ComponentScan(basePackages = {"org.sa.batch.step"})
@EnableBatchProcessing
@RequiredArgsConstructor
public class AnimalJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job animalCrawlJob() {
        log.info("=================== animalCrawlJob ===================");
//        return jobBuilderFactory.get("animalCrawlJob") // batch job 생성
//                .start(animalCrawlStep())
//                .build();

        Tasklet tasklet =  new AnimalCrawlTasklet();

        return jobBuilderFactory.get("animalCrawlJob")

                .start(animalCrawlStep()).build();
    }

    @Bean
    public Step animalCrawlStep() {
        log.info("=================== animalCrawlStep ===================");
//        return stepBuilderFactory.get("animalCrawlStep") // batch step 생성
//                .tasklet((contribution, chunkContext) -> {
//                    // step에서 수행될 기능을 설정
//                    // tasklet은 step 안에서 단일로 수행될 커스텀한 기능들을 선언할 때 사용된다.
//                    log.info("ANIMAL CRAWL STEP STARTED......");
//                    return RepeatStatus.FINISHED;
//                })
//                .build();

        return stepBuilderFactory.get("animalCrawlStep").tasklet(new AnimalCrawlTasklet()).build();
    }


//    @Bean
//    public Job animalCrawlJob(JobBuilderFactory jobBuilderFactory, Step animalCrawlJobStep){
//        return jobBuilderFactory.get("animalCrawlJob")
//                .preventRestart()
//                .start(animalCrawlJobStep)
//                .build();
//    }
//
//    @Bean
//    public Step animalCrawlJopStep(StepBuilderFactory stepBuilderFactory){
//        return stepBuilderFactory.get("animalCrawlJobStep")
//                .<AnimalInfoDTO, AnimalInfoDTO> chunk(10)
//                .processor(animalCrawlProcessor())
//                .writer(animalCrawlWriter())
//                .build();
//    }


}
