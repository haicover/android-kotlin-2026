# Android State and Process Death

Tài liệu này cung cấp hướng dẫn chi tiết về quản lý trạng thái (State Management) trong ứng dụng Android, phân biệt giữa Configuration Change và Process Death, cũng như đưa ra các quy tắc thiết kế và bảng so sánh các cơ chế lưu trữ.

---

## 1. Configuration Change (Thay đổi cấu hình)

Configuration change xảy ra khi cấu hình thiết bị thay đổi tại thời điểm chạy (runtime), chẳng hạn như xoay màn hình, thay đổi ngôn ngữ hệ thống, thay đổi kích thước cửa sổ (chế độ chia màn hình), hoặc chuyển đổi chế độ sáng/tối (Dark Mode).

Khi có Configuration Change xảy ra:
- **Activity bị hủy và tái tạo**: Hệ thống sẽ hủy (destroy) instance `Activity` hiện tại và tạo mới (recreate) một instance khác để áp dụng tài nguyên (resources) phù hợp với cấu hình mới.
- **Application Process vẫn tồn tại**: Tiến trình hệ thống (process) của ứng dụng thường vẫn hoạt động bình thường, vùng nhớ RAM của tiến trình không bị giải phóng.
- **ViewModel được giữ lại**: Vòng đời của `ViewModel` được thiết kế để sống lâu hơn `Activity`. Khi Activity bị recreate, instance `ViewModel` cũ vẫn được giữ lại trong bộ nhớ và gắn (bind) sang instance `Activity` mới. Do đó, state lưu trong ViewModel không bị mất.
- **State dùng `remember` bị mất**: Các trạng thái trong Composable chỉ dùng hàm `remember` sẽ bị giải phóng cùng với Composition cũ của Activity bị hủy. Khi Composable vẽ lại trên Activity mới, trạng thái đó sẽ khởi tạo lại từ đầu.
- **`rememberSaveable` hoạt động**: Trạng thái được bọc trong `rememberSaveable` sẽ được tự động lưu vào một `Bundle` hệ thống trước khi Activity bị hủy và khôi phục lại khi Activity mới được khởi tạo, với điều kiện kiểu dữ liệu của state tương thích với `Bundle` (hoặc có định nghĩa custom `Saver`).

---

## 2. Process Death (Tiến trình bị hủy bởi hệ thống)

Process Death xảy ra khi hệ thống Android quyết định giải phóng tài nguyên bằng cách kết thúc (kill) tiến trình của ứng dụng khi ứng dụng đang chạy ngầm (background) và thiết bị rơi vào tình trạng thiếu hụt bộ nhớ RAM (low memory).

Khi có Process Death xảy ra:
- **Toàn bộ RAM bị giải phóng**: Tiến trình ứng dụng bị hủy hoàn toàn. Mọi đối tượng lưu trữ trong bộ nhớ RAM, bao gồm các instance `ViewModel`, Singletons, Repositories, đều bị xóa sạch.
- **Khôi phục trạng thái qua Saved Instance State**: Khi người dùng quay lại ứng dụng từ danh sách ứng dụng gần đây (Recent Tasks), hệ thống Android sẽ tạo lại tiến trình mới và khôi phục lại Activity stack. Hệ thống sẽ truyền lại `Bundle` chứa trạng thái đã lưu (saved instance state) cho Activity mới qua `onCreate(savedInstanceState)`.
- **Vai trò của `rememberSaveable` và `SavedStateHandle`**: Cả hai cơ chế này đều lưu trữ dữ liệu vào saved instance state thông qua cơ chế `Bundle`. Do đó, chúng có khả năng khôi phục các mảnh trạng thái nhỏ sau khi tiến trình được tái tạo.
- **Dữ liệu lớn phải đến từ Persistent Layer**: Do Bundle bị giới hạn kích thước (thường dưới 1MB và có thể gây lỗi `TransactionTooLargeException` nếu vượt quá), tuyệt đối không lưu dữ liệu lớn (như danh sách phim, ảnh, hoặc cache nặng) vào Bundle. Dữ liệu lớn hoặc lâu dài phải được lưu trữ trong Room Database, DataStore (SharedPreferences nâng cao) hoặc tải lại từ Backend API dựa trên một ID duy nhất được lưu trong Saved Instance State.

> [!IMPORTANT]
> **Không nhầm lẫn xoay màn hình với Process Death**: Xoay màn hình chỉ là Configuration Change (tiến trình vẫn sống, RAM không mất). Process Death là sự kiện tiến trình bị hủy hoàn toàn (RAM bị xóa sạch).

---

## 3. Bảng so sánh các cơ chế lưu trữ State

