package com.dxc.Outstagram.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dxc.Outstagram.entity.Account;
import com.dxc.Outstagram.entity.Post;
import com.dxc.Outstagram.entity.User;

@Component
public class OutstagramAdminServices {

	@Autowired
	private AccountServices as;
	
	@Autowired
	private UserService us;

	@Autowired
	private PostServices ps;
	
	
	
	public boolean checkIfUserIsAdmin(String username) {
		User u = us.findUserByUsername(username);
		Account a = as.findAccountByUserId(u.getUser_id());
		System.out.println(a.getAccount_type());
		if (a.getAccount_type().equals("admin")) {
			return true;
		}
		
		return false;
	}
	
	
	public ArrayList<User> getAllUsers() {
		return us.getAllUsers();
	}
	
	// delete post by admin
	// havent' account for the deletion of media
	public void deletePostByAdmin(int postId) {
		ps.deletePostById(postId);
		System.out.println("Post deleted successfully by admin");
	}
	
	// update post by admin
	public void updatePostByAdmin(int postId, Post p) {
		ps.updatePostByIdAdmin(postId, p);
		System.out.println("Post updated successfully by Admin");
	}
	
	// delete user by admin
	public void deleteUserByAdmin(int userId) {
		us.deleteUserByAdmin(userId);
		System.out.println("User successfully deleted by Admin");
	}

	
	
}
