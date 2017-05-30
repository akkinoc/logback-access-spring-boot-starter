package net.rakugakibox.spring.boot.logback.access;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import ch.qos.logback.access.spi.IAccessEvent;
import lombok.Data;
import org.apache.catalina.valves.RemoteIpValve;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The configuration properties for Logback-access.
 */
@Data
@ConfigurationProperties("logback.access")
public class LogbackAccessProperties {

    /**
     * Whether to enable auto-configuration.
     * Defaults to {@code true}.
     */
    private Boolean enabled = true;

    /**
     * The location of the configuration file.
     * Auto-detected by default:
     *   1. "classpath:logback-access-test.xml"
     *   2. "classpath:logback-access.xml"
     *   3. "classpath:net/rakugakibox/spring/boot/logback/access/logback-access.xml"
     */
    private Optional<String> config = Optional.empty();

    /**
     * Whether to use the server port ({@link HttpServletRequest#getServerPort()})
     * instead of the local port ({@link HttpServletRequest#getLocalPort()})
     * within {@link IAccessEvent#getLocalPort()}.
     * Defaults to {@code true}.
     */
    private Boolean useServerPortInsteadOfLocalPort = true;

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
         * Whether to enable request attributes to work with the {@link RemoteIpValve} enabled
         * with "{@code server.useForwardHeaders}".
         * Defaults to the presence of the {@link RemoteIpValve}.
         */
        private Optional<Boolean> enableRequestAttributes = Optional.empty();

    }

}
