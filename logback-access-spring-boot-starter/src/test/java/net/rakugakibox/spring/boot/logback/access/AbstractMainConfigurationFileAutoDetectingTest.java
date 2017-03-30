package net.rakugakibox.spring.boot.logback.access;

import ch.qos.logback.access.spi.IAccessEvent;
import net.rakugakibox.spring.boot.logback.access.test.ClassPathRule;
import net.rakugakibox.spring.boot.logback.access.test.InMemoryLogbackAccessEventQueues;
import net.rakugakibox.spring.boot.logback.access.test.InMemoryLogbackAccessEventQueuesRule;
import net.rakugakibox.spring.boot.logback.access.test.TestControllerConfiguration;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import static net.rakugakibox.spring.boot.logback.access.test.AccessEventAssert.assertThat;
import static net.rakugakibox.spring.boot.logback.access.test.ResponseEntityAssert.assertThat;

/**
 * The base class for testing to auto detect main configuration file.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractMainConfigurationFileAutoDetectingTest {

    /**
     * The REST template.
     */
    @Autowired
    protected TestRestTemplate rest;

    /**
     * Creates a class test rule.
     *
     * @return a class test rule.
     */
    @ClassRule
    public static TestRule classRule() {
        return new ClassPathRule(AbstractMainConfigurationFileAutoDetectingTest.class);
    }

    /**
     * Creates a test rule.
     *
     * @return a test rule.
     */
    @Rule
    public TestRule rule() {
        return new InMemoryLogbackAccessEventQueuesRule();
    }

    /**
     * Tests a Logback-access event.
     */
    @Test
    public void logbackAccessEvent() {

        ResponseEntity<String> response = rest.getForEntity("/test/text", String.class);
        IAccessEvent event = InMemoryLogbackAccessEventQueues.pop("logback-access.xml");

        assertThat(response).hasStatusCode(HttpStatus.OK);
        assertThat(event).isNotNull();

    }

    /**
     * The base class of context configuration.
     */
    @EnableAutoConfiguration
    @Import(TestControllerConfiguration.class)
    public static abstract class AbstractContextConfiguration {
    }

}
