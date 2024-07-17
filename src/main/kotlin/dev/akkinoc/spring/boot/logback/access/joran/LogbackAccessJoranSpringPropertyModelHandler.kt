package dev.akkinoc.spring.boot.logback.access.joran

import ch.qos.logback.core.Context
import ch.qos.logback.core.joran.action.ActionUtil.stringToScope
import ch.qos.logback.core.model.Model
import ch.qos.logback.core.model.processor.ModelInterpretationContext
import ch.qos.logback.core.model.util.PropertyModelHandlerHelper.setProperty
import org.springframework.core.env.Environment
import ch.qos.logback.core.model.processor.ModelHandlerBase as ModelHandler

/**
 * The Joran [ModelHandler] to support `<springProperty>` tags.
 *
 * @param context The Logback context.
 * @property environment The environment.
 * @see org.springframework.boot.logging.logback.SpringPropertyModelHandler
 */
class LogbackAccessJoranSpringPropertyModelHandler(
    context: Context,
    private val environment: Environment,
) : ModelHandler(context) {

    override fun handle(ic: ModelInterpretationContext, model: Model) {
        model as LogbackAccessJoranSpringPropertyModel
        val name = model.name
        val source = model.source
        val defaultValue = model.defaultValue.orEmpty()
        val scope = stringToScope(model.scope)
        if (name.isNullOrBlank() || source.isNullOrBlank()) {
            addError("""The "name" and "source" attributes of <springProperty> must be set""")
            return
        }
        val value = environment.getProperty(source, defaultValue)
        setProperty(ic, name, value, scope)
    }

}
