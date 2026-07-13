package com.haicover.kata.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MoneyTest {

    @Test
    fun `zero factory creates zero money`() {
        val zero = Money.ZERO
        assertEquals(0L, zero.amountVnd)
    }

    @Test
    fun `ofVnd creates valid positive money`() {
        val money = Money.ofVnd(500_000)
        assertEquals(500_000L, money.amountVnd)
    }

    @Test
    fun `ofVnd throws exception on negative amount`() {
        assertFailsWith<IllegalArgumentException> {
            Money.ofVnd(-1)
        }
    }

    @Test
    fun `money equality works`() {
        val m1 = Money.ofVnd(100)
        val m2 = Money.ofVnd(100)
        assertEquals(m1, m2)
    }

    @Test
    fun `plus operator aggregates money amounts`() {
        val m1 = Money.ofVnd(500_000)
        val m2 = Money.ofVnd(700_000)
        val sum = m1 + m2
        assertEquals(1_200_000L, sum.amountVnd)
    }

    @Test
    fun `plus operator handles zero`() {
        val m1 = Money.ofVnd(500_000)
        val sum = m1 + Money.ZERO
        assertEquals(500_000L, sum.amountVnd)
    }

    @Test
    fun `minus operator subtracts money amounts`() {
        val m1 = Money.ofVnd(1_000_000)
        val m2 = Money.ofVnd(400_000)
        val diff = m1 - m2
        assertEquals(600_000L, diff.amountVnd)
    }

    @Test
    fun `minus operator throws on negative results`() {
        val m1 = Money.ofVnd(100)
        val m2 = Money.ofVnd(200)
        assertFailsWith<IllegalArgumentException> {
            m1 - m2
        }
    }

    @Test
    fun `plus throws on overflow`() {
        val maxVal = Money.ofVnd(Long.MAX_VALUE)
        val one = Money.ofVnd(1)
        assertFailsWith<ArithmeticException> {
            maxVal + one
        }
    }

    @Test
    fun `format deterministic groupings`() {
        assertEquals("0 VND", Money.ZERO.format())
        assertEquals("999 VND", Money.ofVnd(999).format())
        assertEquals("1,000 VND", Money.ofVnd(1_000).format())
        assertEquals("1,250,000 VND", Money.ofVnd(1_250_000).format())
        assertEquals("1,000,000,000 VND", Money.ofVnd(1_000_000_000).format())
    }
}
