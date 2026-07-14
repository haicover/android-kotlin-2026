package com.haicover.smoketest

import org.junit.Assert.assertEquals
import org.junit.Test

class CounterStateTest {

    @Test
    fun initialCountDefaultsToZero() {
        val state = CounterState()
        assertEquals(0, state.count)
    }

    @Test
    fun initialCountCustomValue() {
        val state = CounterState(initialCount = 5)
        assertEquals(5, state.count)
    }

    @Test
    fun oneIncrement() {
        val state = CounterState()
        state.increment()
        assertEquals(1, state.count)
    }

    @Test
    fun multipleIncrements() {
        val state = CounterState(initialCount = 10)
        state.increment()
        state.increment()
        state.increment()
        assertEquals(13, state.count)
    }
}
