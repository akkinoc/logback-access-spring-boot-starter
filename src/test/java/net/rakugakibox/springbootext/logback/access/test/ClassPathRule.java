package net.rakugakibox.springbootext.logback.access.test;

import java.io.FileNotFoundException;
import java.net.URL;
import java.net.URLClassLoader;
import lombok.extern.slf4j.Slf4j;
import org.junit.rules.ExternalResource;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;

/**
 * The test rule to dynamically add a class path.
 */
@Slf4j
public class ClassPathRule extends ExternalResource {

    /**
     * The location to search for classes and resources.
     */
    private final String location;

    /**
     * The current class loader.
     */
    private ClassLoader classLoader;

    /**
     * The original class loader.
     */
    private ClassLoader originalClassLoader;

    /**
     * Constructs an instance.
     *
     * @param location the class that indicates the location to search for classes and resources.
     */
    public ClassPathRule(Class<?> location) {
        this("classpath:" + ClassUtils.convertClassNameToResourcePath(location.getName()) + "/");
    }

    /**
     * Constructs an instance.
     *
     * @param location the location to search for classes and resources.
     */
    public ClassPathRule(String location) {
        this.location = location;
    }

    /** {@inheritDoc} */
    @Override
    protected void before() throws Throwable {
        setClassLoader();
    }

    /** {@inheritDoc} */
    @Override
    protected void after() {
        resetClassLoader();
    }

    /**
     * Sets the class loader.
     *
     * @throws FileNotFoundException if the location is not found.
     */
    private void setClassLoader() throws FileNotFoundException {
        if (classLoader != null) {
            throw new IllegalStateException("Class loader is set.");
        }
        log.debug("Setting class loader: location=[{}]", location);
        classLoader = URLClassLoader.newInstance(
                new URL[] { ResourceUtils.getURL(location) },
                ClassUtils.getDefaultClassLoader()
        );
        originalClassLoader = ClassUtils.overrideThreadContextClassLoader(classLoader);
    }

    /**
     * Resets the class loader.
     */
    private void resetClassLoader() {
        if (classLoader == null) {
            throw new IllegalStateException("Class loader is not set.");
        }
        log.debug("Resetting class loader: location=[{}]", location);
        ClassUtils.overrideThreadContextClassLoader(originalClassLoader);
        originalClassLoader = null;
        classLoader = null;
    }

}
