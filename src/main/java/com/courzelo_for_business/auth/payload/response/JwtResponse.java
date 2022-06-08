package com.courzelo_for_business.auth.payload.response;

import java.util.List;

public class JwtResponse {
	private String token;
	private String type = "Bearer";
	private String idBusiness;
	private String companyName;
	private boolean active;
	private String email;
	private List<String> roles;

	public JwtResponse(String accessToken, String id, String username, String email,boolean active, List<String> roles) {
		this.token = accessToken;
		this.idBusiness = id;
		this.companyName = username;
		this.email = email;
		this.roles = roles;
		this.active=active;
	}

	public String getAccessToken() {
		return token;
	}

	public void setAccessToken(String accessToken) {
		this.token = accessToken;
	}

	public String getTokenType() {
		return type;
	}

	public void setTokenType(String tokenType) {
		this.type = tokenType;
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
	
	

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<String> getRoles() {
		return roles;
	}
}
