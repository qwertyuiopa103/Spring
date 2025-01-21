package ispan.event.model;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EventRepository extends JpaRepository<EventBean, Integer> {
	
    // 添加排序功能，按 eventEnd 排序，最接近結束的活動會排在最前
    List<EventBean> findAll(Sort sort);
    // 根據事件類型查詢
    List<EventBean> findByEventType(String eventType);

    // 根據事件地點查詢
    List<EventBean> findByEventLocation(String eventLocation);

    // 根據事件類型和地點查詢
    List<EventBean> findByEventTypeAndEventLocation(String eventType, String eventLocation);
}

