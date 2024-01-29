plugins {
    springBootConventions
}

group = "kr.co.jiniaslog.memo"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation(project(Modules.Service.Memo.Core.path))
    implementation(project(Modules.Service.Memo.ReadOnly.path))
    implementation(project(Modules.Libs.CoreKernel.path))
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
}