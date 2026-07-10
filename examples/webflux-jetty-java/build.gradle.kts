import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    application
    id("org.springframework.boot")
}

dependencies {
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
    implementation("org.springframework.boot:spring-boot-starter-webflux") {
        exclude("org.springframework.boot", "spring-boot-starter-reactor-netty")
    }
    implementation("org.springframework.boot:spring-boot-starter-jetty")
    implementation("dev.akkinoc.spring.boot:logback-access-spring-boot-starter:$version")
    implementation("ch.qos.logback.access:logback-access-jetty12:2.0.13")
}

application {
    mainClass = "example.Application"
}
