package net.rakugakibox.spring.boot.logback.access.jetty;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import net.rakugakibox.spring.boot.logback.access.AbstractLogbackAccessInstaller;
import net.rakugakibox.spring.boot.logback.access.LogbackAccessListener;
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
     * @param logbackAccessListeners the listeners for Logback-access.
     */
    public JettyLogbackAccessInstaller(
            LogbackAccessProperties logbackAccessProperties, List<LogbackAccessListener> logbackAccessListeners) {
        super(JettyEmbeddedServletContainerFactory.class, logbackAccessProperties, logbackAccessListeners);
    }

    /** {@inheritDoc} */
    @Override
    protected void installLogbackAccess(JettyEmbeddedServletContainerFactory container) {
        LogbackAccessJettyRequestLog requestLog =
                new LogbackAccessJettyRequestLog(logbackAccessProperties, logbackAccessListeners);
        container.addServerCustomizers(server -> {
            RequestLogHandler requestLogHandler = new RequestLogHandler();
            requestLogHandler.setHandler(server.getHandler());
            requestLogHandler.setRequestLog(requestLog);
            server.setHandler(requestLogHandler);
        });
        log.debug("Installed Logback-access: container=[{}], requestLog=[{}]", container, requestLog);
    }

}
