package ispan.order.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;


@Service
public class PaymentService {
    public static AllInOne all;

    public PaymentService() {
        initial();
    }

    private static void initial() {
        all = new AllInOne("");
    }
    @Autowired
    private OrderService orderService;

    /**
     * 生成綠界支付表單
     *
     * @param orderId       訂單編號
     * @param totalPrice    金額
     * @param paymentMethod 支付方式（可選）
     * @return 綠界支付表單 HTML
     */
    public String generatePaymentForm(String orderId, String totalPrice, String paymentMethod) {
        AioCheckOutALL obj = new AioCheckOutALL();

        // 訂單編號 (使用 UUID 避免重複)
        String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
        obj.setMerchantTradeNo(uuId);

        // 設置訂單時間 (當前時間)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String currentTime = sdf.format(new Date());
        obj.setMerchantTradeDate(currentTime);

        // 設置金額與描述
        obj.setTotalAmount(totalPrice);
        obj.setTradeDesc("訂單編號：" + orderId);
        obj.setItemName("看護服務 " + orderId);
        
        // 設置回傳 URL
        obj.setReturnURL("https://8a54-61-222-34-1.ngrok-free.app/api/payment/callback");
        obj.setClientBackURL("http://localhost:5173/#/home");

        // 不需要額外付款資訊
        obj.setNeedExtraPaidInfo("Y");

        // 生成綠界支付表單
        String form = all.aioCheckOut(obj, null);
        // 更新訂單狀態
        try {
        	int orderIdInt = Integer.parseInt(orderId);
            //orderService.updateOrderStatusById(orderIdInt, "付款完成");
            //orderService.updatePaymentMethodByOrderId(orderIdInt,"信用卡" );
            orderService.updateMerchantTradeNo(orderIdInt, uuId);
		} catch (Exception e) {
			// TODO: handle exception
		}
        
        return form;
    }
    // 付款方式轉換方法
    public String convertPaymentMethod(String PaymentMethod) {
        switch (PaymentMethod) {
            case "Credit_CreditCard":
                return "信用卡";
//            case "ATM":
//                return "ATM";
//            case "WebATM":
//                return "WebATM";
//            case "Alipay":
//                return "支付寶";
            default:
                return "未知";
        }
    }
    /**
     * 驗證回傳資料的 CheckMacValue 是否正確
     */
 // 驗證 CheckMacValue
    public boolean validateCheckMacValue(Map<String, String> params) {
        Hashtable<String, String> dict = new Hashtable<>(params);
        return all.compareCheckMacValue(dict);
    }
    /**
     * 處理綠界金流回傳的付款結果
     *
     * @param params 回傳的參數
     * @return 是否處理成功
     */
    public boolean handlePaymentReturn(Map<String, String> params) {
    	System.out.println("開始處理支付回調");
        
        try {
            String merchantTradeNo = params.get("MerchantTradeNo");
            String rtnCode = params.get("RtnCode");
            String PaymentMethod = params.get("PaymentType");
            System.out.println("PaymentMethod:"+PaymentMethod);
            String TradeNo = params.get("TradeNo");
            // 交易成功
            if ("1".equals(rtnCode)) {
            	String convertedPaymentMethod = convertPaymentMethod(PaymentMethod);
                // 根據 MerchantTradeNo 查找訂單並更新狀態
                try {
                    orderService.updateOrderStatusBymerchantTradeNo(merchantTradeNo, "付款完成");
                    orderService.updatePaymentMethodBymerchantTradeNo(merchantTradeNo, convertedPaymentMethod);
                    orderService.updateTradeNoByMerchantTradeNo(merchantTradeNo, TradeNo);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            } else {
                return false;
            }
            
        } catch (Exception e) {
            return false;
        }
    }
}
