package com.haicover.kata.domain

/**
 * Các lỗi kiểm thử định dạng (Validation).
 */
enum class ValidationError {
    REQUIRED,
    TOO_SHORT,
    TOO_LONG,
    CONTAINS_LINE_BREAK,
}

/**
 * Kết quả kiểm thử định dạng kiểu generic.
 */
sealed interface ValidationResult<out T> {

    /**
     * Dữ liệu hợp lệ.
     */
    data class Valid<T>(
        val value: T,
    ) : ValidationResult<T>

    /**
     * Dữ liệu không hợp lệ kèm theo danh sách các lỗi.
     */
    data class Invalid(
        val errors: List<ValidationError>,
    ) : ValidationResult<Nothing>
}

/**
 * Thực hiện kiểm tra tính hợp lệ của tên hiển thị.
 *
 * Quy tắc:
 * - Nếu null hoặc blank: trả về Invalid(REQUIRED).
 * - Trim trước khi tính độ dài. Độ dài hợp lệ là từ 3 đến 30 ký tự.
 *   - Nếu < 3: nạp lỗi TOO_SHORT.
 *   - Nếu > 30: nạp lỗi TOO_LONG.
 * - Nếu chứa '\n' hoặc '\r': nạp lỗi CONTAINS_LINE_BREAK.
 * - Danh sách lỗi trả về được sắp xếp theo thứ tự khai báo của ValidationError.
 *
 * @param input Chuỗi tên hiển thị đầu vào.
 * @return ValidationResult tương ứng chứa giá trị chuẩn hóa (trimmed) hoặc danh sách lỗi.
 */
fun validateDisplayName(
    input: String?,
): ValidationResult<String> {
    if (input == null || input.isBlank()) {
        return ValidationResult.Invalid(listOf(ValidationError.REQUIRED))
    }

    val errors = mutableListOf<ValidationError>()
    val trimmed = input.trim()

    if (trimmed.length < 3) {
        errors.add(ValidationError.TOO_SHORT)
    } else if (trimmed.length > 30) {
        errors.add(ValidationError.TOO_LONG)
    }

    if (input.contains('\n') || input.contains('\r')) {
        errors.add(ValidationError.CONTAINS_LINE_BREAK)
    }

    return if (errors.isEmpty()) {
        ValidationResult.Valid(trimmed)
    } else {
        ValidationResult.Invalid(errors)
    }
}

/**
 * Tạo thông điệp hiển thị cho ValidationResult.
 *
 * @param result Kết quả validation.
 * @return Chuỗi thông báo tương ứng.
 */
fun validationMessage(
    result: ValidationResult<String>,
): String {
    return when (result) {
        is ValidationResult.Valid -> "Hợp lệ: ${result.value}"
        is ValidationResult.Invalid -> {
            val errNames = result.errors.map { it.name }.joinToString(", ")
            "Không hợp lệ: $errNames"
        }
    }
}
