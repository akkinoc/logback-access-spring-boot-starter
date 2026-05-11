import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    application
    id("org.springframework.boot")
}

dependencies {
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
    implementation("org.springframework.boot:spring-boot-starter-webmvc") {
        exclude("org.springframework.boot", "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-jetty")
    implementation("dev.akkinoc.spring.boot:logback-access-spring-boot-starter")
    implementation("ch.qos.logback.access:logback-access-jetty12:2.0.12")
}

application {
    mainClass = "example.Application"
}
