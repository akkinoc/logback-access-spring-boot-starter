package net.rakugakibox.springbootext.logback.access.test;

import static java.util.Collections.unmodifiableMap;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * The controller of JSON resource.
 */
@RestController
@RequestMapping(JsonController.PATH)
public class JsonController {

    /**
     * The path of URL.
     */
    public static final String PATH = "/json";

    /**
     * The JSON resource.
     */
    private static final Map<String, String> resource = new HashMap<>();

    /**
     * Posts (merges) the JSON.
     *
     * @param addition the JSON to merge.
     * @return the JSON.
     */
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public synchronized Map<String, String> post(@RequestBody Map<String, String> addition) {
        resource.putAll(addition);
        return unmodifiableMap(resource);
    }

    /**
     * Gets the JSON.
     *
     * @param additionalKey the key to append.
     * @param additionalValue the value to append.
     * @return the JSON.
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public synchronized Map<String, String> get(
            @RequestParam String additionalKey, @RequestParam String additionalValue) {
        Map<String, String> map = new HashMap<>(resource);
        map.put(additionalKey, additionalValue);
        return unmodifiableMap(map);
    }

    /**
     * Puts the JSON.
     *
     * @param replacement the JSON to replacement.
     */
    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public synchronized void put(@RequestBody Map<String, String> replacement) {
        resource.clear();
        resource.putAll(replacement);
    }

    /**
     * Deletes the JSON.
     */
    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public synchronized void delete() {
        resource.clear();
    }

}
