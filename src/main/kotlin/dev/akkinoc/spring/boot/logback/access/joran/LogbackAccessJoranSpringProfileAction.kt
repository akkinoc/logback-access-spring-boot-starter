package dev.akkinoc.spring.boot.logback.access.joran

import ch.qos.logback.core.joran.action.Action
import ch.qos.logback.core.joran.action.BaseModelAction
import ch.qos.logback.core.joran.spi.SaxEventInterpretationContext
import ch.qos.logback.core.model.Model
import org.xml.sax.Attributes

/**
 * The Joran [Action] to support `<springProfile>` tags.
 * Allows a section of a Logback configuration to only be enabled when a specific Spring profile is active.
 *
 * @see org.springframework.boot.logging.logback.SpringProfileAction
 */
class LogbackAccessJoranSpringProfileAction : BaseModelAction() {

    override fun buildCurrentModel(ic: SaxEventInterpretationContext, name: String, attrs: Attributes): Model {
        val model = LogbackAccessJoranSpringProfileModel()
        model.name = attrs.getValue(NAME_ATTRIBUTE)
        return model
    }

}
