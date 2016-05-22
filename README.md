spring-boot-ext-logback-access
==============================

[![Maven Central][Maven Central Badge]][Maven Central]
[![CircleCI][CircleCI Badge]][CircleCI]

[Maven Central Badge]: https://maven-badges.herokuapp.com/maven-central/net.rakugakibox.springbootext/spring-boot-ext-logback-access/badge.svg
[Maven Central]: https://maven-badges.herokuapp.com/maven-central/net.rakugakibox.springbootext/spring-boot-ext-logback-access
[CircleCI Badge]: https://circleci.com/gh/akihyro/spring-boot-ext-logback-access.svg?style=shield
[CircleCI]: https://circleci.com/gh/akihyro/spring-boot-ext-logback-access

Spring Boot Extension: Logback-access Auto Configuration.  

Requirements
------------

* Java 8+
* Spring Boot 1.3.0+ (web application)
* Any of the following servlet containers:
    * Embedded Tomcat 8.0.28+
    * Embedded Jetty 9.2.14+
* Logback-access 1.1.6+

Usage
-----

### Maven dependency

This artifacts are in the Maven central repository.  
To use this extension on Maven-based projects, use following dependency.  

```xml
<dependency>
    <groupId>net.rakugakibox.springbootext</groupId>
    <artifactId>spring-boot-ext-logback-access</artifactId>
    <version>1.4</version>
</dependency>
```

### Logback-access configuration

Create a Logback-access configuration file to `classpath:logback-access.xml`.  
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

See Also: [Logback-access configuration - Logback-access]  

[Logback-access configuration - Logback-access]: http://logback.qos.ch/access.html#configuration

### Access logging

When access the WEB application, it is logged.  
For example:  

```
192.168.35.1 - - [14/5/2016:17:59:54 +0900] "GET / HTTP/1.1" 200 241
192.168.35.1 - - [14/5/2016:18:00:32 +0900] "GET /favicon.ico HTTP/1.1" 200 946
192.168.35.1 - - [14/5/2016:18:01:21 +0900] "GET / HTTP/1.1" 304 0
```

Configuration properties
------------------------

```yml
logback.access:
  # Enable Logback-access Auto Configuration.
  # Defaults to true.
  enabled: true
  # The location of the configuration file.
  # Auto-detected by default:
  #   => "classpath:logback-access-test.xml"
  #   => "classpath:logback-access.xml"
  #   => "classpath:net/rakugakibox/springbootext/logback/access/logback-access.xml"
  config: "classpath:your-logback-access.xml"
  # for Tomcat.
  tomcat:
    # Enable request attributes to work with the RemoteIpValve enabled with server.useForwardHeaders.
    # Defaults to the the presence of the RemoteIpValve.
    enableRequestAttributes: true
```

Related articles
----------------

* [Provide auto-configuration for logback-access - Issue #2609 - spring-projects/spring-boot - GitHub](https://github.com/spring-projects/spring-boot/issues/2609)
* [Spring Boot: Logback-access が使いやすくなる自動設定を作って公開した - rakugakibox.net](http://blog.rakugakibox.net/entry/2015/12/25/spring-boot-ext-logback-access)

Contributing
------------

Bug reports and pull requests are welcome :)  

License
-------

Licensed under the [Apache License, Version 2.0].  

[Apache License, Version 2.0]: http://www.apache.org/licenses/LICENSE-2.0
