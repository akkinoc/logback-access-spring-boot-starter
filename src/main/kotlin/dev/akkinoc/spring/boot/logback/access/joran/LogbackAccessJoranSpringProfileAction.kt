package dev.akkinoc.spring.boot.logback.access.joran

import ch.qos.logback.core.joran.action.Action
import ch.qos.logback.core.joran.action.BaseModelAction
import ch.qos.logback.core.joran.spi.SaxEventInterpretationContext
import ch.qos.logback.core.model.Model
import dev.akkinoc.spring.boot.logback.access.joran.model.SpringProfileModel
import org.xml.sax.Attributes

/**
 * The Joran [Action] to support `<springProfile>` tags.
 * Allows a section to only be enabled when a specific profile is active.
 *
 * @see org.springframework.boot.logging.logback.SpringProfileAction
 */
class LogbackAccessJoranSpringProfileAction : BaseModelAction() {

    override fun buildCurrentModel(
        interpretationContext: SaxEventInterpretationContext,
        name: String,
        attributes: Attributes
    ): Model {
        val model = SpringProfileModel()
        model.name = attributes.getValue(NAME_ATTRIBUTE).orEmpty()
        return model
    }
}
