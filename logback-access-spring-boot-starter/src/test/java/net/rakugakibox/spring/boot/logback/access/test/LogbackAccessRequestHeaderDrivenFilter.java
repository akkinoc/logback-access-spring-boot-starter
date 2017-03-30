package net.rakugakibox.spring.boot.logback.access.test;

import java.util.stream.Stream;

import ch.qos.logback.access.spi.IAccessEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import lombok.Getter;
import lombok.Setter;

/**
 * The Logback-access filter driven by request header.
 */
public class LogbackAccessRequestHeaderDrivenFilter extends Filter<IAccessEvent> {

    /**
     * The header name of filter reply.
     */
    @Getter
    @Setter
    private String headerName = "X-Filter-Reply";

    /** {@inheritDoc} */
    @Override
    public FilterReply decide(IAccessEvent event) {
        String filterReply = event.getRequestHeader(headerName);
        return Stream.of(FilterReply.values())
                .filter(value -> value.name().equalsIgnoreCase(filterReply))
                .findFirst()
                .orElse(FilterReply.NEUTRAL);
    }

}
