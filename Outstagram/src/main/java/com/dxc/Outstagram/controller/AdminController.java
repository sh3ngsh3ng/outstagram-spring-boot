package com.dxc.Outstagram.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dxc.Outstagram.entity.Post;
import com.dxc.Outstagram.entity.User;
import com.dxc.Outstagram.services.OutstagramAdminServices;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins="*")
public class AdminController {
	
	@Autowired
	private OutstagramAdminServices oas;
	
	@GetMapping("/getusers")
	public ArrayList<User> getAllUsers() {
		return oas.getAllUsers();
	}

	
	@DeleteMapping("/delete/user/{id}")
	public void deleteUserById(@PathVariable("id") int userId) {
		System.out.println("this is user id " + userId);
		oas.deleteUserByAdmin(userId);
	}

	
	// delete post
	@DeleteMapping("/delete/post/{id}")
	public String deletePostById(@PathVariable("id") int postId) {
		oas.deletePostByAdmin(postId);
		return "Post Deleted";
	}
	
	
	// update post
	@PutMapping("/update/post/{id}")
	public String updatePostById(@PathVariable("id") int postId, @RequestBody Post post) {
		System.out.println("called");
		oas.updatePostByAdmin(postId, post);
		return "Post Updated";
	}

}
