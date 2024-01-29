import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.jacoco
import org.gradle.kotlin.dsl.kotlin
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension

plugins {
    kotlin("jvm")
    jacoco
}

jacoco {
    toolVersion = "0.8.11"
}

tasks.test {
    extensions.configure(JacocoTaskExtension::class) {
        setDestinationFile(file("$buildDir/jacoco/jacoco.exec"))
    }
    finalizedBy("jacocoTestReport")
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(false)
        csv.required.set(false)
        html.required.set(true)
    }
    finalizedBy("jacocoTestCoverageVerification")

    classDirectories.setFrom(
        fileTree(project.buildDir) {
            exclude(
                "**/Q*.*",
                "**/*Test.*"
            )
            include(
                "**/classes/**/main/**"
            )
        }
    )
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                counter = "BRANCH"
                minimum = "0.7".toBigDecimal()
            }

            limit {
                counter = "LINE"
                minimum = "0.7".toBigDecimal()
            }
        }
    }
    classDirectories.setFrom(
        fileTree(project.buildDir) {
            exclude(
                "**/Q*.*",
                "**/*Test.*"
            )
            include(
                "**/classes/**/main/**"
            )
        }
    )
}