package com.courzelo_for_business.auth;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

import javax.mail.MessagingException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.courzelo_for_business.auth.entities.Business;
import com.courzelo_for_business.auth.payload.request.LoginRequest;
import com.courzelo_for_business.auth.payload.request.SignupRequest;
import com.courzelo_for_business.auth.payload.response.JwtResponse;
import com.courzelo_for_business.auth.servicerest.iservicesrest.IServiceRestAuth;



@RunWith(SpringRunner.class)
@SpringBootTest
public class BusinessAuthApplicationTests {

	@Autowired
	IServiceRestAuth iServiceRestAuth;
	
	
	
	@Test
	 public void GetUser() {
		 Optional<Business> business =iServiceRestAuth.getUserById("6261e803e22a337c2ec32e05");
		 Assert.assertEquals("jiji",business.get().getCompanyName());
		
	}
	
	
	@Test
	 public void Login() {
		 LoginRequest login = new LoginRequest();
		 login.setEmail("asmachebbi222@gmail.com");
		 login.setPassword("Admin789@");
		 JwtResponse res =iServiceRestAuth.authenticateUser(login);
		 Assert.assertEquals("626b2efbf1d5b22ee0106932",res.getIdBusiness());
		
	}
	
	@Test
	 public void Register() throws IOException {
		 SignupRequest req = new SignupRequest();
		 req.setActive(true);
		 req.setFirstName("jack");
		 req.setEmail("jack.test@gmail.com");
		 req.setPassword("jack789@");
		 Business res =iServiceRestAuth.registerUser(req);
		 Assert.assertEquals("jack.test@gmail.com",res.getEmail());
		
	}
	
	
	@Test
	 public void Activate() throws UnsupportedEncodingException, MessagingException  {
		
		 Business res =iServiceRestAuth.activateCompte("626b2efbf1d5b22ee0106932");
		 Assert.assertEquals(true,res.isActive());
		
	}
	

}
