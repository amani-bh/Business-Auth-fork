package com.courzelo_for_business.auth.servicerest.iservicesrest;

import java.util.List;

import com.courzelo_for_business.auth.entities.Notifications;

public interface IServiceNotification {
	public String getNotification(String userId,  String content);
	public void setReadNotification(String idNotif) ;
	public List<Notifications> getUserUnreadNotification(String userId);
	public List<Notifications> getUserReadNotification(String userId);
	public List<Notifications> getUserNotification( String userId) ;

}
