package dev.akkinoc.spring.boot.logback.access.joran.model

import ch.qos.logback.core.Context
import ch.qos.logback.core.joran.action.ActionUtil
import ch.qos.logback.core.model.Model
import ch.qos.logback.core.model.ModelUtil
import ch.qos.logback.core.model.processor.ModelHandlerBase
import ch.qos.logback.core.model.processor.ModelInterpretationContext
import org.springframework.core.env.Environment

/**
 * Logback model handler to support <springProperty> tags. Allows Logback properties to be sourced
 * from the Spring environment.
 *
 * @see org.springframework.boot.logging.logback.SpringPropertyModelHandler
 */
class SpringPropertyModelHandler(private val environment: Environment, context: Context) : ModelHandlerBase(context) {
    override fun handle(intercon: ModelInterpretationContext, model: Model) {

        val propertyModel = model as SpringPropertyModel
        val scope = ActionUtil.stringToScope(propertyModel.scope)
        val defaultValue = propertyModel.defaultValue
        val source = propertyModel.source
        if (propertyModel.name.isBlank() || source.isBlank()) {
            addError("The \"name\" and \"source\" attributes of <springProperty> must be set")
        }
        ModelUtil.setProperty(intercon, propertyModel.name, getValue(source, defaultValue), scope)
    }

    private fun getValue(source: String, defaultValue: String): String {
        return this.environment.getProperty(source, defaultValue)
    }
}
