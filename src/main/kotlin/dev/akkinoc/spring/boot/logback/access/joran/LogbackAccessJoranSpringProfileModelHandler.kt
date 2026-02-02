package dev.akkinoc.spring.boot.logback.access.joran

import ch.qos.logback.core.Context
import ch.qos.logback.core.model.Model
import ch.qos.logback.core.model.processor.ModelInterpretationContext
import ch.qos.logback.core.util.OptionHelper.substVars
import org.springframework.core.env.Environment
import org.springframework.core.env.Profiles
import org.springframework.util.StringUtils.commaDelimitedListToStringArray
import org.springframework.util.StringUtils.trimArrayElements
import ch.qos.logback.core.model.processor.ModelHandlerBase as ModelHandler

/**
 * The Joran [ModelHandler] to support `<springProfile>` tags.
 *
 * @param context The Logback context.
 * @property environment The environment.
 * @see org.springframework.boot.logging.logback.SpringProfileModelHandler
 */
class LogbackAccessJoranSpringProfileModelHandler(
    context: Context,
    private val environment: Environment,
) : ModelHandler(context) {

    override fun handle(ic: ModelInterpretationContext, model: Model) {
        model as LogbackAccessJoranSpringProfileModel
        val names = trimArrayElements(commaDelimitedListToStringArray(model.name))
        val resolvedNames = names.mapNotNull { it?.let { name -> substVars(name, ic, context) } }.toTypedArray()
        if (resolvedNames.isEmpty() || !environment.acceptsProfiles(Profiles.of(*resolvedNames))) {
            model.deepMarkAsSkipped()
        }
    }

}
