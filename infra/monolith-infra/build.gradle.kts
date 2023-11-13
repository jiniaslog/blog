
plugins {
    springBootConventions
}

dependencies {
    implementation(project(":libs:core-kernel"))
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.integration:spring-integration-webflux")
}