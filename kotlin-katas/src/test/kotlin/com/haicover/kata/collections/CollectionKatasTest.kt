package com.haicover.kata.collections

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

// ==========================================
// KATA 1: activeProductNames
// ==========================================

class ActiveProductNamesTest {

    @Test
    fun `activeProductNames returns empty list when input is empty`() {
        // Arrange
        val products = emptyList<Product>()

        // Act
        val result = activeProductNames(products, 100)

        // Assert
        assertTrue(result.isEmpty())
    }

    @Test
    fun `activeProductNames filters out inactive products`() {
        // Arrange
        val products = listOf(
            Product(1, "Laptop", 1000, true),
            Product(2, "Phone", 500, false)
        )

        // Act
        val result = activeProductNames(products, 0)

        // Assert
        assertEquals(listOf("LAPTOP"), result)
    }

    @Test
    fun `activeProductNames filters by minimum price`() {
        // Arrange
        val products = listOf(
            Product(1, "Keyboard", 100, true),
            Product(2, "Mouse", 50, true),
            Product(3, "Monitor", 300, true)
        )

        // Act
        val result = activeProductNames(products, 100)

        // Assert
        assertEquals(listOf("MONITOR", "KEYBOARD"), result)
    }

    @Test
    fun `activeProductNames treats negative minPrice as zero`() {
        // Arrange
        val products = listOf(
            Product(1, "Pen", 5, true),
            Product(2, "Notebook", 10, true)
        )

        // Act
        val result = activeProductNames(products, -50)

        // Assert
        assertEquals(listOf("NOTEBOOK", "PEN"), result)
    }

    @Test
    fun `activeProductNames ignores products with null or blank names`() {
        // Arrange
        val products = listOf(
            Product(1, null, 100, true),
            Product(2, "   ", 200, true),
            Product(3, "Speaker", 300, true)
        )

        // Act
        val result = activeProductNames(products, 0)

        // Assert
        assertEquals(listOf("SPEAKER"), result)
    }

    @Test
    fun `activeProductNames sorts products by price descending`() {
        // Arrange
        val products = listOf(
            Product(1, "Mouse", 50, true),
            Product(2, "Monitor", 300, true),
            Product(3, "Keyboard", 150, true)
        )

        // Act
        val result = activeProductNames(products, 0)

        // Assert
        assertEquals(listOf("MONITOR", "KEYBOARD", "MOUSE"), result)
    }

    @Test
    fun `activeProductNames tie-breaks equal prices alphabetically by name`() {
        // Arrange
        val products = listOf(
            Product(1, "B_Product", 100, true),
            Product(2, "A_Product", 100, true),
            Product(3, "C_Product", 100, true)
        )

        // Act
        val result = activeProductNames(products, 0)

        // Assert
        assertEquals(listOf("A_PRODUCT", "B_PRODUCT", "C_PRODUCT"), result)
    }

    @Test
    fun `activeProductNames trims and converts names to uppercase`() {
        // Arrange
        val products = listOf(
            Product(1, "  hainguyen  ", 100, true)
        )

        // Act
        val result = activeProductNames(products, 0)

        // Assert
        assertEquals(listOf("HAINGUYEN"), result)
    }

    @Test
    fun `activeProductNames does not mutate input collection`() {
        // Arrange
        val products = listOf(Product(1, "Tablet", 400, true))

        // Act
        activeProductNames(products, 0)

        // Assert
        assertEquals(1, products.size)
        assertEquals("Tablet", products[0].name)
    }

    @Test
    fun `activeProductNames returns empty list when all names are null or blank`() {
        // Arrange
        val products = listOf(
            Product(1, null, 100, true),
            Product(2, "", 200, true),
            Product(3, "   ", 300, true)
        )

        // Act
        val result = activeProductNames(products, 0)

        // Assert
        assertTrue(result.isEmpty())
    }
}

// ==========================================
// KATA 2: groupCompletedOrderIdsByCustomer
// ==========================================

class GroupCompletedOrdersTest {

