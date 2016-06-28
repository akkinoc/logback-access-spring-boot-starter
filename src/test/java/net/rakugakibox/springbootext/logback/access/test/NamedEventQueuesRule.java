package net.rakugakibox.springbootext.logback.access.test;

import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * The test rule for using {@link NamedEventQueues}.
 */
@Slf4j
public class NamedEventQueuesRule extends TestWatcher {

    /** {@inheritDoc} */
    @Override
    protected void starting(Description description) {
        initializeNamedEventQueues();
    }

    /** {@inheritDoc} */
    @Override
    protected void succeeded(Description description) {
        verifyNamedEventQueues();
    }

    /**
     * Initializes the named event queues.
     */
    private void initializeNamedEventQueues() {
        log.debug("Initializing the named event queues.");
        NamedEventQueues.clear();
    }

    /**
     * Verifies the named event queues.
     */
    private void verifyNamedEventQueues() {
        log.debug("Verifying the named event queues.");
        assertThat(NamedEventQueues.isAllEmpty())
                .as("Verifies the named event queues is all poped")
                .isTrue();
    }

}
