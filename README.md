spring-boot-ext-logback-access
==============================

Spring Boot Extension: Logback-access Auto Configuration.

Status
------

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/net.rakugakibox.springbootext/spring-boot-ext-logback-access/badge.svg)](https://maven-badges.herokuapp.com/maven-central/net.rakugakibox.springbootext/spring-boot-ext-logback-access)
[![Circle CI](https://circleci.com/gh/akihyro/spring-boot-ext-logback-access.svg?style=shield)](https://circleci.com/gh/akihyro/spring-boot-ext-logback-access)

Supported servlet containers
----------------------------

* Embedded Tomcat
* Embedded Jetty

Usage
-----

### Maven dependency:

```xml
<dependency>
    <groupId>net.rakugakibox.springbootext</groupId>
    <artifactId>spring-boot-ext-logback-access</artifactId>
    <version>1.0</version>
</dependency>
```

### Configuration properties (application.yml):

```yml
logback.access:
  config: "classpath:your-logback-access.xml"
    # Auto-detected by default.
    #   => "classpath:logback-access-test.xml"
    #   => "classpath:logback-access.xml"
    #   => "classpath:net/rakugakibox/springbootext/logback/access/logback-access.xml"
```

### Logback-access configuration example (logback-access.xml)

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

See Also: [HTTP-access logs with logback-access, Jetty and Tomcat - Logback-access](http://logback.qos.ch/access.html)

License
-------

Licensed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).
