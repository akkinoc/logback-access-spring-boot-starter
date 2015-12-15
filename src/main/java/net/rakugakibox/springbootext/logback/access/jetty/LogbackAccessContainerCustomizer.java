package net.rakugakibox.springbootext.logback.access.jetty;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.rakugakibox.springbootext.logback.access.LogbackAccessConfigurator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;

/**
 * Jetty customizer for Logback-access.
 */
@Slf4j
public class LogbackAccessContainerCustomizer implements EmbeddedServletContainerCustomizer {

    /**
     * Configurator.
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
            log.debug("Skipped customization for Jetty: container=[{}]", container);
        }
    }

    /**
     * Customize the specified {@link JettyEmbeddedServletContainerFactory}.
     *
     * @param container {@link JettyEmbeddedServletContainerFactory} to customize.
     */
    public void customize(JettyEmbeddedServletContainerFactory container) {
        container.addServerCustomizers(new ServerCustomizer());
    }

    /**
     * Jetty server customizer.
     */
    private class ServerCustomizer implements JettyServerCustomizer {

        /** {@inheritDoc} */
        @Override
        public void customize(Server server) {
            ReconfigurableRequestLog requestLog = new ReconfigurableRequestLog();
            requestLog.setReconfigurator(configurator);
            RequestLogHandler requestLogHandler = new RequestLogHandler();
            requestLogHandler.setHandler(server.getHandler());
            requestLogHandler.setRequestLog(requestLog);
            server.setHandler(requestLogHandler);
            log.debug("Set Jetty handler: handler=[{}] to server=[{}]", requestLogHandler, server);
        }

    }

}
