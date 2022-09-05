package com.courzelo_for_business.auth.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "business")
public class Business {
  @Id
  private String idBusiness;

  @NotBlank
  @Size(max = 20)
  private String companyName;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Size(max = 120)
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
  
  private Business company;
  
  private String verificationCode;

  

  @DBRef
  private Set<Role> roles = new HashSet<>();
  
  private List<String> listCourses;

  public Business() {
  }

  
  public Business(String companyName,String email, String password, boolean active,boolean enabled,  String website, String nbEmployee, String firstName,
			String lastName, String recrutementRole, String phone,
			String industry, String country, String address, String logo, String description, Date creationDate, List<String> listCourses) {
		super();
		
		this.companyName = companyName;
		this.email = email;
		this.password = password;
		this.active = active;
		this.enabled = enabled;
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
		this.listCourses=listCourses;
	}
  


public Business(String companyName, String email, String password, boolean active, String firstName,
			String lastName, String phone,Date creationDate, Business company,String verificationCode, List<String> listCourses) {
		super();
		
		this.companyName = companyName;
		this.email = email;
		this.password = password;
		this.active = active;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.creationDate = creationDate;
		this.company=company;
		this.verificationCode=verificationCode;
		this.listCourses=listCourses;
	}

  
  public String getIdBusiness() {
		return idBusiness;
	}

	public void setIdBusiness(String idBusiness) {
		this.idBusiness = idBusiness;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	

	public boolean isActive() {
		return active;
	}


	public void setActive(boolean active) {
		this.active = active;
	}


	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getNbEmployee() {
		return nbEmployee;
	}

	public void setNbEmployee(String nbEmployee) {
		this.nbEmployee = nbEmployee;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	
	public String getRecrutementRole() {
		return recrutementRole;
	}

	public void setRecrutementRole(String recrutementRole) {
		this.recrutementRole = recrutementRole;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}


	public Business getCompany() {
		return company;
	}


	public void setCompany(Business company) {
		this.company = company;
	}


	public String getVerificationCode() {
		return verificationCode;
	}


	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	  public List<String> getListCourses() {
			return listCourses;
		}


		public void setListCourses(List<String> listCourses) {
			this.listCourses = listCourses;
		}
 
	 
	}

  

