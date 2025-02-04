package ispan.order.model;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.QueryTradeInfoObj;
import ecpay.payment.integration.domain.DoActionObj;
import ecpay.payment.integration.exception.EcpayException;
import org.json.JSONObject;


@Service
public class RefundService {
	private static final Logger log = LoggerFactory.getLogger(RefundService.class);
    private final AllInOne allInOne = new AllInOne("");
    private final String MerchantID = "3002607";

    public String queryTradeInfo(String merchantTradeNo) {
        QueryTradeInfoObj queryObj = new QueryTradeInfoObj();
        queryObj.setMerchantTradeNo(merchantTradeNo);
        queryObj.setMerchantID(MerchantID);
        queryObj.setTimeStamp(String.valueOf(System.currentTimeMillis() / 1000));

        String response = allInOne.queryTradeInfo(queryObj);
        log.info("Query Response: {}", response);

        // 解析 response 取得交易狀態
        JSONObject jsonResponse = new JSONObject(response);
        String tradeStatus = jsonResponse.getString("TradeStatus");

        return tradeStatus;
    }

    public String doAction(String merchantTradeNo, String tradeNo, String action, int totalAmount) {
        DoActionObj actionObj = new DoActionObj();
        actionObj.setMerchantTradeNo(merchantTradeNo);
        actionObj.setTradeNo(tradeNo);
        actionObj.setAction(action);
        //actionObj.setTotalAmount(String.valueOf(totalAmount));
        actionObj.setMerchantID(MerchantID);

        String response = allInOne.doAction(actionObj);
        log.info("Action Response: {}", response);

        // 解析 response 確保操作成功
        JSONObject jsonResponse = new JSONObject(response);
        int rtnCode = jsonResponse.getInt("RtnCode");
        if (rtnCode == 1) {
            return "Success";
        } else {
            String rtnMsg = jsonResponse.getString("RtnMsg");
            throw new EcpayException("Operation failed: " + rtnMsg);
        }
    }
}
