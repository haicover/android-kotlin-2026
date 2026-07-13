package com.haicover.kata.baseline

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WordFrequencyTest {

    @Test
    fun `wordFrequency returns empty map when text is null`() {
        val result = wordFrequency(null)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `wordFrequency returns empty map when text is empty`() {
        val result = wordFrequency("")
        assertTrue(result.isEmpty())
    }

    @Test
    fun `wordFrequency returns empty map when text is blank`() {
        val result = wordFrequency("    ")
        assertTrue(result.isEmpty())
    }

    @Test
    fun `wordFrequency handles single word`() {
        val result = wordFrequency("Kotlin")
        assertEquals(mapOf("kotlin" to 1), result)
    }

    @Test
    fun `wordFrequency counts multiple different words`() {
        val result = wordFrequency("Kotlin Java Python")
        assertEquals(
            mapOf(
                "kotlin" to 1,
                "java" to 1,
                "python" to 1
            ),
            result
        )
    }

    @Test
    fun `wordFrequency counts repeating words`() {
        val result = wordFrequency("Kotlin Kotlin Kotlin Java Java")
        assertEquals(
            mapOf(
                "kotlin" to 3,
                "java" to 2
            ),
            result
        )
    }

    @Test
    fun `wordFrequency counts case-insensitively`() {
        val result = wordFrequency("Kotlin KoTlIn KOTLIN kotlin")
        assertEquals(mapOf("kotlin" to 4), result)
    }

    @Test
    fun `wordFrequency handles multiple consecutive spaces`() {
        val result = wordFrequency("Kotlin      Java")
        assertEquals(
            mapOf(
                "kotlin" to 1,
                "java" to 1
            ),
            result
        )
    }

    @Test
    fun `wordFrequency handles tabs`() {
        val result = wordFrequency("Kotlin\tJava\tPython")
        assertEquals(
            mapOf(
                "kotlin" to 1,
                "java" to 1,
                "python" to 1
            ),
            result
        )
    }

    @Test
    fun `wordFrequency handles newlines`() {
        val result = wordFrequency("Kotlin\nJava\n\nAndroid")
        assertEquals(
            mapOf(
                "kotlin" to 1,
                "java" to 1,
                "android" to 1
            ),
            result
        )
    }

    @Test
    fun `wordFrequency handles mixed whitespace separators`() {
        val result = wordFrequency("Kotlin \t \n Java \n \t Android")
        assertEquals(
            mapOf(
                "kotlin" to 1,
                "java" to 1,
                "android" to 1
            ),
            result
        )
    }

    @Test
    fun `wordFrequency keeps punctuation as part of token`() {
        val result = wordFrequency("Kotlin, Kotlin. Java?")
        assertEquals(
            mapOf(
                "kotlin," to 1,
                "kotlin." to 1,
                "java?" to 1
            ),
            result
        )
    }

    @Test
    fun `wordFrequency handles vietnamese unicode`() {
        val result = wordFrequency("Hải học Kotlin Hải học Android")
        assertEquals(
            mapOf(
                "hải" to 2,
                "học" to 2,
                "kotlin" to 1,
                "android" to 1
            ),
            result
        )
    }

    @Test
    fun `wordFrequency does not mutate input text`() {
        val text = "Kotlin Java"
        wordFrequency(text)
        assertEquals("Kotlin Java", text)
    }

    @Test
    fun `wordFrequency handles leading and trailing whitespaces`() {
        val result = wordFrequency("   Kotlin Java   ")
        assertEquals(
            mapOf(
                "kotlin" to 1,
                "java" to 1
            ),
            result
        )
    }

    @Test
    fun `wordFrequency handles non-standard punctuation`() {
        val result = wordFrequency("user@domain.com #kotlin [test]")
        assertEquals(
            mapOf(
                "user@domain.com" to 1,
                "#kotlin" to 1,
                "[test]" to 1
            ),
            result
        )
    }

    @Test
    fun `wordFrequency handles numeric strings`() {
        val result = wordFrequency("123 456 123")
        assertEquals(
            mapOf(
                "123" to 2,
                "456" to 1
            ),
            result
        )
    }

    @Test
    fun `wordFrequency handles special unicode symbols`() {
        val result = wordFrequency("🚀 🚀 💻")
        assertEquals(
            mapOf(
                "🚀" to 2,
                "💻" to 1
            ),
            result
        )
    }

    @Test
    fun `wordFrequency handles long text with mixed symbols`() {
        val result = wordFrequency("A B C a b c")
        assertEquals(
            mapOf(
                "a" to 2,
                "b" to 2,
                "c" to 2
            ),
            result
        )
    }

    @Test
    fun `wordFrequency handles empty lines`() {
        val result = wordFrequency("\n\n\n")
        assertTrue(result.isEmpty())
    }
}
