package dev.akkinoc.spring.boot.logback.access.test.extension

import ch.qos.logback.core.AppenderBase
import dev.akkinoc.spring.boot.logback.access.LogbackAccessEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.util.SerializationUtils.deserialize
import org.springframework.util.SerializationUtils.serialize
import java.util.concurrent.ConcurrentHashMap

/**
 * The Logback-access appender to capture Logback-access events that will be appended.
 */
class EventsCaptureAppender : AppenderBase<LogbackAccessEvent>() {

    override fun append(event: LogbackAccessEvent) {
        event.prepareForDeferredProcessing()
        val serialized = serialize(event)
        val deserialized = deserialize(serialized) as LogbackAccessEvent
        captures.forEach { (id, capture) ->
            capture += deserialized
            log.debug("Captured the {}: {} @{}", LogbackAccessEvent::class.simpleName, event, id)
        }
    }

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(EventsCaptureAppender::class.java)

        /**
         * The Logback-access events captures.
         */
        val captures: MutableMap<String, EventsCapture> = ConcurrentHashMap()

    }

}
