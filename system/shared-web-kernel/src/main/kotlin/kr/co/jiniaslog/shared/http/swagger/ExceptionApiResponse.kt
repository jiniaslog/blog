package kr.co.jiniaslog.shared.http.swagger

import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.HttpStatus
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@ApiResponse(content = [])
annotation class ExceptionApiResponse(
    val httpStatusCode: HttpStatus,
    vararg val exceptions: KClass<out Throwable>,
)