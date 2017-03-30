package net.rakugakibox.spring.boot.logback.access.test;

import java.util.Map;
import java.util.concurrent.Callable;
import javax.servlet.http.HttpServletResponse;
import static java.util.Collections.singletonMap;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The test controller.
 */
@RestController
@RequestMapping("/test")
public class TestController {

    /**
     * Gets the text.
     *
     * @return the text.
     */
    @GetMapping(value = "/text", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getText() {
        return "TEST-TEXT";
    }

    /**
     * Gets the text with header.
     *
     * @param response the HTTP response to give a header.
     * @return the text.
     */
    @GetMapping(value = "/text-with-header", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getTextWithHeader(HttpServletResponse response) {
        response.addHeader("X-Test-Header", "TEST-HEADER");
        return getText();
    }

    /**
     * Gets the text asynchronously.
     *
     * @return the callback to return the text.
     */
    @GetMapping(value = "/text-asynchronously", produces = MediaType.TEXT_PLAIN_VALUE)
    public Callable<String> getTextAsynchronously() {
        return this::getText;
    }

    /**
     * Gets the empty text.
     *
     * @return the empty text.
     */
    @GetMapping(value = "/empty-text", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getEmptyText() {
        return "";
    }

    /**
     * Gets the JSON.
     *
     * @return the map to be JSON.
     */
    @GetMapping(value = "/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<?, ?> getJson() {
        return singletonMap("TEST-KEY", "TEST-VALUE");
    }

}
