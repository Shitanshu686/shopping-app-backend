package com.shitanshu.shopping.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.shitanshu.shopping.repository.mongo")
public class MongoConfig {
}
