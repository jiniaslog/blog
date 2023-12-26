plugins {
    springBootConventions
}

group = "kr.co.jiniaslog.memo"

dependencies {
    implementation(project(Modules.Service.Memo.Core.path))
    implementation(project(Modules.Libs.CoreKernel.path))
    implementation(project(Modules.Service.Memo.ReadOnly.path))
    implementation("org.springframework.boot:spring-boot-starter-data-neo4j")
    testFixturesApi("org.springframework.boot:spring-boot-starter-data-neo4j")
}