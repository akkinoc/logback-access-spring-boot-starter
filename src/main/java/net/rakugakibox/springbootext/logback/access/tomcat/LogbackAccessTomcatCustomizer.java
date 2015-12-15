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
 * {@link EmbeddedServletContainerCustomizer} for Tomcat.
 */
@Slf4j
public class LogbackAccessTomcatCustomizer implements EmbeddedServletContainerCustomizer {

    /**
     * Configurator.
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
            log.debug("Skipped customization for Tomcat: container=[{}]", container);
        }
    }

    /**
     * Customize the specified {@link TomcatEmbeddedServletContainerFactory}.
     *
     * @param container {@link TomcatEmbeddedServletContainerFactory} to customize.
     */
    public void customize(TomcatEmbeddedServletContainerFactory container) {
        container.addContextCustomizers(new ContextCustomizer());
        log.debug("Added Tomcat context customizer: customizer=[{}] to container=[{}]", this, container);
    }

    /**
     * Tomcat context customizer.
     */
    private class ContextCustomizer implements TomcatContextCustomizer {

        /** {@inheritDoc} */
        @Override
        public void customize(Context context) {
            ReconfigurableLogbackValve valve = new ReconfigurableLogbackValve();
            valve.setReconfigurator(configurator);
            context.getPipeline().addValve(valve);
            log.debug("Added Tomcat valve: valve=[{}] to context=[{}]", valve, context);
        }

    }

}
