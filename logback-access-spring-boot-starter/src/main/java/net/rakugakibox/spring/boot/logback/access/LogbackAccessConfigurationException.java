package net.rakugakibox.spring.boot.logback.access;

import ch.qos.logback.core.Context;

/**
 * The Logback-access configuration exception.
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
    public LogbackAccessConfigurationException(Context context, String config, Throwable cause) {
        super(buildMessage(context, config), cause);
    }

    /**
     * Builds a message.
     *
     * @param context the Logback-access context.
     * @param config the location of the configuration file.
     * @return a message.
     */
    private static String buildMessage(Context context, String config) {
        return "Could not configure Logback-access: context=[" + context + "], config=[" + config + "]";
    }

}