    @Test
    fun `groupCompletedOrderIdsByCustomer returns empty map on empty input`() {
        // Act
        val result = groupCompletedOrderIdsByCustomer(emptyList())

        // Assert
        assertTrue(result.isEmpty())
    }

    @Test
    fun `groupCompletedOrderIdsByCustomer groups only completed orders`() {
        // Arrange
        val orders = listOf(
            Order("O-01", "C-01", 100, OrderStatus.COMPLETED),
            Order("O-02", "C-01", 200, OrderStatus.PENDING),
            Order("O-03", "C-02", 300, OrderStatus.CANCELLED)
        )

        // Act
        val result = groupCompletedOrderIdsByCustomer(orders)

        // Assert
        assertEquals(mapOf("C-01" to listOf("O-01")), result)
    }

    @Test
    fun `groupCompletedOrderIdsByCustomer ignores blank customerId or blank orderId`() {
        // Arrange
        val orders = listOf(
            Order("O-01", null, 100, OrderStatus.COMPLETED),
            Order("O-02", "   ", 200, OrderStatus.COMPLETED),
            Order("   ", "C-01", 300, OrderStatus.COMPLETED)
        )

        // Act
        val result = groupCompletedOrderIdsByCustomer(orders)

        // Assert
        assertTrue(result.isEmpty())
    }

    @Test
    fun `groupCompletedOrderIdsByCustomer trims customerId and orderId`() {
        // Arrange
        val orders = listOf(
            Order("  O-01  ", "  C-01  ", 100, OrderStatus.COMPLETED)
        )

        // Act
        val result = groupCompletedOrderIdsByCustomer(orders)

        // Assert
        assertEquals(mapOf("C-01" to listOf("O-01")), result)
    }

    @Test
    fun `groupCompletedOrderIdsByCustomer preserves customer insertion order`() {
        // Arrange
        val orders = listOf(
            Order("O-01", "C-02", 100, OrderStatus.COMPLETED),
            Order("O-02", "C-01", 200, OrderStatus.COMPLETED),
            Order("O-03", "C-03", 300, OrderStatus.COMPLETED)
        )

        // Act
        val result = groupCompletedOrderIdsByCustomer(orders)

        // Assert
        val keys = result.keys.toList()
        assertEquals(listOf("C-02", "C-01", "C-03"), keys)
    }

    @Test
    fun `groupCompletedOrderIdsByCustomer preserves order order inside groups`() {
        // Arrange
        val orders = listOf(
            Order("O-03", "C-01", 300, OrderStatus.COMPLETED),
            Order("O-01", "C-01", 100, OrderStatus.COMPLETED)
        )

        // Act
        val result = groupCompletedOrderIdsByCustomer(orders)

        // Assert
        assertEquals(listOf("O-03", "O-01"), result["C-01"])
    }

    @Test
    fun `groupCompletedOrderIdsByCustomer keeps duplicate order IDs`() {
        // Arrange
        val orders = listOf(
            Order("O-01", "C-01", 100, OrderStatus.COMPLETED),
            Order("O-01", "C-01", 200, OrderStatus.COMPLETED)
        )

        // Act
        val result = groupCompletedOrderIdsByCustomer(orders)

        // Assert
        assertEquals(listOf("O-01", "O-01"), result["C-01"])
    }

    @Test
    fun `groupCompletedOrderIdsByCustomer returns empty map when no completed orders exist`() {
        // Arrange
        val orders = listOf(
            Order("O-01", "C-01", 100, OrderStatus.PENDING),
            Order("O-02", "C-02", 200, OrderStatus.CANCELLED)
        )

        // Act
        val result = groupCompletedOrderIdsByCustomer(orders)

        // Assert
        assertTrue(result.isEmpty())
    }
}

// ==========================================
// KATA 3: totalCompletedRevenue
// ==========================================

class TotalCompletedRevenueTest {

    @Test
    fun `totalCompletedRevenue returns zero on empty input`() {
        // Act
        val result = totalCompletedRevenue(emptyList())

        // Assert
        assertEquals(0L, result)
    }

