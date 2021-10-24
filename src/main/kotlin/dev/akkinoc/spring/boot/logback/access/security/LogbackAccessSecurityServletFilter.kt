package dev.akkinoc.spring.boot.logback.access.security

import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

/**
 * The security filter for the servlet web server.
 */
class LogbackAccessSecurityServletFilter : Filter {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        request as HttpServletRequest
        request.setAttribute(REMOTE_USER_ATTRIBUTE, request.remoteUser)
        chain.doFilter(request, response)
    }

    companion object {

        /**
         * The attribute name for the remote user.
         */
        @JvmField
        val REMOTE_USER_ATTRIBUTE: String = "${LogbackAccessSecurityServletFilter::class.qualifiedName}.remoteUser"

    }

}
