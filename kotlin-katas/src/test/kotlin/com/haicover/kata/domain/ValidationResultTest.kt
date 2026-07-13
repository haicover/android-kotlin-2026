package com.haicover.kata.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ValidationResultTest {

    @Test
    fun `validateDisplayName returns REQUIRED on null or blank`() {
        val r1 = validateDisplayName(null)
        val r2 = validateDisplayName("")
        val r3 = validateDisplayName("    ")

        assertTrue(r1 is ValidationResult.Invalid)
        assertEquals(listOf(ValidationError.REQUIRED), r1.errors)

        assertTrue(r2 is ValidationResult.Invalid)
        assertEquals(listOf(ValidationError.REQUIRED), r2.errors)

        assertTrue(r3 is ValidationResult.Invalid)
        assertEquals(listOf(ValidationError.REQUIRED), r3.errors)
    }

    @Test
    fun `validateDisplayName returns TOO_SHORT when trimmed length is under 3`() {
        val result = validateDisplayName("  Hi  ")
        assertTrue(result is ValidationResult.Invalid)
        assertEquals(listOf(ValidationError.TOO_SHORT), result.errors)
    }

    @Test
    fun `validateDisplayName returns TOO_LONG when trimmed length is over 30`() {
        val longName = "A".repeat(31)
        val result = validateDisplayName(longName)
        assertTrue(result is ValidationResult.Invalid)
        assertEquals(listOf(ValidationError.TOO_LONG), result.errors)
    }

    @Test
    fun `validateDisplayName returns CONTAINS_LINE_BREAK on newline`() {
        val result = validateDisplayName("Hai\nNguyen")
        assertTrue(result is ValidationResult.Invalid)
        assertEquals(listOf(ValidationError.CONTAINS_LINE_BREAK), result.errors)
    }

    @Test
    fun `validateDisplayName returns multiple errors in order`() {
        val result = validateDisplayName("a\n")
        assertTrue(result is ValidationResult.Invalid)
        // trimmed "a" is too short, and it contains newline
        assertEquals(listOf(ValidationError.TOO_SHORT, ValidationError.CONTAINS_LINE_BREAK), result.errors)
    }

    @Test
    fun `validateDisplayName returns Valid with trimmed value on success`() {
        val result = validateDisplayName("  Hai Nguyen  ")
        assertTrue(result is ValidationResult.Valid)
        assertEquals("Hai Nguyen", result.value)
    }

    @Test
    fun `validationMessage format valid message`() {
        val valid = ValidationResult.Valid("Hải")
        val message = validationMessage(valid)
        assertEquals("Hợp lệ: Hải", message)
    }

    @Test
    fun `validationMessage format invalid message`() {
        val invalid = ValidationResult.Invalid(listOf(ValidationError.TOO_SHORT, ValidationError.CONTAINS_LINE_BREAK))
        val message = validationMessage(invalid)
        assertEquals("Không hợp lệ: TOO_SHORT, CONTAINS_LINE_BREAK", message)
    }
}
