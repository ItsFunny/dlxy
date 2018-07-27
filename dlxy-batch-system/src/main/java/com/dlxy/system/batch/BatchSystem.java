package com.dlxy.system.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
public class BatchSystem 
{
    public static void main( String[] args )
    {
    	new SpringApplicationBuilder(BatchSystem.class).run(args);
    }
}
