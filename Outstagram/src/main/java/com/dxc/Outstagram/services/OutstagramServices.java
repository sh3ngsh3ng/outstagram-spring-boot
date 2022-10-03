package com.dxc.Outstagram.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dxc.Outstagram.classes.JwtUtility;
import com.dxc.Outstagram.classes.Login;
import com.dxc.Outstagram.entity.Account;
import com.dxc.Outstagram.entity.Post;
import com.dxc.Outstagram.entity.User;
import com.dxc.Outstagram.model.JwtRequest;
import com.dxc.Outstagram.repository.AccountRepository;
import com.dxc.Outstagram.repository.PostRepository;
import com.dxc.Outstagram.repository.UserRepository;


@Component
public class OutstagramServices {
	
	@Autowired
	private UserRepository ur;
	
	@Autowired
	private AccountRepository ar;
	
	@Autowired 
	private PostRepository pr;
	
	@Autowired
	private PostServices ps;
	
	@Autowired
	private UserService us;
	
	@Autowired
	private AccountServices as;
	
	
	@Autowired
	private JwtServices js;
	
	// get all users
//	public ArrayList getAllUsers() {
//		ArrayList<User> result = (ArrayList<User>) ur.findAll();
//		return result;
//	}
	
	
	// create User
	public void createUser(User user) {
		us.createUser(user);
		System.out.println("User successfully created");
	}
	
	// add Post
//	public void addPost(Post post) {
//		// account shld be provided by user
//		Account temp = ar.findById(12);
//		post.setAccount(temp);
//		pr.save(post);
//		System.out.println("Post added");
//	}
	
	
	
	//  add image
	public void addImage(String username, Post post, String postDir) {
		// retrieve user by username
		User u = us.findUserByUsername(username);
		// get id of the user
		int userId = u.getUser_id();
		// use user id to find account
		Account a = as.findAccountByUserId(userId);
		// save image
		ps.addImage(post, a, postDir);
		System.out.println("Post added");
	}
	
	
	// get image
	public String getImage(int postId) throws IOException {
		return ps.getEncodedImage(postId);
	}
	
	
	// delete post
	public void deletePost(String jwt, int postId) {
		// get username from jwt
		String username = js.getUsernameFromJwt(jwt);
		// get user with username
		User u = us.findUserByUsername(username);
		// delete post
		ps.deleteMedia(u, postId);
	}
	
	@Autowired
	private JwtUtility jwtUtil;
	
	// get all post by user
	public ArrayList<Post>getAllPostByUser(String jwt) {
		// get username from jwt
		String token = jwt.substring(7);
		String username = jwtUtil.getUsernameFromToken(token);
		// get user with username
		User u = ur.findUserByUsername(username);
		// get user_id from user
		int userId = u.getUser_id();
		// get account from userId
		Account a= ar.findAccountByUserId(userId);
		// get account_id from account
		int accountId = a.getAccount_id();
		return pr.getAllPostByUser(accountId);
	}
	
	
	// get all Post
	public ArrayList<Post> getAllPost() {
		System.out.println("All Post returned");
		return (ArrayList<Post>) pr.findAll();
	}
	
	// get Post by id
	public Post getPostById(int id) {
		return pr.findById(id);
	}
	
	
	
	// update Post by id
	public void updatePostById(String jwt, int postId, Post p) {
		// get token
		String token = jwt.substring(7);
		// get username from token
		String username = jwtUtil.getUsernameFromToken(token);
		// find user
		User u = us.findUserByUsername(username);
		// find account of user
		Account a = as.findAccountByUserId(u.getUser_id());
		ps.updatePostById(a, postId, p);
	}
	
	
	
	// check role of user with username
	public String getRoleFromUsername(String username) {
		User u = us.findUserByUsername(username);
		Account a = as.findAccountByUserId(u.getUser_id());
		return a.getAccount_type();
		
	}
	
	
	// add post view by 1
	public void addOnePostView(int postId) {
		ps.addOnePostView(postId);
		System.out.println("Post View Count Added By 1");
	}
	
	
	// like a post
	public void likePost(String jwt, int postId) {
		// get token
		String token = jwt.substring(7);
		// get account from token
		String username = jwtUtil.getUsernameFromToken(token);
		User u = us.findUserByUsername(username);
		Account a = as.findAccountByUserId(u.getUser_id());
		// find post by postId
		Post p = pr.findById(postId);
		// add post to likes
		a.addLikes(p);
		ar.save(a);
	}
	
	
	// check if post is liked
	public int checkIfPostIsLiked(String jwt, int postId) {
		// get token
		String token = jwt.substring(7);
		// get account from token
		String username = jwtUtil.getUsernameFromToken(token);
		User u = us.findUserByUsername(username);
		Account a = as.findAccountByUserId(u.getUser_id());
		return as.checkIfPostIsLiked(a.getAccount_id(), postId);
	}
	
	
	// delete Post by id
//	public void deletePostById(int id) {
//		pr.deleteById(id);
//		System.out.println("Post deleted");
//	}
	
//	public String validateLogin(Login lg) {
//		if (ur.validateUser(lg)) {
//			// return web token
//			JwtTokenUtil jwt = new JwtTokenUtil();
//			System.out.println(jwt.doGenerateToken(new HashMap<>(), lg.username));
//			return jwt.doGenerateToken(new HashMap<>(), lg.username);
//		} else {
//			// return failed
//			return "";
//		}
//		// dlt ltr
//	}
 	
}
