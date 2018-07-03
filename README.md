# logback-access-spring-boot-starter

[![Maven Central][Maven Central Badge]][Maven Central]
[![javadoc.io][javadoc.io Badge]][javadoc.io]
[![CircleCI][CircleCI Badge]][CircleCI]
[![Codecov][Codecov Badge]][Codecov]
[![License][License Badge]][License]

[Maven Central Badge]: https://maven-badges.herokuapp.com/maven-central/net.rakugakibox.spring.boot/logback-access-spring-boot-starter/badge.svg
[Maven Central]: https://maven-badges.herokuapp.com/maven-central/net.rakugakibox.spring.boot/logback-access-spring-boot-starter
[javadoc.io Badge]: https://www.javadoc.io/badge/net.rakugakibox.spring.boot/logback-access-spring-boot-starter.svg
[javadoc.io]: https://www.javadoc.io/doc/net.rakugakibox.spring.boot/logback-access-spring-boot-starter
[CircleCI Badge]: https://circleci.com/gh/akihyro/logback-access-spring-boot-starter.svg?style=shield
[CircleCI]: https://circleci.com/gh/akihyro/logback-access-spring-boot-starter
[Codecov Badge]: https://codecov.io/gh/akihyro/logback-access-spring-boot-starter/branch/master/graph/badge.svg
[Codecov]: https://codecov.io/gh/akihyro/logback-access-spring-boot-starter
[License Badge]: https://img.shields.io/badge/license-Apache%202.0-brightgreen.svg
[License]: LICENSE.txt

[Spring Boot] Starter for [Logback-access].  

[Spring Boot]: http://spring.io/projects/spring-boot
[Logback-access]: https://logback.qos.ch/access.html

Note: This page is for Spring Boot 2. If you use Spring Boot 1, please refer to [v2.6.x branch].  
Note: This artifact name was changed in [v2.0.0]. The old name is "spring-boot-ext-logback-access".  

[v2.6.x branch]: https://github.com/akihyro/logback-access-spring-boot-starter/tree/v2.6.x
[v2.0.0]: https://github.com/akihyro/logback-access-spring-boot-starter/releases/tag/v2.0.0

## Features

* Auto-detects your configuration file and configures Logback-access.
* Supports configuration files on the classpath.
* Supports `X-Forwarded-*` HTTP headers.
* Supports `HttpServletRequest#getRemoteUser()` provided by Spring Security.
* Provides extensions of configuration file.
    * `<springProfile>` tag.
    * `<springProperty>` tag.

## Supported versions

"logback-access-spring-boot-starter" supports the following versions.  
Other versions might also work, but we have not tested it.  

* Java 8
* Spring Boot 2.0.0
* Embedded Tomcat 8.5.28
* Embedded Jetty 9.4.8
* Embedded Undertow 1.4.22
* Logback-access 1.2.3

## Usage

### Adding the dependency

"logback-access-spring-boot-starter" is published on Maven Central Repository.  
If you are using Maven, add the following dependency.  

```xml
<dependency>
    <groupId>net.rakugakibox.spring.boot</groupId>
    <artifactId>logback-access-spring-boot-starter</artifactId>
    <version>2.7.1</version>
</dependency>
```

### Configuring the Logback-access

Create a Logback-access configuration file "logback-access.xml" in the root of the classpath.  

For example:  

```xml
<configuration>
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>common</pattern>
        </encoder>
    </appender>
    <appender-ref ref="CONSOLE" />
</configuration>
```

See also the Logback official documents:  

* [Logback-access configuration]

[Logback-access configuration]: https://logback.qos.ch/access.html#configuration

### Access logging

When access the web application, it is logged.  

For example:  

```
192.168.35.1 - - [14/5/2016:17:59:54 +0900] "GET / HTTP/1.1" 200 241
192.168.35.1 - - [14/5/2016:18:00:32 +0900] "GET /favicon.ico HTTP/1.1" 200 946
192.168.35.1 - - [14/5/2016:18:01:21 +0900] "GET / HTTP/1.1" 304 0
```

## Auto-detection of configuration file

### Priority order

"logback-access-spring-boot-starter" will look for the configuration file in the following order.  
The first found configuration file will be used.  

1. "logback-access-test.xml" in the root of the classpath.
2. "logback-access.xml" in the root of the classpath.
3. "logback-access-test-spring.xml" in the root of the classpath.
4. "logback-access-spring.xml" in the root of the classpath.
5. [fallback configuration file (appends to standard output with common pattern)].

