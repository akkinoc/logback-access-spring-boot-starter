package net.rakugakibox.spring.boot.logback.access.test;

import java.time.Duration;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import ch.qos.logback.access.spi.IAccessEvent;
import lombok.experimental.UtilityClass;

/**
 * The non-persistent and static Logback-access event queues provided by an in-memory map.
 * This class is thread-safe.
 */
@UtilityClass
public class InMemoryLogbackAccessEventQueues {

    /**
     * The default queue name.
     */
    public static final String DEFAULT_QUEUE_NAME = "default";

    /**
     * The timeout for queue access.
     */
    public static final Duration TIMEOUT_FOR_QUEUE_ACCESS = Duration.ofMinutes(1L);

    /**
     * The queues by name.
     */
    private static final ConcurrentMap<String, BlockingQueue<IAccessEvent>> queues = new ConcurrentHashMap<>();

    /**
     * Returns the queue.
     * The queue is created at the first call.
     *
     * @param queueName the queue name.
     * @return the queue.
     */
    private static BlockingQueue<IAccessEvent> getQueue(String queueName) {
        return queues.computeIfAbsent(queueName, key -> new LinkedBlockingQueue<>());
    }

    /**
     * Adds an event to the queue.
     * Waits a moment for space to become available if necessary.
     *
     * @param queueName the queue name.
     * @param event the event.
     */
    public static void push(String queueName, IAccessEvent event) {
        try {
            if (!getQueue(queueName).offer(event, TIMEOUT_FOR_QUEUE_ACCESS.getSeconds(), TimeUnit.SECONDS)) {
                throw new IllegalStateException(
                        "Could not push an event: queueName=[" + queueName + "], event=[" + event + "]");
            }
        } catch (InterruptedException exc) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(
                    "Could not push an event: queueName=[" + queueName + "], event=[" + event + "]", exc);
        }
    }

    /**
     * Adds an event to the default queue.
     * Waits a moment for space to become available if necessary.
     *
     * @param event the event.
     */
    public static void push(IAccessEvent event) {
        push(DEFAULT_QUEUE_NAME, event);
    }

    /**
     * Removes and returns a head event from the queue.
     * Waits a moment for an event becomes available if necessary.
     *
     * @param queueName the queue name.
     * @return an event.
     */
    public static IAccessEvent pop(String queueName) {
        try {
            IAccessEvent event = getQueue(queueName).poll(TIMEOUT_FOR_QUEUE_ACCESS.getSeconds(), TimeUnit.SECONDS);
            if (event == null) {
                throw new IllegalStateException("Could not pop an event: queueName=[" + queueName + "]");
            }
            return event;
        } catch (InterruptedException exc) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Could not pop an event: queueName=[" + queueName + "]", exc);
        }
    }

    /**
     * Removes and returns a head event from the default queue.
     * Waits a moment for an event becomes available if necessary.
     *
     * @return an event.
     */
    public static IAccessEvent pop() {
        return pop(DEFAULT_QUEUE_NAME);
    }

    /**
     * Returns whether all queues are empty.
     *
     * @return {@code true} if all queues are empty, {@code false} otherwise.
     */
    public static boolean isEmpty() {
        return queues.values().stream().allMatch(Collection::isEmpty);
    }

    /**
     * Removes all queues.
     */
    public static void clear() {
        queues.clear();
    }

}
