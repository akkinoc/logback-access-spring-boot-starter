package net.rakugakibox.spring.boot.logback.access;

import ch.qos.logback.access.spi.IAccessEvent;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * The application event indicating that Logback-access event was appended.
 */
public class LogbackAccessAppendedEvent extends ApplicationEvent {

    /**
     * The Logback-access event.
     */
    @Getter
    private final IAccessEvent event;

    /**
     * Constructs an instance.
     *
     * @param source the object on which the event initially occurred.
     * @param event the Logback-access event.
     */
    public LogbackAccessAppendedEvent(Object source, IAccessEvent event) {
        super(source);
        this.event = event;
    }

}
