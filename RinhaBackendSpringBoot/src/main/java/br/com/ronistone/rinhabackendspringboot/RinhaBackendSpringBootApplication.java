package br.com.ronistone.rinhabackendspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.ssl.SslAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.support.DatabaseStartupValidator;

import javax.sql.DataSource;

@SpringBootApplication(exclude = {
        MultipartAutoConfiguration.class, SslAutoConfiguration.class, CacheAutoConfiguration.class,
        TaskExecutionAutoConfiguration.class, TaskSchedulingAutoConfiguration.class, WebSocketServletAutoConfiguration.class,
        AopAutoConfiguration.class, JmxAutoConfiguration.class, ErrorMvcAutoConfiguration.class,
        ErrorMvcAutoConfiguration.class})
public class RinhaBackendSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(RinhaBackendSpringBootApplication.class, args);
    }

    @Bean
    public DatabaseStartupValidator databaseStartupValidator(DataSource dataSource) {
        DatabaseStartupValidator databaseStartupValidator = new DatabaseStartupValidator();
        databaseStartupValidator.setDataSource(dataSource);
        databaseStartupValidator.setValidationQuery("SELECT 1");
        return databaseStartupValidator;
    }

}
