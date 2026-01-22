package io.github.seijikohara.logback.access.test.extension

import io.github.seijikohara.logback.access.LogbackAccessEvent
import java.util.concurrent.CopyOnWriteArrayList

/**
 * The Logback-access events capture.
 */
class EventsCapture : MutableList<LogbackAccessEvent> by CopyOnWriteArrayList()
