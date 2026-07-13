package com.haicover.kata.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class PaymentResultTest {

    @Test
    fun `paymentMessage format success result`() {
        val success = PaymentResult.Success("TX-1234", Money.ofVnd(1_250_000))
        val message = paymentMessage(success)
        assertEquals("Thanh toán 1,250,000 VND thành công. Mã: TX-1234", message)
    }

    @Test
    fun `paymentMessage formats success with blank transactionId`() {
        val success = PaymentResult.Success("   ", Money.ofVnd(100))
        val message = paymentMessage(success)
        assertEquals("Thanh toán 100 VND thành công. Mã: unknown", message)
    }

    @Test
    fun `paymentMessage format decline insufficient funds`() {
        val declined = PaymentResult.Declined(DeclineReason.INSUFFICIENT_FUNDS)
        val message = paymentMessage(declined)
        assertEquals("Thanh toán bị từ chối: Không đủ số dư", message)
    }

    @Test
    fun `paymentMessage format decline card expired`() {
        val declined = PaymentResult.Declined(DeclineReason.CARD_EXPIRED)
        val message = paymentMessage(declined)
        assertEquals("Thanh toán bị từ chối: Thẻ đã hết hạn", message)
    }

    @Test
    fun `paymentMessage format decline fraud suspected`() {
        val declined = PaymentResult.Declined(DeclineReason.FRAUD_SUSPECTED)
        val message = paymentMessage(declined)
        assertEquals("Thanh toán bị từ chối: Giao dịch đáng ngờ", message)
    }

    @Test
    fun `paymentMessage format network error`() {
        val error = PaymentResult.NetworkError("Timeout error  ")
        val message = paymentMessage(error)
        assertEquals("Lỗi kết nối: Timeout error", message)
    }

    @Test
    fun `paymentMessage format network error with blank message`() {
        val error = PaymentResult.NetworkError("   ")
        val message = paymentMessage(error)
        assertEquals("Lỗi kết nối: unknown", message)
    }

    @Test
    fun `paymentMessage format cancelled`() {
        val cancelled = PaymentResult.Cancelled
        val message = paymentMessage(cancelled)
        assertEquals("Thanh toán đã bị hủy", message)
    }
}
