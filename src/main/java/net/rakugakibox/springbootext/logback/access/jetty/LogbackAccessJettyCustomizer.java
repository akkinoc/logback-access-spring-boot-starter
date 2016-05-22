package net.rakugakibox.springbootext.logback.access.jetty;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.rakugakibox.springbootext.logback.access.LogbackAccessConfigurator;
import net.rakugakibox.springbootext.logback.access.LogbackAccessProperties;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;

/**
 * The Jetty customizer.
 */
@Slf4j
public class LogbackAccessJettyCustomizer implements EmbeddedServletContainerCustomizer {

    /**
     * The configuration properties.
     */
    @Getter
    @Setter
    @Autowired
    private LogbackAccessProperties properties;

    /**
     * The configurator.
     */
    @Getter
    @Setter
    @Autowired
    private LogbackAccessConfigurator configurator;

    /** {@inheritDoc} */
    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        if (container instanceof JettyEmbeddedServletContainerFactory) {
            customize((JettyEmbeddedServletContainerFactory) container);
        } else {
            log.debug("Skipped the Jetty customization: container=[{}]", container);
        }
    }

    /**
     * Customizes the Jetty container.
     *
     * @param container the Jetty container factory.
     */
    public void customize(JettyEmbeddedServletContainerFactory container) {
        ServerCustomizer customizer = new ServerCustomizer();
        container.addServerCustomizers(customizer);
        log.debug("Added the Jetty server customizer: customizer=[{}] to container=[{}]", customizer, container);
    }

    /**
     * The Jetty server customizer.
     */
    private class ServerCustomizer implements JettyServerCustomizer {

        /** {@inheritDoc} */
        @Override
        public void customize(Server server) {
            LogbackAccessRequestLog requestLog = new LogbackAccessRequestLog();
            requestLog.setProperties(properties);
            requestLog.setConfigurator(configurator);
            RequestLogHandler requestLogHandler = new RequestLogHandler();
            requestLogHandler.setHandler(server.getHandler());
            requestLogHandler.setRequestLog(requestLog);
            server.setHandler(requestLogHandler);
            log.debug("Set the Jetty handler: handler=[{}] to server=[{}]", requestLogHandler, server);
        }

    }

}
