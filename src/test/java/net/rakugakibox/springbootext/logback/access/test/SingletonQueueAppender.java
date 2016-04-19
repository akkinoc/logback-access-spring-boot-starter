package net.rakugakibox.springbootext.logback.access.test;

import ch.qos.logback.access.spi.IAccessEvent;
import ch.qos.logback.core.AppenderBase;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * The appender that has the singleton event queue.
 */
public class SingletonQueueAppender extends AppenderBase<IAccessEvent> {

    /**
     * The event queue.
     */
    private static final LinkedBlockingQueue<IAccessEvent> events = new LinkedBlockingQueue<>();

    /**
     * Removes all elements from the event queue.
     */
    public static void clear() {
        events.clear();
    }

    /**
     * Removes and returns the first element from the event queue.
     *
     * @return the first element from the event queue.
     */
    public static IAccessEvent pop() {
        try {
            return events.poll(1, TimeUnit.MINUTES);
        } catch (InterruptedException exc) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(exc);
        }
    }

    /**
     * Returns {@code true} if the event queue is empty.
     *
     * @return {@code true} if the event queue is empty.
     */
    public static boolean isEmpty() {
        return events.isEmpty();
    }

    /** {@inheritDoc} */
    @Override
    protected void append(IAccessEvent event) {
        event.prepareForDeferredProcessing();
        try {
            events.offer(event, 1, TimeUnit.MINUTES);
        } catch (InterruptedException exc) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(exc);
        }
    }

}
