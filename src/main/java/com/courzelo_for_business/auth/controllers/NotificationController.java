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
import com.courzelo_for_business.auth.servicerest.iservicesrest.IServiceNotification;


@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping( "/websocket-backend" )
public class NotificationController {


    @Autowired
    private  IServiceNotification notifService;
    
   
    @PostMapping("/notify/{userId}")
    public String getNotification(@PathVariable(name = "userId") String userId,@RequestBody  @Valid  String content) {
    	return notifService.getNotification(userId, content);
    }
    
    @PostMapping("/SetRead/{idNotif}")
    public void setReadNotification(@PathVariable(name = "idNotif") String idNotif) {
    	notifService.setReadNotification(idNotif);
    }
    
    
    @GetMapping("/unread/{userId}")
    public List<Notifications> getUserUnreadNotification(@PathVariable(name = "userId") String userId) {
    	return notifService.getUserUnreadNotification(userId);
    
    	
    }
    
    @GetMapping("/read/{userId}")
    public List<Notifications> getUserReadNotification(@PathVariable(name = "userId") String userId) {
    	return notifService.getUserReadNotification(userId);
    	
    }
    
    @GetMapping("/all/{userId}")
    public List<Notifications> getUserNotification(@PathVariable(name = "userId") String userId) {
    	return notifService.getUserNotification(userId);
    
    	
    }
    
    
}
