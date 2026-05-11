import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    application
    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.springframework.boot")
}

dependencies {
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
    implementation(kotlin("reflect"))
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("dev.akkinoc.spring.boot:logback-access-spring-boot-starter")
    implementation("ch.qos.logback.access:logback-access-tomcat:2.0.12")
}

application {
    mainClass = "example.ApplicationKt"
}
