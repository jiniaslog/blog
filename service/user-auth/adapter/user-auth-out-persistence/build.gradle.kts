plugins {
    springBootConventions
    `kotlin-jpa`
}

group = "kr.co.jiniaslog.user"

dependencies {
    implementation(project(Modules.Service.AuthUser.Application.path))
    implementation(project(Modules.Libs.CoreKernel.path))
    implementation(project(Modules.Libs.JpaKernel.path))
    api("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("mysql:mysql-connector-java:8.0.32")
    implementation("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.9.0")
}
