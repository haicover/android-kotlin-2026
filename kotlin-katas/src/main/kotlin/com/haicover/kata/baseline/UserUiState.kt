package com.haicover.kata.baseline

/**
 * Lớp dữ liệu đại diện cho một người dùng.
 */
data class User(
    val id: Long,
    val name: String,
)

/**
 * Trạng thái giao diện tải danh sách người dùng.
 */
sealed interface UserUiState {

    /**
     * Đang tải dữ liệu.
     */
    data object Loading : UserUiState

    /**
     * Tải dữ liệu thành công.
     */
    data class Success(
        val users: List<User>,
    ) : UserUiState

    /**
     * Tải dữ liệu thất bại.
     */
    data class Error(
        val message: String,
    ) : UserUiState
}

/**
 * Tạo thông báo hiển thị tương ứng với trạng thái UI hiện tại.
 *
 * Quy tắc:
 * - Loading -> "Đang tải..."
 * - Success rỗng -> "Không có người dùng"
 * - Success có dữ liệu -> "Có X người dùng" (với X là số lượng người dùng).
 * - Error -> "Lỗi: {message.trim()}" (nếu message rỗng/blank thì hiển thị "Lỗi: Không xác định").
 *
 * @param state Trạng thái UI hiện tại.
 * @return Thông báo hiển thị.
 */
fun renderMessage(
    state: UserUiState,
): String {
    return when (state) {
        UserUiState.Loading -> "Đang tải..."
        is UserUiState.Success -> {
            if (state.users.isEmpty()) {
                "Không có người dùng"
            } else {
                "Có ${state.users.size} người dùng"
            }
        }
        is UserUiState.Error -> {
            val trimmedMsg = state.message.trim()
            val safeMsg = if (trimmedMsg.isEmpty()) "Không xác định" else trimmedMsg
            "Lỗi: $safeMsg"
        }
    }
}
