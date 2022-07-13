package com.courzelo_for_business.auth.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.courzelo_for_business.auth.entities.Notifications;



@Repository
public interface NotificationRepository extends MongoRepository<Notifications, String>{
	
	public Notifications findByIdNotif(String idNotif);
	public List<Notifications> findByUserIdAndRead(String userId,boolean read);
	public List<Notifications> findByUserId(String userId);

}
