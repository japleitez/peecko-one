package com.peecko.one.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.peecko.one.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.peecko.one.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.peecko.one.domain.User.class.getName());
            createCache(cm, com.peecko.one.domain.Authority.class.getName());
            createCache(cm, com.peecko.one.domain.User.class.getName() + ".authorities");
            createCache(cm, com.peecko.one.domain.ApsUser.class.getName());
            createCache(cm, com.peecko.one.domain.ApsUser.class.getName() + ".apsDevices");
            createCache(cm, com.peecko.one.domain.ApsUser.class.getName() + ".playLists");
            createCache(cm, com.peecko.one.domain.ApsDevice.class.getName());
            createCache(cm, com.peecko.one.domain.Agency.class.getName());
            createCache(cm, com.peecko.one.domain.Agency.class.getName() + ".customers");
            createCache(cm, com.peecko.one.domain.Agency.class.getName() + ".apsPricings");
            createCache(cm, com.peecko.one.domain.Customer.class.getName());
            createCache(cm, com.peecko.one.domain.Customer.class.getName() + ".contacts");
            createCache(cm, com.peecko.one.domain.Customer.class.getName() + ".apsPlans");
            createCache(cm, com.peecko.one.domain.Contact.class.getName());
            createCache(cm, com.peecko.one.domain.ApsPlan.class.getName());
            createCache(cm, com.peecko.one.domain.ApsPlan.class.getName() + ".apsOrders");
            createCache(cm, com.peecko.one.domain.ApsOrder.class.getName());
            createCache(cm, com.peecko.one.domain.ApsOrder.class.getName() + ".apsMemberships");
            createCache(cm, com.peecko.one.domain.ApsOrder.class.getName() + ".invoices");
            createCache(cm, com.peecko.one.domain.ApsMembership.class.getName());
            createCache(cm, com.peecko.one.domain.ApsPricing.class.getName());
            createCache(cm, com.peecko.one.domain.Video.class.getName());
            createCache(cm, com.peecko.one.domain.VideoCategory.class.getName());
            createCache(cm, com.peecko.one.domain.VideoCategory.class.getName() + ".videos");
            createCache(cm, com.peecko.one.domain.Article.class.getName());
            createCache(cm, com.peecko.one.domain.ArticleSeries.class.getName());
            createCache(cm, com.peecko.one.domain.ArticleCategory.class.getName());
            createCache(cm, com.peecko.one.domain.ArticleCategory.class.getName() + ".articles");
            createCache(cm, com.peecko.one.domain.Notification.class.getName());
            createCache(cm, com.peecko.one.domain.LabelTranslation.class.getName());
            createCache(cm, com.peecko.one.domain.PlayList.class.getName());
            createCache(cm, com.peecko.one.domain.PlayList.class.getName() + ".videoItems");
            createCache(cm, com.peecko.one.domain.VideoItem.class.getName());
            createCache(cm, com.peecko.one.domain.Coach.class.getName());
            createCache(cm, com.peecko.one.domain.Coach.class.getName() + ".videos");
            createCache(cm, com.peecko.one.domain.Coach.class.getName() + ".articles");
            createCache(cm, com.peecko.one.domain.Invoice.class.getName());
            createCache(cm, com.peecko.one.domain.Invoice.class.getName() + ".invoiceItems");
            createCache(cm, com.peecko.one.domain.InvoiceItem.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
