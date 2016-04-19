package net.rakugakibox.springbootext.logback.access.test;

import ch.qos.logback.access.spi.IAccessEvent;
import ch.qos.logback.core.AppenderBase;
import java.util.LinkedList;

/**
 * Singleton list appender.
 */
public class SingletonListAppender extends AppenderBase<IAccessEvent> {

    /**
     * Events that appended.
     */
    private static final LinkedList<IAccessEvent> events = new LinkedList<>();

    /**
     * Removes all events.
     */
    public static void clearEvents() {
        events.clear();
    }

    /**
     * Removes and returns the first event.
     *
     * @return events.
     */
    public static IAccessEvent popEvent() {
        return events.pop();
    }

    /**
     * Returns {@code true} if events is not empty.
     *
     * @return {@code true} if events is not empty.
     */
    public static boolean hasEvents() {
        return !events.isEmpty();
    }

    /** {@inheritDoc} */
    @Override
    protected void append(IAccessEvent event) {
        event.prepareForDeferredProcessing();
        events.add(event);
    }

}
