package com.courzelo_for_business.auth.servicerest.iservicesrest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import com.courzelo_for_business.auth.entities.Business;
import com.courzelo_for_business.auth.payload.request.LoginRequest;
import com.courzelo_for_business.auth.payload.request.SignupRequest;
import com.courzelo_for_business.auth.payload.response.JwtResponse;
import com.sun.jersey.api.NotFoundException;
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
	
	public void updateResetPasswordToken(String token, String email) throws NotFoundException;
	
	public Business getByResetPasswordToken(String token);
	
	public String processForgotPassword(HttpServletRequest request) throws MessagingException, UnsupportedEncodingException;
	public Business updatePassword(HttpServletRequest request) ;
}
