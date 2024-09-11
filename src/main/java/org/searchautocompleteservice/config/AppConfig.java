package org.searchautocompleteservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.searchautocompleteservice.config.Constant.TOP_K;

@Configuration
public class AppConfig {

    @Bean
    public TrieConfig getTrieConfig() {
        return new TrieConfig(TOP_K);
    }
}
