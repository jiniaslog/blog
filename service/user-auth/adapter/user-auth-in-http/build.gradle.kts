
plugins {
    springBootConventions
}

group = "kr.co.jiniaslog.user"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation(project(Modules.Service.AuthUser.Application.path))
    implementation(project(Modules.Libs.CoreKernel.path))
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
}
