package net.rakugakibox.springbootext.logback.access;

import ch.qos.logback.access.spi.IAccessEvent;
import javax.servlet.http.HttpServletRequest;
import lombok.Data;
import org.apache.catalina.valves.RemoteIpValve;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The configuration properties.
 */
@ConfigurationProperties("logback.access")
@Data
public class LogbackAccessProperties {

    /**
     * Enable Logback-access Auto Configuration.
     * Defaults to {@code true}.
     */
    private Boolean enabled = true;

    /**
     * The location of the configuration file.
     * Auto-detected by default:
     *  {@code classpath:logback-access-test.xml},
     *  {@code classpath:logback-access.xml},
     *  {@code classpath:net/rakugakibox/springbootext/logback/access/logback-access.xml}
     */
    private String config;

    /**
     * Use the server port ({@link HttpServletRequest#getServerPort()})
     * instead of the local port ({@link HttpServletRequest#getLocalPort()}),
     * in {@link IAccessEvent#getLocalPort()}.
     * Defaults to {@code true}.
     */
    private Boolean useServerPortInsteadOfLocalPort = true;

    /**
     * The configuration properties for if Tomcat is being used.
     */
    private Tomcat tomcat = new Tomcat();

    /**
     * The configuration properties for if Tomcat is being used.
     */
    @Data
    public static class Tomcat {

        /**
         * Enable request attributes to work with the {@link RemoteIpValve}
         * enabled with {@code server.useForwardHeaders}.
         * Defaults to the the presence of the {@link RemoteIpValve}.
         */
        private Boolean enableRequestAttributes;

    }

}
