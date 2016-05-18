package net.rakugakibox.springbootext.logback.access.tomcat;

import ch.qos.logback.access.spi.AccessEvent;
import ch.qos.logback.access.spi.ServerAdapter;
import org.apache.catalina.AccessLog;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * AccessEvent that regards requestAttributes that may be set by other valves.
 */
public class RequestAttributeRegardingAccessEvent extends AccessEvent {

    private String remoteAddress;
    private String remoteHost;
    private String protocol;
    private Integer localPort;

    public RequestAttributeRegardingAccessEvent(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
                                                ServerAdapter adapter) {
        super(httpRequest, httpResponse, adapter);
    }

    @Override
    public String getRemoteAddr() {
        if (remoteAddress == null) {
            HttpServletRequest request = getRequest();
            if (request != null) {
                remoteAddress = (String) request.getAttribute(AccessLog.REMOTE_ADDR_ATTRIBUTE);
                if (remoteAddress == null) {
                    remoteAddress = request.getRemoteAddr();
                }
            } else {
                remoteAddress = NA;
            }
        }
        return remoteAddress;
    }

    @Override
    public String getRemoteHost() {
        if (remoteHost == null) {
            HttpServletRequest request = getRequest();
            if (request != null) {
                remoteHost = (String) request.getAttribute(AccessLog.REMOTE_HOST_ATTRIBUTE);
                if (remoteHost == null) {
                    remoteHost = request.getRemoteHost();
                }
            } else {
                remoteHost = NA;
            }
        }
        return remoteHost;
    }

    @Override
    public String getProtocol() {
        if (protocol == null) {
            HttpServletRequest request = getRequest();
            if (request != null) {
                protocol = (String) request.getAttribute(AccessLog.PROTOCOL_ATTRIBUTE);
                if (protocol == null) {
                    protocol = request.getProtocol();
                }
            } else {
                protocol = NA;
            }
        }
        return protocol;
    }

    @Override
    public int getLocalPort() {
        if (localPort == null) {
            HttpServletRequest request = getRequest();
            if (request != null) {
                localPort = (Integer) request.getAttribute(AccessLog.SERVER_PORT_ATTRIBUTE);
                if (localPort == null) {
                    localPort = request.getLocalPort();
                }
            } else {
                localPort = SENTINEL;
            }
        }
        return localPort;
    }


}
