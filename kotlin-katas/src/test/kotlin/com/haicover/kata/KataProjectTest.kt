package com.haicover.kata

import kotlin.test.Test
import kotlin.test.assertEquals

class KataProjectTest {
    @Test
    fun readyMessageConfirmsPipeline() {
        assertEquals(
            "Kotlin Kata pipeline ready",
            KataProject.readyMessage()
        )
    }
}
