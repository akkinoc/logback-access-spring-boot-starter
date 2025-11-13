package dev.akkinoc.spring.boot.logback.access.security

import jakarta.servlet.DispatcherType.FORWARD
import jakarta.servlet.DispatcherType.REQUEST
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.autoconfigure.web.servlet.ConditionalOnMissingFilterBean
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer

/**
 * The configuration to register the security filter for the servlet web server.
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(AbstractSecurityWebApplicationInitializer::class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
class LogbackAccessSecurityServletFilterConfiguration {

    /**
     * Provides the security filter for the servlet web server.
     *
     * @return The security filter for the servlet web server.
     */
    @Bean
    @ConditionalOnMissingFilterBean
    fun logbackAccessSecurityServletFilter(): FilterRegistrationBean<LogbackAccessSecurityServletFilter> {
        val logbackAccessSecurityServletFilter = FilterRegistrationBean(LogbackAccessSecurityServletFilter())
        logbackAccessSecurityServletFilter.setDispatcherTypes(REQUEST, FORWARD)
        log.debug(
            "Providing the {}: {}",
            LogbackAccessSecurityServletFilter::class.simpleName,
            logbackAccessSecurityServletFilter,
        )
        return logbackAccessSecurityServletFilter
    }

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(LogbackAccessSecurityServletFilterConfiguration::class.java)

    }

}
