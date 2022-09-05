package com.courzelo_for_business.auth.servicerest.iservicesrest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;

import com.courzelo_for_business.auth.entities.Business;
import com.courzelo_for_business.auth.payload.request.LoginRequest;
import com.courzelo_for_business.auth.payload.request.SignupRequest;
import com.courzelo_for_business.auth.payload.response.JwtResponse;

public interface IServiceRestAuth {
	
	public Optional<Business> getUserById(String userId);
	public JwtResponse authenticateUser(LoginRequest loginRequest) ;
	public Business registerUser(SignupRequest signUpRequest) throws IOException ;
    public Boolean verifMail(String mail);
    public Boolean verifName(String companyName);
    
	public Business activateCompte(String id) throws MessagingException, UnsupportedEncodingException ;
	public List<Business> getAllInactive() ;
	public List<Business> getAllActive() ;
	public Boolean validateToken(String token);
	
	/********************/
	public Business registerSubAccount(SignupRequest signUpRequest,String id) throws IOException ;
	public List<Business> getAllSubAccount(String id) ;
	public Business updateSubAccount(String id,SignupRequest signUpRequest);
	public void deleteSubAccount(String id); 
	public List<Business> getAllNotEnabled();
	public boolean verify(String verificationCode);
	public Business updateAccount(String id,SignupRequest signUpRequest);
	
	public Business AddCourseToBusinessUser(String id,String idCourse);
}

