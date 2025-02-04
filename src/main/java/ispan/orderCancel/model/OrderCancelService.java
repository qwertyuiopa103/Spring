package ispan.orderCancel.model;

import java.util.List;
import java.util.Optional;

public interface OrderCancelService {
	// 新增取消記錄
    OrderCancelBean createCancellation(OrderCancelBean orderCancelBean);

    // 根據 ID 查詢取消記錄
    Optional<OrderCancelBean> getCancellationById(Integer cancellationId);

    // 查詢所有取消記錄
    List<OrderCancelBean> getAllCancellations();

    // 更新取消記錄
    OrderCancelBean updateCancellation(Integer cancellationId, OrderCancelBean updatedCancelBean);

    // 刪除取消記錄
    void deleteCancellation(Integer cancellationId);
}


