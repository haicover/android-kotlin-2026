package com.haicover.kata.collections

/**
 * Lớp dữ liệu đại diện cho một sản phẩm.
 *
 * @property id ID định danh duy nhất của sản phẩm.
 * @property name Tên sản phẩm.
 * @property priceVnd Giá sản phẩm (VND).
 * @property active Trạng thái sản phẩm (true nếu đang được bán).
 */
data class Product(
    val id: Long,
    val name: String,
    val priceVnd: Long,
    val active: Boolean,
)

/**
 * Trả về danh sách tên sản phẩm thỏa mãn điều kiện lọc và được chuẩn hóa.
 *
 * Yêu cầu:
 * - Sản phẩm đang hoạt động (active == true).
 * - Giá sản phẩm >= minPriceInclusive. Nếu minPriceInclusive < 0 thì coi như minPriceInclusive = 0.
 * - Tên sản phẩm không rỗng và không chỉ chứa khoảng trắng.
 * - Tên sản phẩm trả về được chuẩn hóa bằng cách trim và chuyển sang chữ hoa (uppercase).
 * - Sắp xếp theo giá giảm dần, nếu trùng giá thì sắp xếp theo tên chuẩn hóa tăng dần (case-insensitive),
 *   nếu vẫn trùng thì sắp xếp theo id tăng dần.
 *
 * @param products Danh sách sản phẩm đầu vào.
 * @param minPriceInclusive Mức giá tối thiểu yêu cầu (mặc định là 0).
 * @return Danh sách tên sản phẩm thỏa mãn điều kiện và đã được sắp xếp.
 */
fun activeProductNames(
    products: List<Product>,
    minPriceInclusive: Long = 0,
): List<String> {
    val actualMinPrice = maxOf(0L, minPriceInclusive)
    return products
        .filter {
            it.active && it.priceVnd >= actualMinPrice && !it.name.isBlank()
        }
        .sortedWith(
            compareByDescending<Product> { it.priceVnd }
                .thenBy(String.CASE_INSENSITIVE_ORDER) { it.name.trim() }
                .thenBy { it.id }
        )
        .map { it.name.trim().uppercase() }
}

/**
 * Trạng thái đơn hàng.
 */
enum class OrderStatus {
    PENDING,
    COMPLETED,
    CANCELLED,
}

/**
 * Lớp dữ liệu đại diện cho một đơn hàng.
 *
 * @property id ID định danh duy nhất của đơn hàng.
 * @property customerId ID của khách hàng sở hữu đơn hàng (có thể null).
 * @property totalVnd Tổng số tiền đơn hàng (VND).
 * @property status Trạng thái của đơn hàng.
 */
data class Order(
    val id: String,
    val customerId: String?,
    val totalVnd: Long,
    val status: OrderStatus,
)

/**
 * Nhóm các ID đơn hàng đã hoàn thành (COMPLETED) theo ID khách hàng.
 *
 * Yêu cầu:
 * - Chỉ lấy đơn hàng có trạng thái COMPLETED.
 * - Bỏ qua đơn hàng nếu customerId là null, empty hoặc blank.
 * - Bỏ qua đơn hàng nếu id là empty hoặc blank.
 * - Cả customerId và id của đơn hàng phải được trim() khi xử lý và lưu trữ.
 * - Giữ nguyên thứ tự xuất hiện của khách hàng (LinkedHashMap) và thứ tự đơn hàng trong mỗi nhóm.
 * - Cho phép giữ lại các ID đơn hàng trùng nhau nếu xuất hiện nhiều lần.
 *
 * @param orders Danh sách đơn hàng cần nhóm.
 * @return Map nhóm ID đơn hàng theo ID khách hàng.
 */
