package dev.akkinoc.spring.boot.logback.access.test.extension

import dev.akkinoc.spring.boot.logback.access.LogbackAccessEvent
import java.util.concurrent.CopyOnWriteArrayList

/**
 * The Logback-access events capture.
 */
class EventsCapture : MutableList<LogbackAccessEvent> by CopyOnWriteArrayList()
