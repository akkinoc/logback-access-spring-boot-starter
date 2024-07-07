package dev.akkinoc.spring.boot.logback.access.test.mock

import ch.qos.logback.access.common.spi.IAccessEvent
import ch.qos.logback.core.filter.Filter
import ch.qos.logback.core.spi.FilterReply

/**
 * The mock event filter.
 * The filter reply can be overridden by request header.
 */
class MockEventFilter : Filter<IAccessEvent>() {

    /**
     * The request header name for overriding filter replies.
     */
    private var requestHeaderName: String = "mock-event-filter-reply"

    override fun decide(event: IAccessEvent): FilterReply {
        val value = event.getRequestHeader(requestHeaderName)
        return FilterReply.entries.find { it.name.equals(value, ignoreCase = true) } ?: FilterReply.NEUTRAL
    }

}
