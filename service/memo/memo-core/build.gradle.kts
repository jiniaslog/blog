plugins {
    conventions
}

group = "kr.co.jiniaslog.memo"

dependencies {
    implementation(project(":libs:core-kernel"))
    implementation(project(":service:message-nexus"))
    testImplementation(testFixtures(project(":libs:core-kernel")))
}