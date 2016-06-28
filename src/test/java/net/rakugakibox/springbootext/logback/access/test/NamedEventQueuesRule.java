package net.rakugakibox.springbootext.logback.access.test;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * The test rule for using {@link NamedEventQueues}.
 */
public class NamedEventQueuesRule extends TestWatcher {

    /** {@inheritDoc} */
    @Override
    protected void starting(Description description) {
        // Initializes the named event queues.
        NamedEventQueues.clear();
    }

    /** {@inheritDoc} */
    @Override
    protected void succeeded(Description description) {
        // Verifies the named event queues is all poped.
        assertThat(NamedEventQueues.isAllEmpty())
                .as("Verifies the named event queues is all poped")
                .isTrue();
    }

}
