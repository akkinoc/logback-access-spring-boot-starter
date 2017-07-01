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
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.springframework.util.SocketUtils.findAvailableTcpPort;

/**
 * The base class for testing in the case where test spring configuration files is invalid.
 */
@RequiredArgsConstructor
public abstract class AbstractTestSpringConfigurationFileInvalidTest {

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
        return new ClassPathRule(AbstractTestSpringConfigurationFileInvalidTest.class);
    }

    /**
     * Tests a Logback-access configuration exception in the case where the test spring configuration file is invalid.
     */
    @Test
    public void logbackAccessConfigurationExceptionWhereTestSpringConfigurationFileIsInvalid() {

        Map<String, Object> properties = new HashMap<>();
        properties.put("server.port", findAvailableTcpPort());

        SpringApplication application = new SpringApplication(contextConfiguration);
        application.setDefaultProperties(properties);
        Throwable throwable = catchThrowable(application::run);
        Optional<LogbackAccessConfigurationException> exc = findLogbackAccessConfigurationException(throwable);

        assertThat(exc).hasValueSatisfying(value ->
                assertThat(value)
                        .hasMessageStartingWith("Could not configure Logback-access:")
                        .hasMessageContaining("config=[classpath:logback-access-test-spring.xml]")
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
    @EnableAutoConfiguration
    public static abstract class AbstractContextConfiguration {
    }

}
