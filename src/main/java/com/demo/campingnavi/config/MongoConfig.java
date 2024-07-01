package com.demo.campingnavi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.demo.campingnavi.repository.mongo")
public class MongoConfig {
}
