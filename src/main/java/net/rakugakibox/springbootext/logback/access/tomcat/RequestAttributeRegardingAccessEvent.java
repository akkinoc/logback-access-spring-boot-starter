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

    public RequestAttributeRegardingAccessEvent(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
                                                ServerAdapter adapter) {
        super(httpRequest, httpResponse, adapter);
    }

    @Override
    public String getRemoteAddr() {
        HttpServletRequest request = getRequest();
        Object remoteAddress = request.getAttribute(AccessLog.REMOTE_ADDR_ATTRIBUTE);
        if (remoteAddress == null) {
            remoteAddress = request.getRemoteAddr();
        }
        return remoteAddress == null ? NA : remoteAddress.toString();
    }

    @Override
    public String getRemoteHost() {
        HttpServletRequest request = getRequest();
        Object remoteHost = request.getAttribute(AccessLog.REMOTE_HOST_ATTRIBUTE);
        if (remoteHost == null) {
            remoteHost = request.getRemoteHost();
        }
        return remoteHost == null ? NA : remoteHost.toString();
    }

    @Override
    public String getProtocol() {
        HttpServletRequest request = getRequest();
        Object protocol = request.getAttribute(AccessLog.PROTOCOL_ATTRIBUTE);
        if (protocol == null) {
            protocol = request.getProtocol();
        }
        return protocol == null ? NA : protocol.toString();
    }

}
