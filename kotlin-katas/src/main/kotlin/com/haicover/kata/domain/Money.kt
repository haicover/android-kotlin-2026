package com.haicover.kata.domain

/**
 * Lớp giá trị bất biến đại diện cho tiền tệ (VND).
 *
 * @property amountVnd Số tiền tính bằng VND (phải >= 0).
 */
@JvmInline
value class Money private constructor(
    val amountVnd: Long,
) {
    companion object {
        /**
         * Giá trị tiền tệ tương đương 0 VND.
         */
        val ZERO: Money = Money(0L)

        /**
         * Khởi tạo đối tượng Money từ số tiền VND.
         *
         * @param amountVnd Số tiền (phải >= 0).
         * @return Đối tượng Money mới.
         * @throws IllegalArgumentException nếu số tiền âm.
         */
        fun ofVnd(amountVnd: Long): Money {
            if (amountVnd < 0) {
                throw IllegalArgumentException("Số tiền không được âm: $amountVnd")
            }
            return Money(amountVnd)
        }
    }

    /**
     * Thực hiện phép cộng tiền tệ.
     * Ngăn chặn overflow trong tính toán.
     *
     * @param other Đối tượng Money cần cộng.
     * @return Đối tượng Money mới chứa tổng số tiền.
     * @throws ArithmeticException nếu kết quả cộng bị tràn số (overflow).
     */
    operator fun plus(other: Money): Money {
        val result = Math.addExact(this.amountVnd, other.amountVnd)
        return Money(result)
    }

    /**
     * Thực hiện phép trừ tiền tệ.
     * Ngăn chặn việc số dư trở nên âm hoặc bị tràn số.
     *
     * @param other Đối tượng Money cần trừ.
     * @return Đối tượng Money mới chứa hiệu số tiền.
     * @throws ArithmeticException nếu kết quả trừ bị tràn số.
     * @throws IllegalArgumentException nếu hiệu số nhỏ hơn 0.
     */
    operator fun minus(other: Money): Money {
        val result = Math.subtractExact(this.amountVnd, other.amountVnd)
        if (result < 0) {
            throw IllegalArgumentException("Phép trừ tạo ra số tiền âm: $result")
        }
        return Money(result)
    }

    /**
     * Định dạng tiền tệ theo quy chuẩn (ví dụ: "1,250,000 VND").
     * Đảm bảo tính nhất quán (deterministic) không phụ thuộc locale của hệ thống.
     *
     * @return Chuỗi định dạng tiền tệ.
     */
    fun format(): String {
        val numStr = amountVnd.toString()
        val sb = java.lang.StringBuilder()
        var count = 0
        for (i in numStr.length - 1 downTo 0) {
            sb.append(numStr[i])
            count++
            if (count % 3 == 0 && i > 0) {
                sb.append(',')
            }
        }
        return sb.reverse().toString() + " VND"
    }
}
