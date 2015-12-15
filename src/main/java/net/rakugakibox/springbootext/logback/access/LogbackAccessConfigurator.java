package net.rakugakibox.springbootext.logback.access;

import ch.qos.logback.access.joran.JoranConfigurator;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.joran.spi.JoranException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import lombok.Cleanup;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

/**
 * Configurator of Logback-access.
 */
@ConfigurationProperties(prefix = "logback.access")
@Slf4j
public class LogbackAccessConfigurator {

    /**
     * Default location of the configuration files.
     */
    private static final String[] DEFAULT_CONFIGS = new String[]{
        "classpath:logback-access-test.xml",
        "classpath:logback-access.xml",
    };

    /**
     * Fallback location of the configuration file.
     */
    private static final String FALLBACK_CONFIG = "classpath:"
            + StringUtils.replace(LogbackAccessConfigurator.class.getPackage().getName(), ".", "/")
            + "/logback-access.xml";

    /**
     * Location of the configuration file.
     */
    @Getter
    @Setter
    private String config;

    /**
     * Configure Logback-access.
     *
     * @param context context to configure.
     */
    public void configure(Context context) {

        if (StringUtils.hasLength(config)) {
            try {
                configure(context, config);
                return;
            } catch (IOException | JoranException exc) {
                throw createException(context, config, exc);
            }
        }

        for (String defaultConfig : DEFAULT_CONFIGS) {
            try {
                configure(context, defaultConfig);
                return;
            } catch (IOException exc) {
                // continue
            } catch (JoranException exc) {
                throw createException(context, defaultConfig, exc);
            }
        }

        try {
            configure(context, FALLBACK_CONFIG);
        } catch (IOException | JoranException exc) {
            throw createException(context, FALLBACK_CONFIG, exc);
        }

    }

    /**
     * Configure Logback-access.
     *
     * @param context context to configure.
     * @param config location of the configuration file.
     * @throws IOException if an I/O exception occurs.
     * @throws JoranException if a {@link JoranConfigurator} exception occurs.
     */
    private void configure(Context context, String config) throws IOException, JoranException {
        URL url = ResourceUtils.getURL(config);
        @Cleanup InputStream stream = url.openStream();
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(context);
        configurator.doConfigure(stream);
        log.info("Configured Logback-access: context=[{}] from config=[{}]", context, config);
    }

    /**
     * Create unchecked exception.
     *
     * @param context context to configure.
     * @param config location of the configuration file.
     * @param cause cause of exception.
     */
    private RuntimeException createException(Context context, String config, Exception cause) {
        return new IllegalStateException("Could not configure Logback-access: "
                + "context=[" + context + "] from config=[" + config + "]", cause);
    }

}
