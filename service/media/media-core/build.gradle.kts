plugins {
    conventions
}

dependencies {
    implementation(project(Modules.Libs.CoreKernel.path))
    implementation(project(Modules.Service.MessageNexus.path))
    testImplementation(testFixtures(project(Modules.Libs.CoreKernel.path)))
}