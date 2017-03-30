package net.rakugakibox.spring.boot.logback.access.test;

import ch.qos.logback.access.spi.IAccessEvent;
import ch.qos.logback.core.AppenderBase;
import static org.springframework.util.SerializationUtils.deserialize;
import static org.springframework.util.SerializationUtils.serialize;

/**
 * The Logback-access appender that adds to static {@link LogbackAccessEventQueue}.
 */
public class LogbackAccessEventQueueAppender extends AppenderBase<IAccessEvent> {

    /**
     * The queue of appended Logback-access event.
     */
    public static final LogbackAccessEventQueue appendedEventQueue = new LogbackAccessEventQueue();

    /** {@inheritDoc} */
    @Override
    protected void append(IAccessEvent event) {
        event.prepareForDeferredProcessing();
        event = (IAccessEvent) deserialize(serialize(event));
        appendedEventQueue.push(event);
    }

}
