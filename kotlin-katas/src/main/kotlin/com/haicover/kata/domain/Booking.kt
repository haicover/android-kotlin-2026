package com.haicover.kata.domain

/**
 * Trạng thái của Booking.
 */
enum class BookingStatus {
    REQUESTED,
    CONFIRMED,
    CANCELLED,
}

/**
 * Lớp dữ liệu đại diện cho một yêu cầu Booking.
 */
data class BookingRequest(
    val bookingId: String,
    val courtId: String,
    val userId: String,
    val startMinute: Long,
    val durationMinutes: Int,
)

/**
 * Lớp dữ liệu đại diện cho một Booking hoàn chỉnh trong hệ thống.
 */
data class Booking(
    val bookingId: String,
    val courtId: String,
    val userId: String,
    val startMinute: Long,
    val durationMinutes: Int,
    val status: BookingStatus,
)

/**
 * Kết quả trả về khi cố gắng tạo một Booking mới.
 */
sealed interface BookingCreationResult {

    /**
     * Tạo Booking thành công.
     */
    data class Success(
        val booking: Booking,
    ) : BookingCreationResult

    /**
     * Dữ liệu yêu cầu Booking không hợp lệ.
     */
    data class Invalid(
        val errors: List<BookingError>,
    ) : BookingCreationResult

    /**
     * Bị trùng lịch (conflict) với một Booking có sẵn.
     */
    data class Conflict(
        val conflictingBookingId: String,
    ) : BookingCreationResult
}

/**
 * Danh sách lỗi validation đối với yêu cầu Booking.
 */
enum class BookingError {
    BOOKING_ID_REQUIRED,
    COURT_ID_REQUIRED,
    USER_ID_REQUIRED,
    START_TIME_INVALID,
    DURATION_INVALID,
}

/**
 * Tạo Booking mới nếu yêu cầu hợp lệ và không bị trùng lịch (conflict).
 *
 * Quy tắc Validation:
 * - Trim các trường ID: bookingId, courtId, userId.
 * - Trả về lỗi tương ứng (REQUIRED) nếu ID rỗng.
 * - startMinute phải >= 0. Nếu < 0 trả về START_TIME_INVALID.
 * - durationMinutes phải trong khoảng 30..180 và chia hết cho 30. Nếu không trả về DURATION_INVALID.
 * - Danh sách lỗi trả về theo đúng thứ tự khai báo của BookingError.
 * - Nếu xảy ra lỗi validation, bỏ qua kiểm tra trùng lịch.
 *
 * Quy tắc Conflict:
 * - Trùng sân (courtId trùng nhau sau khi trim).
 * - Trạng thái của booking cũ khác CANCELLED.
 * - Hai khoảng thời gian nửa mở [start, end) giao nhau:
 *   newStart < existingEnd && existingStart < newEnd.
 * - Nếu có nhiều conflict, trả về Conflict chứa ID của booking bị trùng đầu tiên xuất hiện trong danh sách.
 *
 * Quy tắc thành công:
 * - Lưu Booking mới với ID đã trim và status mặc định là REQUESTED.
 *
 * @param request Yêu cầu tạo Booking mới.
 * @param existingBookings Danh sách các Booking hiện có trong hệ thống.
 * @return Kết quả BookingCreationResult.
 */
fun createBooking(
    request: BookingRequest,
    existingBookings: List<Booking>,
): BookingCreationResult {
    val errors = mutableListOf<BookingError>()

    val trimmedBookingId = request.bookingId.trim()
    val trimmedCourtId = request.courtId.trim()
    val trimmedUserId = request.userId.trim()

    if (trimmedBookingId.isEmpty()) {
        errors.add(BookingError.BOOKING_ID_REQUIRED)
    }
    if (trimmedCourtId.isEmpty()) {
        errors.add(BookingError.COURT_ID_REQUIRED)
    }
    if (trimmedUserId.isEmpty()) {
        errors.add(BookingError.USER_ID_REQUIRED)
    }

    if (request.startMinute < 0) {
        errors.add(BookingError.START_TIME_INVALID)
    }

    val duration = request.durationMinutes
    if (duration < 30 || duration > 180 || duration % 30 != 0) {
        errors.add(BookingError.DURATION_INVALID)
    }

    if (errors.isNotEmpty()) {
        return BookingCreationResult.Invalid(errors)
    }

    // Kiểm tra trùng lịch (Conflict)
    val newStart = request.startMinute
    val newEnd = request.startMinute + request.durationMinutes

    val conflicting = existingBookings.firstOrNull { existing ->
        existing.status != BookingStatus.CANCELLED &&
        existing.courtId.trim() == trimmedCourtId &&
        newStart < (existing.startMinute + existing.durationMinutes) &&
        existing.startMinute < newEnd
    }

    if (conflicting != null) {
        return BookingCreationResult.Conflict(conflicting.bookingId)
    }

    val booking = Booking(
        bookingId = trimmedBookingId,
        courtId = trimmedCourtId,
        userId = trimmedUserId,
        startMinute = request.startMinute,
        durationMinutes = request.durationMinutes,
        status = BookingStatus.REQUESTED
    )
    return BookingCreationResult.Success(booking)
}
