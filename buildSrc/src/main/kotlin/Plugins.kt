import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

val PluginDependenciesSpec.conventions: PluginDependencySpec
    get() = id("conventions")

val PluginDependenciesSpec.springBootConventions: PluginDependencySpec
    get() = id("spring-boot-conventions")

val PluginDependenciesSpec.jacocoFeatures: PluginDependencySpec
    get() = id("jacoco-features")

val PluginDependenciesSpec.koverFeatures: PluginDependencySpec
    get() = id("kover-features")
