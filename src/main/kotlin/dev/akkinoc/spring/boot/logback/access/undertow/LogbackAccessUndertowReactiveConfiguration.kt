package dev.akkinoc.spring.boot.logback.access.undertow

import dev.akkinoc.spring.boot.logback.access.LogbackAccessContext
import io.undertow.Undertow
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryAutoConfiguration
import org.springframework.boot.web.embedded.undertow.UndertowBuilderCustomizer
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * The configuration for the Undertow reactive web server.
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Undertow::class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
class LogbackAccessUndertowReactiveConfiguration {

    /**
     * Provides the [ReactiveWebServerFactory] for the Undertow reactive web server.
     * Overrides the [ReactiveWebServerFactory] provided by [ReactiveWebServerFactoryAutoConfiguration].
     *
     * @param logbackAccessContext The Logback-access context.
     * @param undertowBuilderCustomizers The Undertow builder customizers.
     * @return The [ReactiveWebServerFactory] for the Undertow reactive web server.
     */
    @Bean
    @ConditionalOnMissingBean(ReactiveWebServerFactory::class)
    fun logbackAccessUndertowReactiveWebServerFactory(
        logbackAccessContext: LogbackAccessContext,
        undertowBuilderCustomizers: List<UndertowBuilderCustomizer>,
    ): LogbackAccessUndertowReactiveWebServerFactory {
        val logbackAccessUndertowReactiveWebServerFactory =
            LogbackAccessUndertowReactiveWebServerFactory(logbackAccessContext)
        logbackAccessUndertowReactiveWebServerFactory.builderCustomizers.addAll(undertowBuilderCustomizers)
        log.debug(
            "Providing the {}: {}",
            LogbackAccessUndertowReactiveWebServerFactory::class.simpleName,
            logbackAccessUndertowReactiveWebServerFactory,
        )
        return logbackAccessUndertowReactiveWebServerFactory
    }

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(LogbackAccessUndertowReactiveConfiguration::class.java)

    }

}
