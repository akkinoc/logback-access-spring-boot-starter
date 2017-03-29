package net.rakugakibox.spring.boot.logback.access.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * The test rule for using {@link InMemoryLogbackAccessEventQueues}.
 */
@Slf4j
public class InMemoryLogbackAccessEventQueuesRule extends TestWatcher {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void starting(Description description) {
        initializeNamedEventQueues();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void succeeded(Description description) {
        verifyNamedEventQueues();
    }

    /**
     * Initializes the {@link InMemoryLogbackAccessEventQueues}.
     */
    private void initializeNamedEventQueues() {
        InMemoryLogbackAccessEventQueues.clear();
        log.debug("Initialized the InMemoryLogbackAccessEventQueues");
    }

    /**
     * Verifies the named event queues.
     */
    private void verifyNamedEventQueues() {
        assertThat(InMemoryLogbackAccessEventQueues.isEmpty())
                .as("Verifies that the InMemoryLogbackAccessEventQueues is all poped")
                .isTrue();
        log.debug("Verifyed the InMemoryLogbackAccessEventQueues");
    }

}
