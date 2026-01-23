package io.github.seijikohara.logback.access

import ch.qos.logback.access.common.spi.IAccessEvent
import io.github.seijikohara.logback.access.value.LogbackAccessLocalPortStrategy
import org.apache.catalina.valves.RemoteIpValve
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * The configuration properties for Logback-access.
 *
 * @property enabled
 *  Whether to enable auto-configuration.
 * @property config
 *  The location of the configuration file.
 *  Specify a URL that starts with "classpath:" or "file:".
 *  Auto-detected by default:
 *      1. "classpath:logback-access-test.xml"
 *      2. "classpath:logback-access.xml"
 *      3. "classpath:logback-access-test-spring.xml"
 *      4. "classpath:logback-access-spring.xml"
 *      5. "classpath:io/github/seijikohara/logback/access/logback-access-spring.xml"
 * @property localPortStrategy
 *  The strategy to change the behavior of [IAccessEvent.getLocalPort].
 * @property excludePatterns
 *  The URI patterns to exclude from access logging.
 *  Supports Ant-style path patterns (e.g., `/actuator/` with wildcards, `/health`).
 * @property excludeSuccessfulOnly
 *  When true, only exclude requests that return successful status codes (2xx).
 *  When false (default), exclude all requests matching the patterns regardless of status.
 * @property tomcat
 *  The properties for the Tomcat web server.
 * @property teeFilter
 *  The properties for the tee filter.
 */
@ConfigurationProperties("logback.access")
data class LogbackAccessProperties
@JvmOverloads
constructor(
    val enabled: Boolean = true,
    val config: String? = null,
    val localPortStrategy: LogbackAccessLocalPortStrategy = LogbackAccessLocalPortStrategy.SERVER,
    val excludePatterns: List<String> = emptyList(),
    val excludeSuccessfulOnly: Boolean = false,
    val tomcat: Tomcat = Tomcat(),
    val teeFilter: TeeFilter = TeeFilter(),
) {

    companion object {

        /**
         * The default locations of the configuration file.
         */
        @JvmField
        val DEFAULT_CONFIGS: List<String> = listOf(
            "classpath:logback-access-test.xml",
            "classpath:logback-access.xml",
            "classpath:logback-access-test-spring.xml",
            "classpath:logback-access-spring.xml",
        )

        /**
         * The fallback location of the configuration file.
         */
        const val FALLBACK_CONFIG: String = "classpath:io/github/seijikohara/logback/access/logback-access-spring.xml"
    }

    /**
     * The properties for the Tomcat web server.
     *
     * @property requestAttributesEnabled
     *  Whether to enable the request attributes to work with [RemoteIpValve].
     *  Defaults to the presence of [RemoteIpValve] enabled by the property "server.forward-headers-strategy=native".
     */
    data class Tomcat
    @JvmOverloads
    constructor(
        val requestAttributesEnabled: Boolean? = null,
    )

    /**
     * The properties for the tee filter.
     *
     * @property enabled
     *  Whether to enable the tee filter.
     * @property includes
     *  The host names to activate.
     *  By default, all hosts are activated.
     * @property excludes
     *  The host names to deactivate.
     *  By default, all hosts are activated.
     */
    data class TeeFilter
    @JvmOverloads
    constructor(
        val enabled: Boolean = false,
        val includes: String? = null,
        val excludes: String? = null,
    )
}
