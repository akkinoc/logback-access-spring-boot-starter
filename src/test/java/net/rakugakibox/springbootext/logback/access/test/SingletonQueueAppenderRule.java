package net.rakugakibox.springbootext.logback.access.test;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * The test rule for using {@link SingletonQueueAppender}.
 */
public class SingletonQueueAppenderRule extends TestWatcher {

    /** {@inheritDoc} */
    @Override
    protected void starting(Description description) {
        // Initializes the event queue.
        SingletonQueueAppender.clear();
    }

    /** {@inheritDoc} */
    @Override
    protected void succeeded(Description description) {
        // Verifies the event queue is all poped.
        assertThat(SingletonQueueAppender.isEmpty())
                .as("Verifies SingletonQueueAppender is all poped")
                .isTrue();
    }

}
