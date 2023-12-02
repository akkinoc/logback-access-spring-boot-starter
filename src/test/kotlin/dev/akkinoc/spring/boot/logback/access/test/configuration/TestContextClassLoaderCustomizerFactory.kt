package dev.akkinoc.spring.boot.logback.access.test.configuration

import dev.akkinoc.spring.boot.logback.access.test.type.JettyReactiveWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.JettyServletWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.NettyReactiveWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.TomcatReactiveWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.TomcatServletWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.UndertowReactiveWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.UndertowServletWebTest
import io.undertow.Undertow
import org.apache.catalina.startup.Tomcat
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.core.annotation.MergedAnnotations
import org.springframework.core.io.ClassPathResource
import org.springframework.test.context.ContextConfigurationAttributes
import org.springframework.test.context.ContextCustomizerFactory
import org.springframework.util.ClassUtils.convertClassNameToResourcePath
import java.net.URL
import io.undertow.websockets.jsr.Bootstrap as UndertowBootstrap
import org.apache.tomcat.websocket.server.WsSci as TomcatWsSci
import org.eclipse.jetty.server.Server as JettyServer
import org.eclipse.jetty.websocket.jakarta.server.config.JakartaWebSocketServletContainerInitializer as JettyJakartaWebSocketServletContainerInitializer
import reactor.netty.http.server.HttpServer as NettyHttpServer

/**
 * The test context customizer factory to configure the class loader.
 */
class TestContextClassLoaderCustomizerFactory : ContextCustomizerFactory {

    override fun createContextCustomizer(
        testClass: Class<*>,
        attributes: MutableList<ContextConfigurationAttributes>,
    ): TestContextClassLoaderCustomizer {
        val testContextClassLoaderCustomizer = TestContextClassLoaderCustomizer(
            hiddenClasses = getHiddenClasses(testClass),
            additionalClassPaths = getAdditionalClassPaths(testClass),
        )
        log.debug(
            "Creating the {}: {}",
            TestContextClassLoaderCustomizer::class.simpleName,
            testContextClassLoaderCustomizer,
        )
        return testContextClassLoaderCustomizer
    }

    /**
     * Gets the classes to hide from the class loader.
     * Returns unused web server classes to prevent detection by Spring Boot auto-configuration.
     *
     * @param testClass The test class.
     * @return The classes to hide from the class loader.
     */
    private fun getHiddenClasses(testClass: Class<*>): Set<Class<*>> {
        val annotations = MergedAnnotations.from(testClass, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY)
        val usesTomcat = annotations.isPresent(TomcatServletWebTest::class.java) ||
            annotations.isPresent(TomcatReactiveWebTest::class.java)
        val usesJetty = annotations.isPresent(JettyServletWebTest::class.java) ||
            annotations.isPresent(JettyReactiveWebTest::class.java)
        val usesUndertow = annotations.isPresent(UndertowServletWebTest::class.java) ||
            annotations.isPresent(UndertowReactiveWebTest::class.java)
        val usesNetty = annotations.isPresent(NettyReactiveWebTest::class.java)
        return buildSet {
            if (!usesTomcat) add(Tomcat::class.java)
            if (!usesTomcat) add(TomcatWsSci::class.java)
            if (!usesJetty) add(JettyServer::class.java)
            if (!usesJetty) add(JettyJakartaWebSocketServletContainerInitializer::class.java)
            if (!usesUndertow) add(Undertow::class.java)
            if (!usesUndertow) add(UndertowBootstrap::class.java)
            if (!usesNetty) add(NettyHttpServer::class.java)
        }
    }

    /**
     * Gets the class paths to add to the class loader.
     * Returns a URL containing the test class name as the path so that resources can be prepared for each test class.
     * If the test class is inherited, returns URLs for all super classes as well.
     *
     * @param testClass The test class.
     * @return The class paths to add to the class loader.
     */
    private fun getAdditionalClassPaths(testClass: Class<*>): List<URL> {
        return generateSequence(testClass) { it.superclass }
            .takeWhile { it != Any::class.java }
            .map { convertClassNameToResourcePath(it.name) }
            .map { ClassPathResource("$it/") }
            .filter { it.exists() }
            .map { it.url }
            .toList()
    }

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(TestContextClassLoaderCustomizerFactory::class.java)

    }

}
