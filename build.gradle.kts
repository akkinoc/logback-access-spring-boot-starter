plugins {
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.kotlin.spring) apply false
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.dokka) apply false
    alias(libs.plugins.publish) apply false
    alias(libs.plugins.release)
}

allprojects {
    group = "dev.akkinoc.spring.boot"
    version = rootProject.scmVersion.version
}
