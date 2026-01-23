package io.github.seijikohara.logback.access.tracing

import io.micrometer.tracing.Tracer
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingFilterBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * The configuration to register the tracing filter for the servlet web server.
 *
 * This configuration is activated when Micrometer Tracing is on the classpath
 * and a [Tracer] bean is available.
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Tracer::class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
class LogbackAccessTracingServletFilterConfiguration {

    /**
     * Provides the tracing filter for the servlet web server.
     *
     * @param tracer The Micrometer Tracer.
     * @return The tracing filter for the servlet web server.
     */
    @Bean
    @ConditionalOnBean(Tracer::class)
    @ConditionalOnMissingFilterBean
    fun logbackAccessTracingServletFilter(tracer: Tracer): FilterRegistrationBean<LogbackAccessTracingServletFilter> {
        val filter = LogbackAccessTracingServletFilter(tracer)
        val registration = FilterRegistrationBean(filter)
        log.debug(
            "Providing the {}: {}",
            LogbackAccessTracingServletFilter::class.simpleName,
            registration,
        )
        return registration
    }

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(LogbackAccessTracingServletFilterConfiguration::class.java)
    }
}
