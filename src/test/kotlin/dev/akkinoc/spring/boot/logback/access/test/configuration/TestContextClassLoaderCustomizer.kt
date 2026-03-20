package dev.akkinoc.spring.boot.logback.access.test.configuration

import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.boot.test.context.FilteredClassLoader
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextCustomizer
import org.springframework.test.context.MergedContextConfiguration
import java.net.URL
import java.net.URLClassLoader

/**
 * The test context customizer to configure the class loader.
 *
 * @property hiddenClasses The classes to hide.
 * @property additionalClassPaths The class paths to add.
 */
data class TestContextClassLoaderCustomizer(
    private val hiddenClasses: Set<Class<*>> = emptySet(),
    private val additionalClassPaths: List<URL> = emptyList(),
) : ContextCustomizer {

    override fun customizeContext(context: ConfigurableApplicationContext, config: MergedContextConfiguration) {
        hideClasses(context)
        addClassPaths(context)
        log.debug("Customized the {}: {}", ConfigurableApplicationContext::class.simpleName, context)
    }

    /**
     * Hides the specified classes from the class loader.
     *
     * @param context The test context.
     */
    private fun hideClasses(context: ConfigurableApplicationContext) {
        if (hiddenClasses.isEmpty()) return
        context.setClassLoader(FilteredClassLoader(*hiddenClasses.toTypedArray()))
    }

    /**
     * Adds the specified class paths to the class loader.
     *
     * @param context The test context.
     */
    private fun addClassPaths(context: ConfigurableApplicationContext) {
        if (additionalClassPaths.isEmpty()) return
        val urls = additionalClassPaths.toTypedArray()
        val parent = context.classLoader
        checkNotNull(parent) { "Failed to get the current class loader: $context" }
        context.setClassLoader(URLClassLoader(urls, parent))
    }

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(TestContextClassLoaderCustomizer::class.java)

    }

}
