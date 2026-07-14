package com.haicover.smoketest

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class CounterUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun initialCounterShowsZero() {
        composeTestRule.setContent {
            CounterRoute()
        }
        // Xác nhận hiển thị counter ban đầu bằng 0
        composeTestRule.onNodeWithText("Số lần nhấn: 0").assertExists()
    }

    @Test
    fun clickingButtonIncrementsCounter() {
        composeTestRule.setContent {
            CounterRoute()
        }

        // Nhấn nút ba lần
        composeTestRule.onNodeWithText("Bắt đầu hành trình").performClick()
        composeTestRule.onNodeWithText("Bắt đầu hành trình").performClick()
        composeTestRule.onNodeWithText("Bắt đầu hành trình").performClick()

        // Xác nhận hiển thị là 3
        composeTestRule.onNodeWithText("Số lần nhấn: 3").assertExists()
    }

    @Test
    fun counterContentDisplaysCustomCount() {
        composeTestRule.setContent {
            CounterContent(
                count = 5,
                onIncrement = {}
            )
        }
        // Xác nhận CounterContent(count = 5) hiển thị 5
        composeTestRule.onNodeWithText("Số lần nhấn: 5").assertExists()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun stateRestorationTesterConfirmsCounterPreservation() {
        val restorationTester = StateRestorationTester(composeTestRule)

        restorationTester.setContent {
            CounterRoute()
        }

        // Ban đầu là 0
        composeTestRule.onNodeWithText("Số lần nhấn: 0").assertExists()

        // Nhấn tăng lên 1
        composeTestRule.onNodeWithText("Bắt đầu hành trình").performClick()
        composeTestRule.onNodeWithText("Số lần nhấn: 1").assertExists()

        // Giả lập tái tạo trạng thái (Saved Instance State restoration)
        restorationTester.emulateSavedInstanceStateRestore()

        // Xác nhận counter được khôi phục về 1
        composeTestRule.onNodeWithText("Số lần nhấn: 1").assertExists()
    }
}
