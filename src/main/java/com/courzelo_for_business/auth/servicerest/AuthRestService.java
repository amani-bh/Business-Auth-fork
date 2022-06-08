package com.courzelo_for_business.auth.servicerest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.courzelo_for_business.auth.entities.Business;
import com.courzelo_for_business.auth.entities.ERole;
import com.courzelo_for_business.auth.entities.Role;
import com.courzelo_for_business.auth.payload.request.LoginRequest;
import com.courzelo_for_business.auth.payload.request.SignupRequest;
import com.courzelo_for_business.auth.payload.response.JwtResponse;
import com.courzelo_for_business.auth.repository.RoleRepository;
import com.courzelo_for_business.auth.repository.UserRepository;
import com.courzelo_for_business.auth.security.jwt.JwtUtils;
import com.courzelo_for_business.auth.security.services.UserDetailsImpl;
import com.courzelo_for_business.auth.servicerest.iservicesrest.IServiceRestAuth;

import org.springframework.mail.javamail.JavaMailSender;


@Service
public class AuthRestService implements IServiceRestAuth {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired	
	JavaMailSender mailSender;

	
	
	public Optional<Business> getUserById(String userId) {
		return userRepository.findById(userId); 
	}
	
	
	
	public JwtResponse authenticateUser(LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
       
		return new JwtResponse(jwt, 
												 userDetails.getIdBusiness(), 
												 userDetails.getUsername(), 
												 userDetails.getEmail(), 
												 userDetails.isActive(),
												 roles);
	}

	
	public Business registerUser(SignupRequest signUpRequest) throws IOException {
		if (Boolean.TRUE.equals(userRepository.existsByCompanyName(signUpRequest.getCompanyName()))) {
			throw new IOException("Company with name " + signUpRequest.getCompanyName() + " already exist");
		}

		else if (Boolean.TRUE.equals(userRepository.existsByEmail(signUpRequest.getEmail()))) {
			throw new IOException("Company with email " + signUpRequest.getEmail() + " already exist");
		}

		// Create new user's account
		Business business = new Business(signUpRequest.getCompanyName(), 
				signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()),
				false,false,
				signUpRequest.getWebsite(),
				signUpRequest.getNbEmployee(),
				signUpRequest.getFirstName(),
				signUpRequest.getLastName(),
				signUpRequest.getRecrutementRole(),
				signUpRequest.getPhone(),
				signUpRequest.getIndustry(),
				signUpRequest.getCountry(),
				signUpRequest.getAddress(),
				signUpRequest.getLogo(),
				signUpRequest.getDescription(),
				signUpRequest.getCreationDate() );

		Set<String> strRoles = signUpRequest.getRoles();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				case "mod":
					Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		business.setRoles(roles);
		userRepository.save(business);

		return business;
	}
	
	public Business activateCompte(String id) throws MessagingException, UnsupportedEncodingException {
		   Business business =userRepository.findByIdBusiness(id); 
		   business.setActive(true);
		   userRepository.save(business);
		
		
		
	    MimeMessage message = mailSender.createMimeMessage();              
	    MimeMessageHelper helper = new MimeMessageHelper(message);
	     
	    helper.setFrom("jagermaya2@gmail.com", "Courzelo for business Support");
	    helper.setTo(business.getEmail());
	     
	    String subject = "Hello , ";
	     
	    String content = "<p>Hello,</p>"
	            + "<p>You have created an acount a few days ago .</p>"
	            + "<p>And it's been proven by the admin .Go now and login</p>"
	            + "<p><a href=http://localhost:4200/BusinessLogin>Login</a></p>"
	            + "<br>"
	            + "<p>Good bye , "
	            ;
	     
	    helper.setSubject(subject);
	     
	    helper.setText(content, true);
	     
	    mailSender.send(message);
	    return business;
	}

	
	public List<Business> getAllInactive() {
		return userRepository.findByActive(false);	
	}
	
    public List<Business> getAllActive() {
		return userRepository.findByActive(true);
		
	}
	
    public Boolean verifMail(String mail) {
    	
    	  return userRepository.existsByEmail(mail);
    }
    public Boolean verifName(String companyName) {
    	return userRepository.existsByCompanyName(companyName);
    }

}
