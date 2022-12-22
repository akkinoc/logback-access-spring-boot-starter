package dev.akkinoc.spring.boot.logback.access.joran.model

import ch.qos.logback.core.Context
import ch.qos.logback.core.model.Model
import ch.qos.logback.core.model.processor.ModelHandlerBase
import ch.qos.logback.core.model.processor.ModelInterpretationContext
import ch.qos.logback.core.util.OptionHelper
import org.springframework.core.env.Environment
import org.springframework.core.env.Profiles
import org.springframework.util.StringUtils

/**
 * Logback model handler to support <springProfile> tags.
 *
 * @see org.springframework.boot.logging.logback.SpringProfileModelHandler
 */
class SpringProfileModelHandler(private val environment: Environment, context: Context) : ModelHandlerBase(context) {
    override fun handle(ic: ModelInterpretationContext, model: Model) {
        val springModel = model as SpringProfileModel
        if (!acceptsProfiles(ic, springModel)) {
            model.markAsSkipped()
        }
    }

    private fun acceptsProfiles(
        ic: ModelInterpretationContext,
        model: SpringProfileModel
    ): Boolean {
        val profileNames = StringUtils.trimArrayElements(StringUtils.commaDelimitedListToStringArray(model.name))
        if (profileNames.isEmpty()) {
            return false
        }

        profileNames.indices.forEach { profileNames[it] = OptionHelper.substVars(profileNames[it], ic, context) }

        return environment.acceptsProfiles(Profiles.of(*profileNames))
    }

}
