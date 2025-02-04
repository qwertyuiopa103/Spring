package ispan.orderCancel.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderCancelServiceIMPL implements OrderCancelService{
	@Autowired
	private  OrderCancelRepository cancelRepository;
	
	// 創建新的取消訂單
    @Override
    public OrderCancelBean createCancellation(OrderCancelBean orderCancelBean) {
        return cancelRepository.save(orderCancelBean); // 將 OrderCancelBean 保存到資料庫
    }

    // 根據取消訂單的 ID 查詢取消記錄
    @Override
    public Optional<OrderCancelBean> getCancellationById(Integer cancellationId) {
        return cancelRepository.findById(cancellationId); // 返回指定 ID 的取消訂單記錄
    }

    // 獲取所有取消的訂單記錄
    @Override
    public List<OrderCancelBean> getAllCancellations() {
        return cancelRepository.findAll(); // 返回所有取消訂單的列表
    }

    // 更新取消訂單的內容
    @Override
    public OrderCancelBean updateCancellation(Integer cancellationId, OrderCancelBean updatedCancelBean) {
        // 查找是否存在指定 ID 的取消訂單
        Optional<OrderCancelBean> existingCancellation = cancelRepository.findById(cancellationId);
        if (existingCancellation.isPresent()) {
            // 如果找到了，則更新該取消訂單的字段
            OrderCancelBean cancelBean = existingCancellation.get();
            cancelBean.setCancelDate(updatedCancelBean.getCancelDate());
            cancelBean.setCancellationReason(updatedCancelBean.getCancellationReason());
            cancelBean.setRefundAmount(updatedCancelBean.getRefundAmount());
            cancelBean.setReason(updatedCancelBean.getReason());
            cancelBean.setProofReceived(updatedCancelBean.getProofReceived());
            return cancelRepository.save(cancelBean); // 保存更新後的取消訂單
        } else {
            // 如果沒有找到指定 ID 的取消訂單，則拋出異常
            throw new IllegalArgumentException("Cancellation ID " + cancellationId + " not found.");
        }
    }

    // 刪除指定 ID 的取消訂單
    @Override
    public void deleteCancellation(Integer cancellationId) {
        if (cancelRepository.existsById(cancellationId)) {
            // 如果取消訂單存在，則刪除
            cancelRepository.deleteById(cancellationId);
        } else {
            // 如果沒有找到指定 ID 的取消訂單，則拋出異常
            throw new IllegalArgumentException("Cancellation ID " + cancellationId + " not found.");
        }
    }
}
