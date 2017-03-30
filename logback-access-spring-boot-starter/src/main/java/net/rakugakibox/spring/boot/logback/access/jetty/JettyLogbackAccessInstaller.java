package net.rakugakibox.spring.boot.logback.access.jetty;

import lombok.extern.slf4j.Slf4j;
import net.rakugakibox.spring.boot.logback.access.AbstractLogbackAccessInstaller;
import net.rakugakibox.spring.boot.logback.access.LogbackAccessProperties;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;

/**
 * The Logback-access installer for Jetty.
 */
@Slf4j
public class JettyLogbackAccessInstaller
        extends AbstractLogbackAccessInstaller<JettyEmbeddedServletContainerFactory> {

    /**
     * Constructs an instance.
     *
     * @param logbackAccessProperties the configuration properties for Logback-access.
     */
    public JettyLogbackAccessInstaller(LogbackAccessProperties logbackAccessProperties) {
        super(JettyEmbeddedServletContainerFactory.class, logbackAccessProperties);
    }

    /** {@inheritDoc} */
    @Override
    public void installLogbackAccess(JettyEmbeddedServletContainerFactory container) {
        LogbackAccessJettyRequestLog requestLog = new LogbackAccessJettyRequestLog(logbackAccessProperties);
        container.addServerCustomizers(server -> {
            RequestLogHandler requestLogHandler = new RequestLogHandler();
            requestLogHandler.setHandler(server.getHandler());
            requestLogHandler.setRequestLog(requestLog);
            server.setHandler(requestLogHandler);
        });
        log.debug("Installed Logback-access: container=[{}], requestLog=[{}]", container, requestLog);
    }

}
