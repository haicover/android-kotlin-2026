package com.haicover.kata.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BookingTest {

    @Test
    fun `createBooking success with empty existing bookings`() {
        val request = BookingRequest("B-01", "C-01", "U-01", 120, 60)
        val result = createBooking(request, emptyList())

        assertTrue(result is BookingCreationResult.Success)
        assertEquals("B-01", result.booking.bookingId)
        assertEquals("C-01", result.booking.courtId)
        assertEquals("U-01", result.booking.userId)
        assertEquals(120, result.booking.startMinute)
        assertEquals(60, result.booking.durationMinutes)
        assertEquals(BookingStatus.REQUESTED, result.booking.status)
    }

    @Test
    fun `createBooking trims bookingId courtId userId`() {
        val request = BookingRequest("  B-01  ", "  C-01  ", "  U-01  ", 120, 60)
        val result = createBooking(request, emptyList())

        assertTrue(result is BookingCreationResult.Success)
        assertEquals("B-01", result.booking.bookingId)
        assertEquals("C-01", result.booking.courtId)
        assertEquals("U-01", result.booking.userId)
    }

    @Test
    fun `createBooking returns multiple validation errors in order`() {
        val request = BookingRequest("   ", "   ", "   ", -10, 45)
        val result = createBooking(request, emptyList())

        assertTrue(result is BookingCreationResult.Invalid)
        assertEquals(
            listOf(
                BookingError.BOOKING_ID_REQUIRED,
                BookingError.COURT_ID_REQUIRED,
                BookingError.USER_ID_REQUIRED,
                BookingError.START_TIME_INVALID,
                BookingError.DURATION_INVALID
            ),
            result.errors
        )
    }

    @Test
    fun `createBooking detects conflict when overlapping on same court`() {
        val existing = listOf(
            Booking("B-01", "C-01", "U-01", 120, 60, BookingStatus.CONFIRMED)
        )
        // new booking 150..210 overlaps with 120..180
        val request = BookingRequest("B-02", "C-01", "U-02", 150, 60)
        val result = createBooking(request, existing)

        assertTrue(result is BookingCreationResult.Conflict)
        assertEquals("B-01", result.conflictingBookingId)
    }

    @Test
    fun `createBooking allows consecutive bookings that touch boundaries`() {
        val existing = listOf(
            Booking("B-01", "C-01", "U-01", 120, 60, BookingStatus.CONFIRMED)
        )
        // new booking starts exactly at 180 (A ends at 120+60=180)
        val request = BookingRequest("B-02", "C-01", "U-02", 180, 60)
        val result = createBooking(request, existing)

        assertTrue(result is BookingCreationResult.Success)
    }

    @Test
    fun `createBooking ignores conflict on different court`() {
        val existing = listOf(
            Booking("B-01", "C-01", "U-01", 120, 60, BookingStatus.CONFIRMED)
        )
        // same time but court C-02
        val request = BookingRequest("B-02", "C-02", "U-02", 120, 60)
        val result = createBooking(request, existing)

        assertTrue(result is BookingCreationResult.Success)
    }

    @Test
    fun `createBooking ignores conflict when existing status is cancelled`() {
        val existing = listOf(
            Booking("B-01", "C-01", "U-01", 120, 60, BookingStatus.CANCELLED)
        )
        val request = BookingRequest("B-02", "C-01", "U-02", 120, 60)
        val result = createBooking(request, existing)

        assertTrue(result is BookingCreationResult.Success)
    }

    @Test
    fun `createBooking returns first conflict in list order`() {
        val existing = listOf(
            Booking("B-01", "C-01", "U-01", 120, 60, BookingStatus.CONFIRMED),
            Booking("B-02", "C-01", "U-02", 150, 60, BookingStatus.CONFIRMED)
        )
        // overlaps both 120..180 and 150..210
        val request = BookingRequest("B-03", "C-01", "U-03", 140, 60)
        val result = createBooking(request, existing)

        assertTrue(result is BookingCreationResult.Conflict)
        assertEquals("B-01", result.conflictingBookingId)
    }

    @Test
    fun `createBooking validation duration checking range`() {
        val r1 = createBooking(BookingRequest("B", "C", "U", 0, 20), emptyList())
        val r2 = createBooking(BookingRequest("B", "C", "U", 0, 190), emptyList())

        assertTrue(r1 is BookingCreationResult.Invalid)
        assertTrue(r2 is BookingCreationResult.Invalid)
    }

    @Test
    fun `createBooking validation duration check divisibility by 30`() {
        val result = createBooking(BookingRequest("B", "C", "U", 0, 50), emptyList())
        assertTrue(result is BookingCreationResult.Invalid)
    }
}
