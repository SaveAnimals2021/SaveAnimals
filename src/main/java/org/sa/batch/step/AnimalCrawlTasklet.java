package org.sa.batch.step;

import lombok.extern.log4j.Log4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;


@Log4j
@Component
public class AnimalCrawlTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("작업이 시작되었습니다.....................");
        log.info("Hello, AnimalCrawl!");
        log.info("작업이 완료되었습니다.....................");
        return null;
    }
}
