package com.courzelo_for_business.auth.servicerest;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.courzelo_for_business.auth.entities.Notifications;
import com.courzelo_for_business.auth.repository.NotificationRepository;
import com.courzelo_for_business.auth.servicerest.iservicesrest.IServiceNotification;

@Service
public class NotificationService implements IServiceNotification{
	
    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private  NotificationRepository notifRepository;
    
    
    
    public String getNotification(String userId,  String content) {
    	Notifications notifications = new Notifications("","",false,new Date());
        notifications.setMsg(content);
        notifications.setUserId(userId);
        notifRepository.save(notifications);

        template.convertAndSend("/topic/notification", notifications);

        return "Notifications successfully sent to Angular !";
    }
    
    
    public void setReadNotification(String idNotif) {
    	Notifications notif=notifRepository.findByIdNotif(idNotif);
    	notif.setRead(true);
    	
    	notifRepository.save(notif);  
    }
    
    
    
    public List<Notifications> getUserUnreadNotification(String userId) {
    	return notifRepository.findByUserIdAndRead(userId,false);
    
    	
    }
    
    
    public List<Notifications> getUserReadNotification(String userId) {
    	return notifRepository.findByUserIdAndRead(userId,true);
    
    	
    }
    
    
    public List<Notifications> getUserNotification( String userId) {
    	return notifRepository.findByUserId(userId);
    
    	
    }
    

}
