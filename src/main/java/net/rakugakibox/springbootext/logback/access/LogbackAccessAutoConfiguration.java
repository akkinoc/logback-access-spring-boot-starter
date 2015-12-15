package net.rakugakibox.springbootext.logback.access;

import lombok.extern.slf4j.Slf4j;
import net.rakugakibox.springbootext.logback.access.jetty.LogbackAccessContainerCustomizer;
import net.rakugakibox.springbootext.logback.access.tomcat.LogbackAccessTomcatCustomizer;
import org.apache.catalina.startup.Tomcat;
import org.eclipse.jetty.server.Server;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration for Logback-access.
 */
@Configuration
@EnableConfigurationProperties
@ConditionalOnWebApplication
@Slf4j
public class LogbackAccessAutoConfiguration {

    /**
     * Bean of configurator.
     *
     * @return configurator.
     */
    @Bean
    @ConditionalOnMissingBean
    public LogbackAccessConfigurator logbackAccessConfigurator() {
        LogbackAccessConfigurator configurator = new LogbackAccessConfigurator();
        log.debug("Created LogbackAccessConfigurator: [{}]", configurator);
        return configurator;
    }

    /**
     * Configuration for if Tomcat is being used.
     */
    @Configuration
    @ConditionalOnClass(Tomcat.class)
    public static class ForTomcat {

        /**
         * Bean of {@link EmbeddedServletContainerCustomizer} for Tomcat.
         *
         * @return {@link EmbeddedServletContainerCustomizer} for Tomcat.
         */
        @Bean
        public EmbeddedServletContainerCustomizer logbackAccessTomcatCustomizer() {
            LogbackAccessTomcatCustomizer customizer = new LogbackAccessTomcatCustomizer();
            log.debug("Created LogbackAccessTomcatCustomizer: [{}]", customizer);
            return customizer;
        }

    }

    /**
     * Configuration for if Jetty is being used.
     */
    @Configuration
    @ConditionalOnClass(Server.class)
    public static class ForJetty {

        /**
         * Bean of {@link EmbeddedServletContainerCustomizer} for Jetty.
         *
         * @return {@link EmbeddedServletContainerCustomizer} for Jetty.
         */
        @Bean
        public EmbeddedServletContainerCustomizer logbackAccessJettyContainerCustomizer() {
            LogbackAccessContainerCustomizer customizer = new LogbackAccessContainerCustomizer();
            log.debug("Created LogbackAccessJettyContainerCustomizer: [{}]", customizer);
            return customizer;
        }

    }

}
