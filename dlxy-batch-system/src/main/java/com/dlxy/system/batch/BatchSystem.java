package com.dlxy.system.batch;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class BatchSystem 
{
    public static void main( String[] args )
    {
    	new SpringApplicationBuilder(BatchSystem.class).run(args);
    }
}
