package ispan.event.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ispan.event.model.EventBean;
import ispan.event.service.EventServiceImpl;
@RestController
@RequestMapping("/api/eventAdmin")
public class EventAdminController {
	 @Autowired
	    private EventServiceImpl eventService;

	    // 查詢所有事件
	    @GetMapping("/all")
	    public List<EventBean> findAllEvents() {
	        try {
	        	//System.out.println(eventService.findAllEvents());
	            return eventService.findAllEvents();
	        } catch (Exception e) {
	            e.printStackTrace();
	            return new ArrayList<>(); // 返回空列表表示查詢失敗
	        }
	    }

	    // 查詢單一事件
	    @GetMapping("/get")
	    public EventBean getEvent(@RequestParam("eventID") int eventID) {
	        try {
	            return eventService.findEvent(eventID);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null; // 返回 null 表示查詢失敗
	        }
	    }

	    // 刪除事件
	    @DeleteMapping("/delete/{eventID}")
	    public String deleteEvent(@PathVariable("eventID") int eventID) {
	        try {
	            eventService.deleteEvent(eventID);
	            return "刪除成功";
	        } catch (Exception e) {
	            e.printStackTrace();
	            return "刪除失敗: " + e.getMessage();
	        }
	    }

	    @PostMapping("/insert")
	    public String insertEvent(@RequestParam Map<String, String> formData,
	               
	    		@RequestPart(value = "eventPictureFile") MultipartFile eventPictureFile) {
	        try {System.out.println(formData);
	            EventBean eventBean = new EventBean();
//	            eventBean.setEventID(Integer.parseInt(formData.get("eventID")));
	            eventBean.setEventName(formData.get("eventName"));
	            eventBean.setEventType(formData.get("eventType"));

	            // 日期格式转换 (String -> LocalDateTime)
	            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	            LocalDateTime eventStart = LocalDateTime.parse(formData.get("eventStart"), dateTimeFormatter);
	            LocalDateTime eventEnd = LocalDateTime.parse(formData.get("eventEnd"), dateTimeFormatter);

	            eventBean.setEventStart(eventStart);
	            eventBean.setEventEnd(eventEnd);
	            eventBean.setEventLocation(formData.get("eventLocation"));
	            eventBean.setEventDescription(formData.get("eventDescription"));
	            if ( eventPictureFile != null && ! eventPictureFile.isEmpty()) {
	              // 將 MultipartFile 轉換為 byte[]
	              byte[] photoBytes =  eventPictureFile.getBytes();
	              String base64Image = Base64.getEncoder().encodeToString( photoBytes);
	              // 添加 data URI scheme 前綴
	              base64Image = "data:image/jpeg;base64," + base64Image;
	              eventBean.setEventPicture(base64Image);
	          }
	            eventService.insertEvent(eventBean);
	            return "新增成功";
	        } catch (Exception e) {
	            e.printStackTrace();
	            return "新增失敗: " + e.getMessage();
	        }
	    }

	    @PutMapping("/{eventID}")
	    public String updateEvent(@PathVariable("eventID") int eventID, @RequestParam Map<String, String> formData,
	    		@RequestPart(value = "eventPictureFile", required = false) MultipartFile eventPictureFile){
	        try {
	        	
	            EventBean eventBean = new EventBean();
	            eventBean.setEventID(Integer.parseInt(formData.get("eventID")));
	            eventBean.setEventName(formData.get("eventName"));
	            eventBean.setEventType(formData.get("eventType"));

	            // 處理日期時間轉換 (String -> LocalDateTime)
	            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	            LocalDateTime eventStart = LocalDateTime.parse(formData.get("eventStart"), dateTimeFormatter);
	            LocalDateTime eventEnd = LocalDateTime.parse(formData.get("eventEnd"), dateTimeFormatter);

	            eventBean.setEventStart(eventStart);
	            eventBean.setEventEnd(eventEnd);
	            eventBean.setEventLocation(formData.get("eventLocation"));
	            eventBean.setEventDescription(formData.get("eventDescription"));
	            if ( eventPictureFile != null && ! eventPictureFile.isEmpty()) {
	                // 將 MultipartFile 轉換為 byte[]
	                byte[] photoBytes =  eventPictureFile.getBytes();
	                String base64Image = Base64.getEncoder().encodeToString( photoBytes);
	                // 添加 data URI scheme 前綴
	                base64Image = "data:image/jpeg;base64," + base64Image;
	                eventBean.setEventPicture(base64Image);
	            } else {
	                // 保持现有的 eventPicture（如果存在）
	                EventBean existingEvent = eventService.findEvent(eventID);
	                if (existingEvent != null) {
	                    eventBean.setEventPicture(existingEvent.getEventPicture());
	                }
	            }
	            eventService.updateEvent(eventBean);
	            return "更新成功";
	        } catch (Exception e) {
	            e.printStackTrace();
	            return "更新失敗: " + e.getMessage();
	        }
	    }
	}
