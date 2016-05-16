package net.rakugakibox.springbootext.logback.access.tomcat;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.rakugakibox.springbootext.logback.access.LogbackAccessConfigurator;
import org.apache.catalina.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;

/**
 * The Tomcat customizer for Logback-access.
 */
@Slf4j
public class LogbackAccessTomcatCustomizer implements EmbeddedServletContainerCustomizer {

    /**
     * The Logback-access configurator.
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
            context.getPipeline().addValve(valve);
            log.debug("Added the Tomcat valve: valve=[{}] to context=[{}]", valve, context);
        }

    }

}
