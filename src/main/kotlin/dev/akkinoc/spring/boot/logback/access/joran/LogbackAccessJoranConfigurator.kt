package dev.akkinoc.spring.boot.logback.access.joran

import ch.qos.logback.access.joran.JoranConfigurator
import ch.qos.logback.core.joran.action.NOPAction
import ch.qos.logback.core.joran.spi.ElementSelector
import ch.qos.logback.core.joran.spi.RuleStore
import org.springframework.core.env.Environment

/**
 * The [JoranConfigurator] to support additional rules.
 *
 * @property environment The environment.
 * @see org.springframework.boot.logging.logback.SpringBootJoranConfigurator
 */
class LogbackAccessJoranConfigurator(private val environment: Environment) : JoranConfigurator() {

    override fun addInstanceRules(store: RuleStore) {
        super.addInstanceRules(store)
        store.addRule(
            ElementSelector("*/springProfile"),
            LogbackAccessJoranSpringProfileAction(environment),
        )
        store.addRule(
            ElementSelector("*/springProfile/*"),
            NOPAction(),
        )
        store.addRule(
            ElementSelector("configuration/springProperty"),
            LogbackAccessJoranSpringPropertyAction(environment),
        )
    }

}
