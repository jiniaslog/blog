
tasks.register("importGitPreCommitHook", Copy::class) {
    val preCommitHookFile = file(".git/hooks/pre-commit")
    if (preCommitHookFile.exists()) {
        println( "> \"${preCommitHookFile.absolutePath}\" 이 이미 존재합니다.")
        println("> \"${preCommitHookFile.absolutePath}\" 을 파일을 삭제합니다.")
        preCommitHookFile.delete()
    }
    exec {
        println("> pre-commit 심볼릭 링크 생성")
        commandLine = listOf("ln", "-s", file("githooks/pre-commit").absolutePath, ".git/hooks")
    }.assertNormalExitValue()
}

tasks.register("importGitPrePushHook", Copy::class) {
    val prePushHookFile = file(".git/hooks/pre-push")
    if (prePushHookFile.exists()) {
        println( "> \"${prePushHookFile.absolutePath}\" 이 이미 존재합니다.")
        println("> \"${prePushHookFile.absolutePath}\" 을 파일을 삭제합니다.")
        prePushHookFile.delete()
    }
    exec {
        println("> pre-commit 심볼릭 링크 생성")
        commandLine = listOf("ln", "-s", file("githooks/pre-push").absolutePath, ".git/hooks")
    }.assertNormalExitValue()
}
