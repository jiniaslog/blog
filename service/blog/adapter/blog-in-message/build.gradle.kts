plugins {
    springBootConventions
}

group = "kr.co.jiniaslog.blog"

dependencies {
    implementation(project(":service:blog:blog-core"))
    implementation(project(":libs:core-kernel"))
    implementation(project(":libs:messaging-kernel"))
    implementation(project(":service:message-nexus"))
}