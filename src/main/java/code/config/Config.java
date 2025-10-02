package code.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = "code")
@PropertySource("classpath:application.properties")
@Import({HibernateConfig.class, TransactionConfig.class})
@EnableTransactionManagement(proxyTargetClass = true)
public class Config { }
