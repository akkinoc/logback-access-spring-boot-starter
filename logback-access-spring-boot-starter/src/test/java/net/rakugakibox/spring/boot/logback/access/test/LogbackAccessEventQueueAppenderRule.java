package net.rakugakibox.spring.boot.logback.access.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * The test rule for using {@link LogbackAccessEventQueueAppender}.
 */
@Slf4j
public class LogbackAccessEventQueueAppenderRule extends TestWatcher {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void starting(Description description) {
        initializeLogbackAccessEventQueues();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void succeeded(Description description) {
        verifyinitializeLogbackAccessEventQueues();
    }

    /**
     * Initializes the queues of {@link LogbackAccessEventQueueAppender}.
     */
    private void initializeLogbackAccessEventQueues() {
        LogbackAccessEventQueueAppender.appendedEventQueue.clear();
        log.debug("Initialized the queues of LogbackAccessEventQueueAppender");
    }

    /**
     * Verifies the queues of {@link LogbackAccessEventQueueAppender}.
     */
    private void verifyinitializeLogbackAccessEventQueues() {
        assertThat(LogbackAccessEventQueueAppender.appendedEventQueue.isEmpty())
                .as("Verifies that the LogbackAccessEventQueueAppender.appendedEventQueue is empty")
                .isTrue();
        log.debug("Verifyed the queues of LogbackAccessEventQueueAppender");
    }

}
