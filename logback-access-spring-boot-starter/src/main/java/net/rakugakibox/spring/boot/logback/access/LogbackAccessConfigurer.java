package net.rakugakibox.spring.boot.logback.access;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import ch.qos.logback.access.joran.JoranConfigurator;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.joran.spi.JoranException;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;
import static org.springframework.util.ClassUtils.addResourcePathToPackagePath;

/**
 * The configurer of Logback-access.
 */
@RequiredArgsConstructor
@Slf4j
public class LogbackAccessConfigurer {

    /**
     * The default locations of the configuration file.
     */
    public static final List<String> DEFAULT_CONFIGS = unmodifiableList(asList(
            "classpath:logback-access-test.xml",
            "classpath:logback-access.xml"
    ));

    /**
     * The fallback location of the configuration file.
     */
    public static final String FALLBACK_CONFIG =
            "classpath:" + addResourcePathToPackagePath(LogbackAccessConfigurer.class, "logback-access.xml");

    /**
     * The configuration properties.
     */
    private final LogbackAccessProperties properties;

    /**
     * Configures Logback-access.
     * Cause exceptions is wrapped with {@link LogbackAccessConfigurationException}.
     * If configuration file is absent, applies the fallback configuration ({@link #FALLBACK_CONFIG}).
     *
     * @param context the Logback-access context.
     * @throws LogbackAccessConfigurationException if an exception occurs.
     */
    public void configure(Context context) throws LogbackAccessConfigurationException {
        if (properties.getConfig().isPresent()) {
            configure(context, properties.getConfig().get());
            return;
        }
        for (String config : DEFAULT_CONFIGS) {
            if (configureIfPresent(context, config)) {
                return;
            }
        }
        configure(context, FALLBACK_CONFIG);
    }

    /**
     * Configures Logback-access.
     * Cause exceptions is wrapped with {@link LogbackAccessConfigurationException}.
     * If configuration file is absent, throws an exception ({@link LogbackAccessConfigurationException}).
     *
     * @param context the Logback-access context.
     * @param config the location of the configuration file.
     * @throws LogbackAccessConfigurationException if an exception occurs.
     */
    private void configure(Context context, String config) throws LogbackAccessConfigurationException {
        try {
            configureWithCauseThrowing(context, config);
        } catch (Exception exc) {
            throw new LogbackAccessConfigurationException(context, config, exc);
        }
    }

    /**
     * Configures Logback-access.
     * Cause exceptions is wrapped with {@link LogbackAccessConfigurationException}.
     * If configuration file is absent, skips and returns {@code false}.
     *
     * @param context the Logback-access context.
     * @param config the location of the configuration file.
     * @return {@code true} if successful, {@code false} if the configuration file is absent.
     * @throws LogbackAccessConfigurationException if an exception occurs.
     */
    private boolean configureIfPresent(Context context, String config) throws LogbackAccessConfigurationException {
        try {
            configureWithCauseThrowing(context, config);
            return true;
        } catch (FileNotFoundException exc) {
            return false;
        } catch (Exception exc) {
            throw new LogbackAccessConfigurationException(context, config, exc);
        }
    }

    /**
     * Configures Logback-access.
     * Cause exceptions is not wrapped with {@link LogbackAccessConfigurationException}.
     * If configuration file is absent, throws an exception ({@link FileNotFoundException}).
     *
     * @param context the Logback-access context.
     * @param config the location of the configuration file.
     * @throws IOException if an I/O exception occurs.
     * @throws JoranException if a {@link JoranConfigurator} exception occurs.
     */
    private void configureWithCauseThrowing(Context context, String config) throws IOException, JoranException {
        URL url = ResourceUtils.getURL(config);
        @Cleanup InputStream stream = url.openStream();
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(context);
        configurator.doConfigure(stream);
        log.info("Configured Logback-access: context=[{}], config=[{}]", context, config);
    }

}
