rootProject.name = "logback-access-spring-boot-starter"

include("logback-access-spring-boot-starter")
include("examples:webmvc-tomcat-java")
include("examples:webmvc-tomcat-kotlin")
include("examples:webmvc-jetty-java")
include("examples:webflux-tomcat-java")
include("examples:webflux-jetty-java")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}
