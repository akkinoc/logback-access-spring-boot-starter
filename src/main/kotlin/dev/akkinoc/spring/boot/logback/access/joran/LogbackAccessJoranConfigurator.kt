package dev.akkinoc.spring.boot.logback.access.joran

import ch.qos.logback.access.joran.JoranConfigurator
import ch.qos.logback.core.joran.spi.ElementSelector
import ch.qos.logback.core.joran.spi.RuleStore
import ch.qos.logback.core.model.processor.DefaultProcessor
import dev.akkinoc.spring.boot.logback.access.joran.model.SpringProfileModel
import dev.akkinoc.spring.boot.logback.access.joran.model.SpringProfileModelHandler
import dev.akkinoc.spring.boot.logback.access.joran.model.SpringPropertyModel
import dev.akkinoc.spring.boot.logback.access.joran.model.SpringPropertyModelHandler
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

        store.addRule(
            ElementSelector("configuration/springProperty"),
        ) { LogbackAccessJoranSpringPropertyAction() }
        store.addRule(
            ElementSelector("*/springProfile"),
        ) { LogbackAccessJoranSpringProfileAction() }
        store.addTransparentPathPart("springProfile")
    }

    override fun addModelHandlerAssociations(defaultProcessor: DefaultProcessor) {
        defaultProcessor.addHandler(SpringProfileModel::class.java) { _, _ ->
            SpringProfileModelHandler(
                this.environment,
                this.context
            )
        }
        defaultProcessor.addHandler(SpringPropertyModel::class.java) { _, _ ->
            SpringPropertyModelHandler(
                this.environment,
                this.context
            )
        }

        super.addModelHandlerAssociations(defaultProcessor)
    }

}
