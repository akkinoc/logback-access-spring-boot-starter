package dev.akkinoc.spring.boot.logback.access.joran.model

import ch.qos.logback.core.model.NamedModel

/**
 * Logback model to support <springProperty> tags. Allows Logback properties to be sourced from the Spring environment.
 *
 * @see org.springframework.boot.logging.logback.SpringPropertyModel
 */
class SpringPropertyModel : NamedModel() {
    /**
     * Logback property scope. Can be one of:
     * - LOCAL
     * - CONTEXT
     * - SYSTEM
     */
    var scope: String = ""

    /**
     * If [source] can't be found in the Spring environment this will be the fallback value.
     */
    var defaultValue: String = ""

    /**
     * The property name in the Spring environment.
     */
    var source: String = ""
}
