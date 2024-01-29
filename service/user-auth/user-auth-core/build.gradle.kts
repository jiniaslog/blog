plugins {
    conventions
}

group = "kr.co.jiniaslog.user"

dependencies {
    implementation(project(Modules.Libs.CoreKernel.path))
    implementation(project(Modules.Service.MessageNexus.path))
    testImplementation(testFixtures(project(Modules.Libs.CoreKernel.path)))
    implementation("io.jsonwebtoken:jjwt:0.12.3")
}
