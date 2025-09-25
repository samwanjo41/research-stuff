package com.samwanjo41.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.InputStream;
import java.security.KeyStore;


@Configuration
@RequiredArgsConstructor
public class MongoConfig {


    @Value("${mongo.ssl.key-store}")
    private String keyStorePath;

    @Value("${mongo.ssl.key-store-password}")
    private String keyStorePassword;

    @Value("${mongo.ssl.trust-store}")
    private String trustStorePath;

    @Value("${mongo.ssl.trust-store-password}")
    private String trustStorePassword;

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Bean
    public MongoClient mongoClient() throws Exception {
        // Load KeyStore
        KeyStore keyStore = KeyStore.getInstance("JKS");
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(keyStore, keyStorePassword.toCharArray());

        // Load TrustStore
        KeyStore trustStore = KeyStore.getInstance("JKS");
        try (InputStream tsStream = new ClassPathResource(trustStorePath).getInputStream()) {
            trustStore.load(tsStream, trustStorePassword.toCharArray());
        }

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(trustStore);

        // Create SSLContext
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new com.mongodb.ConnectionString(mongoUri))
                .applyToSslSettings(builder -> builder.enabled(true).context(sslContext))
                .build();

        return MongoClients.create(settings);
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoClient(), "your_db");
    }
}

