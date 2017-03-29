package net.rakugakibox.spring.boot.logback.access.test;

import ch.qos.logback.access.spi.IAccessEvent;
import ch.qos.logback.core.AppenderBase;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.SerializationUtils;

/**
 * The Logback-access appender to add to {@link InMemoryLogbackAccessEventQueues}.
 */
public class InMemoryLogbackAccessEventQueueAppender extends AppenderBase<IAccessEvent> {

    /**
     * The name of event queue.
     */
    @Getter
    @Setter
    private String queueName = InMemoryLogbackAccessEventQueues.DEFAULT_QUEUE_NAME;

    /** {@inheritDoc} */
    @Override
    protected void append(IAccessEvent event) {
        event.prepareForDeferredProcessing();
        event = (IAccessEvent) SerializationUtils.deserialize(SerializationUtils.serialize(event));
        InMemoryLogbackAccessEventQueues.push(queueName, event);
    }

}
