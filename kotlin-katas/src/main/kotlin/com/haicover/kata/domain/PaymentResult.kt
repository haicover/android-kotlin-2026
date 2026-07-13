package com.haicover.kata.domain

/**
 * Lý do thanh toán bị từ chối.
 */
enum class DeclineReason {
    INSUFFICIENT_FUNDS,
    CARD_EXPIRED,
    FRAUD_SUSPECTED,
}

/**
 * Kết quả của quá trình thanh toán.
 */
sealed interface PaymentResult {

    /**
     * Thanh toán thành công.
     */
    data class Success(
        val transactionId: String,
        val amount: Money,
    ) : PaymentResult

    /**
     * Thanh toán bị từ chối bởi cổng thanh toán.
     */
    data class Declined(
        val reason: DeclineReason,
    ) : PaymentResult

    /**
     * Thanh toán gặp lỗi mạng kết nối.
     */
    data class NetworkError(
        val message: String,
    ) : PaymentResult

    /**
     * Thanh toán bị hủy bởi người dùng.
     */
    data object Cancelled : PaymentResult
}

/**
 * Tạo thông điệp hiển thị tương ứng với kết quả thanh toán.
 *
 * Quy tắc:
 * - Success: "Thanh toán {amount.format()} thành công. Mã: {transactionId.trim()}" (nếu blank dùng "unknown").
 * - Declined(INSUFFICIENT_FUNDS): "Thanh toán bị từ chối: Không đủ số dư"
 * - Declined(CARD_EXPIRED): "Thanh toán bị từ chối: Thẻ đã hết hạn"
 * - Declined(FRAUD_SUSPECTED): "Thanh toán bị từ chối: Giao dịch đáng ngờ"
 * - NetworkError: "Lỗi kết nối: {message.trim()}" (nếu blank dùng "unknown").
 * - Cancelled: "Thanh toán đã bị hủy"
 *
 * @param result Kết quả thanh toán đầu vào.
 * @return Thông báo tương ứng.
 */
fun paymentMessage(result: PaymentResult): String {
    return when (result) {
        is PaymentResult.Success -> {
            val txId = result.transactionId.trim()
            val safeTxId = if (txId.isEmpty()) "unknown" else txId
            "Thanh toán ${result.amount.format()} thành công. Mã: $safeTxId"
        }
        is PaymentResult.Declined -> {
            val reasonText = when (result.reason) {
                DeclineReason.INSUFFICIENT_FUNDS -> "Không đủ số dư"
                DeclineReason.CARD_EXPIRED -> "Thẻ đã hết hạn"
                DeclineReason.FRAUD_SUSPECTED -> "Giao dịch đáng ngờ"
            }
            "Thanh toán bị từ chối: $reasonText"
        }
        is PaymentResult.NetworkError -> {
            val msg = result.message.trim()
            val safeMsg = if (msg.isEmpty()) "unknown" else msg
            "Lỗi kết nối: $safeMsg"
        }
        PaymentResult.Cancelled -> {
            "Thanh toán đã bị hủy"
        }
    }
}
