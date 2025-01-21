package ispan.event.service;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ispan.event.model.EventBean;
import ispan.event.model.EventRepository;



@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    
    
    @Override
    public EventBean findEvent(int eventID) {
        Optional<EventBean> result = eventRepository.findById(eventID);
        return result.orElse(null);
    }

    @Override
    public List<EventBean> findAllEvents() {
        // 根據 eventEnd 進行排序，並且將即將結束的活動放在最前
        Sort sort = Sort.by(Sort.Order.asc("eventEnd")); // 排序方式為升序 (即結束時間越早排在前)
        return eventRepository.findAll(sort);
    }

    @Override
    public EventBean getThatUpdateEvent(int eventID) {
        Optional<EventBean> result = eventRepository.findById(eventID);
        return result.orElse(null);
    }

    /*
     * @Override
    public EventBean insertEvent(EventBean event, MultipartFile eventPictureFile) throws IOException {
        if (eventPictureFile != null && !eventPictureFile.isEmpty()) {
            byte[] imageBytes = eventPictureFile.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            base64Image = "data:image/jpeg;base64," + base64Image;
            event.setEventPicture(base64Image);
        }
        return eventRepository.save(event);
    }
     * */
    
    public EventBean insertEvent(EventBean event) throws IOException {
//    	 public EventBean insertEvent(EventBean event, MultipartFile eventPictureFile) throws IOException {
//        if (eventPictureFile != null && !eventPictureFile.isEmpty()) {
//            byte[] imageBytes = eventPictureFile.getBytes();
//            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
//            // 確保前綴格式正確
//            base64Image = "data:image/jpeg;base64," + base64Image;
//            event.setEventPicture(base64Image);
//        }
        return eventRepository.save(event);
    }
    

    @Override
    public EventBean updateEvent(EventBean event) throws IOException {
//        if (eventPictureFile != null && !eventPictureFile.isEmpty()) {
//            byte[] imageBytes = eventPictureFile.getBytes();
//            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
//            base64Image = "data:image/jpeg;base64," + base64Image;
//            event.setEventPicture(base64Image);
//        }
        return eventRepository.save(event);
    }

    @Override
    public void deleteEvent(int eventID) {
        eventRepository.deleteById(eventID);
    }
}
