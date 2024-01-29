
plugins {
    springBootConventions
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation(project(Modules.Service.Media.Core.path))
    implementation(project(Modules.Libs.CoreKernel.path))
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
}