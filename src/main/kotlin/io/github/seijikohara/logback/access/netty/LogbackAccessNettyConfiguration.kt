package io.github.seijikohara.logback.access.netty

import io.github.seijikohara.logback.access.LogbackAccessContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.netty.http.server.HttpServer

/**
 * The configuration for the Logback-access Netty web server.
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(HttpServer::class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
class LogbackAccessNettyConfiguration {

    /**
     * Provides the [LogbackAccessNettyWebFilter].
     *
     * @param logbackAccessContext The Logback-access context.
     * @return The [LogbackAccessNettyWebFilter].
     */
    @Bean
    @ConditionalOnMissingBean
    fun logbackAccessNettyWebFilter(
        logbackAccessContext: LogbackAccessContext,
    ): LogbackAccessNettyWebFilter {
        val filter = LogbackAccessNettyWebFilter(logbackAccessContext)
        log.debug("Providing the {}: {}", LogbackAccessNettyWebFilter::class.simpleName, filter)
        return filter
    }

    companion object {
        private val log: Logger = getLogger(LogbackAccessNettyConfiguration::class.java)
    }
}
