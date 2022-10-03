package com.dxc.Outstagram.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dxc.Outstagram.classes.JwtUtility;

@Component
public class JwtServices {
	
	@Autowired
	private JwtUtility jwtUtil;
	
	// Returns username from the jwt token
	String getUsernameFromJwt(String Jwt) {
		String token = Jwt.substring(7);
		String username = jwtUtil.getUsernameFromToken(token);
		return username;
	}
}