| Cơ chế | Recomposition | Activity recreation | Process death | Dùng cho |
| :--- | :---: | :---: | :---: | :--- |
| **Local variable** (biến cục bộ thường) | Không | Không | Không | Các giá trị tính toán tạm thời trong một lần gọi hàm vẽ UI. |
| **remember** | Có | Không | Không | State tạm thời của UI element (ví dụ: trạng thái mở rộng của một dropdown). |
| **rememberSaveable** | Có | Có | Có điều kiện | Trạng thái UI nhỏ, cần giữ khi xoay màn hình hoặc app bị kill ngầm (ví dụ: text hiện tại trong ô tìm kiếm). |
| **ViewModel** | Có | Có | Không | Trạng thái toàn màn hình (Screen State), chứa business logic và giữ kết nối với data layer. |
| **SavedStateHandle** | Có | Có | Có điều kiện | Trạng thái nhỏ cần giữ trong ViewModel qua cả process death (ví dụ: filter ID, search query). |
| **Room / DataStore** | Có | Có | Có | Trạng thái lâu dài, dữ liệu lớn, cần persistent thực sự (ví dụ: cấu hình người dùng, danh sách offline). |

*Chú thích: "Có điều kiện" nghĩa là hệ thống thực hiện saved-state restoration bình thường và dữ liệu cần tương thích với kiểu dữ liệu của `Bundle` / `Saver`.*

---

## 4. Quy tắc chọn nơi lưu State và State Hoisting

Nguyên tắc chung của luồng dữ liệu một chiều (Unidirectional Data Flow - UDF) trong Jetpack Compose: **State đi xuống (State flows down), Event đi lên (Events flow up)**.

State nên được đưa lên (hoist) tới **lowest common ancestor** (tổ tiên chung thấp nhất) của các Composable cần đọc hoặc ghi nó. Việc đưa state ra ngoài giúp Composable trở nên dễ test, dễ tái sử dụng và tránh việc phân tán nguồn dữ liệu (single source of truth).

### Bảng hướng dẫn chọn nơi lưu phù hợp

| Trạng thái (State) | Nơi lưu trữ phù hợp | Lý do |
| :--- | :--- | :--- |
| Trạng thái tạm thời của `TextField` trong Composable | `rememberSaveable` | Trạng thái thuần UI, cần bảo toàn khi xoay màn hình và process death. |
| Từ khóa tìm kiếm (`search query`) dùng bởi ViewModel | `SavedStateHandle` | Trọng tâm điều hướng dữ liệu của ViewModel, cần bảo toàn qua process death để load lại dữ liệu khi khôi phục. |
| Trạng thái Loading / Success / Error | `ViewModel` | Được sinh ra từ data layer thông qua business logic, dễ dàng tái tạo lại từ repository khi có tham số khôi phục. |
| ID của người dùng/sản phẩm đang xem | `SavedStateHandle` | ID là khoá định danh nhỏ gọn, dùng để tải lại thông tin chi tiết từ Room/Backend sau khi tiến trình phục hồi. |
| Toàn bộ đối tượng thông tin người dùng (User Profile) | `Room Database` hoặc `Repository` | Dữ liệu lớn không được nhét vào Bundle của `SavedStateHandle`. Chỉ lưu ID trong `SavedStateHandle` và lấy object từ database/network. |
| Cấu hình người dùng (User Preferences, Dark Mode setting) | `DataStore` | Cần lưu trữ lâu dài dạng key-value, đọc ghi bất tuần tự qua Coroutine. |
| Lịch sử đặt sân (Booking History) | `Room Database` hoặc `Backend` | Dữ liệu dạng danh sách có cấu trúc lớn, cần lưu trữ bền vững. |

---

## 5. Ví dụ sử dụng `SavedStateHandle` trong ViewModel

Dưới đây là ví dụ triển khai đúng chuẩn lưu trữ và khôi phục trạng thái tìm kiếm sử dụng `SavedStateHandle` trong ViewModel:

```kotlin
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

class SearchViewModel(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    // Sử dụng getStateFlow để nhận luồng dữ liệu tự động cập nhật khi trạng thái thay đổi.
    val query: StateFlow<String> =
        savedStateHandle.getStateFlow(
            key = SEARCH_QUERY_KEY,
            initialValue = "",
        )

    /**
     * Cập nhật từ khóa tìm kiếm mới vào SavedStateHandle.
     * Giá trị này sẽ được tự động lưu vào Bundle hệ thống và khôi phục khi recreate.
     */
    fun updateQuery(value: String) {
        savedStateHandle[SEARCH_QUERY_KEY] = value
    }

    private companion object {
        const val SEARCH_QUERY_KEY = "search_query"
    }
}
```

### Giải thích nguyên tắc hoạt động:
1. **Chỉ lưu từ khoá tìm kiếm (`query`)**: Ta chỉ lưu chuỗi từ khóa tìm kiếm nhỏ gọn vào `SavedStateHandle`. Chúng ta **tuyệt đối không lưu** cả danh sách kết quả tìm kiếm lớn (ví dụ: `List<Product>`) vào đây để tránh tràn dung lượng Bundle.
2. **Cơ chế khôi phục**: Khi hệ thống xảy ra Process Death và sau đó tái tạo lại tiến trình, `SavedStateHandle` sẽ tự động khôi phục lại giá trị `query` cũ từ Bundle hệ thống.
3. **Tải lại dữ liệu**: `SearchViewModel` sẽ lắng nghe sự thay đổi của luồng `query` thông qua `StateFlow` và tự động thực hiện truy vấn xuống Data Layer (Room/API) để tải lại danh sách kết quả mới tương ứng, đảm bảo trải nghiệm người dùng không bị gián đoạn.
