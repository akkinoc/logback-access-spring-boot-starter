package dev.akkinoc.spring.boot.logback.access.test.assertion

import io.kotest.assertions.nondeterministic.continually
import io.kotest.assertions.nondeterministic.continuallyConfig
import io.kotest.assertions.nondeterministic.eventually
import io.kotest.assertions.nondeterministic.eventuallyConfig
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

/**
 * The utility methods to support test assertions.
 */
object Assertions {

    /**
     * Asserts that the assertion function will eventually pass within a short time.
     * Used to assert Logback-access events that may be appended late.
     *
     * @param T The return value type of the assertion function.
     * @param assert The assertion function that is called repeatedly.
     * @return The return value of the assertion function.
     */
    fun <T> assertLogbackAccessEventsEventually(assert: () -> T): T = runBlocking {
        val config = eventuallyConfig {
            duration = 2.seconds
            interval = 100.milliseconds
        }
        eventually(config) { assert() }
    }

    /**
     * Asserts that the assertion function will continually pass within a short time.
     * Used to assert Logback-access events that may be appended late.
     *
     * @param T The return value type of the assertion function.
     * @param assert The assertion function that is called repeatedly.
     * @return The return value of the assertion function.
     */
    fun <T> assertLogbackAccessEventsContinually(assert: () -> T): T = runBlocking {
        val config = continuallyConfig<T> {
            duration = 2.seconds
            interval = 100.milliseconds
        }
        continually(config) { assert() }
    }

}
