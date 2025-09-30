package code.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = "code")
@PropertySource("classpath:application.properties")
@Import({HibernateConfig.class, TransactionConfig.class})
public class Config { }