    @Test
    fun `totalCompletedRevenue computes single completed order`() {
        // Arrange
        val orders = listOf(
            Order("O-01", "C-01", 1500, OrderStatus.COMPLETED)
        )

        // Act
        val result = totalCompletedRevenue(orders)

        // Assert
        assertEquals(1500L, result)
    }

    @Test
    fun `totalCompletedRevenue ignores pending and cancelled status`() {
        // Arrange
        val orders = listOf(
            Order("O-01", "C-01", 1000, OrderStatus.COMPLETED),
            Order("O-02", "C-02", 500, OrderStatus.PENDING),
            Order("O-03", "C-03", 800, OrderStatus.CANCELLED)
        )

        // Act
        val result = totalCompletedRevenue(orders)

        // Assert
        assertEquals(1000L, result)
    }

    @Test
    fun `totalCompletedRevenue ignores zero or negative values`() {
        // Arrange
        val orders = listOf(
            Order("O-01", "C-01", 1000, OrderStatus.COMPLETED),
            Order("O-02", "C-01", 0, OrderStatus.COMPLETED),
            Order("O-03", "C-02", -500, OrderStatus.COMPLETED)
        )

        // Act
        val result = totalCompletedRevenue(orders)

        // Assert
        assertEquals(1000L, result)
    }

    @Test
    fun `totalCompletedRevenue does not mutate input collection`() {
        // Arrange
        val orders = listOf(Order("O-01", "C-01", 100, OrderStatus.COMPLETED))

        // Act
        totalCompletedRevenue(orders)

        // Assert
        assertEquals(1, orders.size)
        assertEquals(100L, orders[0].totalVnd)
    }

    @Test
    fun `totalCompletedRevenue returns 0 when no completed orders exist`() {
        // Arrange
        val orders = listOf(
            Order("O-01", "C-01", 100, OrderStatus.PENDING),
            Order("O-02", "C-02", 200, OrderStatus.CANCELLED)
        )

        // Act
        val result = totalCompletedRevenue(orders)

        // Assert
        assertEquals(0L, result)
    }

    @Test
    fun `totalCompletedRevenue works with mix of completed, pending, cancelled, zero, and negative values`() {
        // Arrange
        val orders = listOf(
            Order("O-01", "C-01", 100, OrderStatus.COMPLETED),
            Order("O-02", "C-01", 200, OrderStatus.PENDING),
            Order("O-03", "C-02", -50, OrderStatus.COMPLETED),
            Order("O-04", "C-02", 300, OrderStatus.COMPLETED),
            Order("O-05", "C-03", 400, OrderStatus.CANCELLED),
            Order("O-06", "C-03", 0, OrderStatus.COMPLETED)
        )

        // Act
        val result = totalCompletedRevenue(orders)

        // Assert
        assertEquals(400L, result) // 100 (O-01) + 300 (O-04)
    }
}

// ==========================================
// KATA 4: distinctByStable
// ==========================================

class DistinctByStableTest {

    @Test
    fun `distinctByStable returns empty list on empty input`() {
        // Act
        val result = distinctByStable(emptyList<String>()) { it }

        // Assert
        assertTrue(result.isEmpty())
    }

    @Test
    fun `distinctByStable preserves original list when no duplicates exist`() {
        // Arrange
        val list = listOf("A", "B", "C")

        // Act
        val result = distinctByStable(list) { it }

        // Assert
        assertEquals(listOf("A", "B", "C"), result)
    }

    @Test
    fun `distinctByStable retains first occurrence of duplicates`() {
        // Arrange
        val list = listOf("a", "b", "A", "B", "c")

        // Act
        val result = distinctByStable(list) { it.lowercase() }

        // Assert
        assertEquals(listOf("a", "b", "c"), result)
    }

    @Test
    fun `distinctByStable preserves original insertion order`() {
        // Arrange
        val list = listOf("Kotlin", "Java", "Python", "kotlin")

        // Act
        val result = distinctByStable(list) { it.lowercase() }

        // Assert
        assertEquals(listOf("Kotlin", "Java", "Python"), result)
    }

