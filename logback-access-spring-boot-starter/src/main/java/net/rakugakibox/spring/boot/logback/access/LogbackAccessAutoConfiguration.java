package net.rakugakibox.spring.boot.logback.access;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rakugakibox.spring.boot.logback.access.jetty.JettyLogbackAccessInstaller;
import net.rakugakibox.spring.boot.logback.access.tomcat.TomcatLogbackAccessInstaller;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The auto-configuration for Logback-access.
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnProperty(name = "logback.access.enabled", matchIfMissing = true)
@EnableConfigurationProperties(LogbackAccessProperties.class)
@Slf4j
public class LogbackAccessAutoConfiguration {

    /**
     * For Tomcat.
     */
    @Configuration
    @ConditionalOnBean(value = TomcatEmbeddedServletContainerFactory.class)
    @RequiredArgsConstructor
    public static class ForTomcat {

        /**
         * The configuration properties for Logback-access.
         */
        private final LogbackAccessProperties logbackAccessProperties;

        /**
         * The listeners for Logback-access.
         */
        private final Optional<List<LogbackAccessListener>> logbackAccessListeners;

        /**
         * Creates a Logback-access installer.
         *
         * @return a Logback-access installer.
         */
        @Bean
        @ConditionalOnMissingBean
        public TomcatLogbackAccessInstaller tomcatLogbackAccessInstaller() {
            TomcatLogbackAccessInstaller installer = new TomcatLogbackAccessInstaller(
                    logbackAccessProperties, logbackAccessListeners.orElseGet(Collections::emptyList));
            log.debug("Created a TomcatLogbackAccessInstaller: [{}]", installer);
            return installer;
        }

    }

    /**
     * For Jetty.
     */
    @Configuration
    @ConditionalOnBean(value = JettyEmbeddedServletContainerFactory.class)
    @RequiredArgsConstructor
    public static class ForJetty {

        /**
         * The configuration properties for Logback-access.
         */
        private final LogbackAccessProperties logbackAccessProperties;

        /**
         * The listeners for Logback-access.
         */
        private final Optional<List<LogbackAccessListener>> logbackAccessListeners;

        /**
         * Creates a Logback-access installer.
         *
         * @return a Logback-access installer.
         */
        @Bean
        @ConditionalOnMissingBean
        public JettyLogbackAccessInstaller jettyLogbackAccessInstaller() {
            JettyLogbackAccessInstaller installer = new JettyLogbackAccessInstaller(
                    logbackAccessProperties, logbackAccessListeners.orElseGet(Collections::emptyList));
            log.debug("Created a JettyLogbackAccessInstaller: [{}]", installer);
            return installer;
        }

    }

}
