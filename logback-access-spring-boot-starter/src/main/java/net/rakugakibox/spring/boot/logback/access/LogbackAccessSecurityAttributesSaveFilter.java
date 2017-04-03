package net.rakugakibox.spring.boot.logback.access;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.filter.GenericFilterBean;

/**
 * The filter that saves the Spring Security attributes for Logback-access.
 */
public class LogbackAccessSecurityAttributesSaveFilter extends GenericFilterBean {

    /**
     * The attribute name of the remote user.
     */
    public static final String REMOTE_USER_ATTRIBUTE_NAME =
            LogbackAccessSecurityAttributesSaveFilter.class.getName() + ".remoteUser";

    /** {@inheritDoc} */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        saveSecurityAttributes((HttpServletRequest) request);
        chain.doFilter(request, response);
    }

    /**
     * Saves the Spring Security attributes.
     */
    private void saveSecurityAttributes(HttpServletRequest request) {
        request.setAttribute(REMOTE_USER_ATTRIBUTE_NAME, request.getRemoteUser());
    }

}
