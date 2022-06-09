package com.courzelo_for_business.auth.controllers;


import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.courzelo_for_business.auth.entities.Business;
import com.courzelo_for_business.auth.payload.request.LoginRequest;
import com.courzelo_for_business.auth.payload.request.SignupRequest;
import com.courzelo_for_business.auth.servicerest.iservicesrest.IServiceRestAuth;


@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	IServiceRestAuth authService;

	
	@GetMapping(path = "/{userId}")
	public Optional<Business> getUserById(@PathVariable(name = "userId") String userId) {
		return authService.getUserById(userId); 
	}

	@GetMapping(path = "/inactive")
	public List<Business> getInactve() {
		return authService.getAllInactive(); 
	}
	
	@GetMapping(path = "/active")
	public List<Business> getActive() {
		return authService.getAllActive(); 
	}
	
	
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		try {
			return ResponseEntity.ok(authService.authenticateUser(loginRequest));
		}
		catch(Exception e)
		{  
		   return new ResponseEntity<>( "Error accurred! "+ e.getMessage() , HttpStatus.BAD_REQUEST);
		}
		   
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest)  {
		

		
		try {
			return ResponseEntity.ok(authService.registerUser(signUpRequest));
		}
		catch(Exception e)
		{  
		   return new ResponseEntity<>( "Error accurred! "+ e.getMessage() , HttpStatus.BAD_REQUEST);
		}
		   

	}
	
	@PostMapping("/activateCompte/{id}")
	public ResponseEntity<?>  activateCompte(@PathVariable(name = "id") String id)  {
		try {
			return ResponseEntity.ok(authService.activateCompte(id));
		}
		catch(Exception e)
		{  
		    return new ResponseEntity<>( "Error accurred! "+ e.getMessage() , HttpStatus.BAD_REQUEST);
		}
		   
		
		}
	
	
	@GetMapping("/verifMail/{email}")
	public ResponseEntity<?>  verifMail(@PathVariable(name = "email") String email)  {
		try {
			return ResponseEntity.ok(authService.verifMail(email));
		}
		catch(Exception e)
		{  
		    return new ResponseEntity<>( "Error accurred! "+ e.getMessage() , HttpStatus.BAD_REQUEST);
		}
		   
		
		}
	
	
	
	@GetMapping("/verifCompanyName/{companyName}")
	public ResponseEntity<?>  verifName(@PathVariable(name = "companyName") String companyName)  {
		try {
			return ResponseEntity.ok(authService.verifName(companyName));
		}
		catch(Exception e)
		{  
		    return new ResponseEntity<>( "Error accurred! "+ e.getMessage() , HttpStatus.BAD_REQUEST);
		}
		   
		
		}
	

	@PostMapping("/validateToken/{token}")
    public ResponseEntity<Boolean> verifyToken(@PathVariable String  token) {
        return  new ResponseEntity<>( authService.validateToken(token),HttpStatus.OK);
    }
	
	
	
	
	}
	
	

