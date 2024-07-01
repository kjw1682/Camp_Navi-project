package com.demo.campingnavi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.demo.campingnavi.repository.jpa")
public class JpaConfig {
}
