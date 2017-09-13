package net.rakugakibox.spring.boot.logback.access.jetty;

import lombok.extern.slf4j.Slf4j;
import net.rakugakibox.spring.boot.logback.access.AbstractLogbackAccessInstaller;
import net.rakugakibox.spring.boot.logback.access.LogbackAccessProperties;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;

/**
 * The Logback-access installer for Jetty.
 */
@Slf4j
public class JettyLogbackAccessInstaller
        extends AbstractLogbackAccessInstaller<JettyServletWebServerFactory> {

    /**
     * Constructs an instance.
     *
     * @param logbackAccessProperties the configuration properties for Logback-access.
     * @param environment the environment.
     * @param applicationEventPublisher the application event publisher.
     */
    public JettyLogbackAccessInstaller(
            LogbackAccessProperties logbackAccessProperties,
            Environment environment,
            ApplicationEventPublisher applicationEventPublisher
    ) {
        super(logbackAccessProperties, environment, applicationEventPublisher);
    }

    /** {@inheritDoc} */
    @Override
    protected void installLogbackAccess(JettyServletWebServerFactory container) {
        container.addServerCustomizers(this::wrapJettyHandler);
        log.debug("Installed Logback-access: container=[{}]", container);
    }

    /**
     * Wraps the Jetty handler.
     *
     * @param server the Jetty server.
     */
    private void wrapJettyHandler(Server server) {
        LogbackAccessJettyRequestLog requestLog = new LogbackAccessJettyRequestLog(
                logbackAccessProperties, environment, applicationEventPublisher);
        RequestLogHandler requestLogHandler = new RequestLogHandler();
        requestLogHandler.setHandler(server.getHandler());
        requestLogHandler.setRequestLog(requestLog);
        server.setHandler(requestLogHandler);
    }

}
