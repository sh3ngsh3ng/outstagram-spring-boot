package com.dxc.Outstagram.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dxc.Outstagram.classes.JwtUtility;
import com.dxc.Outstagram.services.OutstagramAdminServices;


@Component
public class AdminFilter implements Filter {

	@Autowired
	private JwtUtility jwtUtil;
	
	@Autowired
	private OutstagramAdminServices oas;
	
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		// filter logic
		HttpServletRequest req = (HttpServletRequest) request;
		
		String authorization = req.getHeader("authorization");

		String token = null;
		String username = null;
		
		if (authorization != null && authorization.startsWith("Bearer ")) {
			token = authorization.substring(7);
			username = jwtUtil.getUsernameFromToken(token);
		}
		

		System.out.println("here" + oas.checkIfUserIsAdmin(username));
		if (oas.checkIfUserIsAdmin(username)) {
			System.out.println("filter for admin success");
			chain.doFilter(request, response);
		} else {
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are not an admin.");
		}

		
		
		
		
//		chain.doFilter(request, response);
	}

}
