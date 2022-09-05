package com.courzelo_for_business.auth.servicerest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestBody;

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

import net.bytebuddy.utility.*;

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

		List<String> list=new ArrayList<String>();
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
				signUpRequest.getCreationDate(),
				list);

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

    
    public Boolean validateToken(String token) {
    	return jwtUtils.validateJwtToken(token);
    }

    /********* sub account **************/
    
    public Business registerSubAccount(SignupRequest signUpRequest, String id) throws IOException {
    	 if (Boolean.TRUE.equals(userRepository.existsByEmail(signUpRequest.getEmail()))) {
			throw new IOException("User with email " + signUpRequest.getEmail() + " already exist");
		}
    	 List<String> list=new ArrayList<String>();
    	Business businessUser=userRepository.findById(id).get();
    	String randomCode = RandomString.make(64);
		Business business = new Business(businessUser.getCompanyName(), 
				signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()),
				false,
				signUpRequest.getFirstName(),
				signUpRequest.getLastName(),
				signUpRequest.getPhone(),
				signUpRequest.getCreationDate(),
				businessUser,
				randomCode,list
				);

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
				case "ROLE_OFFER_MANAGEMENT":
					Role offerRole = roleRepository.findByName(ERole.ROLE_OFFER_MANAGEMENT)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(offerRole);

					break;
				case "ROLE_APPLICATION_MANAGEMENT":
					Role appRole = roleRepository.findByName(ERole.ROLE_APPLICATION_MANAGEMENT)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(appRole);

					break;
				case "ROLE_TEST_MANAGMENT":
					Role testRole = roleRepository.findByName(ERole.ROLE_TEST_MANAGMENT)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(testRole);

					break;
				case "ROLE_ACCOUNT_MANAGEMENT":
					Role accountrRole = roleRepository.findByName(ERole.ROLE_ACCOUNT_MANAGEMENT)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(accountrRole);

				break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		business.setRoles(roles);
		Business b=userRepository.save(business);
		if(b!=null){
			String siteURL="http://localhost:4200/";
			try {
				sendVerificationEmail(b, siteURL);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}

		return business;
	}



	@Override
	public List<Business> getAllSubAccount(String id) {
		// TODO Auto-generated method stub
		List<Business> list=userRepository.findByCompanyIdBusinessAndEnabledIsFalse(id);
//		List<Business> list2 = new ArrayList<Business>();
//		for (Business business : list) {
//	        if(business.isEnabled()==false)
//	           list2.add(business);
//	        }
		return list;
	}
	
	@Override
	public Business updateSubAccount(String id,SignupRequest signUpRequest) {
   	Business businessUser=userRepository.findById(id).get();
   	businessUser.setEmail(signUpRequest.getEmail());
   	businessUser.setPhone(signUpRequest.getPhone());
   	businessUser.setFirstName(signUpRequest.getFirstName());
   	businessUser.setLastName(signUpRequest.getLastName());
   	if(signUpRequest.getPassword()!=""){
   		businessUser.setPassword(encoder.encode(signUpRequest.getPassword()));
   	}
   	
   	
		

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
			case "ROLE_OFFER_MANAGEMENT":
				Role offerRole = roleRepository.findByName(ERole.ROLE_OFFER_MANAGEMENT)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(offerRole);

				break;
			case "ROLE_APPLICATION_MANAGEMENT":
				Role appRole = roleRepository.findByName(ERole.ROLE_APPLICATION_MANAGEMENT)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(appRole);

				break;
			case "ROLE_TEST_MANAGMENT":
				Role testRole = roleRepository.findByName(ERole.ROLE_TEST_MANAGMENT)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(testRole);

				break;
			case "ROLE_ACCOUNT_MANAGEMENT":
				Role accountrRole = roleRepository.findByName(ERole.ROLE_ACCOUNT_MANAGEMENT)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(accountrRole);

			break;
			default:
				Role userRole = roleRepository.findByName(ERole.ROLE_USER)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(userRole);
			}
		});
	}
		businessUser.setRoles(roles);
		userRepository.save(businessUser);

		return businessUser;
	}



	@Override
	public void deleteSubAccount(String id) {
		Business businessUser=userRepository.findById(id).get();
		businessUser.setEnabled(true);
		userRepository.save(businessUser);
		//userRepository.deleteById(id);
		
	}



	@Override
	public List<Business> getAllNotEnabled() {
		return userRepository.findByEnabled(false);
	}

	private void sendVerificationEmail(Business user, String siteURL)  throws MessagingException, UnsupportedEncodingException {
	    String toAddress = user.getEmail();
	    String fromAddress = "jagermaya2@gmail.com";
	    String senderName = "Courzelo for business Support";
	    String subject = "Please verify your registration";
	    String content = "Dear [[name]],<br>"
	            + "Please click the link below to verify your registration:<br>"
	            + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
	            + "Thank you,<br>"
	            + "Courzelo";
	     
	    MimeMessage message = mailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message);
	     
	    helper.setFrom(fromAddress, senderName);
	    helper.setTo(toAddress);
	    helper.setSubject(subject);
	     
	    content = content.replace("[[name]]", user.getFirstName()+" "+user.getLastName());
	    String verifyURL = siteURL + "/verify/" + user.getVerificationCode();
	     
	    content = content.replace("[[URL]]", verifyURL);
	     
	    helper.setText(content, true);
	     
	    mailSender.send(message);
	}
	
	public boolean verify(String verificationCode) {
	    Business user = userRepository.findByVerificationCode(verificationCode);
	     
	    if (user == null || user.isEnabled()) {
	        return false;
	    } else {
	        user.setVerificationCode(null);
	        user.setActive(true);
	        userRepository.save(user);
	         
	        return true;
	    }
	     
	}
     
	public Business updateAccount(String id,SignupRequest signUpRequest) {
	   	Business businessUser=userRepository.findById(id).get();
	   	businessUser.setEmail(signUpRequest.getEmail());
	   	businessUser.setPhone(signUpRequest.getPhone());
	   	businessUser.setFirstName(signUpRequest.getFirstName());
	   	businessUser.setLastName(signUpRequest.getLastName());

	   	if(signUpRequest.getPassword()!=""){
	   		businessUser.setPassword(encoder.encode(signUpRequest.getPassword()));
	   	}
	   	
	   	businessUser.setNbEmployee(signUpRequest.getNbEmployee()); 
		businessUser.setRecrutementRole(signUpRequest.getRecrutementRole()); 
		businessUser.setWebsite(signUpRequest.getWebsite());
		businessUser.setIndustry(signUpRequest.getIndustry());
		businessUser.setCountry(signUpRequest.getCountry());
		businessUser.setAddress(signUpRequest.getAddress());
		businessUser.setDescription(signUpRequest.getDescription());
	   	userRepository.save(businessUser);
	   	return businessUser;
		}



	@Override
	public Business AddCourseToBusinessUser(String id, String idCourse) {
		Business businessUser=userRepository.findById(id).get();

		if(!businessUser.getListCourses().contains(idCourse))
		businessUser.getListCourses().add(idCourse);
		return userRepository.save(businessUser);
	}


}
