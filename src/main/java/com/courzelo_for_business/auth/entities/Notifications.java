package com.courzelo_for_business.auth.entities;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "notifications")
public class Notifications {

	@Id
	private String idNotif;
    private String msg;
    private String userId; 
    private boolean read;
    private Date date;
    

    public Notifications(String msg,String userId,boolean read,Date date) {
        
        this.msg=msg;
        this.userId=userId;
        this.read=read;
        this.date=date;
    }

    
    
    public String getIdNotif() {
		return idNotif;
	}



	public void setIdNotif(String idNotif) {
		this.idNotif = idNotif;
	}


	

	public Date getDate() {
		return date;
	}



	public void setDate(Date date) {
		this.date = date;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}
    
	
    
    
}
