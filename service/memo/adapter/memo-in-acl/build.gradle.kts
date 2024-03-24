plugins {
    springBootConventions
}

group = "kr.co.jiniaslog.memo"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation(project(Modules.Service.Memo.Core.path))
    implementation(project(Modules.Service.Memo.ReadOnly.path))
    implementation(project(Modules.Libs.CoreKernel.path))
}
