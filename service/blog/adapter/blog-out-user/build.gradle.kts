plugins {
    springBootConventions
}

group = "kr.co.jiniaslog.blog"

dependencies {
    implementation(project(Modules.Service.Blog.Core.path))
    implementation(project(Modules.Service.AuthUser.Adaptors.InAcl.path))
    implementation(project(Modules.Libs.CoreKernel.path))
}
