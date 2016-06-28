package net.rakugakibox.springbootext.logback.access.test;

import ch.qos.logback.access.spi.IAccessEvent;
import ch.qos.logback.core.AppenderBase;
import lombok.Getter;
import lombok.Setter;

/**
 * The Logback-access appender to add to {@link NamedEventQueues}.
 */
public class NamedEventQueueAppender extends AppenderBase<IAccessEvent> {

    /**
     * The name of event queue.
     */
    @Getter
    @Setter
    private String queueName = NamedEventQueues.DEFAULT_NAME;

    /** {@inheritDoc} */
    @Override
    protected void append(IAccessEvent event) {
        event.prepareForDeferredProcessing();
        NamedEventQueues.push(queueName, event);
    }

}
