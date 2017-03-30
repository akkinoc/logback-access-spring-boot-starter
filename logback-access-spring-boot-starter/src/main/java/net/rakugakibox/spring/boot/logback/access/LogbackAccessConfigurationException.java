package net.rakugakibox.spring.boot.logback.access;

/**
 * The exception of Logback-access configuration.
 * Thrown to indicate that Logback-access could not be configured.
 */
public class LogbackAccessConfigurationException extends RuntimeException {

    /**
     * Constructs an instance.
     *
     * @param context the Logback-access context.
     * @param config the location of the configuration file.
     * @param cause the cause.
     */
    public LogbackAccessConfigurationException(LogbackAccessContext context, String config, Throwable cause) {
        super("Could not configure Logback-access: context=[" + context + "], config=[" + config + "]", cause);
    }

}
