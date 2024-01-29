package kr.co.jiniaslog.memo.adapter.inbound.http

/**
 * Auth user id
 *
 * 인증정보가 없는 유저 접근시 argument resolver 에서 null 을 반환할수 있으므로 사용시 항상 nullable 해야함
 *
 * @see kr.co.jiniaslog.shared.security.AuthUserIdArgumentResolver
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class AuthUserId
