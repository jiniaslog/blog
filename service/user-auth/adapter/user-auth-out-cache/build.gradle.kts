plugins {
    springBootConventions
}

group = "kr.co.jiniaslog.user"

dependencies {
    implementation(project(Modules.Service.AuthUser.Application.path))
    implementation(project(Modules.Libs.CoreKernel.path))
    implementation("org.redisson:redisson-spring-boot-starter:3.17.7")
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")
}
