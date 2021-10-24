package dev.akkinoc.spring.boot.logback.access.test.mock

import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.util.concurrent.CompletableFuture

/**
 * The mock controller for testing using the web server.
 */
@RestController
@RequestMapping("/mock-controller")
class MockController {

    /**
     * Gets a text.
     *
     * @return A text.
     */
    @GetMapping("/text")
    fun getText(): String {
        val response = "mock-text"
        log.debug("Getting a text: {}", response)
        return response
    }

    /**
     * Gets a text with response headers.
     *
     * @return A [ResponseEntity] to return a text with response headers.
     */
    @GetMapping("/text-with-response-headers")
    fun getTextWithResponseHeaders(): ResponseEntity<String> {
        val response = ResponseEntity.ok()
                .header("a", "value @a")
                .header("b", "value1 @b", "value2 @b")
                .header("c", "")
                .body("mock-text")
        log.debug("Getting a text with response headers: {}", response)
        return response
    }

    /**
     * Gets an empty text.
     *
     * @return An empty text.
     */
    @GetMapping("/empty-text")
    fun getEmptyText(): String {
        val response = ""
        log.debug("Getting an empty text: {}", response)
        return response
    }

    /**
     * Gets a text asynchronously.
     *
     * @return A [CompletableFuture] to return a text asynchronously.
     */
    @GetMapping("/text-asynchronously")
    fun getTextAsynchronously(): CompletableFuture<String> {
        val response = CompletableFuture.supplyAsync { "mock-text" }
        log.debug("Getting a text asynchronously: {}", response)
        return response
    }

    /**
     * Gets a text with chunked transfer encoding.
     *
     * @return A [Flux] to return a text with chunked transfer encoding.
     */
    @GetMapping("/text-with-chunked-transfer-encoding")
    fun getTextWithChunkedTransferEncoding(): Flux<String> {
        val response = Flux.just("mock-text")
        log.debug("Getting a text with chunked transfer encoding: {}", response)
        return response
    }

    /**
     * Posts the text.
     *
     * @param posted The posted text.
     * @return A text.
     */
    @PostMapping("/text")
    fun postText(@RequestBody posted: String): String {
        val response = "mock-text"
        log.debug("Posting the text: {} => {}", posted, response)
        return response
    }

    /**
     * Posts the form data.
     *
     * @param posted The posted form data.
     * @return A text.
     */
    @PostMapping("/form-data")
    fun postFormData(@ModelAttribute posted: FormData): String {
        val response = "mock-text"
        log.debug("Posting the form data: {} => {}", posted, response)
        return response
    }

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(MockController::class.java)

    }

    /**
     * The form data.
     *
     * @property a The string value.
     * @property b The string values.
     * @property c The string value.
     */
    data class FormData(val a: String?, val b: List<String>?, val c: String?)

}
