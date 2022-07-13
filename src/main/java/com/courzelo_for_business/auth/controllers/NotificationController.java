package com.courzelo_for_business.auth.controllers;



import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.courzelo_for_business.auth.entities.Notifications;
import com.courzelo_for_business.auth.repository.NotificationRepository;


@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping( "/websocket-backend" )
public class NotificationController {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private  NotificationRepository notifRepository;
    
    //private Notifications notifications = new Notifications(0,"","",false,new Date());

    @GetMapping("/notify/{userId}")
    public String getNotification(@PathVariable(name = "userId") String userId,@RequestBody  @Valid  String content) {
    	Notifications notifications = new Notifications("","",false,new Date());
        notifications.setMsg(content);
        notifications.setUserId(userId);
        notifRepository.save(notifications);

        template.convertAndSend("/topic/notification", notifications);

        return "Notifications successfully sent to Angular !";
    }
    
    @PostMapping("/notify/{idNotif}")
    public void setReadNotification(@PathVariable(name = "idNotif") String idNotif) {
    	Notifications notif=notifRepository.findByIdNotif(idNotif);
    	notif.setRead(true);
    	
    	notifRepository.save(notif);  
    }
    
    
    @GetMapping("/unread/{userId}")
    public List<Notifications> getUserUnreadNotification(@PathVariable(name = "userId") String userId) {
    	return notifRepository.findByUserIdAndRead(userId,false);
    
    	
    }
    
    @GetMapping("/read/{userId}")
    public List<Notifications> getUserReadNotification(@PathVariable(name = "userId") String userId) {
    	return notifRepository.findByUserIdAndRead(userId,true);
    
    	
    }
    
    @GetMapping("/all/{userId}")
    public List<Notifications> getUserNotification(@PathVariable(name = "userId") String userId) {
    	return notifRepository.findByUserId(userId);
    
    	
    }
    
    
}
