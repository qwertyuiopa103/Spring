package ispan.event.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import ispan.event.model.EventBean;

public interface EventService {
    EventBean findEvent(int eventID);
    List<EventBean> findAllEvents();
    EventBean getThatUpdateEvent(int eventID);

//    EventBean insertEvent(EventBean event, MultipartFile eventPictureFile) throws IOException;
    EventBean insertEvent(EventBean event) throws IOException;
    EventBean updateEvent(EventBean event) throws IOException;

    void deleteEvent(int eventID);
}
