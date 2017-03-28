package net.rakugakibox.spring.boot.logback.access;

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
     * Creates a configurer of Logback-access.
     *
     * @param logbackAccessProperties the configuration properties for Logback-access.
     * @return a configurer of Logback-access.
     */
    @Bean
    @ConditionalOnMissingBean
    public LogbackAccessConfigurer logbackAccessConfigurer(LogbackAccessProperties logbackAccessProperties) {
        LogbackAccessConfigurer configurer = new LogbackAccessConfigurer(logbackAccessProperties);
        log.debug("Created a LogbackAccessConfigurer: [{}]", configurer);
        return configurer;
    }

    /**
     * for Tomcat.
     */
    @Configuration
    @ConditionalOnBean(value = TomcatEmbeddedServletContainerFactory.class)
    public static class Tomcat {

        /**
         * Creates a Logback-access installer.
         *
         * @param logbackAccessProperties the configuration properties for Logback-access.
         * @param logbackAccessConfigurer the configurer of Logback-access.
         * @return a Logback-access installer.
         */
        @Bean
        @ConditionalOnMissingBean
        public TomcatLogbackAccessInstaller tomcatLogbackAccessInstaller(
                LogbackAccessProperties logbackAccessProperties, LogbackAccessConfigurer logbackAccessConfigurer) {
            TomcatLogbackAccessInstaller installer =
                    new TomcatLogbackAccessInstaller(logbackAccessProperties, logbackAccessConfigurer);
            log.debug("Created a TomcatLogbackAccessInstaller: [{}]", installer);
            return installer;
        }

    }

    /**
     * for Jetty.
     */
    @Configuration
    @ConditionalOnBean(value = JettyEmbeddedServletContainerFactory.class)
    public static class Jetty {

        /**
         * Creates a Logback-access installer.
         *
         * @param logbackAccessProperties the configuration properties for Logback-access.
         * @param logbackAccessConfigurer the configurer of Logback-access.
         * @return a Logback-access installer.
         */
        @Bean
        @ConditionalOnMissingBean
        public JettyLogbackAccessInstaller jettyLogbackAccessInstaller(
                LogbackAccessProperties logbackAccessProperties, LogbackAccessConfigurer logbackAccessConfigurer) {
            JettyLogbackAccessInstaller installer =
                    new JettyLogbackAccessInstaller(logbackAccessProperties, logbackAccessConfigurer);
            log.debug("Created a JettyLogbackAccessInstaller: [{}]", installer);
            return installer;
        }

    }

}
