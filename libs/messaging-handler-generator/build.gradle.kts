plugins {
    conventions
    id("com.google.devtools.ksp") version "1.9.0-1.0.13"
}

dependencies {
    api(project(Modules.Libs.CoreKernel.path))
    implementation("com.google.devtools.ksp:symbol-processing-api:1.9.0-1.0.13")
    implementation("com.squareup:kotlinpoet:1.14.2")
}