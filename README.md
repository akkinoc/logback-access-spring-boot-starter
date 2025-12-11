# logback-access-spring-boot-starter

[![maven central badge]][maven central]
[![javadoc badge]][javadoc]
[![release badge]][release]
[![build badge]][build]
[![codecov badge]][codecov]
[![license badge]][license]
[![sponsor badge]][sponsor]

[maven central]: https://maven-badges.herokuapp.com/maven-central/dev.akkinoc.spring.boot/logback-access-spring-boot-starter
[maven central badge]: https://maven-badges.herokuapp.com/maven-central/dev.akkinoc.spring.boot/logback-access-spring-boot-starter/badge.svg
[javadoc]: https://javadoc.io/doc/dev.akkinoc.spring.boot/logback-access-spring-boot-starter
[javadoc badge]: https://javadoc.io/badge2/dev.akkinoc.spring.boot/logback-access-spring-boot-starter/javadoc.svg
[release]: https://github.com/akkinoc/logback-access-spring-boot-starter/releases
[release badge]: https://img.shields.io/github/v/release/akkinoc/logback-access-spring-boot-starter?color=brightgreen&sort=semver
[build]: https://github.com/akkinoc/logback-access-spring-boot-starter/actions/workflows/build.yml
[build badge]: https://github.com/akkinoc/logback-access-spring-boot-starter/actions/workflows/build.yml/badge.svg
[codecov]: https://codecov.io/gh/akkinoc/logback-access-spring-boot-starter
[codecov badge]: https://codecov.io/gh/akkinoc/logback-access-spring-boot-starter/branch/main/graph/badge.svg
[license]: LICENSE.txt
[license badge]: https://img.shields.io/github/license/akkinoc/logback-access-spring-boot-starter?color=blue
[sponsor]: https://github.com/sponsors/akkinoc
[sponsor badge]: https://img.shields.io/static/v1?logo=github&label=sponsor&message=%E2%9D%A4&color=db61a2

[Spring Boot] Starter for [Logback-access].

[Spring Boot]: https://spring.io/projects/spring-boot
[Logback-access]: https://logback.qos.ch/access.html

## Features

* Auto-detects your configuration file and auto-configures Logback-access.
* Supports configuration files on the classpath.
* Provides extensions (`<springProfile>` tag, `<springProperty>` tag) for configuration files.
* Supports rewriting of some attributes by HTTP forward headers ("X-Forwarded-*").
* Supports remote user provided by Spring Security.
* Provides configuration properties to enable the tee filter.

Supports the following web servers:

|          | Web MVC (Servlet Stack) | WebFlux (Reactive Stack) |
|:--------:|:-----------------------:|:------------------------:|
|  Tomcat  |            ✅           |            ✅            |
|  Jetty   |            ✅           |            ✅            |
|  Netty   |            -            |  🚧 (under development)  |

## Dependencies

Depends on:

* Java 17 or later
* Kotlin 2.2 or later
* Spring Boot 4.0
* Logback-access 2.0

## Usage

### Adding the Dependency

The artifact is published on [Maven Central Repository][maven central].
If you are using Maven, add the following dependency.

```xml
<dependency>
    <groupId>dev.akkinoc.spring.boot</groupId>
    <artifactId>logback-access-spring-boot-starter</artifactId>
    <version>${logback-access-spring-boot-starter.version}</version>
</dependency>
```

If you are using the Tomcat web server, also add the following dependency.

```xml
<dependency>
    <groupId>ch.qos.logback.access</groupId>
    <artifactId>logback-access-tomcat</artifactId>
    <version>${logback-access.version}</version>
</dependency>
```

If you are using the Jetty web server, also add the following dependency.

```xml
<dependency>
    <groupId>ch.qos.logback.access</groupId>
    <artifactId>logback-access-jetty12</artifactId>
    <version>${logback-access.version}</version>
</dependency>
```

### Configuring the Logback-access

Create a Logback-access configuration file "logback-access.xml" in the root of the classpath.

For example:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <appender name="console" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>common</pattern>
        </encoder>
    </appender>
    <appender-ref ref="console"/>
