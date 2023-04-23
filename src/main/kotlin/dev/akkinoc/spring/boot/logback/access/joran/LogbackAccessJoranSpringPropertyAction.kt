package dev.akkinoc.spring.boot.logback.access.joran

import ch.qos.logback.core.joran.action.Action
import ch.qos.logback.core.joran.action.BaseModelAction
import ch.qos.logback.core.joran.spi.SaxEventInterpretationContext
import ch.qos.logback.core.model.Model
import dev.akkinoc.spring.boot.logback.access.joran.model.SpringPropertyModel
import org.xml.sax.Attributes

/**
 * The Joran [Action] to support `<springProperty>` tags.
 * Allows properties to be sourced from the environment.
 *
 * @see org.springframework.boot.logging.logback.SpringPropertyAction
 */
class LogbackAccessJoranSpringPropertyAction : BaseModelAction() {
    override fun buildCurrentModel(
        interpretationContext: SaxEventInterpretationContext,
        name: String,
        attributes: Attributes
    ): Model {
        val model = SpringPropertyModel()
        model.name = attributes.getValue(NAME_ATTRIBUTE).orEmpty()
        model.source = attributes.getValue(SOURCE_ATTRIBUTE).orEmpty()
        model.scope = attributes.getValue(SCOPE_ATTRIBUTE).orEmpty()
        model.defaultValue = attributes.getValue(DEFAULT_VALUE_ATTRIBUTE).orEmpty()
        return model
    }

    companion object {
        private const val SOURCE_ATTRIBUTE = "source"
        private const val DEFAULT_VALUE_ATTRIBUTE = "defaultValue"
    }
}
