package net.rakugakibox.springbootext.logback.access.test;

import ch.qos.logback.access.spi.IAccessEvent;
import org.assertj.core.api.AbstractAssert;

/**
 * The assertion of the {@link IAccessEvent}.
 */
public class AccessEventAssert<S extends AccessEventAssert<S, A>, A extends IAccessEvent> extends AbstractAssert<S, A> {

    /**
     * Constructs an instance.
     *
     * @param actual the actual value.
     */
    protected AccessEventAssert(A actual) {
        super(actual, AccessEventAssert.class);
    }

    /**
     * Constructs an instance.
     *
     * @param actual the actual value.
     * @param selfClass the self class.
     */
    protected AccessEventAssert(A actual, Class<?> selfClass) {
        super(actual, selfClass);
    }

    /**
     * Starts the assertion.
     *
     * @param <A> the actual value type.
     * @param actual the actual value.
     * @return an assertion instance.
     */
    public static <A extends IAccessEvent> AccessEventAssert<?, A> assertThat(A actual) {
        return new AccessEventAssert<>(actual);
    }

}
