package net.rakugakibox.springbootext.logback.access.tomcat;

import net.rakugakibox.springbootext.logback.access.AbstractGeneralTest;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The test of general cases on Tomcat.
 */
@SpringApplicationConfiguration
public class TomcatGeneralTest extends AbstractGeneralTest {

    /**
     * The context configuration.
     */
    @Configuration
    @Import(EmbeddedServletContainerAutoConfiguration.EmbeddedTomcat.class)
    public static class TestContextConfiguration {
    }

}
