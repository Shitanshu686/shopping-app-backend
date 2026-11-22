package com.shitanshu.shopping.config.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
    basePackages = {
        "com.shitanshu.shopping.repository",
        "com.shitanshu.shopping.audit.repository"
    },
    entityManagerFactoryRef = "primaryEntityManagerFactory",
    transactionManagerRef = "primaryTransactionManager"
)
public class PrimaryDataSourceConfig {

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Primary
    @Bean
    public DataSource primaryDataSource() {
        HikariDataSource dataSource = new HikariDataSource();

        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setJdbcUrl(
            "jdbc:mysql://host.docker.internal:3306/shopping_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
        );
        dataSource.setUsername("root");
        dataSource.setPassword(dbPassword);

        dataSource.setPoolName("ShopEaseHikariCP");
        dataSource.setMaximumPoolSize(20);
        dataSource.setMinimumIdle(5);
        dataSource.setConnectionTimeout(20000);
        dataSource.setIdleTimeout(300000);
        dataSource.setMaxLifetime(1200000);

        return dataSource;
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em =
            new LocalContainerEntityManagerFactoryBean();

        em.setDataSource(primaryDataSource());
        em.setPackagesToScan(
            "com.shitanshu.shopping.model",
            "com.shitanshu.shopping.entity",
            "com.shitanshu.shopping.audit.model"
        );

        HibernateJpaVendorAdapter vendorAdapter =
            new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.hbm2ddl.auto", "none");
        props.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        em.setJpaPropertyMap(props);

        return em;
    }

    @Primary
    @Bean
    public PlatformTransactionManager primaryTransactionManager(
            EntityManagerFactory primaryEntityManagerFactory) {
        return new JpaTransactionManager(primaryEntityManagerFactory);
    }
}
