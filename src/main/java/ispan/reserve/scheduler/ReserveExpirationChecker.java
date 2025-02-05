package ispan.reserve.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ispan.reserve.model.Reserve;
import ispan.reserve.service.ReserveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Component
public class ReserveExpirationChecker {
    
    private static final Logger logger = LoggerFactory.getLogger(ReserveExpirationChecker.class);
    
    @Autowired
    private ReserveService reserveService;
    
    // 每天 00:00 執行
    @Scheduled(cron = "0 0 0 * * *")
    public void checkExpiredReserves() {
        logger.info("開始檢查過期預約...");
        
        try {
            // 取得所有狀態為待接單的預約
            List<Reserve> pendingReserves = reserveService.findReservesByStatus("待確認");
            // 取得今天的日期
            LocalDate today = LocalDate.now();
            Date currentDate = Date.valueOf(today);
            
            for (Reserve reserve : pendingReserves) {
                // 如果開始日期小於等於今天，且狀態仍為待接單，則將預約標記為過期
                if (reserve.getStartDate().compareTo(currentDate) <= 0) {
                    reserve.setStatus("已過期");
                    reserveService.updateReserve(reserve);
                    logger.info("預約 {} 已標記為過期", reserve.getReserveId());
                }
            }
            
            logger.info("過期預約檢查完成");
            
        } catch (Exception e) {
            logger.error("檢查過期預約時發生錯誤: ", e);
        }
    }
}