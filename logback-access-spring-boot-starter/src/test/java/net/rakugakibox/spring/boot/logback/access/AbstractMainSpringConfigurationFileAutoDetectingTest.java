package net.rakugakibox.spring.boot.logback.access;

import net.rakugakibox.spring.boot.logback.access.test.ClassPathRule;
import net.rakugakibox.spring.boot.logback.access.test.LogbackAccessEventQueuingAppender;
import net.rakugakibox.spring.boot.logback.access.test.LogbackAccessEventQueuingAppenderRule;
import net.rakugakibox.spring.boot.logback.access.test.LogbackAccessEventQueuingListener;
import net.rakugakibox.spring.boot.logback.access.test.LogbackAccessEventQueuingListenerConfiguration;
import net.rakugakibox.spring.boot.logback.access.test.LogbackAccessEventQueuingListenerRule;
import net.rakugakibox.spring.boot.logback.access.test.TestControllerConfiguration;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
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
import static net.rakugakibox.spring.boot.logback.access.test.ResponseEntityAssert.assertThat;

/**
 * The base class for testing to auto detect main spring configuration file.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractMainSpringConfigurationFileAutoDetectingTest {

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
        return new ClassPathRule(AbstractMainSpringConfigurationFileAutoDetectingTest.class);
    }

    /**
     * Creates a test rule.
     *
     * @return a test rule.
     */
    @Rule
    public TestRule rule() {
        return RuleChain
                .outerRule(new LogbackAccessEventQueuingAppenderRule())
                .around(new LogbackAccessEventQueuingListenerRule());
    }

    /**
     * Tests a Logback-access event.
     */
    @Test
    public void logbackAccessEvent() {

        ResponseEntity<String> response = rest.getForEntity("/test/text", String.class);
        LogbackAccessEventQueuingAppender.appendedEventQueue.pop();
        LogbackAccessEventQueuingListener.appendedEventQueue.pop();

        assertThat(response).hasStatusCode(HttpStatus.OK);

    }

    /**
     * The base class of context configuration.
     */
    @EnableAutoConfiguration
    @Import({LogbackAccessEventQueuingListenerConfiguration.class, TestControllerConfiguration.class})
    public static abstract class AbstractContextConfiguration {
    }

}
