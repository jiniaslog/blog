package kr.co.jiniaslog.shared.log

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

@Aspect
@ConditionalOnProperty(value = ["logging.global.enabled"], havingValue = "true", matchIfMissing = true)
@Component
class LogAspect(private val logTracer: LogTracer) {
    // controller
    @Around("execution(* kr.co.jiniaslog..*Resources.*(..))")
    fun controllerLog(joinPoint: ProceedingJoinPoint): Any? {
        return doLog(joinPoint)
    }

    // handler
    @Around("execution(* kr.co.jiniaslog..*Handler.*(..))")
    fun handlerLog(joinPoint: ProceedingJoinPoint): Any? {
        return doLog(joinPoint)
    }

    // interactor, repository, service, adapter
    @Around("execution(* kr.co.jiniaslog..*Interactor.*(..))")
    fun interactorLog(joinPoint: ProceedingJoinPoint): Any? {
        return doLog(joinPoint)
    }

    @Around("execution(* kr.co.jiniaslog..*Repository.*(..))")
    fun repositoryLog(joinPoint: ProceedingJoinPoint): Any? {
        return doLog(joinPoint)
    }

    @Around("execution(* kr.co.jiniaslog..*Adapter.*(..))")
    fun adapterLog(joinPoint: ProceedingJoinPoint): Any? {
        return doLog(joinPoint)
    }

    private fun doLog(joinPoint: ProceedingJoinPoint): Any? {
        val signature = joinPoint.signature.toString().substringAfter(" ")
        val arguments = joinPoint.args.contentDeepToString()
        val returnType = joinPoint.signature.toString().split(" ")[0]
        val status = logTracer.begin(signature, arguments, returnType)

        return try {
            joinPoint.proceed().also {
                logTracer.end(status)
            }
        } catch (ex: Exception) {
            logTracer.handleException(status, ex)
            throw ex
        }
    }
}
