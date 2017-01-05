package net.rakugakibox.spring.boot.logback.access.test;

import java.io.FileNotFoundException;
import java.net.URL;
import java.net.URLClassLoader;

import lombok.extern.slf4j.Slf4j;
import org.junit.rules.ExternalResource;
import org.springframework.util.ResourceUtils;
import static org.springframework.util.ClassUtils.convertClassNameToResourcePath;
import static org.springframework.util.ClassUtils.getDefaultClassLoader;
import static org.springframework.util.ClassUtils.overrideThreadContextClassLoader;

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
     * The class loader.
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
        this("classpath:" + convertClassNameToResourcePath(location.getName()) + "/");
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
        URL[] urls = new URL[] { ResourceUtils.getURL(location) };
        classLoader = URLClassLoader.newInstance(urls, getDefaultClassLoader());
        originalClassLoader = overrideThreadContextClassLoader(classLoader);
        log.debug("Set the class loader: location=[{}], classLoader=[{}]", location);
    }

    /**
     * Resets the class loader.
     */
    private void resetClassLoader() {
        overrideThreadContextClassLoader(originalClassLoader);
        originalClassLoader = null;
        classLoader = null;
        log.debug("Reset the class loader: location=[{}]", location);
    }

}
