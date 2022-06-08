package com.courzelo_for_business.auth.security.services;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.courzelo_for_business.auth.entities.Business;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L;

	private String idBusiness;

	private String companyName;

	private String email;

	@JsonIgnore
	private String password;
	
	
	private boolean active;
	
	private boolean enabled;
	  
	  private String website;
	  
	  private String nbEmployee;
	  
	  private String firstName;
	  
	  private String lastName;
	  
	 
	  private String recrutementRole;
	  
	  
	  private String phone;
	  
	 
	  private String industry;
	  
	  
	  private String country;
	  
	  
	  private String address;
	  

	  private String logo;
	  

	  
	  private String description;


	  
	  private Date creationDate;

	private Collection<? extends GrantedAuthority> authorities;

	public UserDetailsImpl(String idBusiness, String companyName, String email, String password,
			boolean enabled,boolean active, String website, String nbEmployee, String firstName,
			String lastName, String recrutementRole, String phone,
			String industry, String country, String address, String logo, String description, Date creationDate,
			Collection<? extends GrantedAuthority> authorities) {
		this.idBusiness = idBusiness;
		this.companyName = companyName;
		this.email = email;
		this.password = password;
		this.enabled = enabled;
		this.active = active;
		this.website = website;
		this.nbEmployee = nbEmployee;
		this.firstName = firstName;
		this.lastName = lastName;
		this.recrutementRole = recrutementRole;
		this.phone = phone;
		this.industry = industry;
		this.country = country;
		this.address = address;
		this.logo = logo;
		this.description = description;
		this.creationDate = creationDate;
		this.authorities = authorities;
	}

	public static UserDetailsImpl build(Business business) {
		List<GrantedAuthority> authorities = business.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName().name()))
				.collect(Collectors.toList());

		return new UserDetailsImpl(
				business.getIdBusiness(), 
				business.getCompanyName(), 
				business.getEmail(),
				business.getPassword(), 
				business.isEnabled(),
				business.isActive(), 
				business.getWebsite(), 
				business.getNbEmployee(), 
				business.getFirstName(), 
				business.getLastName(), 
				business.getRecrutementRole(), 
				business.getPhone(), 
				business.getIndustry(), 
				business.getCountry(),
				business.getAddress(), 
				business.getLogo(),
				business.getDescription(), 
			    business.getCreationDate(), 
				authorities);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	

	
	public String getIdBusiness() {
		return idBusiness;
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String getUsername() {
		return companyName;
	} 
	
	
	@Override
	public String getPassword() {
		return password;
	} 
	
	public String getWebsite() {
		return website;
	}

	public String getNbEmployee() {
		return nbEmployee;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getRecrutementRole() {
		return recrutementRole;
	}

	public String getPhone() {
		return phone;
	}

	public String getIndustry() {
		return industry;
	}

	public String getCountry() {
		return country;
	}

	public String getAddress() {
		return address;
	}

	public String getLogo() {
		return logo;
	}

	public String getDescription() {
		return description;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}


	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetailsImpl user = (UserDetailsImpl) o;
		return Objects.equals(idBusiness, user.idBusiness);
	}
}
