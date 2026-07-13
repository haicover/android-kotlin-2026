package com.haicover.kata.domain

/**
 * Trạng thái của đơn hàng trong hệ thống.
 */
enum class OrderState {
    CREATED,
    CONFIRMED,
    SHIPPED,
    COMPLETED,
    CANCELLED,
}

/**
 * Kiểm tra xem đơn hàng có thể chuyển từ trạng thái hiện tại sang trạng thái tiếp theo hay không.
 *
 * Workflow hợp lệ:
 * - CREATED   → CONFIRMED
 * - CREATED   → CANCELLED
 * - CONFIRMED → SHIPPED
 * - CONFIRMED → CANCELLED
 * - SHIPPED   → COMPLETED
 *
 * @param next Trạng thái tiếp theo cần kiểm tra.
 * @return true nếu chuyển đổi hợp lệ, ngược lại false.
 */
fun OrderState.canTransitionTo(next: OrderState): Boolean {
    return when (this) {
        OrderState.CREATED -> next == OrderState.CONFIRMED || next == OrderState.CANCELLED
        OrderState.CONFIRMED -> next == OrderState.SHIPPED || next == OrderState.CANCELLED
        OrderState.SHIPPED -> next == OrderState.COMPLETED
        OrderState.COMPLETED -> false
        OrderState.CANCELLED -> false
    }
}
