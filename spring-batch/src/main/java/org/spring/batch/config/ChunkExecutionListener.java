package org.spring.batch.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

@Slf4j
public class ChunkExecutionListener implements ChunkListener {
    @Override
    public void beforeChunk(ChunkContext chunkContext) {
        log.info("Chunk execution read count: " + chunkContext.getStepContext().getStepExecution().getReadCount());
    }
    @Override
    public void afterChunk(ChunkContext chunkContext) {
        log.info("Chunk execution write count: " + chunkContext.getStepContext().getStepExecution().getWriteCount());
    }
}
