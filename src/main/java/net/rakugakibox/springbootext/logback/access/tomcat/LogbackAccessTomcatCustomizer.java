package net.rakugakibox.springbootext.logback.access.tomcat;

import java.util.stream.Stream;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.rakugakibox.springbootext.logback.access.LogbackAccessConfigurator;
import net.rakugakibox.springbootext.logback.access.LogbackAccessProperties;
import org.apache.catalina.Context;
import org.apache.catalina.valves.RemoteIpValve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;

/**
 * The Tomcat customizer.
 */
@Slf4j
public class LogbackAccessTomcatCustomizer implements EmbeddedServletContainerCustomizer {

    /**
     * The configuration properties.
     */
    @Getter
    @Setter
    @Autowired
    private LogbackAccessProperties properties;

    /**
     * The configurator.
     */
    @Getter
    @Setter
    @Autowired
    private LogbackAccessConfigurator configurator;

    /** {@inheritDoc} */
    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        if (container instanceof TomcatEmbeddedServletContainerFactory) {
            customize((TomcatEmbeddedServletContainerFactory) container);
        } else {
            log.debug("Skipped the Tomcat customization: container=[{}]", container);
        }
    }

    /**
     * Customizes the Tomcat container.
     *
     * @param container the Tomcat container factory.
     */
    public void customize(TomcatEmbeddedServletContainerFactory container) {
        ContextCustomizer customizer = new ContextCustomizer();
        container.addContextCustomizers(customizer);
        log.debug("Added the Tomcat context customizer: customizer=[{}] to container=[{}]", customizer, container);
    }

    /**
     * The Tomcat context customizer.
     */
    private class ContextCustomizer implements TomcatContextCustomizer {

        /** {@inheritDoc} */
        @Override
        public void customize(Context context) {
            LogbackAccessValve valve = new LogbackAccessValve();
            valve.setConfigurator(configurator);
            valve.setRequestAttributesEnabled(getOrDeduceRequestAttributesEnabled(context));
            context.getPipeline().addValve(valve);
            log.debug("Added the Tomcat valve: valve=[{}] to context=[{}]", valve, context);
        }

        /**
         * Returns whether request attributes is enabled,
         * or deduce the value from the presence of the {@link RemoteIpValve}.
         *
         * @param context the Tomcat context.
         * @return {@code true} if request attributes is enabled, {@code false} otherwise.
         */
        private boolean getOrDeduceRequestAttributesEnabled(Context context) {
            Boolean enableRequestAttributes = properties.getTomcat().getEnableRequestAttributes();
            if (enableRequestAttributes != null) {
                return enableRequestAttributes;
            }
            // Deduce the value from the presence of the RemoteIpValve.
            return Stream.of(context.getPipeline().getValves())
                    .map(Object::getClass)
                    .anyMatch(RemoteIpValve.class::isAssignableFrom);
        }

    }

}
