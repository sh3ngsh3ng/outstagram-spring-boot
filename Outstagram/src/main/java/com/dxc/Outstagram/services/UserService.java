package com.dxc.Outstagram.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dxc.Outstagram.entity.Account;
import com.dxc.Outstagram.repository.UserRepository;

@Service
public class UserService implements UserDetailsService{
	
	@Autowired
	private UserRepository ur;
	
	@Autowired
	private AccountServices as;
	


	// Function: Returns a User object needed for jwt
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		// User from entity
		com.dxc.Outstagram.entity.User u = ur.findUserByUsername(username);
		
		// user from UserDetailsService
		return new User(u.getUsername(), u.getPassword() , new ArrayList<>());
	}
	
	// Function: Find the user entity by username
	// Note: returns User entity
	public com.dxc.Outstagram.entity.User findUserByUsername(String username) {
		return ur.findUserByUsername(username);
	}
	
	
	// Function: Create User
	// Note: this method creates a new user and a new account
	public void createUser(com.dxc.Outstagram.entity.User user) {
		// Create a new user
//		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
		com.dxc.Outstagram.entity.User u = ur.save(user);
		System.out.println("User Created");
		// create account for user
		as.createAccountForUser(u);
		System.out.println("Account created");
	}
	
	// Function: Return list of Users
	public ArrayList<com.dxc.Outstagram.entity.User> getAllUsers() {
		return (ArrayList<com.dxc.Outstagram.entity.User>) ur.findAll();
	}
	
	
	// Function: Delete User (admin function)
	public void deleteUserByAdmin(int userId) {
		System.out.println("fawefawecalled");
		ur.deleteById(userId);
	}
	
}
