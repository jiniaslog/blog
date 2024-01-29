plugins {
    conventions
    id("com.google.devtools.ksp") version "1.9.0-1.0.13"
}

dependencies {
    implementation(project(":libs:core-kernel"))
    implementation(project(":libs:messaging-handler-generator"))
    ksp(project(":libs:messaging-handler-generator"))
}

tasks.withType<org.jmailen.gradle.kotlinter.tasks.ConfigurableKtLintTask>().configureEach {
    exclude { it.file.path.contains("build/generated") }
}