fun groupCompletedOrderIdsByCustomer(
    orders: List<Order>,
): Map<String, List<String>> {
    if (orders.isEmpty()) {
        return emptyMap()
    }
    return orders
        .filter { it.status == OrderStatus.COMPLETED }
        .mapNotNull { order ->
            val customerId = order.customerId?.trim()
            val orderId = order.id.trim()
            if (customerId.isNullOrBlank() || orderId.isBlank()) {
                null
            } else {
                Pair(customerId, orderId)
            }
        }
        .groupBy(
            keySelector = { it.first },
            valueTransform = { it.second }
        )
}

/**
 * Tính tổng doanh thu của các đơn hàng đã hoàn thành sử dụng fold.
 *
 * Yêu cầu:
 * - Chỉ cộng các đơn hàng có trạng thái COMPLETED và số tiền totalVnd > 0.
 * - Trả về 0L nếu không có đơn hàng nào thỏa mãn hoặc danh sách trống.
 *
 * @param orders Danh sách đơn hàng.
 * @return Tổng doanh thu của các đơn hàng hoàn thành hợp lệ.
 */
fun totalCompletedRevenue(
    orders: List<Order>,
): Long {
    return orders.fold(0L) { acc, order ->
        if (order.status == OrderStatus.COMPLETED && order.totalVnd > 0) {
            acc + order.totalVnd
        } else {
            acc
        }
    }
}

/**
 * Loại bỏ các phần tử trùng nhau dựa trên khóa được chọn, giữ lại phần tử đầu tiên xuất hiện.
 *
 * Yêu cầu:
 * - Bảo toàn thứ tự xuất hiện ban đầu (stable).
 * - Hỗ trợ khóa (key) trả về giá trị null. Nếu nhiều phần tử có khóa null, chỉ giữ phần tử đầu tiên.
 *
 * @param items Danh sách các phần tử đầu vào.
 * @param keySelector Hàm chọn khóa so sánh từ phần tử.
 * @return Danh sách phần tử đã loại bỏ trùng lặp và giữ nguyên thứ tự.
 */
fun <T, K> distinctByStable(
    items: List<T>,
    keySelector: (T) -> K,
): List<T> {
    val seen = HashSet<K>()
    val result = ArrayList<T>()
    for (item in items) {
        val key = keySelector(item)
        if (seen.add(key)) {
            result.add(item)
        }
    }
    return result
}

/**
 * Lớp dữ liệu đại diện cho thống kê từ.
 *
 * @property word Từ đã chuẩn hóa (lowercase).
 * @property count Tần suất xuất hiện của từ.
 */
data class WordCount(
    val word: String,
    val count: Int,
)

/**
 * Thống kê top N từ xuất hiện nhiều nhất trong văn bản.
 *
 * Yêu cầu:
 * - Tách từ bằng biểu thức chính quy Regex("""[\p{L}\p{N}]+""").
 * - Không phân biệt chữ hoa, chữ thường (tất cả chuyển thành lowercase).
 * - Sắp xếp kết quả theo tần suất xuất hiện giảm dần, tie-break theo thứ tự chữ cái của từ tăng dần.
 * - Lấy tối đa limit phần tử. Trả về rỗng nếu limit <= 0 hoặc văn bản rỗng.
 *
 * @param text Văn bản cần phân tích.
 * @param limit Số lượng từ cần lấy.
 * @return Danh sách đối tượng WordCount được sắp xếp đúng quy tắc.
 */
fun topNWordFrequency(
    text: String?,
    limit: Int,
): List<WordCount> {
    if (text.isNullOrBlank() || limit <= 0) {
        return emptyList()
    }
    val regex = Regex("""[\p{L}\p{N}]+""")
    val matches = regex.findAll(text)
    val wordsList = matches.map { it.value.lowercase() }.toList()
    if (wordsList.isEmpty()) {
        return emptyList()
    }
    val counts = wordsList.groupingBy { it }.eachCount()
    return counts.map { WordCount(it.key, it.value) }
        .sortedWith(
            compareByDescending<WordCount> { it.count }
                .thenBy { it.word }
        )
        .take(limit)
}
