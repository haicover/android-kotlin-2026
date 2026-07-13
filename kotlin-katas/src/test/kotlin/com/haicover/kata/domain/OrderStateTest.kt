package com.haicover.kata.domain

import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class OrderStateTest {

    @Test
    fun `created can transition to confirmed`() {
        assertTrue(OrderState.CREATED.canTransitionTo(OrderState.CONFIRMED))
    }

    @Test
    fun `created can transition to cancelled`() {
        assertTrue(OrderState.CREATED.canTransitionTo(OrderState.CANCELLED))
    }

    @Test
    fun `confirmed can transition to shipped`() {
        assertTrue(OrderState.CONFIRMED.canTransitionTo(OrderState.SHIPPED))
    }

    @Test
    fun `confirmed can transition to cancelled`() {
        assertTrue(OrderState.CONFIRMED.canTransitionTo(OrderState.CANCELLED))
    }

    @Test
    fun `shipped can transition to completed`() {
        assertTrue(OrderState.SHIPPED.canTransitionTo(OrderState.COMPLETED))
    }

    @Test
    fun `cannot transition to same state`() {
        assertFalse(OrderState.CREATED.canTransitionTo(OrderState.CREATED))
        assertFalse(OrderState.CONFIRMED.canTransitionTo(OrderState.CONFIRMED))
        assertFalse(OrderState.SHIPPED.canTransitionTo(OrderState.SHIPPED))
    }

    @Test
    fun `completed cannot transition to any other state`() {
        assertFalse(OrderState.COMPLETED.canTransitionTo(OrderState.CREATED))
        assertFalse(OrderState.COMPLETED.canTransitionTo(OrderState.CONFIRMED))
        assertFalse(OrderState.COMPLETED.canTransitionTo(OrderState.SHIPPED))
        assertFalse(OrderState.COMPLETED.canTransitionTo(OrderState.CANCELLED))
    }

    @Test
    fun `cancelled cannot transition to any other state`() {
        assertFalse(OrderState.CANCELLED.canTransitionTo(OrderState.CREATED))
        assertFalse(OrderState.CANCELLED.canTransitionTo(OrderState.CONFIRMED))
        assertFalse(OrderState.CANCELLED.canTransitionTo(OrderState.SHIPPED))
        assertFalse(OrderState.CANCELLED.canTransitionTo(OrderState.COMPLETED))
    }
}
