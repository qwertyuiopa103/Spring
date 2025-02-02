package ispan.order.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
        obj.setReturnURL("http://localhost:8080/api/payment/callback");
        obj.setClientBackURL("http://localhost:5173/#/home");

        // 不需要額外付款資訊
        obj.setNeedExtraPaidInfo("N");

        // 生成綠界支付表單
        String form = all.aioCheckOut(obj, null);
        // 更新訂單狀態
        try {
        	int orderIdInt = Integer.parseInt(orderId);
            //orderService.updateOrderStatusById(orderIdInt, "付款完成");
            orderService.updatePaymentMethodByOrderId(orderIdInt,"信用卡" );
            orderService.updateTradeNo(orderIdInt, uuId);
		} catch (Exception e) {
			// TODO: handle exception
		}
        
        return form;
    }

    /**
     * 驗證回傳資料的 CheckMacValue 是否正確
     */
    public boolean validateCheckMacValue(Hashtable<String, String> params) {
        try {
            return all.compareCheckMacValue(params);
        } catch (Exception e) {
            System.out.println("驗證 CheckMacValue 時發生錯誤: " + e.getMessage());
            return false;
        }
    }
    /**
     * 處理綠界金流回傳的付款結果
     *
     * @param params 回傳的參數
     * @return 是否處理成功
     */
    public boolean handlePaymentReturn(Hashtable<String, String> params) {
        System.out.println("收到支付回調: " + params);
        
        if (!params.containsKey("RtnCode") || !params.containsKey("TradeDesc")) {
            System.out.println("缺少必要參數！");
            return false;
        }

        // 驗證 CheckMacValue
        if (!validateCheckMacValue(params)) {
            System.out.println("CheckMacValue 驗證失敗！");
            return false;
        }

        String tradeStatus = params.get("RtnCode");
        String tradeDesc = params.get("TradeDesc");
        String tradeMessage = params.get("RtnMsg");

        try {
            // 從 TradeDesc 中解析訂單編號
            String orderIdStr = tradeDesc.replace("訂單編號：", "");
            int orderId = Integer.parseInt(orderIdStr);
            System.out.println("處理的訂單 ID：" + orderId);

            // 判斷付款狀態
            if ("1".equals(tradeStatus)) {
                System.out.println("付款完成：" + tradeMessage);
                // 更新訂單狀態為「付款完成」
                boolean updated = orderService.updateOrderStatusById(orderId, "付款完成");
                if (updated) {
                    System.out.println("訂單狀態已更新為付款完成！");
                } else {
                    System.out.println("更新訂單狀態時發生錯誤！");
                }
                return true;
            } else {
                System.out.println("付款失敗：" + tradeMessage);
                return false;
            }
        } catch (Exception e) {
            System.out.println("處理回傳時發生錯誤：" + e.getMessage());
            return false;
        }
    }
}
