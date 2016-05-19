package net.rakugakibox.springbootext.logback.access.test;

import ch.qos.logback.access.spi.IAccessEvent;
import ch.qos.logback.core.AppenderBase;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * The Logback-access appender that has the singleton event queue.
 */
public class SingletonQueueAppender extends AppenderBase<IAccessEvent> {

    /**
     * The event queue.
     */
    private static final LinkedBlockingQueue<IAccessEvent> queue = new LinkedBlockingQueue<>();

    /**
     * Removes all elements from the event queue.
     */
    public static void clear() {
        queue.clear();
    }

    /**
     * Removes and returns the head element of the event queue.
     * Waits a moment if necessary until an element becomes available.
     *
     * @return the head element of the event queue.
     */
    public static IAccessEvent pop() {
        try {
            return queue.poll(1, TimeUnit.MINUTES);
        } catch (InterruptedException exc) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Events was not added to the queue.", exc);
        }
    }

    /**
     * Returns whether the event queue is empty.
     *
     * @return {@code true} if the event queue is empty, {@code false} otherwise.
     */
    public static boolean isEmpty() {
        return queue.isEmpty();
    }

    /** {@inheritDoc} */
    @Override
    protected void append(IAccessEvent event) {
        event.prepareForDeferredProcessing();
        try {
            queue.offer(event, 1, TimeUnit.MINUTES);
        } catch (InterruptedException exc) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Could not add the event: event=[" + event + "]", exc);
        }
    }

}
