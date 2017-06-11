package net.rakugakibox.spring.boot.logback.access;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The configuration properties for Logback-access.
 */
@Data
@ConfigurationProperties("logback.access")
public class LogbackAccessProperties {

    /**
     * Whether to enable auto-configuration.
     */
    private boolean enabled = true;

    /**
     * The location of the configuration file.
     * Auto-detected by default:
     *   1. "classpath:logback-access-test.xml"
     *   2. "classpath:logback-access.xml"
     *   3. "classpath:net/rakugakibox/spring/boot/logback/access/logback-access.xml"
     */
    private String config;

    /**
     * Whether to use the server port (HttpServletRequest#getServerPort())
     * instead of the local port (HttpServletRequest#getLocalPort()) within IAccessEvent#getLocalPort().
     */
    private boolean useServerPortInsteadOfLocalPort = true;

    /**
     * for Tomcat.
     */
    private Tomcat tomcat = new Tomcat();

    /**
     * for Tomcat.
     */
    @Data
    public static class Tomcat {

        /**
         * Whether to enable request attributes to work with the RemoteIpValve enabled with "server.useForwardHeaders".
         * Defaults to the presence of the RemoteIpValve.
         */
        private Boolean enableRequestAttributes;

    }

}
