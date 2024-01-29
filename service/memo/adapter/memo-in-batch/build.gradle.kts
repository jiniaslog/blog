
plugins {
    springBootConventions
}

group = "kr.co.jiniaslog.memo"

dependencies {
    implementation(project(Modules.Service.Memo.Core.path))
    implementation(project(Modules.Libs.CoreKernel.path))
}