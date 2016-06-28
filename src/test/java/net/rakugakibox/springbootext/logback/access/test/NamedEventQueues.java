package net.rakugakibox.springbootext.logback.access.test;

import ch.qos.logback.access.spi.IAccessEvent;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * The named event queues.
 */
public class NamedEventQueues {

    /**
     * The name of default event queue.
     */
    public static final String DEFAULT_NAME = "default";

    /**
     * The named event queues.
     */
    private static final ConcurrentMap<String, BlockingQueue<IAccessEvent>> queues = new ConcurrentHashMap<>();

    /**
     * Constructor is not supported.
     */
    private NamedEventQueues() {
        throw new UnsupportedOperationException("Constructor is not supported.");
    }

    /**
     * Returns the named event queue.
     *
     * @param name the name of event queue.
     * @return the named event queue.
     */
    private static BlockingQueue<IAccessEvent> getQueue(String name) {
        return queues.computeIfAbsent(name, key -> new LinkedBlockingQueue<>());
    }

    /**
     * Adds an element to the named event queue.
     * Waits a moment if necessary for space to become available.
     *
     * @param name the name of event queue.
     * @param event an event.
     */
    public static void push(String name, IAccessEvent event) {
        try {
            if (!getQueue(name).offer(event, 1, TimeUnit.MINUTES)) {
                throw new IllegalStateException("Could not push an event: event=[" + event + "]");
            }
        } catch (InterruptedException exc) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Could not push an event: event=[" + event + "]", exc);
        }
    }

    /**
     * Adds an element to the default event queue.
     * Waits a moment if necessary for space to become available.
     *
     * @param event an event.
     */
    public static void push(IAccessEvent event) {
        push(DEFAULT_NAME, event);
    }

    /**
     * Removes and returns a head element from the named event queue.
     * Waits a moment if necessary for an element becomes available.
     *
     * @param name the name of event queue.
     * @return a head element of the event queue.
     */
    public static IAccessEvent pop(String name) {
        try {
            IAccessEvent event = getQueue(name).poll(1, TimeUnit.MINUTES);
            if (event == null) {
                throw new IllegalStateException("Could not pop an event.");
            }
            return event;
        } catch (InterruptedException exc) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Could not pop an event.", exc);
        }
    }

    /**
     * Removes and returns a head element from the default event queue.
     * Waits a moment if necessary for an element becomes available.
     *
     * @return a head element of the event queue.
     */
    public static IAccessEvent pop() {
        return pop(DEFAULT_NAME);
    }

    /**
     * Returns whether all event queues is empty.
     *
     * @return {@code true} if all event queues is empty, {@code false} otherwise.
     */
    public static boolean isAllEmpty() {
        return queues.values().stream().allMatch(Collection::isEmpty);
    }

    /**
     * Removes all event queues.
     */
    public static void clear() {
        queues.clear();
    }

}