</configuration>
```

See also the Logback-access official documents:

* [Logback-access configuration](https://logback.qos.ch/access.html)

### Access Logging

When access the web application, it is logged.

For example:

```console
0:0:0:0:0:0:0:1 - - [24/Oct/2021:15:32:03 +0900] "GET / HTTP/1.1" 200 319
0:0:0:0:0:0:0:1 - - [24/Oct/2021:15:32:03 +0900] "GET /favicon.ico HTTP/1.1" 404 111
0:0:0:0:0:0:0:1 - - [24/Oct/2021:15:32:04 +0900] "GET / HTTP/1.1" 304 0
```

## Auto-detection of Configuration File

### Priority Order

When the web application is started, the configuration files are searched in the following order.
The first configuration file found will be used.

1. "logback-access-test.xml" in the root of the classpath.
2. "logback-access.xml" in the root of the classpath.
3. "logback-access-test-spring.xml" in the root of the classpath.
4. "logback-access-spring.xml" in the root of the classpath.
5. [fallback configuration file (appends to the console with a common pattern)](src/main/resources/dev/akkinoc/spring/boot/logback/access/logback-access-spring.xml).

### Separation for Testing

If you are using Maven and place the "logback-access-test(-spring).xml" file under the "src/test/resources" folder,
Maven will ensure that it won't be included in the artifact produced.
Thus, you can use a different configuration file "logback-access-test(-spring).xml" during testing,
and another file "logback-access(-spring).xml" in production.
This is the same concept as the [Logback configuration ("logback.xml" and "logback-test.xml")].

[Logback configuration ("logback.xml" and "logback-test.xml")]: https://logback.qos.ch/manual/configuration.html#auto_configuration

## Extensions for Configuration File

### Profile-specific Configuration

The `<springProfile>` tag lets you optionally include or exclude sections of configuration based on the active Spring profiles.
The usage of this extension is the same as the [Spring Boot Logback Extension "Profile-specific Configuration"].

[Spring Boot Logback Extension "Profile-specific Configuration"]: https://docs.spring.io/spring-boot/reference/features/logging.html#features.logging.logback-extensions.profile-specific

> ```xml
> <springProfile name="staging">
>     <!-- configuration to be enabled when the "staging" profile is active -->
> </springProfile>
> <springProfile name="dev | staging">
>     <!-- configuration to be enabled when the "dev" or "staging" profiles are active -->
> </springProfile>
> <springProfile name="!production">
>     <!-- configuration to be enabled when the "production" profile is not active -->
> </springProfile>
> ```

### Environment Properties

The `<springProperty>` tag lets you expose properties from the Spring Environment for use within Logback.
The usage of this extension is the same as the [Spring Boot Logback Extension "Environment Properties"].

[Spring Boot Logback Extension "Environment Properties"]: https://docs.spring.io/spring-boot/reference/features/logging.html#features.logging.logback-extensions.environment-properties

> ```xml
> <springProperty scope="context" name="fluentHost" source="myapp.fluentd.host" defaultValue="localhost"/>
> <appender name="FLUENT" class="ch.qos.logback.more.appenders.DataFluentAppender">
>     <remoteHost>${fluentHost}</remoteHost>
>     ...
> </appender>
> ```

## Configuration Properties

Provides the following configuration properties.
These can be configured by your "application.yml", "application.properties", etc.

```yaml
# The configuration properties for Logback-access.
logback.access:
  # Whether to enable auto-configuration.
  # Defaults to true.
  enabled: true
  # The location of the configuration file.
  # Specify a URL that starts with "classpath:" or "file:".
  # Auto-detected by default:
  #   1. "classpath:logback-access-test.xml"
  #   2. "classpath:logback-access.xml"
  #   3. "classpath:logback-access-test-spring.xml"
  #   4. "classpath:logback-access-spring.xml"
  #   5. "classpath:dev/akkinoc/spring/boot/logback/access/logback-access-spring.xml"
  config: classpath:your-logback-access.xml
  # The strategy to change the behavior of IAccessEvent.getLocalPort.
  # Defaults to "server".
  #   "local":
  #     Returns the port number of the interface on which the request was received.
  #     Equivalent to ServletRequest.getLocalPort when using a servlet web server.
  #   "server":
  #     Returns the port number to which the request was sent.
  #     Equivalent to ServletRequest.getServerPort when using a servlet web server.
  #     Helps to identify the destination port number used by the client when forward headers are enabled.
  local-port-strategy: server
  # The properties for the Tomcat web server.
  tomcat:
    # Whether to enable the request attributes to work with RemoteIpValve.
    # Defaults to the presence of RemoteIpValve enabled by the property "server.forward-headers-strategy=native".
    request-attributes-enabled: true
  # The properties for the tee filter.
  tee-filter:
    # Whether to enable the tee filter.
    # Defaults to false.
    enabled: true
    # The host names to activate.
    # By default, all hosts are activated.
    includes: your-development-host
    # The host names to deactivate.
    # By default, all hosts are activated.
    excludes: your-production-host
```

## API Reference

Please refer to the [Javadoc][javadoc].

## Release Notes

Please refer to the [Releases][release] page.

## License

Licensed under the [Apache License, Version 2.0][license].

## Support the Project

If this project is useful to you, I appreciate giving a ⭐ star to this repository.
I would also appreciate if you would consider 💖 [sponsoring][sponsor] as well.
Your support is my biggest motive force. Thanks ✨
