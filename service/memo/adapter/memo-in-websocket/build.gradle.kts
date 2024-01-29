
plugins {
    springBootConventions
}

group = "kr.co.jiniaslog.memo"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation(project(Modules.Service.Memo.Core.path))
    implementation(project(Modules.Libs.CoreKernel.path))
}