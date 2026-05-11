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
    implementation("org.springframework.boot:spring-boot-starter-tomcat")
    implementation("dev.akkinoc.spring.boot:logback-access-spring-boot-starter:$version")
    implementation("ch.qos.logback.access:logback-access-tomcat:2.0.12")
}

application {
    mainClass = "example.Application"
}
