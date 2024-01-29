plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

java.sourceCompatibility = JavaVersion.VERSION_17

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")
    implementation("org.jetbrains.kotlin:kotlin-allopen:1.9.0")
    implementation("org.jetbrains.kotlin:kotlin-noarg:1.9.0")
    implementation("org.jmailen.gradle:kotlinter-gradle:4.1.0")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:3.2.0")
    implementation("io.spring.gradle:dependency-management-plugin:1.1.4")
    implementation("org.jetbrains.kotlinx:kover-gradle-plugin:0.7.5")
}
