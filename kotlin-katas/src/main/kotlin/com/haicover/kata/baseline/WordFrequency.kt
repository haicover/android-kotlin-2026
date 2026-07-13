package com.haicover.kata.baseline

/**
 * Đếm tần suất xuất hiện của từng từ trong chuỗi văn bản.
 *
 * Quy tắc:
 * - null, empty hoặc blank trả về emptyMap().
 * - Không phân biệt chữ hoa, chữ thường (chuyển sang lowercase).
 * - Các từ được tách bởi một hoặc nhiều khoảng trắng (khoảng trắng, tab, newline).
 * - Không đưa từ rỗng vào kết quả.
 * - Giữ nguyên dấu câu đi kèm từ (ví dụ: "Kotlin," -> "kotlin,").
 * - Không thay đổi chuỗi đầu vào.
 *
 * @param text Văn bản đầu vào.
 * @return Map thống kê tần suất từ.
 */
fun wordFrequency(
    text: String?,
): Map<String, Int> {
    if (text.isNullOrBlank()) {
        return emptyMap()
    }
    val trimmed = text.trim()
    if (trimmed.isEmpty()) {
        return emptyMap()
    }
    val tokens = trimmed.split(Regex("""\s+"""))
    return tokens
        .map { it.lowercase() }
        .filter { it.isNotEmpty() }
        .groupingBy { it }
        .eachCount()
}
