subprojects {
    configurations.configureEach {
        resolutionStrategy.dependencySubstitution {
            "logback-access-spring-boot-starter".also { substitute(module("$group:$it")).using(project(":$it")) }
        }
    }
}
