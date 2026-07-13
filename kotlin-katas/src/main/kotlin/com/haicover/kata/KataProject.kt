package com.haicover.kata

object KataProject {
    fun readyMessage(): String = "Kotlin Kata pipeline ready"
}

/**
 * Chuẩn hóa tên người dùng.
 * Xóa khoảng trắng ở đầu/cuối, thay thế chuỗi khoảng trắng liên tiếp ở giữa bằng dấu gạch dưới '_',
 * và chuyển toàn bộ thành chữ thường (giữ nguyên Unicode).
 *
 * @param username Tên người dùng cần chuẩn hóa (có thể null).
 * @return Chuỗi đã chuẩn hóa, hoặc "guest" nếu tên null/blank.
 */
fun normalizeUsername(username: String?): String {
    if (username.isNullOrBlank()) {
        return "guest"
    }
    val trimmed = username.trim()
    val normalized = trimmed.replace(Regex("\\s+"), "_")
    return normalized.lowercase()
}

/**
 * Chuyển đổi chuỗi tuổi thành số nguyên an toàn.
 * Xóa khoảng trắng đầu/cuối và parse thành Int trong khoảng 0..150.
 *
 * @param input Chuỗi nhập vào đại diện cho tuổi (có thể null).
 * @return Số tuổi hợp lệ, hoặc null nếu không thể parse hoặc ngoài khoảng 0..150.
 */
fun parseNullableAge(input: String?): Int? {
    if (input.isNullOrBlank()) {
        return null
    }
    val trimmed = input.trim()
    val parsed = trimmed.toIntOrNull() ?: return null
    if (parsed in 0..150) {
        return parsed
    }
    return null
}

/**
 * Che (mask) phần tên tài khoản (local part) của email để bảo vệ thông tin nhạy cảm.
 *
 * Yêu cầu hợp lệ:
 * - Có đúng một ký tự '@'.
 * - Phần local trước '@' và phần domain sau '@' không rỗng, không chứa khoảng trắng.
 * - Phần domain có chứa ít nhất một dấu '.' và không bắt đầu/kết thúc bằng dấu '.'.
 *
 * Quy tắc che:
 * - Độ dài local = 1 -> '*'
 * - Độ dài local = 2 -> 'x*'
 * - Độ dài local >= 3 -> 'x***y' (chỉ giữ ký tự đầu và cuối).
 *
 * @param email Địa chỉ email cần che (có thể null).
 * @return Email đã che hoặc null nếu email không hợp lệ.
 */
fun maskEmail(email: String?): String? {
    if (email.isNullOrBlank()) {
        return null
    }
    val trimmed = email.trim()
    val lowercaseEmail = trimmed.lowercase()

    val atIndex = lowercaseEmail.indexOf('@')
    if (atIndex == -1 || atIndex != lowercaseEmail.lastIndexOf('@')) {
        return null
    }

    val local = lowercaseEmail.substring(0, atIndex)
    val domain = lowercaseEmail.substring(atIndex + 1)

    if (local.isEmpty() || domain.isEmpty()) {
        return null
    }

    if (local.any { it.isWhitespace() } || domain.any { it.isWhitespace() }) {
        return null
    }

    if (!domain.contains('.') || domain.startsWith('.') || domain.endsWith('.')) {
        return null
    }

    val maskedLocal = when {
        local.length == 1 -> "*"
        local.length == 2 -> "${local[0]}*"
        else -> {
            val middleLength = local.length - 2
            val stars = "*".repeat(middleLength)
            "${local.first()}$stars${local.last()}"
        }
    }

    return "$maskedLocal@$domain"
}

/**
 * Lấy giá trị không trống đầu tiên từ danh sách các chuỗi truyền vào.
 *
 * @param values Các chuỗi cần kiểm tra (vararg).
 * @return Giá trị trim() hợp lệ đầu tiên, hoặc null nếu không có.
 */
fun firstNonBlank(vararg values: String?): String? {
    for (value in values) {
        if (!value.isNullOrBlank()) {
            return value.trim()
        }
    }
    return null
}

/**
 * Đếm tần suất xuất hiện của các từ trong văn bản một cách an sau.
 * Từ được cấu thành từ các chữ cái hoặc chữ số (Unicode letter/digit).
 * Khoảng trắng và dấu câu được coi là ký tự phân cách.
 *
 * @param text Văn bản cần phân tích (có thể null).
 * @return Map lưu tần suất xuất hiện của từ (đã chuyển về chữ thường).
 */
fun safeWordStatistics(text: String?): Map<String, Int> {
    if (text.isNullOrBlank()) {
        return emptyMap()
    }
    val regex = Regex("""[\p{L}\p{N}]+""")
    val matches = regex.findAll(text)
    val wordsList = matches.map { it.value.lowercase() }.toList()
    if (wordsList.isEmpty()) {
        return emptyMap()
    }
    return wordsList.groupingBy { it }.eachCount()
}
