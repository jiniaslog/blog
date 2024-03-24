plugins {
    springBootConventions
}

dependencies {
    api(project(Modules.Libs.CoreKernel.path))
    implementation("org.springframework.boot:spring-boot-starter-web")
}
