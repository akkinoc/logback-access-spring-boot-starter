package net.rakugakibox.springbootext.logback.access.test;

import java.util.concurrent.atomic.AtomicReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * The REST Controller of text resource.
 */
@RestController
@RequestMapping(value = TextRestController.PATH, produces = MediaType.TEXT_PLAIN_VALUE)
public class TextRestController {

    /**
     * The path of URL.
     */
    public static final String PATH = "/text";

    /**
     * The text resource.
     */
    private static final AtomicReference<String> resource = new AtomicReference<>();

    /**
     * Posts (appends) the text.
     *
     * @param text the text.
     * @return the text.
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String post(@RequestBody String text) {
        return resource.accumulateAndGet(text, String::concat);
    }

    /**
     * Gets the text.
     *
     * @return the text.
     */
    @RequestMapping(method = RequestMethod.GET)
    public String get(@RequestParam(required = false, defaultValue = "") String addition) {
        return resource.get() + addition;
    }

    /**
     * Puts the text.
     *
     * @param text the text.
     */
    @RequestMapping(method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void put(@RequestBody String text) {
        resource.set(text);
    }

    /**
     * Deletes the text.
     */
    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        resource.set(null);
    }

}
