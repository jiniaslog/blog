plugins {
    springBootConventions
}

group = "kr.co.jiniaslog.blog"

dependencies {
    implementation(project(Modules.Service.Blog.Core.path))
    implementation(project(Modules.Libs.CoreKernel.path))
    implementation(project(Modules.Libs.RestKernel.path))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
}
