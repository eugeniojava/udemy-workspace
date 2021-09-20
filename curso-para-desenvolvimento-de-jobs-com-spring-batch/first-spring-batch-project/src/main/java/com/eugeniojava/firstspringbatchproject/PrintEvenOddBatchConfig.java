package com.eugeniojava.firstspringbatchproject;

import java.util.Arrays;
import java.util.List;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.function.FunctionItemProcessor;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class PrintEvenOddBatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public PrintEvenOddBatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job printEvenOddJob() {
        return jobBuilderFactory.get("printEvenOddJob")
                .start(printEvenOddStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    private Step printEvenOddStep() {
        return stepBuilderFactory.get("printEvenOddStep")
                .<Integer, String>chunk(10)
                .reader(countToTenReader())
                .processor(evenOrOddProcessor())
                .writer(printWriter())
                .build();
    }

    private IteratorItemReader<Integer> countToTenReader() {
        List<Integer> numbersFromOneToTen = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        return new IteratorItemReader<>(numbersFromOneToTen.iterator());
    }

    private FunctionItemProcessor<Integer, String> evenOrOddProcessor() {
        return new FunctionItemProcessor<>(
                item -> item % 2 == 0 ? String.format("Item %s is even", item) : String.format("Item %s is odd", item)
        );
    }

    private ItemWriter<String> printWriter() {
        return items -> items.forEach(System.out::println);
    }
}
