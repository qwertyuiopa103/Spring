package ispan.order.controller;

import ispan.order.model.OrderService;
import ispan.order.model.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
//@CrossOrigin(origins = "http://localhost:5173")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    private OrderService orderService;;
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * 接收前端付款請求並返回綠界支付表單
     *
     * @param paymentData 包含訂單資料，例如 orderId、totalPrice、paymentMethod
     * @return 包含生成的表單 HTML
     */
    @PostMapping("/ecpay/create")
    public Map<String, String> createECPayForm(@RequestBody Map<String, String> paymentData) {
        // 接收參數
        String orderId = paymentData.get("orderId");
        String totalPrice = paymentData.get("totalPrice");
        String paymentMethod = paymentData.get("paymentMethod"); // 可選參數

        // 驗證參數完整性
        if (orderId == null || totalPrice == null) {
            throw new IllegalArgumentException("訂單資料不完整，請確認參數");
        }

        // 調用 Service 層生成支付表單 HTML
        String formHtml = paymentService.generatePaymentForm(orderId, totalPrice, paymentMethod);

        // 將表單返回給前端
        Map<String, String> response = new HashMap<>();
        response.put("formHtml", formHtml);
        return response;
    }

    /**
     * 接收綠界金流回傳的付款結果
     *
     * @param params 綠界回傳的參數
     * @return 處理結果訊息
     */
    @PostMapping("/callback")
    public ResponseEntity<String> handlePaymentCallback(@RequestParam Map<String, String> params) {
    	
    	// 處理回傳的付款結果
        boolean isSuccess = paymentService.handlePaymentReturn(params);

        // 根據付款處理結果回應不同的訊息
        if (isSuccess) {
            // 付款成功
            return ResponseEntity.ok("付款成功！");
        } else {
            // 付款失敗
        	return ResponseEntity.badRequest().body("付款失敗！");
        }
    }
}

    
