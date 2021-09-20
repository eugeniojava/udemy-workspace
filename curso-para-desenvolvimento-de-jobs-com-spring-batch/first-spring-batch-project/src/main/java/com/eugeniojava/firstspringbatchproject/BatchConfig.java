package com.eugeniojava.firstspringbatchproject;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

//@Configuration
//@EnableBatchProcessing
public class BatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public BatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job printWelcomeMessageJob() {
        return jobBuilderFactory.get("printWelcomeMessageJob")
                .start(printWelcomeMessageStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    private Step printWelcomeMessageStep() {
        return stepBuilderFactory.get("printWelcomeMessageStep")
                .tasklet(printWelcomeMessageTasklet(null))
                .build();
    }

    @Bean
    @StepScope
    public Tasklet printWelcomeMessageTasklet(@Value("#{jobParameters['name']}") String name) {
        return (stepContribution, chunkContext) -> {
            System.out.printf("Hello, %s!%n", name);
            return RepeatStatus.FINISHED;
        };
    }
}
