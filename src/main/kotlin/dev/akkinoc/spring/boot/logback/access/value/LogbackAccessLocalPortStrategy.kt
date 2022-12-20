package dev.akkinoc.spring.boot.logback.access.value

import ch.qos.logback.access.spi.IAccessEvent
import jakarta.servlet.ServletRequest

/**
 * The strategy to change the behavior of [IAccessEvent.getLocalPort].
 */
enum class LogbackAccessLocalPortStrategy {

    /**
     * Returns the port number of the interface on which the request was received.
     * Equivalent to [ServletRequest.getLocalPort] when using a servlet web server.
     */
    LOCAL,

    /**
     * Returns the port number to which the request was sent.
     * Equivalent to [ServletRequest.getServerPort] when using a servlet web server.
     * Helps to identify the destination port number used by the client when forward headers are enabled.
     */
    SERVER,

}
