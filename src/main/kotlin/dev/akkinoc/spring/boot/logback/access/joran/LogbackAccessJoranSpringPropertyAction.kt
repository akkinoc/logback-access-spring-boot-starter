package dev.akkinoc.spring.boot.logback.access.joran

import ch.qos.logback.core.joran.action.Action
import ch.qos.logback.core.joran.action.ActionUtil.setProperty
import ch.qos.logback.core.joran.action.ActionUtil.stringToScope
import ch.qos.logback.core.joran.spi.InterpretationContext
import org.springframework.core.env.Environment
import org.xml.sax.Attributes

/**
 * The Joran [Action] to support `<springProperty>` tags.
 * Allows properties to be sourced from the environment.
 *
 * @property environment The environment.
 * @see org.springframework.boot.logging.logback.SpringPropertyAction
 */
class LogbackAccessJoranSpringPropertyAction(private val environment: Environment) : Action() {

    override fun begin(ic: InterpretationContext, elem: String, attrs: Attributes) {
        val name = attrs.getValue(NAME_ATTRIBUTE)
        val scope = stringToScope(attrs.getValue(SCOPE_ATTRIBUTE))
        val source = attrs.getValue("source")
        val defaultValue = attrs.getValue("defaultValue")
        val value = environment.getProperty(source, defaultValue)
        setProperty(ic, name, value, scope)
    }

    override fun end(ic: InterpretationContext, elem: String) = Unit

}
