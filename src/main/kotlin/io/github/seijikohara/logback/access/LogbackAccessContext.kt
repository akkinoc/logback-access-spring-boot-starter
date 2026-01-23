package io.github.seijikohara.logback.access

import ch.qos.logback.access.common.spi.AccessContext
import ch.qos.logback.core.spi.FilterReply
import ch.qos.logback.core.status.Status
import io.github.seijikohara.logback.access.LogbackAccessProperties.Companion.DEFAULT_CONFIGS
import io.github.seijikohara.logback.access.LogbackAccessProperties.Companion.FALLBACK_CONFIG
import io.github.seijikohara.logback.access.joran.LogbackAccessJoranConfigurator
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.core.env.Environment
import org.springframework.core.io.ResourceLoader
import org.springframework.util.AntPathMatcher
import org.springframework.util.ResourceUtils.getURL

/**
 * The Logback-access context.
 *
 * @property properties The configuration properties for Logback-access.
 * @param resourceLoader The resource loader.
 * @param environment The environment.
 */
class LogbackAccessContext(
    val properties: LogbackAccessProperties,
    resourceLoader: ResourceLoader,
    environment: Environment,
) : AutoCloseable {

    /**
     * The raw Logback-access context.
     */
    val raw: AccessContext = AccessContext()

    /**
     * The path matcher for exclude patterns.
     */
    private val pathMatcher: AntPathMatcher = AntPathMatcher()

    init {
        val (name, resource) = run {
            properties.config
                ?.also { return@run it to resourceLoader.getResource("${getURL(it)}") }
            DEFAULT_CONFIGS.asSequence()
                .map { it to resourceLoader.getResource(it) }
                .firstOrNull { (_, resource) -> resource.exists() }
                ?.also { return@run it }
            return@run FALLBACK_CONFIG.let { it to resourceLoader.getResource(it) }
        }
        raw.name = name
        raw.statusManager.add(::log)
        val configurator = LogbackAccessJoranConfigurator(environment)
        configurator.context = raw
        configurator.doConfigure(resource.url)
        raw.start()
        log.debug("Initialized the {}: {}", LogbackAccessContext::class.simpleName, this)
    }

    /**
     * Logs the Logback-access status.
     *
     * @param status The Logback-access status.
     */
    private fun log(status: Status) {
        log.debug("Added the {}: {} @{}", Status::class.simpleName, status, this, status.throwable)
    }

    /**
     * Emits the Logback-access event.
     *
     * @param event The Logback-access event.
     */
    fun emit(event: LogbackAccessEvent) {
        if (shouldExclude(event)) {
            log.debug("Excluding the {}: {} @{}", LogbackAccessEvent::class.simpleName, event, this)
            return
        }
        val filterReply = raw.getFilterChainDecision(event)
        log.debug("Emitting the {}: {} {} @{}", LogbackAccessEvent::class.simpleName, filterReply, event, this)
        if (filterReply != FilterReply.DENY) raw.callAppenders(event)
    }

    /**
     * Checks if the event should be excluded from logging based on the configured patterns.
     *
     * @param event The Logback-access event.
     * @return true if the event should be excluded, false otherwise.
     */
    private fun shouldExclude(event: LogbackAccessEvent): Boolean {
        if (properties.excludePatterns.isEmpty()) return false
        val uri = event.requestURI ?: return false
        val matchesPattern = properties.excludePatterns.any { pattern ->
            pathMatcher.match(pattern, uri)
        }
        if (!matchesPattern) return false
        // If excludeSuccessfulOnly is true, only exclude successful (2xx) responses
        if (properties.excludeSuccessfulOnly) {
            val statusCode = event.statusCode
            return statusCode in HTTP_STATUS_SUCCESS_MIN..HTTP_STATUS_SUCCESS_MAX
        }
        return true
    }

    override fun close() {
        log.debug("Closing the {}: {}", LogbackAccessContext::class.simpleName, this)
        raw.stop()
        raw.reset()
        raw.detachAndStopAllAppenders()
        raw.copyOfAttachedFiltersList.forEach { it.stop() }
        raw.clearAllFilters()
    }

    override fun toString(): String = "${this::class.simpleName}(${raw.name})"

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(LogbackAccessContext::class.java)

        /**
         * The minimum HTTP status code for successful responses (2xx).
         */
        private const val HTTP_STATUS_SUCCESS_MIN: Int = 200

        /**
         * The maximum HTTP status code for successful responses (2xx).
         */
        private const val HTTP_STATUS_SUCCESS_MAX: Int = 299
    }
}
