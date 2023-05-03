package dev.akkinoc.spring.boot.logback.access

import ch.qos.logback.access.spi.IAccessEvent
import dev.akkinoc.spring.boot.logback.access.value.LogbackAccessLocalPortStrategy
import io.undertow.UndertowOptions
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
 *      5. "classpath:dev/akkinoc/spring/boot/logback/access/logback-access-spring.xml"
 * @property localPortStrategy
 *  The strategy to change the behavior of [IAccessEvent.getLocalPort].
 * @property tomcat
 *  The properties for the Tomcat web server.
 * @property undertow
 *  The properties for the Undertow web server.
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
    val tomcat: Tomcat = Tomcat(),
    val undertow: Undertow = Undertow(),
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
        const val FALLBACK_CONFIG: String = "classpath:dev/akkinoc/spring/boot/logback/access/logback-access-spring.xml"

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
     * The properties for the Undertow web server.
     *
     * @property recordRequestStartTime
     *  Whether to enable [UndertowOptions.RECORD_REQUEST_START_TIME].
     *  Used to measure [IAccessEvent.getElapsedTime] and [IAccessEvent.getElapsedSeconds].
     */
    data class Undertow
    @JvmOverloads
    constructor(
        val recordRequestStartTime: Boolean = true,
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
