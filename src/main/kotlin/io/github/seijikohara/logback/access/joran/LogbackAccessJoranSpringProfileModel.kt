package io.github.seijikohara.logback.access.joran

import ch.qos.logback.core.model.Model
import ch.qos.logback.core.model.NamedModel

/**
 * The Joran [Model] to support `<springProfile>` tags.
 *
 * @see org.springframework.boot.logging.logback.SpringProfileModel
 */
class LogbackAccessJoranSpringProfileModel : NamedModel()
