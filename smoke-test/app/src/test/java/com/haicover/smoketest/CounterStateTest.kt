package com.haicover.smoketest

import org.junit.Assert.assertEquals
import org.junit.Test

class CounterStateTest {
    @Test
    fun incrementIncreasesCountByOne() {
        val state = CounterState()

        state.increment()

        assertEquals(1, state.count)
    }
}
