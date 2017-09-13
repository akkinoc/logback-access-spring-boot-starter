package net.rakugakibox.spring.boot.logback.access;

import java.util.ArrayList;
import java.util.List;

import ch.qos.logback.access.joran.JoranConfigurator;
import ch.qos.logback.core.joran.action.Action;
import ch.qos.logback.core.joran.action.ActionUtil;
import ch.qos.logback.core.joran.action.ActionUtil.Scope;
import ch.qos.logback.core.joran.action.NOPAction;
import ch.qos.logback.core.joran.event.InPlayListener;
import ch.qos.logback.core.joran.event.SaxEvent;
import ch.qos.logback.core.joran.spi.ActionException;
import ch.qos.logback.core.joran.spi.ElementSelector;
import ch.qos.logback.core.joran.spi.InterpretationContext;
import ch.qos.logback.core.joran.spi.RuleStore;
import ch.qos.logback.core.util.OptionHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.xml.sax.Attributes;

/**
 * The {@link JoranConfigurator} that supports additional rules.
 * This class is own implementation from scratch,
 * based on "org.springframework.boot.logging.logback.SpringBootJoranConfigurator".
 * Because it is intended for classic log, and it is invisible by access modifiers.
 */
@RequiredArgsConstructor
public class LogbackAccessJoranConfigurator extends JoranConfigurator {

    /**
     * The environment.
     */
    private final Environment environment;

    /** {@inheritDoc} */
    @Override
    public void addInstanceRules(RuleStore ruleStore) {
        super.addInstanceRules(ruleStore);
        ruleStore.addRule(new ElementSelector("*/springProfile"), new SpringProfileAction());
        ruleStore.addRule(new ElementSelector("*/springProfile/*"), new NOPAction());
        ruleStore.addRule(new ElementSelector("configuration/springProperty"), new SpringPropertyAction());
    }

    /**
     * The {@link JoranConfigurator}'s action to optionally include or exclude sections of configuration
     * based on the active Spring profiles.
     */
    private class SpringProfileAction extends Action implements InPlayListener {

        /**
         * The depth in parsing.
         */
        private int depth;

        /**
         * Whether to accept.
         */
        private boolean accepts;

        /**
         * The events.
         */
        private final List<SaxEvent> events = new ArrayList<>();

        /** {@inheritDoc} */
        @Override
        public void begin(InterpretationContext ic, String name, Attributes attributes) throws ActionException {
            depth++;
            if (depth != 1) {
                return;
            }
            String profiles = OptionHelper.substVars(attributes.getValue(NAME_ATTRIBUTE), ic, context);
            String[] normalizedProfiles = StringUtils.commaDelimitedListToStringArray(profiles);
            normalizedProfiles = StringUtils.trimArrayElements(normalizedProfiles);
            accepts = normalizedProfiles.length > 0 && environment.acceptsProfiles(normalizedProfiles);
            ic.addInPlayListener(this);
        }

        /** {@inheritDoc} */
        @Override
        public void inPlay(SaxEvent event) {
            events.add(event);
        }

        /** {@inheritDoc} */
        @Override
        public void end(InterpretationContext ic, String name) throws ActionException {
            depth--;
            if (depth != 0) {
                return;
            }
            ic.removeInPlayListener(this);
            if (accepts) {
                events.remove(0);
                events.remove(events.size() - 1);
                ic.getJoranInterpreter().getEventPlayer().addEventsDynamically(events, 1);
            }
            events.clear();
        }

    }

    /**
     * The {@link JoranConfigurator}'s action to surface properties from the {@link Environment}.
     */
    private class SpringPropertyAction extends Action {

        /** {@inheritDoc} */
        @Override
        public void begin(InterpretationContext ic, String name, Attributes attributes) throws ActionException {
            String key = attributes.getValue(NAME_ATTRIBUTE);
            Scope scope = ActionUtil.stringToScope(attributes.getValue(SCOPE_ATTRIBUTE));
            String source = attributes.getValue("source");
            String defaultValue = attributes.getValue("defaultValue");
            String value = environment.getProperty(source, defaultValue);
            ActionUtil.setProperty(ic, key, value, scope);
        }

        /** {@inheritDoc} */
        @Override
        public void end(InterpretationContext ic, String name) throws ActionException {
        }

    }

}
