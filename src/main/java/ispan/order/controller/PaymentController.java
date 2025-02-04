package ispan.order.controller;

import ispan.order.model.OrderService;
import ispan.order.model.PaymentService;
import ispan.order.model.RefundService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.exception.EcpayException;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
//@CrossOrigin(origins = "http://localhost:5173")
public class PaymentController {
	@Autowired
    private final PaymentService paymentService;

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private RefundService refundService;  // 注入 RefundService
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
        System.out.println("收到回調的參數: " + params);

        try {
            // 驗證 CheckMacValue
            boolean isValid = paymentService.validateCheckMacValue(params);

            if (!isValid) {
                System.out.println("CheckMacValue 驗證失敗");
                return ResponseEntity.ok("0|ErrorMessage");
            }

            // 驗證成功，處理支付回調邏輯
            paymentService.handlePaymentReturn(params);
            return ResponseEntity.ok("1|OK");

        } catch (Exception e) {
            System.out.println("處理回調時發生錯誤：" + e.getMessage());
            return ResponseEntity.ok("0|" + e.getMessage());
        }
    }
    
    /**
     * 查詢訂單狀態
     * @param merchantTradeNo 訂單的商戶交易號
     * @return 訂單狀態
     */
    @GetMapping("/query/{merchantTradeNo}")
    public ResponseEntity<String> queryOrderStatus(@PathVariable String merchantTradeNo) {
        try {
            String tradeStatus = refundService.queryTradeInfo(merchantTradeNo);
            return ResponseEntity.ok(tradeStatus);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("查詢訂單狀態失敗: " + e.getMessage());
        }
    }
    /**
     * 退款操作
     *
     * @param merchantTradeNo 訂單交易編號
     * @param tradeNo 交易編號
     * @param totalAmount 退款金額
     * @param fullRefund 是否為全額退款
     * @return 退款處理結果訊息
     */
    @PostMapping("/refund")
    public ResponseEntity<String> refund(
            @RequestParam String merchantTradeNo,
            @RequestParam String tradeNo,
            @RequestParam int totalAmount,
            @RequestParam boolean fullRefund) {
        try {
            // 先查詢交易狀態
            String tradeStatus = refundService.queryTradeInfo(merchantTradeNo);

            // 根據不同的交易狀態進行相應操作
            String actionResult = "";
            if ("AUTHORIZED".equals(tradeStatus)) {
                // 已授權階段，執行放棄
                actionResult = refundService.doAction(merchantTradeNo, tradeNo, "N", totalAmount);
            } else if ("CAPTURED".equals(tradeStatus)) {
                if (fullRefund) {
                    // 已關帳階段，執行取消後放棄
                    refundService.doAction(merchantTradeNo, tradeNo, "E", totalAmount);
                    actionResult = refundService.doAction(merchantTradeNo, tradeNo, "N", totalAmount);
                } else {
                    // 部分退款，執行退刷
                    actionResult = refundService.doAction(merchantTradeNo, tradeNo, "R", totalAmount);
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid trade status: " + tradeStatus);
            }

            return ResponseEntity.ok(actionResult);
        } catch (EcpayException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Refund operation failed: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing refund: " + e.getMessage());
        }
    }


}

    
