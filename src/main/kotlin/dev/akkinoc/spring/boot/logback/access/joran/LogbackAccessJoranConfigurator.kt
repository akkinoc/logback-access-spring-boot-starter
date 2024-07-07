package dev.akkinoc.spring.boot.logback.access.joran

import ch.qos.logback.access.common.joran.JoranConfigurator
import ch.qos.logback.core.joran.spi.ElementSelector
import ch.qos.logback.core.joran.spi.RuleStore
import ch.qos.logback.core.model.processor.DefaultProcessor
import org.springframework.core.env.Environment

/**
 * The [JoranConfigurator] to support additional rules.
 *
 * @property environment The environment.
 * @see org.springframework.boot.logging.logback.SpringBootJoranConfigurator
 */
class LogbackAccessJoranConfigurator(private val environment: Environment) : JoranConfigurator() {

    override fun addElementSelectorAndActionAssociations(store: RuleStore) {
        super.addElementSelectorAndActionAssociations(store)
        store.addRule(ElementSelector("configuration/springProperty")) { LogbackAccessJoranSpringPropertyAction() }
        store.addRule(ElementSelector("*/springProfile")) { LogbackAccessJoranSpringProfileAction() }
        store.addTransparentPathPart("springProfile")
    }

    override fun addModelHandlerAssociations(processor: DefaultProcessor) {
        processor.addHandler(LogbackAccessJoranSpringPropertyModel::class.java) { _, _ ->
            LogbackAccessJoranSpringPropertyModelHandler(context, environment)
        }
        processor.addHandler(LogbackAccessJoranSpringProfileModel::class.java) { _, _ ->
            LogbackAccessJoranSpringProfileModelHandler(context, environment)
        }
        super.addModelHandlerAssociations(processor)
    }

}
