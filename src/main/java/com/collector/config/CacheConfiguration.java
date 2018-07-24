package com.collector.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(com.collector.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(com.collector.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(com.collector.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.collector.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.collector.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.collector.domain.CollectorUser.class.getName(), jcacheConfiguration);
            cm.createCache(com.collector.domain.Address.class.getName(), jcacheConfiguration);
            cm.createCache(com.collector.domain.Country.class.getName(), jcacheConfiguration);
            cm.createCache(com.collector.domain.State.class.getName(), jcacheConfiguration);
            cm.createCache(com.collector.domain.Publisher.class.getName(), jcacheConfiguration);
            cm.createCache(com.collector.domain.Licensor.class.getName(), jcacheConfiguration);
            cm.createCache(com.collector.domain.Category.class.getName(), jcacheConfiguration);
            cm.createCache(com.collector.domain.Genre.class.getName(), jcacheConfiguration);
            cm.createCache(com.collector.domain.Status.class.getName(), jcacheConfiguration);
            cm.createCache(com.collector.domain.Title.class.getName(), jcacheConfiguration);
            cm.createCache(com.collector.domain.Format.class.getName(), jcacheConfiguration);
            cm.createCache(com.collector.domain.Finishing.class.getName(), jcacheConfiguration);
            cm.createCache(com.collector.domain.Currency.class.getName(), jcacheConfiguration);
            cm.createCache(com.collector.domain.Action.class.getName(), jcacheConfiguration);
            cm.createCache(com.collector.domain.ContributionType.class.getName(), jcacheConfiguration);
            cm.createCache(com.collector.domain.Contribution.class.getName(), jcacheConfiguration);
            cm.createCache(com.collector.domain.Issue.class.getName(), jcacheConfiguration);
            cm.createCache(com.collector.domain.Issue.class.getName() + ".collaborators", jcacheConfiguration);
            cm.createCache(com.collector.domain.Issue.class.getName() + ".histories", jcacheConfiguration);
            cm.createCache(com.collector.domain.Issue.class.getName() + ".comments", jcacheConfiguration);
            cm.createCache(com.collector.domain.Comment.class.getName(), jcacheConfiguration);
            cm.createCache(com.collector.domain.Arc.class.getName(), jcacheConfiguration);
            cm.createCache(com.collector.domain.Team.class.getName(), jcacheConfiguration);
            cm.createCache(com.collector.domain.Personage.class.getName(), jcacheConfiguration);
            cm.createCache(com.collector.domain.History.class.getName(), jcacheConfiguration);
            cm.createCache(com.collector.domain.History.class.getName() + ".collaborators", jcacheConfiguration);
            cm.createCache(com.collector.domain.History.class.getName() + ".characters", jcacheConfiguration);
            cm.createCache(com.collector.domain.Artist.class.getName(), jcacheConfiguration);
            cm.createCache(com.collector.domain.Role.class.getName(), jcacheConfiguration);
            cm.createCache(com.collector.domain.Collaborator.class.getName(), jcacheConfiguration);
            cm.createCache(com.collector.domain.IssueStatus.class.getName(), jcacheConfiguration);
            cm.createCache(com.collector.domain.ReadingStatus.class.getName(), jcacheConfiguration);
            cm.createCache(com.collector.domain.IssueInCollection.class.getName(), jcacheConfiguration);
            cm.createCache(com.collector.domain.IssueInWishlist.class.getName(), jcacheConfiguration);
            cm.createCache(com.collector.domain.Wishlist.class.getName(), jcacheConfiguration);
            cm.createCache(com.collector.domain.Wishlist.class.getName() + ".issues", jcacheConfiguration);
            cm.createCache(com.collector.domain.Collection.class.getName(), jcacheConfiguration);
            cm.createCache(com.collector.domain.Collection.class.getName() + ".issues", jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
