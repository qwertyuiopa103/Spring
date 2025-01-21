package ispan.event.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity @Table(name = "Event")
public class EventBean implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private int eventID;  // 事件ID

    @Column(name = "event_name")
    private String eventName;  // 事件名稱

    @Column(name = "event_type")
    private String eventType;  // 事件類型

    
    @Column(name = "event_start")
    private LocalDateTime eventStart; // 事件開始

    @Column(name = "event_end")
    private LocalDateTime eventEnd; // 事件結束
    
    @Column(name = "event_location")
    private String eventLocation;  // 事件地點

    @Column(name = "event_description")
    private String eventDescription;  // 事件描述

    @Column(name = "event_picture")
    private String eventPicture;  // 事件圖片

    

    
}
