package org.apereo.cas.config;

import org.apereo.cas.CipherExecutor;
import org.apereo.cas.adaptors.yubikey.YubiKeyAccountRegistry;
import org.apereo.cas.adaptors.yubikey.YubiKeyAccountValidator;
import org.apereo.cas.adaptors.yubikey.dao.MongoDbYubiKeyAccountRegistry;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.mongo.MongoDbConnectionFactory;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * This is {@link MongoDbYubiKeyConfiguration}.
 *
 * @author Misagh Moayyed
 * @since 5.2.0
 */
@Configuration("mongoDbYubiKeyConfiguration")
@EnableConfigurationProperties(CasConfigurationProperties.class)
@Slf4j
public class MongoDbYubiKeyConfiguration {

    @Autowired
    @Qualifier("yubiKeyAccountValidator")
    private YubiKeyAccountValidator yubiKeyAccountValidator;

    @Autowired
    private CasConfigurationProperties casProperties;

    @Autowired
    @Qualifier("yubikeyAccountCipherExecutor")
    private CipherExecutor yubikeyAccountCipherExecutor;

    @RefreshScope
    @Bean
    public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @RefreshScope
    @Bean
    public MongoTemplate mongoYubiKeyTemplate() {
        val mongo = casProperties.getAuthn().getMfa().getYubikey().getMongo();
        val factory = new MongoDbConnectionFactory();
        val mongoTemplate = factory.buildMongoTemplate(mongo);
        factory.createCollection(mongoTemplate, mongo.getCollection(), mongo.isDropCollection());
        return mongoTemplate;
    }

    @RefreshScope
    @Bean
    public YubiKeyAccountRegistry yubiKeyAccountRegistry() {
        val yubi = casProperties.getAuthn().getMfa().getYubikey();
        val registry = new MongoDbYubiKeyAccountRegistry(yubiKeyAccountValidator,
            mongoYubiKeyTemplate(),
            yubi.getMongo().getCollection());
        registry.setCipherExecutor(this.yubikeyAccountCipherExecutor);
        return registry;
    }
}
