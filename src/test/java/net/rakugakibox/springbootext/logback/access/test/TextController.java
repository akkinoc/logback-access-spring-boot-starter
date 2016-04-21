package net.rakugakibox.springbootext.logback.access.test;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * The controller of text resource.
 */
@RestController
@RequestMapping(TextController.PATH)
public class TextController {

    /**
     * The path of URL.
     */
    public static final String PATH = "/text";

    /**
     * The text resource.
     */
    private static String resource = "";

    /**
     * Posts (appends) the text.
     *
     * @param addition the text to append.
     * @return the text.
     */
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.TEXT_PLAIN_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public synchronized String post(@RequestBody String addition) {
        resource += addition;
        return resource;
    }

    /**
     * Gets the text.
     *
     * @param addition the text to append.
     * @return the text.
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public synchronized String get(@RequestParam String addition) {
        return resource + addition;
    }

    /**
     * Puts the text.
     *
     * @param replacement the text to replace.
     */
    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public synchronized void put(@RequestBody String replacement) {
        resource = replacement;
    }

    /**
     * Deletes the text.
     */
    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public synchronized void delete() {
        resource = "";
    }

}
