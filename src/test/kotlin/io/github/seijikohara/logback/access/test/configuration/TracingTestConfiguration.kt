package io.github.seijikohara.logback.access.test.configuration

import brave.Tracing
import io.micrometer.tracing.Tracer
import io.micrometer.tracing.brave.bridge.BraveCurrentTraceContext
import io.micrometer.tracing.brave.bridge.BraveTracer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * The test configuration for Micrometer Tracing.
 */
@Configuration(proxyBeanMethods = false)
class TracingTestConfiguration {

    /**
     * Provides a Brave-based Tracing instance for testing.
     *
     * @return The Brave Tracing instance.
     */
    @Bean
    fun braveTracing(): Tracing = Tracing.newBuilder()
        .localServiceName("test-service")
        .build()

    /**
     * Provides a Micrometer Tracer for testing.
     *
     * @param braveTracing The Brave Tracing instance.
     * @return The Micrometer Tracer.
     */
    @Bean
    fun micrometerTracer(braveTracing: Tracing): Tracer = BraveTracer(
        braveTracing.tracer(),
        BraveCurrentTraceContext(braveTracing.currentTraceContext()),
    )
}
