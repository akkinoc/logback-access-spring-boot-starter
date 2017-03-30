package net.rakugakibox.spring.boot.logback.access;

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
    public static class Tomcat {

        /**
         * The configuration properties for Logback-access.
         */
        private final LogbackAccessProperties logbackAccessProperties;

        /**
         * Creates a Logback-access installer.
         *
         * @return a Logback-access installer.
         */
        @Bean
        @ConditionalOnMissingBean
        public TomcatLogbackAccessInstaller tomcatLogbackAccessInstaller() {
            TomcatLogbackAccessInstaller installer = new TomcatLogbackAccessInstaller(logbackAccessProperties);
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
    public static class Jetty {

        /**
         * The configuration properties for Logback-access.
         */
        private final LogbackAccessProperties logbackAccessProperties;

        /**
         * Creates a Logback-access installer.
         *
         * @return a Logback-access installer.
         */
        @Bean
        @ConditionalOnMissingBean
        public JettyLogbackAccessInstaller jettyLogbackAccessInstaller() {
            JettyLogbackAccessInstaller installer = new JettyLogbackAccessInstaller(logbackAccessProperties);
            log.debug("Created a JettyLogbackAccessInstaller: [{}]", installer);
            return installer;
        }

    }

}
