package kr.co.jiniaslog.shared.messaging

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration

class ChannelHandlerGenerator(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        logger.info("ChannelHandlerGenerator process start")
        resolver.getSymbolsWithAnnotation(AbstractChannelHandlerBuilder::class.java.canonicalName)
            .filterIsInstance<KSClassDeclaration>()
            .forEach { classDeclaration ->
                generateChannelHandler(classDeclaration, codeGenerator)
            }
        return emptyList()
    }

    private fun generateChannelHandler(
        classDeclaration: KSClassDeclaration,
        codeGenerator: CodeGenerator,
    ) {
        val packageName = "kr.co.jiniaslog.message.nexus.event"
        val className = classDeclaration.simpleName.asString()
        val fileName = "${className}EventHandleable"

        val fileContent =
            buildString {
                appendLine(
                    """
                    package $packageName

                    interface $fileName {
                        companion object{
                                const val CHANNEL_NAME = "${className}Channel"
                        }
                        suspend fun handle(event: $className)
                    }
                    """.trimIndent(),
                )
            }

        val file =
            codeGenerator.createNewFile(
                dependencies = Dependencies(aggregating = false, classDeclaration.containingFile!!),
                packageName = packageName,
                fileName = fileName,
            )
        file.write(fileContent.toByteArray())
        file.close()
    }
}
