plugins {
    id("conventions")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("plugin.spring")
}


tasks.findByName("bootJar")?.let {
    it.enabled = false
}

tasks.named<Jar>("jar") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    enabled = true
    archiveFileName.set("${project.parent?.parent?.name}-${project.parent?.name}-${project.name}.jar")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}