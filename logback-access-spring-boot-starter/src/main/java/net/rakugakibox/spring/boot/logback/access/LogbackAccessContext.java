package net.rakugakibox.spring.boot.logback.access;

import ch.qos.logback.access.spi.AccessContext;
import ch.qos.logback.core.CoreConstants;

/**
 * The Logback-access context.
 */
public class LogbackAccessContext extends AccessContext {

    /**
     * Constructs an instance.
     */
    public LogbackAccessContext() {
        setName(CoreConstants.DEFAULT_CONTEXT_NAME);
    }

}
