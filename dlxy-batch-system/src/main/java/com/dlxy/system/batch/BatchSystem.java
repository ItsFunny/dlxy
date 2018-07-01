package com.dlxy.system.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableTransactionManagement
public class BatchSystem 
{
    public static void main( String[] args )
    {
    	SpringApplication.run(BatchSystem.class, args);
    }
}
