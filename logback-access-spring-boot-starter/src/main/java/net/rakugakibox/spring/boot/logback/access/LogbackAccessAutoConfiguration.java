package net.rakugakibox.spring.boot.logback.access;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rakugakibox.spring.boot.logback.access.jetty.JettyLogbackAccessInstaller;
import net.rakugakibox.spring.boot.logback.access.tomcat.TomcatLogbackAccessInstaller;
import net.rakugakibox.spring.boot.logback.access.undertow.UndertowLogbackAccessInstaller;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * The auto-configuration for Logback-access.
 */
@Slf4j
@ConditionalOnProperty(name = "logback.access.enabled", matchIfMissing = true)
@ConditionalOnWebApplication
@EnableConfigurationProperties(LogbackAccessProperties.class)
@Configuration
public class LogbackAccessAutoConfiguration {

    /**
     * For Tomcat.
     */
    @RequiredArgsConstructor
    @ConditionalOnBean(value = TomcatServletWebServerFactory.class)
    @Configuration
    public static class ForTomcat {

        /**
         * The configuration properties for Logback-access.
         */
        private final LogbackAccessProperties logbackAccessProperties;

        /**
         * The environment.
         */
        private final Environment environment;

        /**
         * The application event publisher.
         */
        private final ApplicationEventPublisher applicationEventPublisher;

        /**
         * Creates a Logback-access installer.
         *
         * @return a Logback-access installer.
         */
        @ConditionalOnMissingBean
        @Bean
        public TomcatLogbackAccessInstaller tomcatLogbackAccessInstaller() {
            TomcatLogbackAccessInstaller installer = new TomcatLogbackAccessInstaller(
                    logbackAccessProperties, environment, applicationEventPublisher);
            log.debug("Created a TomcatLogbackAccessInstaller: [{}]", installer);
            return installer;
        }

    }

    /**
     * For Jetty.
     */
    @RequiredArgsConstructor
    @ConditionalOnBean(value = JettyServletWebServerFactory.class)
    @Configuration
    public static class ForJetty {

        /**
         * The configuration properties for Logback-access.
         */
        private final LogbackAccessProperties logbackAccessProperties;

        /**
         * The environment.
         */
        private final Environment environment;

        /**
         * The application event publisher.
         */
        private final ApplicationEventPublisher applicationEventPublisher;

        /**
         * Creates a Logback-access installer.
         *
         * @return a Logback-access installer.
         */
        @ConditionalOnMissingBean
        @Bean
        public JettyLogbackAccessInstaller jettyLogbackAccessInstaller() {
            JettyLogbackAccessInstaller installer = new JettyLogbackAccessInstaller(
                    logbackAccessProperties, environment, applicationEventPublisher);
            log.debug("Created a JettyLogbackAccessInstaller: [{}]", installer);
            return installer;
        }

    }

    /**
     * For Undertow.
     */
    @RequiredArgsConstructor
    @ConditionalOnBean(value = UndertowServletWebServerFactory.class)
    @Configuration
    public static class ForUndertow {

        /**
         * The configuration properties for Logback-access.
         */
        private final LogbackAccessProperties logbackAccessProperties;

        /**
         * The environment.
         */
        private final Environment environment;

        /**
         * The application event publisher.
         */
        private final ApplicationEventPublisher applicationEventPublisher;

        /**
         * Creates a Logback-access installer.
         *
         * @return a Logback-access installer.
         */
        @ConditionalOnMissingBean
        @Bean
        public UndertowLogbackAccessInstaller undertowLogbackAccessInstaller() {
            UndertowLogbackAccessInstaller installer = new UndertowLogbackAccessInstaller(
                    logbackAccessProperties, environment, applicationEventPublisher);
            log.debug("Created a UndertowLogbackAccessInstaller: [{}]", installer);
            return installer;
        }

    }

    /**
     * For Spring Security.
     */
    @RequiredArgsConstructor
    @ConditionalOnClass(AbstractSecurityWebApplicationInitializer.class)
    @Configuration
    public static class ForSpringSecurity {

        /**
         * Creates a filter that saves Spring Security attributes for Logback-access.
         *
         * @return a filter that saves Spring Security attributes for Logback-access.
         */
        @ConditionalOnMissingBean(name = "logbackAccessSecurityAttributesSaveFilter")
        @Bean("logbackAccessSecurityAttributesSaveFilter")
        public FilterRegistrationBean logbackAccessSecurityAttributesSaveFilter() {
            LogbackAccessSecurityAttributesSaveFilter filter = new LogbackAccessSecurityAttributesSaveFilter();
            FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(filter);
            log.debug("Created a LogbackAccessSecurityAttributesSaveFilter: [{}]", filter);
            return filterRegistrationBean;
        }

    }

}
