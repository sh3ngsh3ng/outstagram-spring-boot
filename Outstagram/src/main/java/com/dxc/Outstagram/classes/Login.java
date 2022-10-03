package com.dxc.Outstagram.classes;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Login {
	@JsonProperty("username")
	public String username;
	
	@JsonProperty("password")
	public String password;

	@Override
	public String toString() {
		return "Login [username=" + username + ", password=" + password + "]";
	}
	
	
	
}
