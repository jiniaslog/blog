plugins {
    conventions
    koverFeatures
}

group = "kr.co.jiniaslog.memo"

dependencies {
    implementation(project(Modules.Libs.CoreKernel.path))
    implementation(project(Modules.Service.MessageNexus.path))
    testImplementation(testFixtures(project(Modules.Libs.CoreKernel.path)))
    testImplementation("com.navercorp.fixturemonkey:fixture-monkey-starter-kotlin:1.0.12")
}

configurations {
    val testArtifact by creating {
        extendsFrom(configurations.testCompileOnly.get())
    }
}

tasks.register<Jar>("testJar") {
    archiveClassifier.set("test")
    from(sourceSets["test"].output)
}


artifacts {
    add("testArtifact", tasks.named<Jar>("testJar").get())
}
