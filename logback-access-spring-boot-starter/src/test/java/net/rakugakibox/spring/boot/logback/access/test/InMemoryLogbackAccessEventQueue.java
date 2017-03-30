package net.rakugakibox.spring.boot.logback.access.test;

import java.time.Duration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import ch.qos.logback.access.spi.IAccessEvent;

/**
 * The queue of Logback-access event that stores in memory.
 * This class is thread-safe.
 */
public class InMemoryLogbackAccessEventQueue {

    /**
     * The timeout for queue access.
     */
    public static final Duration TIMEOUT_FOR_QUEUE_ACCESS = Duration.ofMinutes(1L);

    /**
     * The queue.
     */
    private final BlockingQueue<IAccessEvent> queue = new LinkedBlockingQueue<>();

    /**
     * Adds the Logback-access event.
     * Waits a moment for space becomes available if necessary.
     *
     * @param event the Logback-access event.
     */
    public void push(IAccessEvent event) {
        try {
            if (!queue.offer(event, TIMEOUT_FOR_QUEUE_ACCESS.getSeconds(), TimeUnit.SECONDS)) {
                throw new IllegalStateException("Could not push a Logback-access event: event=[" + event + "]");
            }
        } catch (InterruptedException exc) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Could not push a Logback-access event: event=[" + event + "]", exc);
        }
    }

    /**
     * Removes and returns a head Logback-access event.
     * Waits a moment for a Logback-access event becomes available if necessary.
     *
     * @return a Logback-access event.
     */
    public IAccessEvent pop() {
        try {
            IAccessEvent event = queue.poll(TIMEOUT_FOR_QUEUE_ACCESS.getSeconds(), TimeUnit.SECONDS);
            if (event == null) {
                throw new IllegalStateException("Could not pop a Logback-access event");
            }
            return event;
        } catch (InterruptedException exc) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Could not pop a Logback-access event", exc);
        }
    }

    /**
     * Returns whether the queue is empty.
     *
     * @return {@code true} if the queue is empty, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    /**
     * Removes all Logback-access events from queue.
     */
    public void clear() {
        queue.clear();
    }

}
