plugins {
    springBootConventions
}

group = "kr.co.jiniaslog.blog"

dependencies {
    implementation(project(":service:blog:blog-core"))
    implementation(project(":libs:core-kernel"))
    implementation(project(":libs:http-security-kernel"))
}