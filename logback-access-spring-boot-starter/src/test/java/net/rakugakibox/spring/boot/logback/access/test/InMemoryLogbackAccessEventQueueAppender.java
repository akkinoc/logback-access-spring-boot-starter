package net.rakugakibox.spring.boot.logback.access.test;

import ch.qos.logback.access.spi.IAccessEvent;
import ch.qos.logback.core.AppenderBase;
import static org.springframework.util.SerializationUtils.deserialize;
import static org.springframework.util.SerializationUtils.serialize;

/**
 * The Logback-access appender that adds to static {@link InMemoryLogbackAccessEventQueue}.
 */
public class InMemoryLogbackAccessEventQueueAppender extends AppenderBase<IAccessEvent> {

    /**
     * The queue of Logback-access event.
     */
    public static final InMemoryLogbackAccessEventQueue queue = new InMemoryLogbackAccessEventQueue();

    /** {@inheritDoc} */
    @Override
    protected void append(IAccessEvent event) {
        event.prepareForDeferredProcessing();
        event = (IAccessEvent) deserialize(serialize(event));
        queue.push(event);
    }

}
