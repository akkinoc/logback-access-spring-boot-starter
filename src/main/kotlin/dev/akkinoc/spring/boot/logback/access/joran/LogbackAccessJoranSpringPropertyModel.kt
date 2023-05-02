package dev.akkinoc.spring.boot.logback.access.joran

import ch.qos.logback.core.model.Model
import ch.qos.logback.core.model.NamedModel

/**
 * The Joran [Model] to support `<springProperty>` tags.
 *
 * @see org.springframework.boot.logging.logback.SpringPropertyModel
 */
class LogbackAccessJoranSpringPropertyModel : NamedModel() {

    /**
     * The property name in the Spring environment.
     */
    var source: String? = null

    /**
     * The fallback value if [source] can't be found in the Spring environment.
     */
    var defaultValue: String? = null

    /**
     * The Logback property scope.
     */
    var scope: String? = null

}