    @Test
    fun `distinctByStable supports null keys and keeps first null`() {
        // Arrange
        val list = listOf(
            Product(1, null, 100, true),
            Product(2, "Mouse", 200, true),
            Product(3, null, 300, true)
        )

        // Act
        val result = distinctByStable(list) { it.name }

        // Assert
        assertEquals(2, result.size)
        assertEquals(1, result[0].id)
        assertEquals(2, result[1].id)
    }

    @Test
    fun `distinctByStable does not mutate input collection`() {
        // Arrange
        val list = listOf("A", "A", "B")

        // Act
        distinctByStable(list) { it }

        // Assert
        assertEquals(3, list.size)
    }

    @Test
    fun `distinctByStable correctly distinguishes keys of different data types`() {
        // Arrange
        val list = listOf("1", 1, "2", 2, "1", 1)

        // Act
        val result = distinctByStable(list) { it }

        // Assert
        assertEquals(listOf("1", 1, "2", 2), result)
    }
}

// ==========================================
// KATA 5: topNWordFrequency
// ==========================================

class TopNWordFrequencyTest {

    @Test
    fun `topNWordFrequency returns empty list when text is null or blank`() {
        // Act
        val res1 = topNWordFrequency(null, 5)
        val res2 = topNWordFrequency("   ", 5)

        // Assert
        assertTrue(res1.isEmpty())
        assertTrue(res2.isEmpty())
    }

    @Test
    fun `topNWordFrequency returns empty list when limit is less than or equal to zero`() {
        // Arrange
        val text = "Kotlin is nice"

        // Act
        val res1 = topNWordFrequency(text, 0)
        val res2 = topNWordFrequency(text, -10)

        // Assert
        assertTrue(res1.isEmpty())
        assertTrue(res2.isEmpty())
    }

    @Test
    fun `topNWordFrequency handles single word`() {
        // Arrange
        val text = "Kotlin"

        // Act
        val result = topNWordFrequency(text, 5)

        // Assert
        assertEquals(listOf(WordCount("kotlin", 1)), result)
    }

    @Test
    fun `topNWordFrequency counts words case-insensitively`() {
        // Arrange
        val text = "Kotlin kotlin KOTLIN"

        // Act
        val result = topNWordFrequency(text, 5)

        // Assert
        assertEquals(listOf(WordCount("kotlin", 3)), result)
    }

    @Test
    fun `topNWordFrequency sorts by count descending`() {
        // Arrange
        val text = "Android Kotlin Android Java Java Android"

        // Act
        val result = topNWordFrequency(text, 5)

        // Assert
        assertEquals(
            listOf(
                WordCount("android", 3),
                WordCount("java", 2),
                WordCount("kotlin", 1)
            ),
            result
        )
    }

    @Test
    fun `topNWordFrequency tie-breaks alphabetically`() {
        // Arrange
        val text = "Java Kotlin Android"

        // Act
        val result = topNWordFrequency(text, 5)

        // Assert
        assertEquals(
            listOf(
                WordCount("android", 1),
                WordCount("java", 1),
                WordCount("kotlin", 1)
            ),
            result
        )
    }

    @Test
    fun `topNWordFrequency handles vietnamese unicode`() {
        // Arrange
        val text = "Hải học Kotlin, Hải học Android"

        // Act
        val result = topNWordFrequency(text, 2)

        // Assert
        assertEquals(
            listOf(
                WordCount("hải", 2),
                WordCount("học", 2)
            ),
            result
        )
    }

    @Test
    fun `topNWordFrequency treats punctuation, hyphens, and underscores as separators`() {
        // Arrange
        val text = "user-2004, user_2004; user"

        // Act
        val result = topNWordFrequency(text, 5)

        // Assert
        assertEquals(
            listOf(
                WordCount("user", 3),
                WordCount("2004", 2)
            ),
            result
        )
    }

    @Test
    fun `topNWordFrequency limits results correctly when limit is smaller than unique word count`() {
        // Arrange
        val text = "one two three four five"

        // Act
        val result = topNWordFrequency(text, 3)

        // Assert
        assertEquals(3, result.size)
    }
}
