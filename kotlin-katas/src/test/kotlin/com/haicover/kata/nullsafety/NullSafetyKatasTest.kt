package com.haicover.kata.nullsafety

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class NullSafetyKatasTest {

    // ==========================================
    // KATA 1: normalizeUsername
    // ==========================================

    @Test
    fun `normalizeUsername returns guest when username is null`() {
        // Arrange
        val username: String? = null

        // Act
        val result = normalizeUsername(username)

        // Assert
        assertEquals("guest", result)
    }

    @Test
    fun `normalizeUsername returns guest when username is empty`() {
        // Arrange
        val username = ""

        // Act
        val result = normalizeUsername(username)

        // Assert
        assertEquals("guest", result)
    }

    @Test
    fun `normalizeUsername returns guest when username is blank`() {
        // Arrange
        val username = "   "

        // Act
        val result = normalizeUsername(username)

        // Assert
        assertEquals("guest", result)
    }

    @Test
    fun `normalizeUsername trims leading and trailing spaces`() {
        // Arrange
        val username = "  hainguyen  "

        // Act
        val result = normalizeUsername(username)

        // Assert
        assertEquals("hainguyen", result)
    }

    @Test
    fun `normalizeUsername replaces multiple spaces with single underscore`() {
        // Arrange
        val username = "hai   nguyen"

        // Act
        val result = normalizeUsername(username)

        // Assert
        assertEquals("hai_nguyen", result)
    }

    @Test
    fun `normalizeUsername handles unicode characters like Vietnamese`() {
        // Arrange
        val username = "  Hải   Nguyễn  "

        // Act
        val result = normalizeUsername(username)

        // Assert
        assertEquals("hải_nguyễn", result)
    }

    @Test
    fun `normalizeUsername keeps numbers and non-whitespace characters`() {
        // Arrange
        val username = "User  2004"

        // Act
        val result = normalizeUsername(username)

        // Assert
        assertEquals("user_2004", result)
    }

    // ==========================================
    // KATA 2: parseNullableAge
    // ==========================================

    @Test
    fun `parseNullableAge returns null when input is null`() {
        // Arrange
        val input: String? = null

        // Act
        val result = parseNullableAge(input)

        // Assert
        assertNull(result)
    }

    @Test
    fun `parseNullableAge returns null when input is blank`() {
        // Arrange
        val input = "   "

        // Act
        val result = parseNullableAge(input)

        // Assert
        assertNull(result)
    }

    @Test
    fun `parseNullableAge parses valid normal age`() {
        // Arrange
        val input = " 22 "

        // Act
        val result = parseNullableAge(input)

        // Assert
        assertEquals(22, result)
    }

    @Test
    fun `parseNullableAge parses boundary age zero`() {
        // Arrange
        val input = "0"

        // Act
        val result = parseNullableAge(input)

        // Assert
        assertEquals(0, result)
    }

    @Test
    fun `parseNullableAge parses boundary age 150`() {
        // Arrange
        val input = "150"

        // Act
        val result = parseNullableAge(input)

        // Assert
        assertEquals(150, result)
    }

    @Test
    fun `parseNullableAge returns null when age is negative`() {
        // Arrange
        val input = "-1"

        // Act
        val result = parseNullableAge(input)

        // Assert
        assertNull(result)
    }

    @Test
    fun `parseNullableAge returns null when age is greater than 150`() {
        // Arrange
        val input = "151"

        // Act
        val result = parseNullableAge(input)

        // Assert
        assertNull(result)
    }

    @Test
    fun `parseNullableAge returns null when input is double`() {
        // Arrange
        val input = "22.5"

        // Act
        val result = parseNullableAge(input)

        // Assert
        assertNull(result)
    }

    @Test
    fun `parseNullableAge returns null when input is non-numeric`() {
        // Arrange
        val input = "abc"

        // Act
        val result = parseNullableAge(input)

        // Assert
        assertNull(result)
    }

    @Test
    fun `parseNullableAge returns null when integer overflow occurs`() {
        // Arrange
        val input = "999999999999999999999"

        // Act
        val result = parseNullableAge(input)

        // Assert
        assertNull(result)
    }

    // ==========================================
    // KATA 3: maskEmail
    // ==========================================

    @Test
    fun `maskEmail returns null when email is null`() {
        // Arrange
        val email: String? = null

        // Act
        val result = maskEmail(email)

        // Assert
        assertNull(result)
    }

    @Test
    fun `maskEmail returns null when email is blank`() {
        // Arrange
        val email = "   "

        // Act
        val result = maskEmail(email)

        // Assert
        assertNull(result)
    }

    @Test
    fun `maskEmail masks local part of length 1`() {
        // Arrange
        val email = "a@example.com"

        // Act
        val result = maskEmail(email)

        // Assert
        assertEquals("*@example.com", result)
    }

    @Test
    fun `maskEmail masks local part of length 2`() {
        // Arrange
        val email = "ab@example.com"

        // Act
        val result = maskEmail(email)

        // Assert
        assertEquals("a*@example.com", result)
    }

    @Test
    fun `maskEmail masks local part of length greater than 2`() {
        // Arrange
        val email = "hai@example.com"

        // Act
        val result = maskEmail(email)

        // Assert
        assertEquals("h*i@example.com", result)
    }

    @Test
    fun `maskEmail converts email to lowercase and trims whitespace`() {
        // Arrange
        val email = "  HaiNguyen@Example.COM  "

        // Act
        val result = maskEmail(email)

        // Assert
        assertEquals("h*******n@example.com", result)
    }

    @Test
    fun `maskEmail returns null when email has no at-symbol`() {
        // Arrange
        val email = "invalid-email.com"

        // Act
        val result = maskEmail(email)

        // Assert
        assertNull(result)
    }

    @Test
    fun `maskEmail returns null when email has multiple at-symbols`() {
        // Arrange
        val email = "a@b@example.com"

        // Act
        val result = maskEmail(email)

        // Assert
        assertNull(result)
    }

    @Test
    fun `maskEmail returns null when local part is empty`() {
        // Arrange
        val email = "@example.com"

        // Act
        val result = maskEmail(email)

        // Assert
        assertNull(result)
    }

    @Test
    fun `maskEmail returns null when domain part is empty`() {
        // Arrange
        val email = "hai@"

        // Act
        val result = maskEmail(email)

        // Assert
        assertNull(result)
    }

    @Test
    fun `maskEmail returns null when domain part has no dot`() {
        // Arrange
        val email = "hai@example"

        // Act
        val result = maskEmail(email)

        // Assert
        assertNull(result)
    }

    @Test
    fun `maskEmail returns null when domain starts or ends with dot`() {
        // Arrange
        val email1 = "hai@.example.com"
        val email2 = "hai@example.com."

        // Act & Assert
        assertNull(maskEmail(email1))
        assertNull(maskEmail(email2))
    }

    @Test
    fun `maskEmail returns null when email contains whitespace`() {
        // Arrange
        val email = "hai @example.com"

        // Act
        val result = maskEmail(email)

        // Assert
        assertNull(result)
    }

    // ==========================================
    // KATA 4: firstNonBlank
    // ==========================================

    @Test
    fun `firstNonBlank returns null when no arguments passed`() {
        // Act
        val result = firstNonBlank()

        // Assert
        assertNull(result)
    }

    @Test
    fun `firstNonBlank returns null when all values are null`() {
        // Act
        val result = firstNonBlank(null, null)

        // Assert
        assertNull(result)
    }

    @Test
    fun `firstNonBlank returns null when all values are blank`() {
        // Act
        val result = firstNonBlank("", "   ", null)

        // Assert
        assertNull(result)
    }

    @Test
    fun `firstNonBlank returns first valid value at start`() {
        // Arrange & Act
        val result = firstNonBlank("  Hải  ", "Nguyễn", null)

        // Assert
        assertEquals("Hải", result)
    }

    @Test
    fun `firstNonBlank returns first valid value in middle`() {
        // Arrange & Act
        val result = firstNonBlank(null, "   ", "  Kotlin Developer  ", "User")

        // Assert
        assertEquals("Kotlin Developer", result)
    }

    @Test
    fun `firstNonBlank keeps original casing`() {
        // Arrange & Act
        val result = firstNonBlank("aDmIn")

        // Assert
        assertEquals("aDmIn", result)
    }

    // ==========================================
    // KATA 5: safeWordStatistics
    // ==========================================

    @Test
    fun `safeWordStatistics returns empty map when text is null`() {
        // Arrange
        val text: String? = null

        // Act
        val result = safeWordStatistics(text)

        // Assert
        assertEquals(emptyMap(), result)
    }

    @Test
    fun `safeWordStatistics returns empty map when text is blank`() {
        // Arrange
        val text = "    "

        // Act
        val result = safeWordStatistics(text)

        // Assert
        assertEquals(emptyMap(), result)
    }

    @Test
    fun `safeWordStatistics returns single word statistics`() {
        // Arrange
        val text = "Kotlin"

        // Act
        val result = safeWordStatistics(text)

        // Assert
        assertEquals(mapOf("kotlin" to 1), result)
    }

    @Test
    fun `safeWordStatistics counts words ignoring case`() {
        // Arrange
        val text = "Kotlin kotlin Android"

        // Act
        val result = safeWordStatistics(text)

        // Assert
        assertEquals(mapOf("kotlin" to 2, "android" to 1), result)
    }

    @Test
    fun `safeWordStatistics handles punctuation as separator`() {
        // Arrange
        val text = "Kotlin, kotlin! Android."

        // Act
        val result = safeWordStatistics(text)

        // Assert
        assertEquals(mapOf("kotlin" to 2, "android" to 1), result)
    }

    @Test
    fun `safeWordStatistics handles unicode vietnamese`() {
        // Arrange
        val text = "Hải học Kotlin, Hải học Android"

        // Act
        val result = safeWordStatistics(text)

        // Assert
        assertEquals(
            mapOf("hải" to 2, "học" to 2, "kotlin" to 1, "android" to 1),
            result
        )
    }

    @Test
    fun `safeWordStatistics treats underscore and hyphen as separator`() {
        // Arrange
        val text = "user-2004 user_2004"

        // Act
        val result = safeWordStatistics(text)

        // Assert
        assertEquals(mapOf("user" to 2, "2004" to 2), result)
    }
}
