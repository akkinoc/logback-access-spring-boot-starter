package net.rakugakibox.spring.boot.logback.access;

import java.util.EventListener;

import ch.qos.logback.access.spi.IAccessEvent;

/**
 * The listener interface for Logback-access.
 */
public interface LogbackAccessListener extends EventListener {

    /**
     * Invoked after calling the Logback-access appenders.
     *
     * @param event the Logback-access event.
     */
    default void onCalledAppenders(IAccessEvent event) {
    }

    /**
     * Invoked after denying by the Logback-access filter chain decision.
     *
     * @param event the Logback-access event.
     */
    default void onDeniedByFilterChainDecision(IAccessEvent event) {
    }

}
