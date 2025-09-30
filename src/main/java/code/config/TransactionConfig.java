package code.config;


import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class TransactionConfig {

    @Bean
    public PlatformTransactionManager transactionManager(SessionFactory session) {
        return new HibernateTransactionManager(session);
    }
}
