package ispan.order.model;

import java.util.Map;

public class PaymentReturn {

    private int orderId; // 訂單編號，對應到 OrderBean 中的 orderId
    private String paymentStatus;  // 付款狀態
    private String paymentMessage; // 回傳訊息
    private String tradeNo; // 交易編號
    private boolean success; // 付款是否成功

    // 設定付款回傳參數
    public static PaymentReturn fromMap(Map<String, String> params) {
        int orderId = Integer.parseInt(params.get("MerchantOrderNo")); // 假設 MerchantOrderNo 對應到 OrderBean 的 orderId
        String paymentStatus = params.get("PaymentStatus");
        String paymentMessage = params.get("Message");
        String tradeNo = params.get("TradeNo");
        boolean success = "1".equals(params.get("Result")); // 假設 "1" 代表成功，"0" 代表失敗

        PaymentReturn paymentReturn = new PaymentReturn();
        paymentReturn.setOrderId(orderId);
        paymentReturn.setPaymentStatus(paymentStatus);
        paymentReturn.setPaymentMessage(paymentMessage);
        paymentReturn.setTradeNo(tradeNo);
        paymentReturn.setSuccess(success);

        return paymentReturn;
    }

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMessage() {
        return paymentMessage;
    }

    public void setPaymentMessage(String paymentMessage) {
        this.paymentMessage = paymentMessage;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