[fallback configuration file (appends to standard output with common pattern)]: logback-access-spring-boot-starter/src/main/resources/net/rakugakibox/spring/boot/logback/access/logback-access-spring.xml

### Separation for testing

If you are using Maven and place "logback-access-test.xml" under the "src/test/resources" folder,
Maven does not include it in the artifact.  
Thus, you can use a different configuration file "logback-access-test.xml" during testing
and another file "logback-access.xml" in production.  
This is the same concept as [Logback configuration ("logback.xml" and "logback-test.xml")].  

[Logback configuration ("logback.xml" and "logback-test.xml")]: https://logback.qos.ch/manual/configuration.html#auto_configuration

## Extensions of configuration file

### `<springProfile>` tag: Profile-specific configuration

The `<springProfile>` tag allows you to optionally include or exclude sections of configuration based on the active Spring profiles.  
Usage of this extension follows [Spring Boot Logback extension "Profile-specific configuration"].  

> ```xml
> <springProfile name="staging">
>     <!-- configuration to be enabled when the "staging" profile is active -->
> </springProfile>
> 
> <springProfile name="dev, staging">
>     <!-- configuration to be enabled when the "dev" or "staging" profiles are active -->
> </springProfile>
> 
> <springProfile name="!production">
>     <!-- configuration to be enabled when the "production" profile is not active -->
> </springProfile>
> ```

[Spring Boot Logback extension "Profile-specific configuration"]: https://docs.spring.io/spring-boot/docs/2.0.0.RELEASE/reference/htmlsingle/#_profile_specific_configuration

### `<springProperty>` tag: Environment properties

The `<springProperty>` tag allows you to surface properties from the Spring `Environment`.  
Usage of this extension follows [Spring Boot Logback extension "Environment properties"].  

> ```xml
> <springProperty scope="context" name="fluentHost" source="myapp.fluentd.host" defaultValue="localhost" />
> <appender name="FLUENT" class="ch.qos.logback.more.appenders.DataFluentAppender">
>     <remoteHost>${fluentHost}</remoteHost>
>     ...
> </appender>
> ```

[Spring Boot Logback extension "Environment properties"]: https://docs.spring.io/spring-boot/docs/2.0.0.RELEASE/reference/htmlsingle/#_environment_properties

## Configuration properties

"logback-access-spring-boot-starter" provides the following configuration properties.  
These can be configure by your "application.yml" / "application.properties".  

```yml
logback.access:
  # Whether to enable auto-configuration.
  # Defaults to true.
  enabled: true
  # The location of the configuration file.
  # Auto-detected by default:
  #   1. "classpath:logback-access-test.xml"
  #   2. "classpath:logback-access.xml"
  #   3. "classpath:logback-access-test-spring.xml"
  #   4. "classpath:logback-access-spring.xml"
  #   5. "classpath:net/rakugakibox/spring/boot/logback/access/logback-access-spring.xml"
  config: "classpath:your-logback-access.xml"
  # Whether to use the server port (HttpServletRequest#getServerPort())
  # instead of the local port (HttpServletRequest#getLocalPort()) within IAccessEvent#getLocalPort().
  # Defaults to true.
  useServerPortInsteadOfLocalPort: true
  # for Tomcat.
  tomcat:
    # Whether to enable request attributes to work with the RemoteIpValve enabled with "server.useForwardHeaders".
    # Defaults to the presence of the RemoteIpValve.
    enableRequestAttributes: true
```

## Release notes

Please refer to the "[Releases]" page.  

[Releases]: https://github.com/akihyro/logback-access-spring-boot-starter/releases

## Related articles

* [Issue #2609 Add logback-access-spring-boot-starter to 3rd party starter list - GitHub spring-projects/spring-boot]
* [Spring Boot: Logback-access が使いやすくなる自動設定を作って公開した - rakugakibox.net]

[Issue #2609 Add logback-access-spring-boot-starter to 3rd party starter list - GitHub spring-projects/spring-boot]: https://github.com/spring-projects/spring-boot/issues/2609
[Spring Boot: Logback-access が使いやすくなる自動設定を作って公開した - rakugakibox.net]: http://blog.rakugakibox.net/entry/2015/12/25/spring-boot-ext-logback-access

## Contributing

Bug reports and pull requests are welcome :)  

## Building and testing

To build and test, you can run:  

```sh
$ cd logback-access-spring-boot-starter
$ ./mvnw clean install
```

## License

Licensed under the [Apache License, Version 2.0].  

[Apache License, Version 2.0]: LICENSE.txt
