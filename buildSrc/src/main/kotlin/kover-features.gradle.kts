import kotlinx.kover.gradle.plugin.dsl.AggregationType
import kotlinx.kover.gradle.plugin.dsl.GroupingEntityType
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.jacoco
import org.gradle.kotlin.dsl.kotlin
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension

plugins {
    kotlin("jvm")
    id("org.jetbrains.kotlinx.kover")
}

koverReport {
    verify {
        rule {
            isEnabled = true
            bound {
                minValue = 1
                maxValue = 99
                metric = kotlinx.kover.gradle.plugin.dsl.MetricType.LINE
                aggregation = AggregationType.COVERED_PERCENTAGE
            }
            minBound(2)
            maxBound(98)
        }
        defaults {
            html {
                title = "My report title"
                charset = "UTF-8"
                onCheck = true
                setReportDir(layout.buildDirectory.dir("$buildDir/reports/kover/html-result"))
            }
            verify {
                onCheck = true
            }
        }
    }
}