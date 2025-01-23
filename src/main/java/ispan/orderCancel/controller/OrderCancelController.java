package ispan.orderCancel.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ispan.orderCancel.model.OrderCancelBean;
import ispan.orderCancel.model.OrderCancelService;

@RestController
@RequestMapping("api/ordercancel")
public class OrderCancelController {
	private final OrderCancelService orderCancelService;

    // 通過建構器注入服務層
    @Autowired
    public OrderCancelController(OrderCancelService orderCancelService) {
        this.orderCancelService = orderCancelService;
    }

    // 創建新的取消訂單
    @PostMapping("/createcancel")
    public ResponseEntity<OrderCancelBean> createCancellation(@RequestBody  OrderCancelBean orderCancelBean) {
        OrderCancelBean createdOrderCancel = orderCancelService.createCancellation(orderCancelBean);
        return new ResponseEntity<>(createdOrderCancel, HttpStatus.CREATED); // 返回創建成功的狀態碼 201
    }

    // 根據 ID 查詢取消訂單
    @GetMapping("/getcancel/{cancellationId}") // 接收 GET 請求，並帶有 ID 參數
    public ResponseEntity<OrderCancelBean> getCancellationById(@PathVariable Integer cancellationId) {
        Optional<OrderCancelBean> orderCancelBean = orderCancelService.getCancellationById(cancellationId);
        return orderCancelBean.map(
                ResponseEntity::ok // 如果存在，返回 200 和訂單資料
        ).orElseGet(() -> ResponseEntity.notFound().build()); // 如果找不到，返回 404
    }

    // 查詢所有取消訂單
    @GetMapping ("/getallcancel")
    public ResponseEntity<List<OrderCancelBean>> getAllCancellations() {
        List<OrderCancelBean> cancellations = orderCancelService.getAllCancellations();
        return new ResponseEntity<>(cancellations, HttpStatus.OK); // 返回 200 和所有訂單資料
    }

    // 更新指定 ID 的取消訂單
    @PutMapping("/updatecancel/{id}") 
    public ResponseEntity<OrderCancelBean> updateCancellation(
            @PathVariable Integer id, @RequestBody OrderCancelBean updatedCancelBean) {
        try {
            OrderCancelBean updatedOrderCancel = orderCancelService.updateCancellation(id, updatedCancelBean);
            return new ResponseEntity<>(updatedOrderCancel, HttpStatus.OK); // 返回 200 和更新後的訂單資料
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build(); // 返回 404 當指定 ID 找不到時
        }
    }

    // 刪除指定 ID 的取消訂單
    @DeleteMapping("/deletecancel/{id}") 
    public ResponseEntity<Void> deleteCancellation(@PathVariable Integer id) {
        try {
            orderCancelService.deleteCancellation(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 返回 204 表示刪除成功
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build(); // 返回 404 當指定 ID 找不到時
        }
    }
}
