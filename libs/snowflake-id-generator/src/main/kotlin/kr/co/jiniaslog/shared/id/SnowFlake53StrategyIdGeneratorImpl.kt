package kr.co.jiniaslog.shared.id

import kr.co.jiniaslog.shared.core.domain.IdGenerator
import kotlin.math.pow

/**
 * 분산 시스템을 위한 유일 ID 생성기 - SnowFlake53
 */
class SnowFlake53StrategyIdGeneratorImpl(serverId: Int, serverIdBitSize: Int) : IdGenerator {
    companion object {
        /**
         * 자바 스크립트 Number 원시값이 안정적으로 나타낼수 있는 최대치 2^53-1 을 고려, 53bit로 제한
         */
        private const val MAX_BIT_SIZE = 53

        /**
         * 2022.05.20 : 02:31, 기원 시각 이후로 몇 ms가 경과했는지를 나타내는 값
         */
        private const val BASE_TIMESTAMP = 1652981464342L

        /**
         * 타임스탬프 비트 사이즈 정의
         */
        private const val TIMESTAMP_BIT_SIZE = 41

        /**
         * 타임스탬프 뒤로 붙을 비트 파싱을 위한 이동 사이즈 정의
         */
        private const val TIMESTAMP_SHIFT = MAX_BIT_SIZE - TIMESTAMP_BIT_SIZE
    }

    /**
     * 서버 고유 Id
     */
    private val serverId: Int

    /**
     * 서버 아이디 비트사이즈만큼 이동 정의
     */
    private val serverIdShift: Int

    /**
     * 일련번호 비트 사이즈 확보를 위한 값
     */
    private val serialNumberMask: Int

    /**
     * 마지막으로 사용한 타임스탬프를 기록하여 동일값이 기록되지 않도록 유효성검증
     */
    private var lastTimestamp: Long

    /**
     * 마지막 일련번호를 기록하여 동일값이 기록되지 않도록 유효성 검증
     */
    private var serialNumber: Int

    init {
        this.serverId = serverId
        serverIdShift = MAX_BIT_SIZE - TIMESTAMP_BIT_SIZE - serverIdBitSize
        val serialNumberBitSize = MAX_BIT_SIZE - TIMESTAMP_BIT_SIZE - serverIdBitSize
        serialNumberMask = 2.0.pow(serialNumberBitSize.toDouble()).toInt() - 1
        lastTimestamp = System.currentTimeMillis()
        serialNumber = 0
    }

    @Synchronized
    override fun generate(): Long {
        var timestamp = System.currentTimeMillis()
        if (timestamp < lastTimestamp) throw IdGeneratorTimestampException()

        when (timestamp) {
            lastTimestamp -> {
                serialNumber = serialNumber + 1 and serialNumberMask
                if (serialNumber == 0) timestamp = waitNextTimestamp(lastTimestamp)
            }
            else -> serialNumber = 0
        }
        lastTimestamp = timestamp

        return (
            timestamp - BASE_TIMESTAMP shl TIMESTAMP_SHIFT
                or (serverId shl serverIdShift).toLong()
                or serialNumber.toLong()
        )
    }

    private fun waitNextTimestamp(lastTimestamp: Long): Long {
        var current = System.currentTimeMillis()
        while (lastTimestamp >= current) {
            current = System.currentTimeMillis()
        }
        return lastTimestamp + 1
    }
}
