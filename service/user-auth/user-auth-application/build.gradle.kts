plugins {
    springBootConventions
}

group = "kr.co.jiniaslog.user"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    api(project(Modules.Service.AuthUser.Core.path))
    implementation(project(Modules.Libs.CoreKernel.path))

    testImplementation(testFixtures(project(Modules.Libs.CoreKernel.path)))
    testImplementation(testFixtures(project(Modules.Service.AuthUser.Application.path)))


    testFixturesApi("org.springframework.boot:spring-boot-starter-test")
    testFixturesApi("io.kotest:kotest-assertions-core:5.8.0")
    testFixturesApi("io.mockk:mockk:1.13.8")
    testFixturesApi(project(Modules.Libs.CoreKernel.path))

}
