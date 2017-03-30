package net.rakugakibox.spring.boot.logback.access.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * The test rule for using {@link InMemoryLogbackAccessEventQueueAppender}.
 */
@Slf4j
public class InMemoryLogbackAccessEventQueueAppenderRule extends TestWatcher {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void starting(Description description) {
        initializeQueueOfInMemoryLogbackAccessEventQueueAppender();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void succeeded(Description description) {
        verifyQueueOfInMemoryLogbackAccessEventQueueAppender();
    }

    /**
     * Initializes the {@link InMemoryLogbackAccessEventQueueAppender#queue}.
     */
    private void initializeQueueOfInMemoryLogbackAccessEventQueueAppender() {
        InMemoryLogbackAccessEventQueueAppender.queue.clear();
        log.debug("Initialized the InMemoryLogbackAccessEventQueueAppender.queue");
    }

    /**
     * Verifies the {@link InMemoryLogbackAccessEventQueueAppender#queue}.
     */
    private void verifyQueueOfInMemoryLogbackAccessEventQueueAppender() {
        assertThat(InMemoryLogbackAccessEventQueueAppender.queue.isEmpty())
                .as("Verifies that the InMemoryLogbackAccessEventQueueAppender.queue is empty")
                .isTrue();
        log.debug("Verifyed the InMemoryLogbackAccessEventQueueAppender.queue");
    }

}
