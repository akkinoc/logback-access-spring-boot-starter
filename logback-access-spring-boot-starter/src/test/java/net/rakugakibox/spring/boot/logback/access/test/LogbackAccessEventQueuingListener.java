package net.rakugakibox.spring.boot.logback.access.test;

import ch.qos.logback.access.spi.IAccessEvent;
import net.rakugakibox.spring.boot.logback.access.LogbackAccessListener;

/**
 * The Logback-access listener that pushes to static {@link LogbackAccessEventQueue}.
 */
public class LogbackAccessEventQueuingListener implements LogbackAccessListener {

    /**
     * The queue of appended Logback-access event.
     */
    public static final LogbackAccessEventQueue appendedEventQueue = new LogbackAccessEventQueue();

    /**
     * The queue of denied Logback-access event.
     */
    public static final LogbackAccessEventQueue deniedEventQueue = new LogbackAccessEventQueue();

    /** {@inheritDoc} */
    @Override
    public void onCalledAppenders(IAccessEvent event) {
        appendedEventQueue.push(event);
    }

    /** {@inheritDoc} */
    @Override
    public void onDeniedByFilterChainDecision(IAccessEvent event) {
        deniedEventQueue.push(event);
    }

}
