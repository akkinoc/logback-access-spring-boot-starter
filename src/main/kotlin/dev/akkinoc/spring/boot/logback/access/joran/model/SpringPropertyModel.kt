package dev.akkinoc.spring.boot.logback.access.joran.model

import ch.qos.logback.core.model.NamedModel

// Copied from org.springframework.boot.logging.logback.SpringPropertyModel since it was package-private
class SpringPropertyModel : NamedModel() {
    var scope: String = ""
    var defaultValue: String = ""
    var source: String = ""
}