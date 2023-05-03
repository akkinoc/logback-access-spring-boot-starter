package dev.akkinoc.spring.boot.logback.access.joran

import ch.qos.logback.core.joran.action.Action
import ch.qos.logback.core.joran.action.BaseModelAction
import ch.qos.logback.core.joran.spi.SaxEventInterpretationContext
import ch.qos.logback.core.model.Model
import org.xml.sax.Attributes

/**
 * The Joran [Action] to support `<springProperty>` tags.
 * Allows Logback properties to be sourced from the Spring environment.
 *
 * @see org.springframework.boot.logging.logback.SpringPropertyAction
 */
class LogbackAccessJoranSpringPropertyAction : BaseModelAction() {

    override fun buildCurrentModel(ic: SaxEventInterpretationContext, name: String, attrs: Attributes): Model {
        val model = LogbackAccessJoranSpringPropertyModel()
        model.name = attrs.getValue(NAME_ATTRIBUTE)
        model.source = attrs.getValue("source")
        model.defaultValue = attrs.getValue("defaultValue")
        model.scope = attrs.getValue(SCOPE_ATTRIBUTE)
        return model
    }

}
