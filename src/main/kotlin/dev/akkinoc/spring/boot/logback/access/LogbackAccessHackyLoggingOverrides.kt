package dev.akkinoc.spring.boot.logback.access

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

/**
 * This is a hacky workaround for https://github.com/akkinoc/logback-access-spring-boot-starter/issues/98.
 * A user who wants to modify this logic should copy this class and modify it,
 * and make sure the modified class has a higher classloader priority.
 * Please note that this is a temporary solution.
 */
object LogbackAccessHackyLoggingOverrides {

    /**
     * Returning null means no override.
     * Returning non-null value will result in that value being logged instead of request body.
     */
    @JvmStatic
    @Suppress("UNUSED_PARAMETER")
    fun overriddenRequestBody(request: HttpServletRequest?): String? = null

    /**
     * Returning null means no override.
     * Returning non-null value will result in that value being logged instead of response body.
     */
    @JvmStatic
    @Suppress("UNUSED_PARAMETER")
    fun overriddenResponseBody(request: HttpServletRequest?, response: HttpServletResponse?): String? = null

}
