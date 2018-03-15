package com.curve.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.Clock;

@Configuration
@EnableWebMvc
@ComponentScan({ "com.curve" })
public class SpringConfig {

    @Bean
    public DispatcherServlet dispatcherServlet () {
        DispatcherServlet ds = new DispatcherServlet();
        ds.setThrowExceptionIfNoHandlerFound(true);
        return ds;
    }

    @Bean
    public Clock clock(){
        return Clock.systemUTC();
    }


}
