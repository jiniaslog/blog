plugins {
    springBootConventions
}

group = "kr.co.jiniaslog.memo"

dependencies {
    implementation(project(Modules.Libs.CoreKernel.path))
    implementation(project(Modules.Service.Memo.ReadOnly.path))
    implementation("org.springframework.boot:spring-boot-starter-data-neo4j")
    testFixturesApi("org.springframework.boot:spring-boot-starter-data-neo4j")
    testImplementation("org.testcontainers:testcontainers:1.19.3")
    testImplementation("org.testcontainers:junit-jupiter:1.19.3")
    testImplementation("org.testcontainers:neo4j:1.19.3")
    testImplementation("io.kotest.extensions:kotest-extensions-testcontainers:2.0.2")
    implementation(project(Modules.Service.Memo.Core.path))
    testImplementation(testFixtures(project(Modules.Libs.CoreKernel.path)))
}
