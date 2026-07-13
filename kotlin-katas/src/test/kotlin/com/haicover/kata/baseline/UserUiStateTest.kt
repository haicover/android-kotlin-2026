package com.haicover.kata.baseline

import kotlin.test.Test
import kotlin.test.assertEquals

class UserUiStateTest {

    @Test
    fun `renderMessage returns correct message for Loading`() {
        val state = UserUiState.Loading
        val message = renderMessage(state)
        assertEquals("Đang tải...", message)
    }

    @Test
    fun `renderMessage returns correct message for Success with empty list`() {
        val state = UserUiState.Success(emptyList())
        val message = renderMessage(state)
        assertEquals("Không có người dùng", message)
    }

    @Test
    fun `renderMessage returns correct message for Success with one user`() {
        val state = UserUiState.Success(listOf(User(1L, "Hải")))
        val message = renderMessage(state)
        assertEquals("Có 1 người dùng", message)
    }

    @Test
    fun `renderMessage returns correct message for Success with multiple users`() {
        val state = UserUiState.Success(
            listOf(
                User(1L, "Hải"),
                User(2L, "Nguyễn"),
                User(3L, "An")
            )
        )
        val message = renderMessage(state)
        assertEquals("Có 3 người dùng", message)
    }

    @Test
    fun `renderMessage returns correct message for Error with normal message`() {
        val state = UserUiState.Error("Kết nối thất bại")
        val message = renderMessage(state)
        assertEquals("Lỗi: Kết nối thất bại", message)
    }

    @Test
    fun `renderMessage trims message in Error state`() {
        val state = UserUiState.Error("  Lỗi kết nối mạng  ")
        val message = renderMessage(state)
        assertEquals("Lỗi: Lỗi kết nối mạng", message)
    }

    @Test
    fun `renderMessage handles blank message in Error state`() {
        val state = UserUiState.Error("   ")
        val message = renderMessage(state)
        assertEquals("Lỗi: Không xác định", message)
    }

    @Test
    fun `renderMessage handles empty message in Error state`() {
        val state = UserUiState.Error("")
        val message = renderMessage(state)
        assertEquals("Lỗi: Không xác định", message)
    }

    @Test
    fun `renderMessage handles newline in Error state message`() {
        val state = UserUiState.Error("Timeout\nServer error")
        val message = renderMessage(state)
        assertEquals("Lỗi: Timeout\nServer error", message)
    }

    @Test
    fun `user data class equality works`() {
        val u1 = User(1L, "Hải")
        val u2 = User(1L, "Hải")
        assertEquals(u1, u2)
    }

    @Test
    fun `success state users list is immutable`() {
        val users = listOf(User(1L, "Hải"))
        val state = UserUiState.Success(users)
        assertEquals(1, state.users.size)
        assertEquals("Hải", state.users[0].name)
    }

    @Test
    fun `success state supports multiple distinct user instances`() {
        val u1 = User(1L, "A")
        val u2 = User(2L, "B")
        val state = UserUiState.Success(listOf(u1, u2))
        assertEquals(listOf(u1, u2), state.users)
    }

    @Test
    fun `error state supports special characters in message`() {
        val state = UserUiState.Error("!!! Warning: Error code 500 !!!")
        val message = renderMessage(state)
        assertEquals("Lỗi: !!! Warning: Error code 500 !!!", message)
    }

    @Test
    fun `renderMessage is deterministic`() {
        val state = UserUiState.Loading
        val m1 = renderMessage(state)
        val m2 = renderMessage(state)
        assertEquals(m1, m2)
    }

    @Test
    fun `renderMessage for error is deterministic`() {
        val state = UserUiState.Error("Fail")
        val m1 = renderMessage(state)
        val m2 = renderMessage(state)
        assertEquals(m1, m2)
    }

    @Test
    fun `renderMessage for success is deterministic`() {
        val state = UserUiState.Success(emptyList())
        val m1 = renderMessage(state)
        val m2 = renderMessage(state)
        assertEquals(m1, m2)
    }

    @Test
    fun `success state copy replaces users without changing original state`() {
        // Arrange
        val originalUsers = listOf(
            User(id = 1L, name = "Hải"),
        )
        val replacementUsers = listOf(
            User(id = 2L, name = "An"),
            User(id = 3L, name = "Bình"),
        )
        val original = UserUiState.Success(originalUsers)

        // Act
        val copied = original.copy(users = replacementUsers)

        // Assert
        assertEquals(originalUsers, original.users)
        assertEquals(replacementUsers, copied.users)
        assertEquals(1, original.users.size)
        assertEquals(2, copied.users.size)
    }
}
