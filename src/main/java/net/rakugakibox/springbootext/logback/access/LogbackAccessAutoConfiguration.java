package net.rakugakibox.springbootext.logback.access;

import lombok.extern.slf4j.Slf4j;
import net.rakugakibox.springbootext.logback.access.jetty.LogbackAccessJettyCustomizer;
import net.rakugakibox.springbootext.logback.access.tomcat.LogbackAccessTomcatCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The auto-configuration.
 */
@Configuration
@EnableConfigurationProperties
@ConditionalOnWebApplication
@Slf4j
public class LogbackAccessAutoConfiguration {

    /**
     * Creates a configuration properties.
     *
     * @return a configuration properties.
     */
    @Bean
    @ConditionalOnMissingBean
    public LogbackAccessProperties logbackAccessProperties() {
        LogbackAccessProperties properties = new LogbackAccessProperties();
        log.debug("Created a LogbackAccessProperties: [{}]", properties);
        return properties;
    }

    /**
     * Creates a configurator.
     *
     * @return a configurator.
     */
    @Bean
    @ConditionalOnMissingBean
    public LogbackAccessConfigurator logbackAccessConfigurator() {
        LogbackAccessConfigurator configurator = new LogbackAccessConfigurator();
        log.debug("Created a LogbackAccessConfigurator: [{}]", configurator);
        return configurator;
    }

    /**
     * The auto-configuration for if Tomcat is being used.
     */
    @Configuration
    @ConditionalOnBean(value = TomcatEmbeddedServletContainerFactory.class)
    public static class Tomcat {

        /**
         * Creates a container customizer.
         *
         * @return a container customizer.
         */
        @Bean
        public EmbeddedServletContainerCustomizer logbackAccessContainerCustomizer() {
            LogbackAccessTomcatCustomizer customizer = new LogbackAccessTomcatCustomizer();
            log.debug("Created a LogbackAccessTomcatCustomizer: [{}]", customizer);
            return customizer;
        }

    }

    /**
     * The auto-configuration for if Jetty is being used.
     */
    @Configuration
    @ConditionalOnBean(value = JettyEmbeddedServletContainerFactory.class)
    public static class Jetty {

        /**
         * Creates a container customizer.
         *
         * @return a container customizer.
         */
        @Bean
        public EmbeddedServletContainerCustomizer logbackAccessContainerCustomizer() {
            LogbackAccessJettyCustomizer customizer = new LogbackAccessJettyCustomizer();
            log.debug("Created a LogbackAccessJettyCustomizer: [{}]", customizer);
            return customizer;
        }

    }

}
