package net.rakugakibox.spring.boot.logback.access;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import ch.qos.logback.core.joran.spi.JoranException;
import lombok.RequiredArgsConstructor;
import net.rakugakibox.spring.boot.logback.access.test.ClassPathRule;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.springframework.util.SocketUtils.findAvailableTcpPort;

/**
 * The base class for testing in the case where main spring configuration files is invalid.
 */
@RequiredArgsConstructor
public abstract class AbstractMainSpringConfigurationFileInvalidTest {

    /**
     * The class of context configuration.
     */
    private final Class<? extends AbstractContextConfiguration> contextConfiguration;

    /**
     * Creates a class test rule.
     *
     * @return a class test rule.
     */
    @ClassRule
    public static TestRule classRule() {
        return new ClassPathRule(AbstractMainSpringConfigurationFileInvalidTest.class);
    }

    /**
     * Tests a Logback-access configuration exception in the case where the main spring configuration file is invalid.
     */
    @Test
    public void logbackAccessConfigurationExceptionWhereMainSpringConfigurationFileIsInvalid() {

        Map<String, Object> properties = new HashMap<>();
        properties.put("server.port", findAvailableTcpPort());

        SpringApplication application = new SpringApplication(contextConfiguration);
        application.setDefaultProperties(properties);
        Throwable throwable = catchThrowable(application::run);
        Optional<LogbackAccessConfigurationException> exc = findLogbackAccessConfigurationException(throwable);

        assertThat(exc).hasValueSatisfying(value ->
                assertThat(value)
                        .hasMessageStartingWith("Could not configure Logback-access:")
                        .hasMessageContaining("config=[classpath:logback-access-spring.xml]")
                        .hasCauseInstanceOf(JoranException.class)
        );

    }

    /**
     * Finds the {@link LogbackAccessConfigurationException}.
     *
     * @param throwable the throwable.
     * @return the Logback-access configuration exception.
     */
    private Optional<LogbackAccessConfigurationException> findLogbackAccessConfigurationException(Throwable throwable) {
        while (throwable != null && !(throwable instanceof LogbackAccessConfigurationException)) {
            throwable = throwable.getCause();
        }
        return Optional.ofNullable((LogbackAccessConfigurationException) throwable);
    }

    /**
     * The base class of context configuration.
     */
    @EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
    public static abstract class AbstractContextConfiguration {
    }

}
